package domain.movie;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class MovieService {

	private final MovieRepository repository;

	public MovieService(MovieRepository repository) {
		this.repository = repository;
	}

	public List<Movie> findAll() throws SQLException { return repository.findAll(); }

	public void save(Movie movie) throws SQLException {
		normalize(movie);
		if (repository.existsByTitle(movie.getTitle(), 0)) {
			throw new SQLException("이미 동일한 제목의 영화가 등록되어 있습니다.");
		}
		try {
			repository.save(movie);
		} catch (SQLException e) {
			if (isDuplicate(e)) throw new SQLException("이미 동일한 제목의 영화가 등록되어 있습니다.");
			throw e;
		}
	}

	public void update(Movie movie) throws SQLException {
		normalize(movie);
		if (repository.existsByTitle(movie.getTitle(), movie.getMovieId())) {
			throw new SQLException("이미 동일한 제목의 영화가 등록되어 있습니다.");
		}
		try {
			repository.update(movie);
		} catch (SQLException e) {
			if (isDuplicate(e)) throw new SQLException("이미 동일한 제목의 영화가 등록되어 있습니다.");
			throw e;
		}
	}

	public void delete(int movieId) throws SQLException { repository.delete(movieId); }

	private void normalize(Movie movie) {
		movie.setTitle(normalizeText(movie.getTitle()));
		movie.setGenre(normalizeText(movie.getGenre()));
		movie.setDirector(normalizeText(movie.getDirector()));
		movie.setRating(normalizeText(movie.getRating()));
	}

	private String normalizeText(String value) {
		return value == null ? null : value.trim().replaceAll("\\s+", " ");
	}

	private boolean isDuplicate(SQLException e) {
		return e instanceof SQLIntegrityConstraintViolationException
			|| "23000".equals(e.getSQLState())
			|| (e.getMessage() != null && e.getMessage().contains("Duplicate entry"));
	}

}
