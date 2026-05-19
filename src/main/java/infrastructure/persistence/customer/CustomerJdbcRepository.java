package infrastructure.persistence.customer;

import infrastructure.db.DBUtil;
import domain.customer.Customer;
import domain.customer.CustomerRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerJdbcRepository implements CustomerRepository {

	@Override
	public List<Customer> findAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("custSelectAll"));
		List<Customer> list = new ArrayList<>();
		while (rs.next()) {
			list.add(new Customer(rs.getInt("custid"), rs.getString("name"),
				rs.getString("phone"), rs.getString("email")));
		}
		stmt.close(); rs.close();
		return list;
	}

	@Override
	public void save(Customer customer) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custInsertOne"));
		psmt.setString(1, customer.getName()); psmt.setString(2, customer.getPhone());
		psmt.setString(3, customer.getEmail());
		psmt.executeUpdate();
	}

	@Override
	public void update(Customer customer) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custUpdateOne"));
		psmt.setString(1, customer.getName()); psmt.setString(2, customer.getPhone());
		psmt.setString(3, customer.getEmail()); psmt.setInt(4, customer.getCustId());
		psmt.executeUpdate();
	}

	@Override
	public void delete(int custId) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custDeleteOne"));
		psmt.setInt(1, custId);
		psmt.executeUpdate();
	}

}
