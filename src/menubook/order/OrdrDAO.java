package menubook.order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import menubook.util.DBUtil;

public class OrdrDAO {

	public Vector<Vector<String>> readAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();

		ResultSet rs = stmt.executeQuery( DBUtil.getSQL( "ordrSelectAll" ) );

		Vector<Vector<String>> list = new Vector<Vector<String>>();

		while( rs.next() ) {
			Vector<String> obj = new Vector<String>();
			obj.add( rs.getString("orderid") );
			obj.add( rs.getString("custid") );
			obj.add( rs.getString("name") );
			obj.add( rs.getString("bookid") );
			obj.add( rs.getString("bookname") );
			obj.add( rs.getString("saleprice") );
			obj.add( rs.getString("orderdate") );
			list.add(obj);
		} // while

		stmt.close();
		rs.close();

		return list;
	} // readAll

	public int insertOne(String custid, String bookid, String saleprice) throws SQLException {
		PreparedStatement psmt
			= DBUtil.getConnection().prepareStatement( DBUtil.getSQL( "ordrInsertOne" ) );
		psmt.setString(1, custid);
		psmt.setString(2, bookid);
		psmt.setString(3, saleprice);

		int successCount = 0;
		successCount = psmt.executeUpdate();

		return successCount;
	} // insertOne

	public int updateOne(String custid, String bookid
						, String saleprice, String orderid) throws SQLException {
		PreparedStatement psmt
			= DBUtil.getConnection().prepareStatement( DBUtil.getSQL( "ordrUpdateOne" ) );
		psmt.setString(1, custid);
		psmt.setString(2, bookid);
		psmt.setString(3, saleprice);
		psmt.setString(4, orderid);

		int successCount = 0;
		successCount = psmt.executeUpdate();

		return successCount;
	} // updateOne

	public int deleteOne(String orderid) throws SQLException {
		PreparedStatement psmt
			= DBUtil.getConnection().prepareStatement( DBUtil.getSQL( "ordrDeleteOne" ) );
		psmt.setString(1, orderid);

		int successCount = 0;
		successCount = psmt.executeUpdate();

		return successCount;
	} // deleteOne

} // class
