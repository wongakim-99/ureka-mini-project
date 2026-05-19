package domain.screening;

import common.ComboItem;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ScreeningService {

	private final ScreeningRepository repository;

	public ScreeningService(ScreeningRepository repository) {
		this.repository = repository;
	}

	public List<Screening> findAll() throws SQLException { return repository.findAll(); }

	public void save(Screening screening) throws SQLException {
		try {
			repository.save(screening);
		} catch (SQLException e) {
			if (isDuplicate(e)) throw new SQLException("해당 상영관에 같은 시간의 상영일정이 이미 존재합니다.");
			throw e;
		}
	}

	public void update(Screening screening) throws SQLException {
		try {
			repository.update(screening);
		} catch (SQLException e) {
			if (isDuplicate(e)) throw new SQLException("해당 상영관에 같은 시간의 상영일정이 이미 존재합니다.");
			throw e;
		}
	}

	public void delete(int screenId) throws SQLException          { repository.delete(screenId); }
	public List<ComboItem> getMovieOptions() throws SQLException  { return repository.findMovieOptions(); }
	public List<ComboItem> getTheaterOptions() throws SQLException{ return repository.findTheaterOptions(); }

	private boolean isDuplicate(SQLException e) {
		return e instanceof SQLIntegrityConstraintViolationException
			|| "23000".equals(e.getSQLState())
			|| (e.getMessage() != null && e.getMessage().contains("Duplicate entry"));
	}

}
