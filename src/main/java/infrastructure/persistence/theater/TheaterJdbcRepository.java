package infrastructure.persistence.theater;

import infrastructure.db.DBUtil;
import domain.theater.Theater;
import domain.theater.TheaterRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TheaterJdbcRepository implements TheaterRepository {

	@Override
	public List<Theater> findAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("theaterSelectAll"));
		List<Theater> list = new ArrayList<>();
		while (rs.next()) {
			list.add(new Theater(rs.getInt("theaterid"), rs.getString("name"), rs.getInt("total_seats")));
		}
		stmt.close(); rs.close();
		return list;
	}

	@Override
	public void save(Theater theater) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("theaterInsertOne"));
		psmt.setString(1, theater.getName()); psmt.setInt(2, theater.getTotalSeats());
		psmt.executeUpdate();
	}

	@Override
	public void update(Theater theater) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("theaterUpdateOne"));
		psmt.setString(1, theater.getName()); psmt.setInt(2, theater.getTotalSeats());
		psmt.setInt(3, theater.getTheaterId());
		psmt.executeUpdate();
	}

	@Override
	public void delete(int theaterId) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("theaterDeleteOne"));
		psmt.setInt(1, theaterId);
		psmt.executeUpdate();
	}

	@Override
	public boolean existsByName(String name, int excludeTheaterId) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("theaterExistsByName"));
		psmt.setString(1, name);
		psmt.setInt(2, excludeTheaterId);
		ResultSet rs = psmt.executeQuery();
		boolean exists = rs.next() && rs.getInt("cnt") > 0;
		rs.close(); psmt.close();
		return exists;
	}

}
