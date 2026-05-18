package infrastructure.reservation;

import common.ComboItem;
import common.DBUtil;
import domain.reservation.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservDAO {

	public List<Reservation> findAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("reservSelectAll"));

		List<Reservation> list = new ArrayList<>();
		while (rs.next()) {
			Reservation r = new Reservation();
			r.setReservId(rs.getInt("reservid"));
			r.setCustId(rs.getInt("custid"));
			r.setCustName(rs.getString("cust_name"));
			r.setScreenId(rs.getInt("screenid"));
			r.setMovieTitle(rs.getString("title"));
			r.setTheaterName(rs.getString("theater_name"));
			r.setShowtime(rs.getString("showtime"));
			r.setSeatNo(rs.getString("seatno"));
			r.setPrice(rs.getInt("price"));
			r.setReservDate(rs.getString("reservdate"));
			list.add(r);
		}
		stmt.close(); rs.close();
		return list;
	}

	public void insertOne(Reservation reservation) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("reservInsertOne"));
		psmt.setInt(1, reservation.getCustId());
		psmt.setInt(2, reservation.getScreenId());
		psmt.setString(3, reservation.getSeatNo());
		psmt.executeUpdate();
	}

	public void updateOne(Reservation reservation) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("reservUpdateOne"));
		psmt.setInt(1, reservation.getCustId());
		psmt.setInt(2, reservation.getScreenId());
		psmt.setString(3, reservation.getSeatNo());
		psmt.setInt(4, reservation.getReservId());
		psmt.executeUpdate();
	}

	public void deleteOne(int reservId) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("reservDeleteOne"));
		psmt.setInt(1, reservId);
		psmt.executeUpdate();
	}

	public List<ComboItem> findCustomerOptions() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("reservSelectCustOpts"));
		List<ComboItem> list = new ArrayList<>();
		while (rs.next()) list.add(new ComboItem(rs.getInt("custid"), rs.getString("name")));
		stmt.close(); rs.close();
		return list;
	}

	public List<ComboItem> findScreeningOptions() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("reservSelectScreenOpts"));
		List<ComboItem> list = new ArrayList<>();
		while (rs.next()) list.add(new ComboItem(rs.getInt("screenid"), rs.getString("info")));
		stmt.close(); rs.close();
		return list;
	}

}
