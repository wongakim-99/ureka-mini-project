package domain.reservation;

import common.ComboItem;
import java.sql.SQLException;
import java.util.List;

public class ReservationService {

	private final ReservationRepository repository;

	public ReservationService(ReservationRepository repository) {
		this.repository = repository;
	}

	public List<Reservation> findAll() throws SQLException             { return repository.findAll(); }
	public void save(Reservation reservation) throws SQLException      { repository.save(reservation); }
	public void update(Reservation reservation) throws SQLException    { repository.update(reservation); }
	public void delete(int reservId) throws SQLException               { repository.delete(reservId); }
	public List<ComboItem> getCustomerOptions() throws SQLException              { return repository.findCustomerOptions(); }
	public List<ComboItem> getMovieOptions() throws SQLException                { return repository.findMovieOptions(); }
	public List<ComboItem> getScreeningsByMovie(int movieId) throws SQLException { return repository.findScreeningsByMovie(movieId); }

}
