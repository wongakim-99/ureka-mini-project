package infrastructure.persistence.movie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import infrastructure.db.DBUtil;
import domain.movie.Movie;
import domain.movie.MovieRepository;

public class MovieJdbcRepository implements MovieRepository {

	@Override
	public List<Movie> findAll() throws SQLException {
		try (Statement stmt = DBUtil.getConnection().createStatement();
			 ResultSet rs = stmt.executeQuery(DBUtil.getSQL("movieSelectAll"))) {
			List<Movie> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Movie(rs.getInt("movieid"), rs.getString("title"),
					rs.getString("director"), rs.getString("rating"),
					rs.getInt("runtime")));
			}
			return list;
		}
	}

	@Override
	public void save(Movie movie) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("movieInsertOne"))) {
			psmt.setString(1, movie.getTitle());
			psmt.setString(2, movie.getDirector()); psmt.setString(3, movie.getRating());
			psmt.setInt(4, movie.getRuntime());
			psmt.executeUpdate();
		}
	}

	@Override
	public void update(Movie movie) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("movieUpdateOne"))) {
			psmt.setString(1, movie.getTitle());
			psmt.setString(2, movie.getDirector()); psmt.setString(3, movie.getRating());
			psmt.setInt(4, movie.getRuntime()); psmt.setInt(5, movie.getMovieId());
			psmt.executeUpdate();
		}
	}

	@Override
	public void delete(int movieId) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("movieDeleteOne"))) {
			psmt.setInt(1, movieId);
			psmt.executeUpdate();
		}
	}

	@Override
	public boolean existsByTitle(String title, int excludeMovieId) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("movieExistsByTitle"))) {
			psmt.setString(1, title);
			psmt.setInt(2, excludeMovieId);
			try (ResultSet rs = psmt.executeQuery()) {
				return rs.next() && rs.getInt("cnt") > 0;
			}
		}
	}

}
