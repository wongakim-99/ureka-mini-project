package domain.screening;

import common.ComboItem;
import infrastructure.screening.ScreeningDAO;
import java.sql.SQLException;
import java.util.List;

public class ScreeningService {

	private final ScreeningDAO dao;

	public ScreeningService(ScreeningDAO dao) {
		this.dao = dao;
	}

	public List<Screening> findAll() throws SQLException {
		return dao.findAll();
	}

	public void save(Screening screening) throws SQLException {
		dao.insertOne(screening);
	}

	public void update(Screening screening) throws SQLException {
		dao.updateOne(screening);
	}

	public void delete(int screenId) throws SQLException {
		dao.deleteOne(screenId);
	}

	public List<ComboItem> getMovieOptions() throws SQLException {
		return dao.findMovieOptions();
	}

	public List<ComboItem> getTheaterOptions() throws SQLException {
		return dao.findTheaterOptions();
	}

}
