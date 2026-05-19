package domain.revenue;

import java.sql.SQLException;
import java.util.List;

public class RevenueService {
    private final RevenueRepository revenueRepository;

    public RevenueService(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository;
    }

    public List<Revenue> findAllByMovie() throws SQLException {
        return revenueRepository.findAllByMovie();
    }

    public int calculateAll() throws SQLException {
        return revenueRepository.calculateAll();
    }
}
