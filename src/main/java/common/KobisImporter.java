package common;

import com.google.gson.*;
import java.net.URI;
import java.net.http.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class KobisImporter {

	private static final String BOX_OFFICE_URL =
		"http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
	private static final String MOVIE_INFO_URL =
		"http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json";

	private final String apiKey;
	private final HttpClient httpClient;
	private final Gson gson;

	public KobisImporter() {
		this.apiKey = DBUtil.getDbProperty("kobis.api.key");
		this.httpClient = HttpClient.newHttpClient();
		this.gson = new Gson();
	}

	public void importMovies() throws Exception {
		String yesterday = LocalDate.now().minusDays(1)
			.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		String json = fetch(BOX_OFFICE_URL + "?key=" + apiKey + "&targetDt=" + yesterday);

		JsonArray dailyList = gson.fromJson(json, JsonObject.class)
			.getAsJsonObject("boxOfficeResult")
			.getAsJsonArray("dailyBoxOfficeList");

		Connection conn = DBUtil.getConnection();
		conn.createStatement().executeUpdate("DELETE FROM reservation");
		conn.createStatement().executeUpdate("DELETE FROM screening");
		conn.createStatement().executeUpdate("DELETE FROM movie");
		conn.createStatement().executeUpdate("ALTER TABLE reservation AUTO_INCREMENT = 1");
		conn.createStatement().executeUpdate("ALTER TABLE screening AUTO_INCREMENT = 1");
		conn.createStatement().executeUpdate("ALTER TABLE movie AUTO_INCREMENT = 1");

		PreparedStatement psmt = conn.prepareStatement(
			"INSERT INTO movie(title, genre, director, rating) VALUES(?,?,?,?)");

		for (JsonElement elem : dailyList) {
			JsonObject item = elem.getAsJsonObject();
			String movieCd = item.get("movieCd").getAsString();
			String movieNm = item.get("movieNm").getAsString();

			String detailJson = fetch(MOVIE_INFO_URL + "?key=" + apiKey + "&movieCd=" + movieCd);
			JsonObject movieInfo = gson.fromJson(detailJson, JsonObject.class)
				.getAsJsonObject("movieInfoResult")
				.getAsJsonObject("movieInfo");

			String genre    = safeStr(movieInfo, "repGenreNm");
			String director = extractFirst(movieInfo.getAsJsonArray("directors"), "peopleNm");
			String rating   = extractFirst(movieInfo.getAsJsonArray("audits"), "watchGradeNm");

			psmt.setString(1, movieNm);
			psmt.setString(2, genre);
			psmt.setString(3, director);
			psmt.setString(4, rating);
			psmt.executeUpdate();
		}
		psmt.close();
	}

	private String extractFirst(JsonArray arr, String key) {
		return arr != null && arr.size() > 0
			? safeStr(arr.get(0).getAsJsonObject(), key)
			: "";
	}

	private String fetch(String url) throws Exception {
		HttpRequest req = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.GET()
			.build();
		return httpClient.send(req, HttpResponse.BodyHandlers.ofString()).body();
	}

	private String safeStr(JsonObject obj, String key) {
		JsonElement el = obj.get(key);
		return el == null || el.isJsonNull() ? "" : el.getAsString();
	}

}
