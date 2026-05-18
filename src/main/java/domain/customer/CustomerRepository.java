package domain.customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerRepository {
	List<Customer> findAll() throws SQLException;
	void save(Customer customer) throws SQLException;
	void update(Customer customer) throws SQLException;
	void delete(int custId) throws SQLException;
}
