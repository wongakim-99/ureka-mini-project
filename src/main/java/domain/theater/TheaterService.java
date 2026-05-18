package domain.theater;

import infrastructure.theater.TheaterDAO;
import java.sql.SQLException;
import java.util.List;

public class TheaterService {

	private final TheaterDAO dao;

	public TheaterService(TheaterDAO dao) {
		this.dao = dao;
	}

	public List<Theater> findAll() throws SQLException {
		return dao.findAll();
	}

	public void save(Theater theater) throws SQLException {
		dao.insertOne(theater);
	}

	public void update(Theater theater) throws SQLException {
		dao.updateOne(theater);
	}

	public void delete(int theaterId) throws SQLException {
		dao.deleteOne(theaterId);
	}

}
