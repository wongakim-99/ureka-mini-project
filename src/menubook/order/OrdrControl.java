package menubook.order;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import menubook.book.BookDAO;
import menubook.customer.CustDAO;

public class OrdrControl extends MouseAdapter implements ActionListener {

	private OrdrDAO dao;
	private Vector<Vector<String>> readAllData;
	private Vector<String> columnNames;

	private JDialog dialog;
	private JLabel dialogLabel;
	private JTable table;
	// ===========================
	private OrdrInsFrm ordrInsFrm;
	private OrdrUpFrm ordrUpFrm;
	// ===========================
	private String selectedOrdrID;
	// ===========================
	private CustDAO daoCust;
	private BookDAO daoBook;
	private Vector<Vector<String>> readAllDataCust;
	private Vector<Vector<String>> readAllDataBook;
	private Vector<String> columnNamesCust;
	private Vector<String> columnNamesBook;
	private String selectedCustID;
	private String selectedBookID;

	public OrdrControl( JDialog dialog, JLabel dialogLabel ) {
		dao = new OrdrDAO();
		columnNames = new Vector<String>();
		columnNames.add("OrderID");
		columnNames.add("CustID");		columnNames.add("Name");
		columnNames.add("BookID");		columnNames.add("BookName");
		columnNames.add("SalePrice");	columnNames.add("OrderDate");

		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
		// ===========================
		daoCust = new CustDAO();
		daoBook = new BookDAO();
		columnNamesCust = new Vector<String>();
		columnNamesCust.add("CustID");		columnNamesCust.add("Name");
		columnNamesCust.add("Address");		columnNamesCust.add("Phone");
		columnNamesBook = new Vector<String>();
		columnNamesBook.add("BookID");		columnNamesBook.add("BookName");
		columnNamesBook.add("Publisher");	columnNamesBook.add("Price");
	}

	public void setTable(JTable table) {
		this.table = table;
	} // setTable

	public void setOrdrInsFrm(OrdrInsFrm ordrInsFrm) {
		this.ordrInsFrm = ordrInsFrm;
	} // setOrdrInsFrm

	public void setOrdrUpFrm(OrdrUpFrm ordrUpFrm) {
		this.ordrUpFrm = ordrUpFrm;
	} // setOrdrUpFrm

	private void dialogOpen( String message ) {
		dialogLabel.setText(message);
		dialog.setVisible(true);
	} // dialogOpen

	private void readAll() {
		try {
			readAllData = dao.readAll();
		} catch (SQLException e) {
			e.printStackTrace();
			readAllData = new Vector<Vector<String>>();
			dialogOpen( "주문 목록 조회 실패." );
		}

		DefaultTableModel model = new DefaultTableModel(readAllData, columnNames);
		table.setModel(model);
	} // readAll

