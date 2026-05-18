package domain.movie;

import java.sql.SQLException;
import java.util.List;

public class MovieService {

	private final MovieRepository repository;

	public MovieService(MovieRepository repository) {
		this.repository = repository;
	}

	public List<Movie> findAll() throws SQLException   { return repository.findAll(); }
	public void save(Movie movie) throws SQLException  { repository.save(movie); }
	public void update(Movie movie) throws SQLException{ repository.update(movie); }
	public void delete(int movieId) throws SQLException{ repository.delete(movieId); }

}
