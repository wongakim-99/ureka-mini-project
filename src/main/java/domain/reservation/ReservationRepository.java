package domain.reservation;

import domain.common.OptionItem;
import java.sql.SQLException;
import java.util.List;

public interface ReservationRepository {
	List<Reservation> findAll() throws SQLException;
	void save(Reservation reservation) throws SQLException;
	void update(Reservation reservation) throws SQLException;
	void delete(int reservId) throws SQLException;
	int findRemainSeatsByScreening(int screenId) throws SQLException;
	boolean existsByScreenIdAndSeatNo(int screenId, String seatNo, int excludeReservationId) throws SQLException;
	List<OptionItem> findCustomerOptions() throws SQLException;
	List<OptionItem> findMovieOptions() throws SQLException;
	List<OptionItem> findScreeningsByMovie(int movieId) throws SQLException;
	Integer findCustomerAge(int custId) throws SQLException;
	String findMovieRatingByScreening(int screenId) throws SQLException;
}
