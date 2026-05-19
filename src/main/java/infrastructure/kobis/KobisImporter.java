package infrastructure.kobis;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import infrastructure.db.DBUtil;

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

		// 1단계: API에서 데이터 전부 수집 (DB 건드리기 전)
		String json = fetch(BOX_OFFICE_URL + "?key=" + apiKey + "&targetDt=" + yesterday);

		JsonArray dailyList = gson.fromJson(json, JsonObject.class)
			.getAsJsonObject("boxOfficeResult")
			.getAsJsonArray("dailyBoxOfficeList");

		record MovieRow(String title, String genre, String director, String rating, int runtime) {}
		List<MovieRow> rows = new ArrayList<>();

		for (JsonElement elem : dailyList) {
			JsonObject item = elem.getAsJsonObject();
			String movieCd = item.get("movieCd").getAsString();
			String movieNm = item.get("movieNm").getAsString();

			String detailJson = fetch(MOVIE_INFO_URL + "?key=" + apiKey + "&movieCd=" + movieCd);
			JsonObject movieInfo = gson.fromJson(detailJson, JsonObject.class)
				.getAsJsonObject("movieInfoResult")
				.getAsJsonObject("movieInfo");

			int runtime = 0;
			try { runtime = Integer.parseInt(safeStr(movieInfo, "showTm")); } catch (NumberFormatException ignored) {}

			rows.add(new MovieRow(
				movieNm,
				safeStr(movieInfo, "repGenreNm"),
				extractFirst(movieInfo.getAsJsonArray("directors"), "peopleNm"),
				extractFirst(movieInfo.getAsJsonArray("audits"), "watchGradeNm"),
				runtime
			));
		}

		// 2단계: API 수집 성공 후에만 DB 초기화 및 삽입 (트랜잭션)
		Connection conn = DBUtil.getConnection();
		conn.setAutoCommit(false);
		try {
			conn.createStatement().executeUpdate("DELETE FROM reservation");
			conn.createStatement().executeUpdate("DELETE FROM screening");
			conn.createStatement().executeUpdate("DELETE FROM movie");
			conn.createStatement().executeUpdate("ALTER TABLE reservation AUTO_INCREMENT = 1");
			conn.createStatement().executeUpdate("ALTER TABLE screening AUTO_INCREMENT = 1");
			conn.createStatement().executeUpdate("ALTER TABLE movie AUTO_INCREMENT = 1");

			PreparedStatement psmt = conn.prepareStatement(
				"INSERT INTO movie(title, genre, director, rating, runtime) VALUES(?,?,?,?,?)");

			for (MovieRow m : rows) {
				psmt.setString(1, m.title());
				psmt.setString(2, m.genre());
				psmt.setString(3, m.director());
				psmt.setString(4, m.rating());
				psmt.setInt(5, m.runtime());
				psmt.executeUpdate();
			}
			psmt.close();
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			conn.setAutoCommit(true);
		}
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
