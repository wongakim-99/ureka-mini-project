package cinema.movie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import cinema.util.DBUtil;

public class MovieDAO {

	public Vector<Vector<String>> readAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("movieSelectAll"));

		Vector<Vector<String>> list = new Vector<>();
		while (rs.next()) {
			Vector<String> row = new Vector<>();
			row.add(rs.getString("movieid"));
			row.add(rs.getString("title"));
			row.add(rs.getString("genre"));
			row.add(rs.getString("director"));
			row.add(rs.getString("rating"));
			list.add(row);
		}
		stmt.close(); rs.close();
		return list;
	}

	public int insertOne(String title, String genre, String director, String rating) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("movieInsertOne"));
		psmt.setString(1, title);
		psmt.setString(2, genre);
		psmt.setString(3, director);
		psmt.setString(4, rating);
		return psmt.executeUpdate();
	}

	public int updateOne(String title, String genre, String director, String rating, String movieid) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("movieUpdateOne"));
		psmt.setString(1, title);
		psmt.setString(2, genre);
		psmt.setString(3, director);
		psmt.setString(4, rating);
		psmt.setString(5, movieid);
		return psmt.executeUpdate();
	}

	public int deleteOne(String movieid) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("movieDeleteOne"));
		psmt.setString(1, movieid);
		return psmt.executeUpdate();
	}

}
