package domain.screening;

import domain.common.OptionItem;
import java.sql.SQLException;
import java.util.List;

public interface ScreeningRepository {
	List<Screening> findAll() throws SQLException;
	void save(Screening screening) throws SQLException;
	void update(Screening screening) throws SQLException;
	void delete(int screenId) throws SQLException;
	List<OptionItem> findMovieOptions() throws SQLException;
	List<OptionItem> findTheaterOptions() throws SQLException;
	boolean hasOverlap(int theaterId, int excludeScreenId, String showtime, int movieId) throws SQLException;
	boolean hasReservations(int screenId) throws SQLException;
}
