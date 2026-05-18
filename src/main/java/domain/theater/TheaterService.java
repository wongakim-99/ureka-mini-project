package domain.theater;

import java.sql.SQLException;
import java.util.List;

public class TheaterService {

	private final TheaterRepository repository;

	public TheaterService(TheaterRepository repository) {
		this.repository = repository;
	}

	public List<Theater> findAll() throws SQLException      { return repository.findAll(); }
	public void save(Theater theater) throws SQLException   { repository.save(theater); }
	public void update(Theater theater) throws SQLException { repository.update(theater); }
	public void delete(int theaterId) throws SQLException   { repository.delete(theaterId); }

}
