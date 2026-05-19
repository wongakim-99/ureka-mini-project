package domain.customer;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class CustomerService {

	private final CustomerRepository repository;

	public CustomerService(CustomerRepository repository) {
		this.repository = repository;
	}

	public List<Customer> findAll() throws SQLException { return repository.findAll(); }

	public void save(Customer customer) throws SQLException {
		try {
			repository.save(customer);
		} catch (SQLException e) {
			if (isDuplicate(e)) throw new SQLException("이미 동일한 전화번호로 등록된 고객이 있습니다.");
			throw e;
		}
	}

	public void update(Customer customer) throws SQLException {
		try {
			repository.update(customer);
		} catch (SQLException e) {
			if (isDuplicate(e)) throw new SQLException("이미 동일한 전화번호로 등록된 고객이 있습니다.");
			throw e;
		}
	}

	public void delete(int custId) throws SQLException { repository.delete(custId); }

	private boolean isDuplicate(SQLException e) {
		return e instanceof SQLIntegrityConstraintViolationException
			|| "23000".equals(e.getSQLState())
			|| (e.getMessage() != null && e.getMessage().contains("Duplicate entry"));
	}

}
