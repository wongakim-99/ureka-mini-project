package infrastructure.persistence.revenue;

import infrastructure.db.DBUtil;
import domain.revenue.Revenue;
import domain.revenue.RevenueRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RevenueJdbcRepository implements RevenueRepository {

    @Override
    public List<Revenue> findAllByMovie() throws SQLException {
        try (Statement stmt = DBUtil.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(DBUtil.getSQL("selectTotalSaleByMovie"))) {
            List<Revenue> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Revenue(rs.getInt("movieid"), rs.getString("title"),
                        rs.getString("total")));
            }
            return list;
        }
    }

    @Override
    public int calculateAll() throws SQLException {
        try (Statement stmt = DBUtil.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(DBUtil.getSQL("salesCalculateAll"))) {
            return rs.next() ? rs.getInt("sum_all") : 0;
        }
    }
}
