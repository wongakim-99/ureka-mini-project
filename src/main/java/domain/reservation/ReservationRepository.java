package domain.reservation;

import common.ComboItem;
import java.sql.SQLException;
import java.util.List;

public interface ReservationRepository {
	List<Reservation> findAll() throws SQLException;
	void save(Reservation reservation) throws SQLException;
	void update(Reservation reservation) throws SQLException;
	void delete(int reservId) throws SQLException;
	int findRemainSeatsByScreening(int screenId) throws SQLException;
	List<ComboItem> findCustomerOptions() throws SQLException;
	List<ComboItem> findMovieOptions() throws SQLException;
	List<ComboItem> findScreeningsByMovie(int movieId) throws SQLException;
}
