package cinema.theater;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import cinema.util.DBUtil;

public class TheaterDAO {

	public Vector<Vector<String>> readAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("theaterSelectAll"));

		Vector<Vector<String>> list = new Vector<>();
		while (rs.next()) {
			Vector<String> row = new Vector<>();
			row.add(rs.getString("theaterid"));
			row.add(rs.getString("name"));
			row.add(rs.getString("total_seats"));
			list.add(row);
		}
		stmt.close(); rs.close();
		return list;
	}

	public int insertOne(String name, String totalSeats) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("theaterInsertOne"));
		psmt.setString(1, name);
		psmt.setString(2, totalSeats);
		return psmt.executeUpdate();
	}

	public int updateOne(String name, String totalSeats, String theaterid) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("theaterUpdateOne"));
		psmt.setString(1, name);
		psmt.setString(2, totalSeats);
		psmt.setString(3, theaterid);
		return psmt.executeUpdate();
	}

	public int deleteOne(String theaterid) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("theaterDeleteOne"));
		psmt.setString(1, theaterid);
		return psmt.executeUpdate();
	}

}
