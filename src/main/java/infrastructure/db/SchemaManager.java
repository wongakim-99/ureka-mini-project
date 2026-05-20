package infrastructure.db;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import infrastructure.AppLogger;

public class SchemaManager {

    private static final Logger log = AppLogger.get(SchemaManager.class);

    public static void run() {
        String mode = DBUtil.getDbProperty("ddl.auto");
        if (mode == null || "none".equals(mode)) return;

        try {
            Connection conn = DBUtil.getConnection();
            switch (mode) {
                case "create" -> runCreate(conn);
                case "update" -> runUpdate(conn);
                default -> log.warning("알 수 없는 ddl.auto 값: " + mode);
            }
        } catch (Exception e) {
            log.warning("스키마 초기화 실패: " + e.getMessage());
        }
    }

    private static void runCreate(Connection conn) throws Exception {
        log.info("스키마 초기화 시작 (create)");
        InputStream is = SchemaManager.class.getClassLoader().getResourceAsStream("schema.sql");
        if (is == null) {
            log.warning("schema.sql 을 classpath에서 찾을 수 없습니다.");
            return;
        }
        String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        for (String raw : sql.split(";")) {
            String stmt = raw.replaceAll("--[^\n]*", "").trim();
            if (stmt.isEmpty()) continue;
            try {
                conn.createStatement().execute(stmt);
            } catch (SQLException e) {
                log.fine("건너뜀: " + stmt.substring(0, Math.min(60, stmt.length())).replace('\n', ' ') + " → " + e.getMessage());
            }
        }
        log.info("스키마 초기화 완료");
    }

    private static void runUpdate(Connection conn) throws Exception {
        log.info("스키마 업데이트 시작 (update)");
        String db = conn.getCatalog();
        addColumnIfMissing(conn, db, "movie", "runtime",  "INT DEFAULT 0");
        addColumnIfMissing(conn, db, "movie", "director", "VARCHAR(100)");
        addColumnIfMissing(conn, db, "movie", "rating",   "VARCHAR(50)");
        addUniqueIndexIfMissing(conn, db, "movie",       "uk_movie_title",                  "title",              "title");
        addUniqueIndexIfMissing(conn, db, "theater",     "uk_theater_name",                 "name",               "name");
        addUniqueIndexIfMissing(conn, db, "screening",   "uk_screening_theater_showtime",   "theaterid, showtime","theaterid, showtime");
        addUniqueIndexIfMissing(conn, db, "customer",    "uk_customer_phone",               "phone",              "phone");
        addUniqueIndexIfMissing(conn, db, "reservation", "uk_reservation_screen_seat",      "screenid, seatno",   "screenid, seatno");
        log.info("스키마 업데이트 완료");
    }

    private static void addColumnIfMissing(Connection conn, String db, String table, String column, String definition) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=? AND TABLE_NAME=? AND COLUMN_NAME=?"
        );
        ps.setString(1, db); ps.setString(2, table); ps.setString(3, column);
        ResultSet rs = ps.executeQuery();
        rs.next();
        boolean missing = rs.getInt(1) == 0;
        rs.close(); ps.close();

        if (missing) {
            conn.createStatement().execute("ALTER TABLE " + table + " ADD COLUMN " + column + " " + definition);
            log.info("컬럼 추가: " + table + "." + column);
        }
    }

    private static void addUniqueIndexIfMissing(
        Connection conn, String db, String table,
        String indexName, String columns, String duplicateGroupColumns
    ) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT COUNT(*) FROM information_schema.STATISTICS WHERE TABLE_SCHEMA=? AND TABLE_NAME=? AND INDEX_NAME=?"
        );
        ps.setString(1, db); ps.setString(2, table); ps.setString(3, indexName);
        ResultSet rs = ps.executeQuery();
        rs.next();
        boolean missing = rs.getInt(1) == 0;
        rs.close(); ps.close();

        if (!missing) return;
        if (hasDuplicateRows(conn, table, duplicateGroupColumns)) {
            log.warning("중복 데이터가 있어 UNIQUE 인덱스 추가를 건너뜀: " + table + "." + indexName);
            return;
        }

        conn.createStatement().execute("CREATE UNIQUE INDEX " + indexName + " ON " + table + " (" + columns + ")");
        log.info("UNIQUE 인덱스 추가: " + table + "." + indexName);
    }

    private static boolean hasDuplicateRows(Connection conn, String table, String groupColumns) throws SQLException {
        String sql = "SELECT COUNT(*) AS cnt FROM (SELECT 1 FROM " + table
            + " GROUP BY " + groupColumns + " HAVING COUNT(*) > 1) duplicates";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        boolean hasDuplicates = rs.getInt("cnt") > 0;
        rs.close(); stmt.close();
        return hasDuplicates;
    }
}
