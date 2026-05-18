package adapter.screening;

import common.ComboItem;
import common.DBUtil;
import domain.screening.Screening;
import domain.screening.ScreeningRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScreeningDAO implements ScreeningRepository {

	@Override
	public List<Screening> findAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("screeningSelectAll"));
		List<Screening> list = new ArrayList<>();
		while (rs.next()) {
			Screening s = new Screening();
			s.setScreenId(rs.getInt("screenid")); s.setMovieId(rs.getInt("movieid"));
			s.setMovieTitle(rs.getString("title")); s.setTheaterId(rs.getInt("theaterid"));
			s.setTheaterName(rs.getString("theater_name")); s.setShowtime(rs.getString("showtime"));
			s.setPrice(rs.getInt("price")); s.setRemainSeats(rs.getInt("remain_seats"));
			list.add(s);
		}
		stmt.close(); rs.close();
		return list;
	}

	@Override
	public void save(Screening screening) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("screeningInsertOne"));
		psmt.setInt(1, screening.getMovieId()); psmt.setInt(2, screening.getTheaterId());
		psmt.setString(3, screening.getShowtime()); psmt.setInt(4, screening.getPrice());
		psmt.executeUpdate();
	}

	@Override
	public void update(Screening screening) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("screeningUpdateOne"));
		psmt.setInt(1, screening.getMovieId()); psmt.setInt(2, screening.getTheaterId());
		psmt.setString(3, screening.getShowtime()); psmt.setInt(4, screening.getPrice());
		psmt.setInt(5, screening.getScreenId());
		psmt.executeUpdate();
	}

	@Override
	public void delete(int screenId) throws SQLException {
		PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("screeningDeleteOne"));
		psmt.setInt(1, screenId);
		psmt.executeUpdate();
	}

	@Override
	public List<ComboItem> findMovieOptions() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("screeningSelectMovieOpts"));
		List<ComboItem> list = new ArrayList<>();
		while (rs.next()) list.add(new ComboItem(rs.getInt("movieid"), rs.getString("title")));
		stmt.close(); rs.close();
		return list;
	}

	@Override
	public List<ComboItem> findTheaterOptions() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(DBUtil.getSQL("screeningSelectTheaterOpts"));
		List<ComboItem> list = new ArrayList<>();
		while (rs.next()) list.add(new ComboItem(rs.getInt("theaterid"), rs.getString("name")));
		stmt.close(); rs.close();
		return list;
	}

}
