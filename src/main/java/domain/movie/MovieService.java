package domain.movie;

import infrastructure.movie.MovieDAO;
import java.sql.SQLException;
import java.util.List;

public class MovieService {

	private final MovieDAO dao;

	public MovieService(MovieDAO dao) {
		this.dao = dao;
	}

	public List<Movie> findAll() throws SQLException {
		return dao.findAll();
	}

	public void save(Movie movie) throws SQLException {
		dao.insertOne(movie);
	}

	public void update(Movie movie) throws SQLException {
		dao.updateOne(movie);
	}

	public void delete(int movieId) throws SQLException {
		dao.deleteOne(movieId);
	}

}
