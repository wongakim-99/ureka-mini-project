package domain.movie;

import java.sql.SQLException;
import java.util.List;

public interface MovieRepository {
	List<Movie> findAll() throws SQLException;
	void save(Movie movie) throws SQLException;
	void update(Movie movie) throws SQLException;
	void delete(int movieId) throws SQLException;
	boolean existsByTitle(String title, int excludeMovieId) throws SQLException;
}
