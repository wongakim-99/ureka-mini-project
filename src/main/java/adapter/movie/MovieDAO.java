package adapter.movie;

import common.DBUtil;
import domain.movie.Movie;
import domain.movie.MovieRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements MovieRepository {

	@Override
	public List<Movie> findAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("movieSelectAll"));
		List<Movie> list = new ArrayList<>();
		while (rs.next()) {
			list.add(new Movie(rs.getInt("movieid"), rs.getString("title"),
				rs.getString("genre"), rs.getString("director"), rs.getString("rating")));
		}
		stmt.close(); rs.close();
		return list;
	}

	@Override
	public void save(Movie movie) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("movieInsertOne"));
		psmt.setString(1, movie.getTitle()); psmt.setString(2, movie.getGenre());
		psmt.setString(3, movie.getDirector()); psmt.setString(4, movie.getRating());
		psmt.executeUpdate();
	}

	@Override
	public void update(Movie movie) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("movieUpdateOne"));
		psmt.setString(1, movie.getTitle()); psmt.setString(2, movie.getGenre());
		psmt.setString(3, movie.getDirector()); psmt.setString(4, movie.getRating());
		psmt.setInt(5, movie.getMovieId());
		psmt.executeUpdate();
	}

	@Override
	public void delete(int movieId) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("movieDeleteOne"));
		psmt.setInt(1, movieId);
		psmt.executeUpdate();
	}

}