	private void insertOne() {
		int successCount = 0;
		try {
			successCount = dao.insertOne(	selectedCustID
											, selectedBookID
											, ordrInsFrm.tfSalePrice.getText()	);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if( successCount < 1 ) { // 실패
			dialogOpen("주문 정보 입력 실패");
		} else { // 성공
			ordrInsFrm.tfName.setText("");
			ordrInsFrm.tfBookName.setText("");
			ordrInsFrm.tfSalePrice.setText("");
			ordrInsFrm.setVisible(false);
			this.readAll();
		}
	} // insertOne

	private void updateOne() {
		int successCount = 0;
		try {
			successCount = dao.updateOne(	selectedCustID
											, selectedBookID
											, ordrUpFrm.tfSalePrice.getText()
											, selectedOrdrID );
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if( successCount < 1 ) { // 실패
			dialogOpen("주문 정보 수정 실패");
		} else { // 성공
			this.clearUpFrm();
			this.readAll();
		}
	} // updateOne

	private void deleteOne() {
		int successCount = 0;
		try {
			successCount = dao.deleteOne( selectedOrdrID );
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if( successCount < 1 ) { // 실패
			dialogOpen("주문 정보 삭제 실패");
		} else { // 성공
			this.clearUpFrm();
			this.readAll();
		}
	} // deleteOne

	private void clearUpFrm() {
		ordrUpFrm.tfName.setText("");
		ordrUpFrm.tfName.setText("");
		ordrUpFrm.tfSalePrice.setText("");
		ordrUpFrm.setVisible(false);
	} // clearUpFrm

	private void readCustAll(String buttonName) {
		try {
			readAllDataCust = daoCust.readAll();
		} catch (SQLException e) {
			e.printStackTrace();
			readAllDataCust = new Vector<Vector<String>>();
			dialogOpen( "고객 목록 조회 실패." );
		}

		DefaultTableModel model = new DefaultTableModel(readAllDataCust, columnNamesCust);
		if( buttonName.equals("ordrInsCustBtn") ) {
			ordrInsFrm.custTable.setModel(model);
		} else if( buttonName.equals("ordrUpCustBtn") ) {
			ordrUpFrm.custTable.setModel(model);
		}
	} // readCustAll

	private void readBookAll(String buttonName) {
		try {
			readAllDataBook = daoBook.readAll();
		} catch (SQLException e) {
			readAllDataBook = new Vector<Vector<String>>();
			dialogOpen( "도서 목록 조회 실패." );
		}

		DefaultTableModel model = new DefaultTableModel(readAllDataBook, columnNamesBook);
		if( buttonName.equals("ordrInsBookBtn") ) {
			ordrInsFrm.bookTable.setModel(model);
		} else if( buttonName.equals("ordrUpBookBtn") ) {
			ordrUpFrm.bookTable.setModel(model);
		}
	} // readBookAll

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		String buttonName = ( (JButton) e.getSource() ).getName();

		if( command.equals( "목록 조회" ) ) {
			this.readAll();
		} else if( command.equals( "주문 추가" ) ) {
			ordrInsFrm.setVisible(true);
		} else if( command.equals( "주문 저장" ) ) {
			this.insertOne();
		} else if( command.equals( "입력 취소" ) ) {
			ordrInsFrm.setVisible(false);
		} else if( command.equals( "주문 수정" ) ) {
			this.updateOne();
		} else if( command.equals( "주문 삭제" ) ) {
			this.deleteOne();
		} else if( command.equals( "수정 취소" ) ) {
			ordrUpFrm.setVisible(false);
		} else if( command.equals( "고객 검색" ) ) {
			this.readCustAll(buttonName);
		} else if( command.equals( "도서 검색" ) ) {
			this.readBookAll(buttonName);
		}

	} // actionPerformed

	@Override
	public void mouseClicked(MouseEvent e) {
		String tableName = ( (JTable) e.getSource() ).getName();

		if( tableName.equals("ordrList") ) {
			int rowIndex = table.getSelectedRow();
			Vector<String> readOne = readAllData.get( rowIndex );
			selectedOrdrID = readOne.get(0);

			ordrUpFrm.tfName.setText( readOne.get(2) );
			ordrUpFrm.tfBookName.setText( readOne.get(4) );
			ordrUpFrm.tfSalePrice.setText( readOne.get(5) );
			ordrUpFrm.setVisible(true);
		} else if( tableName.equals("ordrInsCust") ) {
			int rowIndex = ordrInsFrm.custTable.getSelectedRow();
			Vector<String> readOne = readAllDataCust.get( rowIndex );
			selectedCustID = readOne.get(0);
			ordrInsFrm.tfName.setText( readOne.get(1) );
		} else if( tableName.equals("ordrInsBook") ) {
			int rowIndex = ordrInsFrm.bookTable.getSelectedRow();
			Vector<String> readOne = readAllDataBook.get( rowIndex );
			selectedBookID = readOne.get(0);
			ordrInsFrm.tfBookName.setText( readOne.get(1) );
		} else if( tableName.equals("ordrUpCust") ) {
			int rowIndex = ordrUpFrm.custTable.getSelectedRow();
			Vector<String> readOne = readAllDataCust.get( rowIndex );
			selectedCustID = readOne.get(0);
			ordrUpFrm.tfName.setText( readOne.get(1) );
		} else if( tableName.equals("ordrUpBook") ) {
			int rowIndex = ordrUpFrm.bookTable.getSelectedRow();
			Vector<String> readOne = readAllDataBook.get( rowIndex );
			selectedBookID = readOne.get(0);
			ordrUpFrm.tfBookName.setText( readOne.get(1) );
		}
	} // mouseClicked

} // class
