package cinema.customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import cinema.util.DBUtil;

public class CustDAO {

	public Vector<Vector<String>> readAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("custSelectAll"));

		Vector<Vector<String>> list = new Vector<>();
		while (rs.next()) {
			Vector<String> row = new Vector<>();
			row.add(rs.getString("custid"));
			row.add(rs.getString("name"));
			row.add(rs.getString("phone"));
			row.add(rs.getString("email"));
			list.add(row);
		}
		stmt.close(); rs.close();
		return list;
	}

	public int insertOne(String name, String phone, String email) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custInsertOne"));
		psmt.setString(1, name);
		psmt.setString(2, phone);
		psmt.setString(3, email);
		return psmt.executeUpdate();
	}

	public int updateOne(String name, String phone, String email, String custid) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custUpdateOne"));
		psmt.setString(1, name);
		psmt.setString(2, phone);
		psmt.setString(3, email);
		psmt.setString(4, custid);
		return psmt.executeUpdate();
	}

	public int deleteOne(String custid) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custDeleteOne"));
		psmt.setString(1, custid);
		return psmt.executeUpdate();
	}

}
