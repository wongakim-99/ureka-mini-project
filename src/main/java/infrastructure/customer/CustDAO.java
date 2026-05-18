package infrastructure.customer;

import common.DBUtil;
import domain.customer.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustDAO {

	public List<Customer> findAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("custSelectAll"));

		List<Customer> list = new ArrayList<>();
		while (rs.next()) {
			list.add(new Customer(
				rs.getInt("custid"),
				rs.getString("name"),
				rs.getString("phone"),
				rs.getString("email")
			));
		}
		stmt.close(); rs.close();
		return list;
	}

	public void insertOne(Customer customer) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custInsertOne"));
		psmt.setString(1, customer.getName());
		psmt.setString(2, customer.getPhone());
		psmt.setString(3, customer.getEmail());
		psmt.executeUpdate();
	}

	public void updateOne(Customer customer) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custUpdateOne"));
		psmt.setString(1, customer.getName());
		psmt.setString(2, customer.getPhone());
		psmt.setString(3, customer.getEmail());
		psmt.setInt(4, customer.getCustId());
		psmt.executeUpdate();
	}

	public void deleteOne(int custId) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custDeleteOne"));
		psmt.setInt(1, custId);
		psmt.executeUpdate();
	}

}
