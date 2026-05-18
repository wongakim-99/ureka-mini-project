package domain.theater;

import java.sql.SQLException;
import java.util.List;

public interface TheaterRepository {
	List<Theater> findAll() throws SQLException;
	void save(Theater theater) throws SQLException;
	void update(Theater theater) throws SQLException;
	void delete(int theaterId) throws SQLException;
}
