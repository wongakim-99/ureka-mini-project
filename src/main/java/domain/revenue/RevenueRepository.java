package domain.revenue;

import java.sql.SQLException;
import java.util.List;

public interface RevenueRepository {
    List<Revenue> findAllByMovie() throws SQLException;
    int calculateAll() throws SQLException;
}
