package domain.customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {

	private final CustomerRepository repository;

	public CustomerService(CustomerRepository repository) {
		this.repository = repository;
	}

	public List<Customer> findAll() throws SQLException        { return repository.findAll(); }
	public void save(Customer customer) throws SQLException    { repository.save(customer); }
	public void update(Customer customer) throws SQLException  { repository.update(customer); }
	public void delete(int custId) throws SQLException         { repository.delete(custId); }

}
