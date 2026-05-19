package domain.reservation;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import domain.common.OptionItem;

public class ReservationService {

	private final ReservationRepository repository;
	private static final String SOLD_OUT_MESSAGE = "남은 좌석이 없습니다. 예약할 수 없습니다.";
	private static final String SEAT_EMPTY_MESSAGE = "좌석번호를 입력해주세요.";
	private static final String DUPLICATE_SEAT_MESSAGE = "이미 해당 좌석은 예약되어 있습니다.";

	public ReservationService(ReservationRepository repository) {
		this.repository = repository;
	}

	public List<Reservation> findAll() throws SQLException             { return repository.findAll(); }
	public void save(Reservation reservation) throws SQLException {
		validateSeatNo(reservation.getSeatNo());
		if (repository.findRemainSeatsByScreening(reservation.getScreenId()) <= 0) {
			throw new SQLException(SOLD_OUT_MESSAGE);
		}
		try {
			repository.save(reservation);
		} catch (SQLException e) {
			if (isDuplicateSeatException(e)) {
				throw new SQLException(DUPLICATE_SEAT_MESSAGE);
			}
			throw e;
		}
	}
	public void update(Reservation reservation) throws SQLException {
		validateSeatNo(reservation.getSeatNo());
		try {
			repository.update(reservation);
		} catch (SQLException e) {
			if (isDuplicateSeatException(e)) {
				throw new SQLException(DUPLICATE_SEAT_MESSAGE);
			}
			throw e;
		}
	}
	public void delete(int reservId) throws SQLException               { repository.delete(reservId); }
	private void validateSeatNo(String seatNo) throws SQLException {
		if (seatNo == null || seatNo.trim().isEmpty()) {
			throw new SQLException(SEAT_EMPTY_MESSAGE);
		}
	}

	private boolean isDuplicateSeatException(SQLException e) {
		return e instanceof SQLIntegrityConstraintViolationException
			|| "23000".equals(e.getSQLState())
			|| e.getMessage() != null && e.getMessage().contains("Duplicate entry");
	}

	public List<OptionItem> getCustomerOptions() throws SQLException              { return repository.findCustomerOptions(); }
	public List<OptionItem> getMovieOptions() throws SQLException                { return repository.findMovieOptions(); }
	public List<OptionItem> getScreeningsByMovie(int movieId) throws SQLException { return repository.findScreeningsByMovie(movieId); }

}
