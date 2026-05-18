package domain.customer;

import infrastructure.customer.CustDAO;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {

	private final CustDAO dao;

	public CustomerService(CustDAO dao) {
		this.dao = dao;
	}

	public List<Customer> findAll() throws SQLException {
		return dao.findAll();
	}

	public void save(Customer customer) throws SQLException {
		dao.insertOne(customer);
	}

	public void update(Customer customer) throws SQLException {
		dao.updateOne(customer);
	}

	public void delete(int custId) throws SQLException {
		dao.deleteOne(custId);
	}

}
