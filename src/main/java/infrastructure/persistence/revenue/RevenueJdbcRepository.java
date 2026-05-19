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
        Statement stmt = DBUtil.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(DBUtil.getSQL("selectTotalSaleByMovie"));
        List<Revenue> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Revenue(rs.getInt("movieid"), rs.getString("title"),
                    rs.getString("total")));
        }
        stmt.close(); rs.close();
        return list;
    }

    public int calculateAll() throws SQLException{
        Statement stmt = DBUtil.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(DBUtil.getSQL("salesCalculateAll"));
        int sum = 0;
        if(rs.next()) {
            sum = rs.getInt("sum_all");
        }
        rs.close(); stmt.close();
        return sum;
    }
}

