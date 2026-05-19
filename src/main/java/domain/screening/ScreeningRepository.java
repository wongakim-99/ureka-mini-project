package domain.screening;

import common.ComboItem;
import java.sql.SQLException;
import java.util.List;

public interface ScreeningRepository {
	List<Screening> findAll() throws SQLException;
	void save(Screening screening) throws SQLException;
	void update(Screening screening) throws SQLException;
	void delete(int screenId) throws SQLException;
	List<ComboItem> findMovieOptions() throws SQLException;
	List<ComboItem> findTheaterOptions() throws SQLException;
	boolean hasOverlap(int theaterId, int excludeScreenId, String showtime, int movieId) throws SQLException;
}
