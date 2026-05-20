package infrastructure.persistence.screening;

import domain.common.OptionItem;
import infrastructure.db.DBUtil;
import domain.screening.Screening;
import domain.screening.ScreeningRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScreeningJdbcRepository implements ScreeningRepository {

	@Override
	public List<Screening> findAll() throws SQLException {
		try (Statement stmt = DBUtil.getConnection().createStatement();
			 ResultSet rs = stmt.executeQuery(DBUtil.getSQL("screeningSelectAll"))) {
			List<Screening> list = new ArrayList<>();
			while (rs.next()) {
				Screening s = new Screening();
				s.setScreenId(rs.getInt("screenid")); s.setMovieId(rs.getInt("movieid"));
				s.setMovieTitle(rs.getString("title")); s.setTheaterId(rs.getInt("theaterid"));
				s.setTheaterName(rs.getString("theater_name")); s.setShowtime(rs.getString("showtime"));
				s.setPrice(rs.getInt("price")); s.setRemainSeats(rs.getInt("remain_seats"));
				list.add(s);
			}
			return list;
		}
	}

	@Override
	public void save(Screening screening) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("screeningInsertOne"))) {
			psmt.setInt(1, screening.getMovieId()); psmt.setInt(2, screening.getTheaterId());
			psmt.setString(3, screening.getShowtime()); psmt.setInt(4, screening.getPrice());
			psmt.executeUpdate();
		}
	}

	@Override
	public void update(Screening screening) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("screeningUpdateOne"))) {
			psmt.setInt(1, screening.getMovieId()); psmt.setInt(2, screening.getTheaterId());
			psmt.setString(3, screening.getShowtime()); psmt.setInt(4, screening.getPrice());
			psmt.setInt(5, screening.getScreenId());
			psmt.executeUpdate();
		}
	}

	@Override
	public void delete(int screenId) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("screeningDeleteOne"))) {
			psmt.setInt(1, screenId);
			psmt.executeUpdate();
		}
	}

	@Override
	public List<OptionItem> findMovieOptions() throws SQLException {
		try (Statement stmt = DBUtil.getConnection().createStatement();
			 ResultSet rs = stmt.executeQuery(DBUtil.getSQL("screeningSelectMovieOpts"))) {
			List<OptionItem> list = new ArrayList<>();
			while (rs.next()) list.add(new OptionItem(rs.getInt("movieid"), rs.getString("title")));
			return list;
		}
	}

	@Override
	public List<OptionItem> findTheaterOptions() throws SQLException {
		try (Statement stmt = DBUtil.getConnection().createStatement();
			 ResultSet rs = stmt.executeQuery(DBUtil.getSQL("screeningSelectTheaterOpts"))) {
			List<OptionItem> list = new ArrayList<>();
			while (rs.next()) list.add(new OptionItem(rs.getInt("theaterid"), rs.getString("name")));
			return list;
		}
	}

	@Override
	public boolean hasOverlap(int theaterId, int excludeScreenId, String showtime, int movieId) throws SQLException {
		try (PreparedStatement psmt = DBUtil.getConnection().prepareStatement(DBUtil.getSQL("screeningCheckOverlap"))) {
			psmt.setInt(1, theaterId);
			psmt.setInt(2, excludeScreenId);
			psmt.setString(3, showtime);
			psmt.setString(4, showtime);
			psmt.setInt(5, movieId);
			try (ResultSet rs = psmt.executeQuery()) {
				return rs.next() && rs.getInt("cnt") > 0;
			}
		}
	}

}
