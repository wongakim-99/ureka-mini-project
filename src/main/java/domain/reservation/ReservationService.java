package domain.reservation;

import common.ComboItem;
import infrastructure.reservation.ReservDAO;
import java.sql.SQLException;
import java.util.List;

public class ReservationService {

	private final ReservDAO dao;

	public ReservationService(ReservDAO dao) {
		this.dao = dao;
	}

	public List<Reservation> findAll() throws SQLException {
		return dao.findAll();
	}

	public void save(Reservation reservation) throws SQLException {
		dao.insertOne(reservation);
	}

	public void update(Reservation reservation) throws SQLException {
		dao.updateOne(reservation);
	}

	public void delete(int reservId) throws SQLException {
		dao.deleteOne(reservId);
	}

	public List<ComboItem> getCustomerOptions() throws SQLException {
		return dao.findCustomerOptions();
	}

	public List<ComboItem> getScreeningOptions() throws SQLException {
		return dao.findScreeningOptions();
	}

}
