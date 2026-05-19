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
		normalize(customer);
		if (repository.existsByPhone(customer.getPhone(), 0)) {
			throw new SQLException("이미 동일한 전화번호로 등록된 고객이 있습니다.");
		}
		try {
			repository.save(customer);
		} catch (SQLException e) {
			if (isDuplicate(e)) throw new SQLException("이미 동일한 전화번호로 등록된 고객이 있습니다.");
			throw e;
		}
	}

	public void update(Customer customer) throws SQLException {
		normalize(customer);
		if (repository.existsByPhone(customer.getPhone(), customer.getCustId())) {
			throw new SQLException("이미 동일한 전화번호로 등록된 고객이 있습니다.");
		}
		try {
			repository.update(customer);
		} catch (SQLException e) {
			if (isDuplicate(e)) throw new SQLException("이미 동일한 전화번호로 등록된 고객이 있습니다.");
			throw e;
		}
	}

	public void delete(int custId) throws SQLException { repository.delete(custId); }

	private void normalize(Customer customer) {
		customer.setName(normalizeText(customer.getName()));
		customer.setPhone(normalizeText(customer.getPhone()));
		customer.setEmail(normalizeText(customer.getEmail()));
	}

	private String normalizeText(String value) {
		return value == null ? null : value.trim().replaceAll("\\s+", " ");
	}

	private boolean isDuplicate(SQLException e) {
		return e instanceof SQLIntegrityConstraintViolationException
			|| "23000".equals(e.getSQLState())
			|| (e.getMessage() != null && e.getMessage().contains("Duplicate entry"));
	}

}
