package infrastructure.persistence.reservation;

import domain.common.OptionItem;
import infrastructure.db.DBUtil;
import domain.reservation.Reservation;
import domain.reservation.ReservationRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationJdbcRepository implements ReservationRepository {

	@Override
	public List<Reservation> findAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("reservSelectAll"));
		List<Reservation> list = new ArrayList<>();
		while (rs.next()) {
			Reservation r = new Reservation();
			r.setReservId(rs.getInt("reservid")); r.setCustId(rs.getInt("custid"));
			r.setCustName(rs.getString("cust_name")); r.setScreenId(rs.getInt("screenid")); r.setMovieId(rs.getInt("movieid"));
			r.setMovieTitle(rs.getString("title")); r.setTheaterName(rs.getString("theater_name"));
			r.setShowtime(rs.getString("showtime")); r.setSeatNo(rs.getString("seatno"));
			r.setPrice(rs.getInt("price")); r.setReservDate(rs.getString("reservdate"));
			list.add(r);
		}
		stmt.close(); rs.close();
		return list;
	}

	@Override
	public void save(Reservation reservation) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("reservInsertOne"));
		psmt.setInt(1, reservation.getCustId()); psmt.setInt(2, reservation.getScreenId());
		psmt.setString(3, reservation.getSeatNo());
		psmt.executeUpdate();
	}

	@Override
	public void update(Reservation reservation) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("reservUpdateOne"));
		psmt.setInt(1, reservation.getCustId()); psmt.setInt(2, reservation.getScreenId());
		psmt.setString(3, reservation.getSeatNo()); psmt.setInt(4, reservation.getReservId());
		psmt.executeUpdate();
	}

	@Override
	public void delete(int reservId) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("reservDeleteOne"));
		psmt.setInt(1, reservId);
		psmt.executeUpdate();
	}

	@Override
	public int findRemainSeatsByScreening(int screenId) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("reservSelectRemainSeatsByScreening"));
		psmt.setInt(1, screenId);
		ResultSet rs = psmt.executeQuery();
		int remainSeats = 0;
		if (rs.next()) remainSeats = rs.getInt("remain_seats");
		psmt.close(); rs.close();
		return remainSeats;
	}

	@Override
	public List<OptionItem> findCustomerOptions() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("reservSelectCustOpts"));
		List<OptionItem> list = new ArrayList<>();
		while (rs.next()) list.add(new OptionItem(rs.getInt("custid"), rs.getString("name")));
		stmt.close(); rs.close();
		return list;
	}

	@Override
	public List<OptionItem> findMovieOptions() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("reservSelectMovieOpts"));
		List<OptionItem> list = new ArrayList<>();
		while (rs.next()) list.add(new OptionItem(rs.getInt("movieid"), rs.getString("title")));
		stmt.close(); rs.close();
		return list;
	}

	@Override
	public List<OptionItem> findScreeningsByMovie(int movieId) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("reservSelectScreeningsByMovie"));
		psmt.setInt(1, movieId);
		ResultSet rs = psmt.executeQuery();
		List<OptionItem> list = new ArrayList<>();
		while (rs.next()) list.add(new OptionItem(rs.getInt("screenid"), rs.getString("info")));
		psmt.close(); rs.close();
		return list;
	}

}
