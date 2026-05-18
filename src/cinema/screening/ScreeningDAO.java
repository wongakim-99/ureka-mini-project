package cinema.screening;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import cinema.util.ComboItem;
import cinema.util.DBUtil;

public class ScreeningDAO {

	public Vector<Vector<String>> readAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("screeningSelectAll"));

		Vector<Vector<String>> list = new Vector<>();
		while (rs.next()) {
			Vector<String> row = new Vector<>();
			row.add(rs.getString("screenid"));
			row.add(rs.getString("title"));
			row.add(rs.getString("theater_name"));
			row.add(rs.getString("showtime"));
			row.add(rs.getString("price"));
			row.add(rs.getString("remain_seats"));
			row.add(rs.getString("movieid"));    // index 6 - hidden
			row.add(rs.getString("theaterid"));  // index 7 - hidden
			list.add(row);
		}
		stmt.close(); rs.close();
		return list;
	}

	public int insertOne(String movieid, String theaterid, String showtime, String price) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("screeningInsertOne"));
		psmt.setString(1, movieid);
		psmt.setString(2, theaterid);
		psmt.setString(3, showtime);
		psmt.setString(4, price);
		return psmt.executeUpdate();
	}

	public int updateOne(String movieid, String theaterid, String showtime, String price, String screenid) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("screeningUpdateOne"));
		psmt.setString(1, movieid);
		psmt.setString(2, theaterid);
		psmt.setString(3, showtime);
		psmt.setString(4, price);
		psmt.setString(5, screenid);
		return psmt.executeUpdate();
	}

	public int deleteOne(String screenid) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("screeningDeleteOne"));
		psmt.setString(1, screenid);
		return psmt.executeUpdate();
	}

	public Vector<ComboItem> readMovieOptions() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("screeningSelectMovieOpts"));
		Vector<ComboItem> list = new Vector<>();
		while (rs.next()) {
			list.add(new ComboItem(rs.getString("movieid"), rs.getString("title")));
		}
		stmt.close(); rs.close();
		return list;
	}

	public Vector<ComboItem> readTheaterOptions() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("screeningSelectTheaterOpts"));
		Vector<ComboItem> list = new Vector<>();
		while (rs.next()) {
			list.add(new ComboItem(rs.getString("theaterid"), rs.getString("name")));
		}
		stmt.close(); rs.close();
		return list;
	}

}
