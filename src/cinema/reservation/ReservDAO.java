package cinema.reservation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import cinema.util.ComboItem;
import cinema.util.DBUtil;

public class ReservDAO {

	public Vector<Vector<String>> readAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("reservSelectAll"));

		Vector<Vector<String>> list = new Vector<>();
		while (rs.next()) {
			Vector<String> row = new Vector<>();
			row.add(rs.getString("reservid"));
			row.add(rs.getString("cust_name"));
			row.add(rs.getString("title"));
			row.add(rs.getString("theater_name"));
			row.add(rs.getString("showtime"));
			row.add(rs.getString("seatno"));
			row.add(rs.getString("price"));
			row.add(rs.getString("reservdate"));
			row.add(rs.getString("custid"));    // index 8 - hidden
			row.add(rs.getString("screenid"));  // index 9 - hidden
			list.add(row);
		}
		stmt.close(); rs.close();
		return list;
	}

	public int insertOne(String custid, String screenid, String seatno) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("reservInsertOne"));
		psmt.setString(1, custid);
		psmt.setString(2, screenid);
		psmt.setString(3, seatno);
		return psmt.executeUpdate();
	}

	public int updateOne(String custid, String screenid, String seatno, String reservid) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("reservUpdateOne"));
		psmt.setString(1, custid);
		psmt.setString(2, screenid);
		psmt.setString(3, seatno);
		psmt.setString(4, reservid);
		return psmt.executeUpdate();
	}

	public int deleteOne(String reservid) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("reservDeleteOne"));
		psmt.setString(1, reservid);
		return psmt.executeUpdate();
	}

	public Vector<ComboItem> readCustomerOptions() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("reservSelectCustOpts"));
		Vector<ComboItem> list = new Vector<>();
		while (rs.next()) {
			list.add(new ComboItem(rs.getString("custid"), rs.getString("name")));
		}
		stmt.close(); rs.close();
		return list;
	}

	public Vector<ComboItem> readScreeningOptions() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("reservSelectScreenOpts"));
		Vector<ComboItem> list = new Vector<>();
		while (rs.next()) {
			list.add(new ComboItem(rs.getString("screenid"), rs.getString("info")));
		}
		stmt.close(); rs.close();
		return list;
	}

}
