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
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import infrastructure.AppLogger;
import infrastructure.db.DBUtil;

public class KobisImporter {

	private static final Logger log = AppLogger.get(KobisImporter.class);

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
		log.info("KobisImporter 초기화 - API 키 설정 여부: " + (apiKey != null && !apiKey.isBlank() ? "OK (길이=" + apiKey.length() + ")" : "없음 또는 빈값"));
	}
	
	private String mapRating(String kobisRating) {
	    if (kobisRating == null) return "전체";

	    return switch (kobisRating) {
	        case "전체관람가" -> "전체";
	        case "12세이상관람가" -> "12세+";
	        case "15세이상관람가" -> "15세+";
	        case "청소년관람불가" -> "19세+";
	        default -> "전체";
	    };
	}

	public void importMovies() throws Exception {
		String yesterday = LocalDate.now().minusDays(1)
			.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		log.info("KOBIS 박스오피스 조회 날짜: " + yesterday);

		// 1단계: API에서 데이터 전부 수집 (DB 건드리기 전)
		String boxOfficeUrl = BOX_OFFICE_URL + "?key=" + apiKey + "&targetDt=" + yesterday;
		log.info("박스오피스 API 요청: " + boxOfficeUrl.replace(apiKey, "****"));
		String json = fetch(boxOfficeUrl);
		log.info("박스오피스 응답 수신 (앞 200자): " + json.substring(0, Math.min(200, json.length())));

		JsonArray dailyList = gson.fromJson(json, JsonObject.class)
			.getAsJsonObject("boxOfficeResult")
			.getAsJsonArray("dailyBoxOfficeList");
		log.info("박스오피스 항목 수: " + (dailyList != null ? dailyList.size() : 0));

		record MovieRow(String title, String director, String rating, int runtime) {}
		List<MovieRow> rows = new ArrayList<>();

		for (JsonElement elem : dailyList) {
			JsonObject item = elem.getAsJsonObject();
			String movieCd = item.get("movieCd").getAsString();
			String movieNm = item.get("movieNm").getAsString();
			log.info("영화 상세 조회: " + movieNm + " (코드=" + movieCd + ")");

			String detailJson = fetch(MOVIE_INFO_URL + "?key=" + apiKey + "&movieCd=" + movieCd);
			JsonObject movieInfo = gson.fromJson(detailJson, JsonObject.class)
				.getAsJsonObject("movieInfoResult")
				.getAsJsonObject("movieInfo");

			int runtime = 0;
			try { runtime = Integer.parseInt(safeStr(movieInfo, "showTm")); } catch (NumberFormatException ignored) {}

			rows.add(new MovieRow(
				movieNm,
				extractFirst(movieInfo.getAsJsonArray("directors"), "peopleNm"),
				extractFirst(movieInfo.getAsJsonArray("audits"), "watchGradeNm"),
				runtime
			));
			log.info("수집 완료: " + movieNm + " | 감독=" + rows.get(rows.size()-1).director() + " | 등급=" + rows.get(rows.size()-1).rating() + " | 러닝타임=" + runtime);
		}

		log.info("API 수집 완료 - 총 " + rows.size() + "편. DB 저장 시작...");

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
				"INSERT INTO movie(title, director, rating, runtime) VALUES(?,?,?,?)");

			for (MovieRow m : rows) {
				psmt.setString(1, m.title());
				psmt.setString(2, m.director());
				psmt.setString(3, mapRating(m.rating()));
				psmt.setInt(4, m.runtime());
				psmt.executeUpdate();
			}
			psmt.close();
			conn.commit();
			log.info("DB 저장 완료 - " + rows.size() + "편 삽입");
		} catch (Exception e) {
			conn.rollback();
			log.warning("DB 저장 중 오류 발생: " + e.getMessage());
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
