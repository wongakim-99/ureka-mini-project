package domain.reservation;

import java.sql.SQLException;
import java.util.List;

import common.ComboItem;

public class ReservationService {

	private final ReservationRepository repository;
	private static final String SOLD_OUT_MESSAGE = "남은 좌석이 없습니다. 예약할 수 없습니다.";

	public ReservationService(ReservationRepository repository) {
		this.repository = repository;
	}

	public List<Reservation> findAll() throws SQLException             { return repository.findAll(); }
	public void save(Reservation reservation) throws SQLException {
		if (repository.findRemainSeatsByScreening(reservation.getScreenId()) <= 0) {
			throw new SQLException(SOLD_OUT_MESSAGE);
		}
		repository.save(reservation);
	}
	public void update(Reservation reservation) throws SQLException    { repository.update(reservation); }
	public void delete(int reservId) throws SQLException               { repository.delete(reservId); }
	public List<ComboItem> getCustomerOptions() throws SQLException              { return repository.findCustomerOptions(); }
	public List<ComboItem> getMovieOptions() throws SQLException                { return repository.findMovieOptions(); }
	public List<ComboItem> getScreeningsByMovie(int movieId) throws SQLException { return repository.findScreeningsByMovie(movieId); }

}
