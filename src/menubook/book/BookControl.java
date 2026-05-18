package menubook.book;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BookControl extends MouseAdapter implements ActionListener {

	private BookDAO dao;
	private Vector<Vector<String>> readAllData;
	private Vector<String> columnNames;

	private JDialog dialog;
	private JLabel dialogLabel;
	private JTable table;
	// ===========================
	private BookInsFrm bookInsFrm;
	private BookUpFrm bookUpFrm;
	// ===========================
	private String selectedBookID;

	public BookControl( JDialog dialog, JLabel dialogLabel ) {
		dao = new BookDAO();
		columnNames = new Vector<String>();
		columnNames.add("BookID");		columnNames.add("BookName");
		columnNames.add("Publisher");	columnNames.add("Price");

		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable table) {
		this.table = table;
	} // setTable

	public void setBookInsFrm(BookInsFrm bookInsFrm) {
		this.bookInsFrm = bookInsFrm;
	} // setBookInsFrm

	public void setBookUpFrm(BookUpFrm bookUpFrm) {
		this.bookUpFrm = bookUpFrm;
	} // setBookUpFrm

	private void dialogOpen( String message ) {
		dialogLabel.setText(message);
		dialog.setVisible(true);
	} // dialogOpen

	private void readAll() {
		try {
			readAllData = dao.readAll();
		} catch (SQLException e) {
			readAllData = new Vector<Vector<String>>();
			dialogOpen( "도서 목록 조회 실패." );
		}

		DefaultTableModel model = new DefaultTableModel(readAllData, columnNames);
		table.setModel(model);
	} // readAll

	private void insertOne() {
		int successCount = 0;
		try {
			successCount = dao.insertOne(	bookInsFrm.tfBookName.getText()
												, bookInsFrm.tfPublisher.getText()
												, bookInsFrm.tfPrice.getText()	);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if( successCount < 1 ) { // 실패
			dialogOpen("도서 정보 입력 실패");
		} else { // 성공
			bookInsFrm.tfBookName.setText("");
			bookInsFrm.tfPublisher.setText("");
			bookInsFrm.tfPrice.setText("");
			bookInsFrm.setVisible(false);
			this.readAll();
		}
	} // insertOne

	private void updateOne() {
		int successCount = 0;
		try {
			successCount = dao.updateOne(	bookUpFrm.tfBookName.getText()
											, bookUpFrm.tfPublisher.getText()
											, bookUpFrm.tfPrice.getText()
											, selectedBookID );
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if( successCount < 1 ) { // 실패
			dialogOpen("도서 정보 수정 실패");
		} else { // 성공
			this.clearUpFrm();
			this.readAll();
		}
	} // updateOne

	private void deleteOne() {
		int successCount = 0;
		try {
			successCount = dao.deleteOne( selectedBookID );
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if( successCount < 1 ) { // 실패
			dialogOpen("도서 정보 삭제 실패");
		} else { // 성공
			this.clearUpFrm();
			this.readAll();
		}
	} // deleteOne

	private void clearUpFrm() {
		bookUpFrm.tfBookName.setText("");
		bookUpFrm.tfPublisher.setText("");
		bookUpFrm.tfPrice.setText("");
		bookUpFrm.setVisible(false);
	} // clearUpFrm

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		if( command.equals( "목록 조회" ) ) {
			this.readAll();
		} else if( command.equals( "도서 추가" ) ) {
			bookInsFrm.setVisible(true);
		} else if( command.equals( "도서 저장" ) ) {
			this.insertOne();
		} else if( command.equals( "입력 취소" ) ) {
			bookInsFrm.setVisible(false);
		} else if( command.equals( "도서 수정" ) ) {
			this.updateOne();
		} else if( command.equals( "도서 삭제" ) ) {
			this.deleteOne();
		} else if( command.equals( "수정 취소" ) ) {
			bookUpFrm.setVisible(false);
		}

	} // actionPerformed

	@Override
	public void mouseClicked(MouseEvent e) {
		int rowIndex = table.getSelectedRow();
		Vector<String> readOne = readAllData.get( rowIndex );
		selectedBookID = readOne.get(0);

		bookUpFrm.tfBookName.setText( readOne.get(1) );
		bookUpFrm.tfPublisher.setText( readOne.get(2) );
		bookUpFrm.tfPrice.setText( readOne.get(3) );
		bookUpFrm.setVisible(true);
	} // mouseClicked

} // class
