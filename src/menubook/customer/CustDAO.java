package menubook.customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import menubook.util.DBUtil;

public class CustDAO {

	public Vector<Vector<String>> readAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();

		ResultSet rs = stmt.executeQuery( DBUtil.getSQL( "custSelectAll" ) );

		Vector<Vector<String>> list = new Vector<Vector<String>>();

		while( rs.next() ) {
			Vector<String> obj = new Vector<String>();
			obj.add( rs.getString("custid") );
			obj.add( rs.getString("name") );
			obj.add( rs.getString("address") );
			obj.add( rs.getString("phone") );
			list.add(obj);
		} // while

		stmt.close();
		rs.close();

		return list;
	} // readAll

	public int insertOne(String name, String address, String phone) throws SQLException {
		PreparedStatement psmt
			= DBUtil.getConnection().prepareStatement( DBUtil.getSQL( "custInsertOne" ) );
		psmt.setString(1, name);
		psmt.setString(2, address);
		psmt.setString(3, phone);

		int successCount = 0;
		successCount = psmt.executeUpdate();

		return successCount;
	} // insertOne

	public int updateOne(String name, String address
						, String phone, String custid) throws SQLException {
		PreparedStatement psmt
			= DBUtil.getConnection().prepareStatement( DBUtil.getSQL( "custUpdateOne" ) );
		psmt.setString(1, name);
		psmt.setString(2, address);
		psmt.setString(3, phone);
		psmt.setString(4, custid);

		int successCount = 0;
		successCount = psmt.executeUpdate();

		return successCount;
	} // updateOne

	public int deleteOne(String custid) throws SQLException {
		PreparedStatement psmt
			= DBUtil.getConnection().prepareStatement( DBUtil.getSQL( "custDeleteOne" ) );
		psmt.setString(1, custid);

		int successCount = 0;
		successCount = psmt.executeUpdate();

		return successCount;
	} // deleteOne

} // class
