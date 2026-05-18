package infrastructure.theater;

import common.DBUtil;
import domain.theater.Theater;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TheaterDAO {

	public List<Theater> findAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("theaterSelectAll"));

		List<Theater> list = new ArrayList<>();
		while (rs.next()) {
			list.add(new Theater(
				rs.getInt("theaterid"),
				rs.getString("name"),
				rs.getInt("total_seats")
			));
		}
		stmt.close(); rs.close();
		return list;
	}

	public void insertOne(Theater theater) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("theaterInsertOne"));
		psmt.setString(1, theater.getName());
		psmt.setInt(2, theater.getTotalSeats());
		psmt.executeUpdate();
	}

	public void updateOne(Theater theater) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("theaterUpdateOne"));
		psmt.setString(1, theater.getName());
		psmt.setInt(2, theater.getTotalSeats());
		psmt.setInt(3, theater.getTheaterId());
		psmt.executeUpdate();
	}

	public void deleteOne(int theaterId) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("theaterDeleteOne"));
		psmt.setInt(1, theaterId);
		psmt.executeUpdate();
	}

}
