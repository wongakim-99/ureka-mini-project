package infrastructure.db;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

	private static Connection connection;
	private static Properties dbProperties;
	private static Properties sqlProperties;

	private DBUtil() {}

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			dbProperties = new Properties();
			dbProperties.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));

			sqlProperties = new Properties();
			sqlProperties.load(new InputStreamReader(
				DBUtil.class.getClassLoader().getResourceAsStream("sql.properties"),
				StandardCharsets.UTF_8
			));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized Connection getConnection() throws SQLException {
		if (connection == null) {
			connection = DriverManager.getConnection(
				dbProperties.getProperty("url"),
				dbProperties.getProperty("user"),
				dbProperties.getProperty("password")
			);
		}
		return connection;
	}

	public static void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	public static String getSQL(String key) {
		return sqlProperties.getProperty(key);
	}

	public static String getDbProperty(String key) {
		return dbProperties.getProperty(key);
	}

}
