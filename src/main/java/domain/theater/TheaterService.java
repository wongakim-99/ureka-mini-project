package domain.theater;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class TheaterService {

	private final TheaterRepository repository;
	private static final String DUPLICATE_NAME_MESSAGE = "이미 동일한 상영관명이 등록되어 있습니다.";
	private static final String NAME_EMPTY_MESSAGE = "상영관명을 입력해주세요.";

	public TheaterService(TheaterRepository repository) {
		this.repository = repository;
	}

	public List<Theater> findAll() throws SQLException      { return repository.findAll(); }
	public void save(Theater theater) throws SQLException {
		theater.setName(normalizeName(theater.getName()));
		validateName(theater.getName());
		if (repository.existsByName(theater.getName(), 0)) {
			throw new SQLException(DUPLICATE_NAME_MESSAGE);
		}
		try {
			repository.save(theater);
		} catch (SQLException e) {
			if (isDuplicateNameException(e)) {
				throw new SQLException(DUPLICATE_NAME_MESSAGE);
			}
			throw e;
		}
	}
	public void update(Theater theater) throws SQLException {
		theater.setName(normalizeName(theater.getName()));
		validateName(theater.getName());
		if (repository.existsByName(theater.getName(), theater.getTheaterId())) {
			throw new SQLException(DUPLICATE_NAME_MESSAGE);
		}
		try {
			repository.update(theater);
		} catch (SQLException e) {
			if (isDuplicateNameException(e)) {
				throw new SQLException(DUPLICATE_NAME_MESSAGE);
			}
			throw e;
		}
	}
	public void delete(int theaterId) throws SQLException   { repository.delete(theaterId); }

	private String normalizeName(String name) {
		return name == null ? null : name.trim().replaceAll("\\s+", " ");
	}

	private void validateName(String name) throws SQLException {
		if (name == null || name.isEmpty()) {
			throw new SQLException(NAME_EMPTY_MESSAGE);
		}
	}

	private boolean isDuplicateNameException(SQLException e) {
		return e instanceof SQLIntegrityConstraintViolationException
			|| "23000".equals(e.getSQLState())
			|| (e.getMessage() != null && e.getMessage().contains("Duplicate entry"));
	}

}
