package domain.reservation;

import common.ComboItem;
import java.sql.SQLException;
import java.util.List;

public interface ReservationRepository {
	List<Reservation> findAll() throws SQLException;
	void save(Reservation reservation) throws SQLException;
	void update(Reservation reservation) throws SQLException;
	void delete(int reservId) throws SQLException;
	List<ComboItem> findCustomerOptions() throws SQLException;
	List<ComboItem> findScreeningOptions() throws SQLException;
}
