package cinema.util;

import java.io.IOException;
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
			dbProperties.load(DBUtil.class.getResourceAsStream("db.properties"));

			sqlProperties = new Properties();
			sqlProperties.load(DBUtil.class.getResourceAsStream("sql.properties"));
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

	public static String getSQL(String propertyName) {
		return sqlProperties.getProperty(propertyName);
	}

}
