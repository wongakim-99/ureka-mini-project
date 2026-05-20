package domain.reservation;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import domain.common.OptionItem;

public class ReservationService {

	private final ReservationRepository repository;
	private static final String SOLD_OUT_MESSAGE = "남은 좌석이 없습니다. 예약할 수 없습니다.";
	private static final String SEAT_EMPTY_MESSAGE = "좌석번호를 입력해주세요.";
	private static final String DUPLICATE_SEAT_MESSAGE = "이미 해당 좌석은 예약되어 있습니다.";
	private static final String AGE_MISSING_MESSAGE = "고객의 나이 정보가 없습니다. 예약할 수 없습니다.";
	private static final String AGE_RESTRICTED_MESSAGE = "관람 연령 제한(%d세 이상)으로 예약할 수 없습니다.";

	public ReservationService(ReservationRepository repository) {
		this.repository = repository;
	}

	public List<Reservation> findAll() throws SQLException             { return repository.findAll(); }
	public void save(Reservation reservation) throws SQLException {
		validateSeatNo(reservation.getSeatNo());
		validateAgeRestriction(reservation.getCustId(), reservation.getScreenId());
		int requestedSeats = reservation.getSeatNo().split(",").length;
		if (repository.findRemainSeatsByScreening(reservation.getScreenId()) < requestedSeats) {
			throw new SQLException(SOLD_OUT_MESSAGE);
		}
		if (repository.existsByScreenIdAndSeatNo(reservation.getScreenId(), reservation.getSeatNo(), 0)) {
			throw new SQLException(DUPLICATE_SEAT_MESSAGE);
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
		validateAgeRestriction(reservation.getCustId(), reservation.getScreenId());
		if (repository.existsByScreenIdAndSeatNo(reservation.getScreenId(), reservation.getSeatNo(), reservation.getReservId())) {
			throw new SQLException(DUPLICATE_SEAT_MESSAGE);
		}
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

	private void validateAgeRestriction(int custId, int screenId) throws SQLException {
		String rating = repository.findMovieRatingByScreening(screenId);
		int ageLimit = extractAgeLimit(rating);
		if (ageLimit == 0) return;
		Integer customerAge = repository.findCustomerAge(custId);
		if (customerAge == null) {
			throw new SQLException(AGE_MISSING_MESSAGE);
		}
		if (customerAge < ageLimit) {
			throw new SQLException(String.format(AGE_RESTRICTED_MESSAGE, ageLimit));
		}
	}

	private int extractAgeLimit(String rating) {
		if (rating == null || rating.isBlank()) return 0;
		if (rating.contains("청소년관람불가")) return 19;
		Matcher m = Pattern.compile("\\d+").matcher(rating);
		return m.find() ? Integer.parseInt(m.group()) : 0;
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
