package menubook.book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import menubook.util.DBUtil;

public class BookDAO {

	public Vector<Vector<String>> readAll() throws SQLException {
		Statement stmt = DBUtil.getConnection().createStatement();

		ResultSet rs = stmt.executeQuery( DBUtil.getSQL( "bookSelectAll" ) );

		Vector<Vector<String>> list = new Vector<Vector<String>>();

		while( rs.next() ) {
			Vector<String> obj = new Vector<String>();
			obj.add( rs.getString("bookid") );
			obj.add( rs.getString("bookname") );
			obj.add( rs.getString("publisher") );
			obj.add( rs.getString("price") );
			list.add(obj);
		} // while

		stmt.close();
		rs.close();

		return list;
	} // readAll

	public int insertOne(String bookname, String publisher, String price) throws SQLException {
		PreparedStatement psmt
			= DBUtil.getConnection().prepareStatement( DBUtil.getSQL( "bookInsertOne" ) );
		psmt.setString(1, bookname);
		psmt.setString(2, publisher);
		psmt.setString(3, price);

		int successCount = 0;
		successCount = psmt.executeUpdate();

		return successCount;
	} // insertOne

	public int updateOne(String bookname, String publisher
						, String price, String bookid) throws SQLException {
		PreparedStatement psmt
			= DBUtil.getConnection().prepareStatement( DBUtil.getSQL( "bookUpdateOne" ) );
		psmt.setString(1, bookname);
		psmt.setString(2, publisher);
		psmt.setString(3, price);
		psmt.setString(4, bookid);

		int successCount = 0;
		successCount = psmt.executeUpdate();

		return successCount;
	} // updateOne

	public int deleteOne(String bookid) throws SQLException {
		PreparedStatement psmt
			= DBUtil.getConnection().prepareStatement( DBUtil.getSQL( "bookDeleteOne" ) );
		psmt.setString(1, bookid);

		int successCount = 0;
		successCount = psmt.executeUpdate();

		return successCount;
	} // deleteOne

} // class
