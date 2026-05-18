package domain.screening;

import common.ComboItem;
import java.sql.SQLException;
import java.util.List;

public class ScreeningService {

	private final ScreeningRepository repository;

	public ScreeningService(ScreeningRepository repository) {
		this.repository = repository;
	}

	public List<Screening> findAll() throws SQLException          { return repository.findAll(); }
	public void save(Screening screening) throws SQLException     { repository.save(screening); }
	public void update(Screening screening) throws SQLException   { repository.update(screening); }
	public void delete(int screenId) throws SQLException          { repository.delete(screenId); }
	public List<ComboItem> getMovieOptions() throws SQLException  { return repository.findMovieOptions(); }
	public List<ComboItem> getTheaterOptions() throws SQLException{ return repository.findTheaterOptions(); }

}
