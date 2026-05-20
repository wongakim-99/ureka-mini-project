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
		try (Statement stmt = DBUtil.getConnection().createStatement();
			 ResultSet rs = stmt.executeQuery(DBUtil.getSQL("custSelectAll"))) {
			List<Customer> list = new ArrayList<>();
			while (rs.next()) {
				int age = rs.getInt("age");
				Integer ageVal = rs.wasNull() ? null : age;
				list.add(new Customer(rs.getInt("custid"), rs.getString("name"),
					rs.getString("phone"), rs.getString("email"), ageVal));
			}
			return list;
		}
	}

	@Override
	public void save(Customer customer) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custInsertOne"))) {
			psmt.setString(1, customer.getName()); psmt.setString(2, customer.getPhone());
			psmt.setString(3, customer.getEmail());
			if (customer.getAge() != null) psmt.setInt(4, customer.getAge());
			else psmt.setNull(4, java.sql.Types.INTEGER);
			psmt.executeUpdate();
		}
	}

	@Override
	public void update(Customer customer) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custUpdateOne"))) {
			psmt.setString(1, customer.getName()); psmt.setString(2, customer.getPhone());
			psmt.setString(3, customer.getEmail());
			if (customer.getAge() != null) psmt.setInt(4, customer.getAge());
			else psmt.setNull(4, java.sql.Types.INTEGER);
			psmt.setInt(5, customer.getCustId());
			psmt.executeUpdate();
		}
	}

	@Override
	public void delete(int custId) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custDeleteOne"))) {
			psmt.setInt(1, custId);
			psmt.executeUpdate();
		}
	}

	@Override
	public boolean existsByPhone(String phone, int excludeCustomerId) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custExistsByPhone"))) {
			psmt.setString(1, phone);
			psmt.setInt(2, excludeCustomerId);
			try (ResultSet rs = psmt.executeQuery()) {
				return rs.next() && rs.getInt("cnt") > 0;
			}
		}
	}

	@Override
	public boolean hasReservations(int custId) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("custHasReservations"))) {
			psmt.setInt(1, custId);
			try (ResultSet rs = psmt.executeQuery()) {
				return rs.next() && rs.getInt("cnt") > 0;
			}
		}
	}

}
