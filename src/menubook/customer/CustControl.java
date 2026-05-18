package menubook.customer;

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

public class CustControl extends MouseAdapter implements ActionListener {

	private CustDAO dao;
	private Vector<Vector<String>> readAllData;
	private Vector<String> columnNames;

	private JDialog dialog;
	private JLabel dialogLabel;
	private JTable table;
	// ===========================
	private CustInsFrm custInsFrm;
	private CustUpFrm custUpFrm;
	// ===========================
	private String selectedCustID;

	public CustControl( JDialog dialog, JLabel dialogLabel ) {
		dao = new CustDAO();
		columnNames = new Vector<String>();
		columnNames.add("CustID");		columnNames.add("Name");
		columnNames.add("Address");		columnNames.add("Phone");

		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable table) {
		this.table = table;
	} // setTable

	public void setCustInsFrm(CustInsFrm custInsFrm) {
		this.custInsFrm = custInsFrm;
	} // setCustInsFrm

	public void setCustUpFrm(CustUpFrm custUpFrm) {
		this.custUpFrm = custUpFrm;
	} // setCustUpFrm

	private void dialogOpen( String message ) {
		dialogLabel.setText(message);
		dialog.setVisible(true);
	} // dialogOpen

	private void readAll() {
		try {
			readAllData = dao.readAll();
		} catch (SQLException e) {
			readAllData = new Vector<Vector<String>>();
			dialogOpen( "고객 목록 조회 실패." );
		}

		DefaultTableModel model = new DefaultTableModel(readAllData, columnNames);
		table.setModel(model);
	} // readAll

	private void insertOne() {
		int successCount = 0;
		try {
			successCount = dao.insertOne(	custInsFrm.tfName.getText()
												, custInsFrm.tfAddress.getText()
												, custInsFrm.tfPhone.getText()	);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if( successCount < 1 ) { // 실패
			dialogOpen("고객 정보 입력 실패");
		} else { // 성공
			custInsFrm.tfName.setText("");
			custInsFrm.tfAddress.setText("");
			custInsFrm.tfPhone.setText("");
			custInsFrm.setVisible(false);
			this.readAll();
		}
	} // insertOne

	private void updateOne() {
		int successCount = 0;
		try {
			successCount = dao.updateOne(	custUpFrm.tfName.getText()
											, custUpFrm.tfAddress.getText()
											, custUpFrm.tfPhone.getText()
											, selectedCustID );
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if( successCount < 1 ) { // 실패
			dialogOpen("고객 정보 수정 실패");
		} else { // 성공
			this.clearUpFrm();
			this.readAll();
		}
	} // updateOne

	private void deleteOne() {
		int successCount = 0;
		try {
			successCount = dao.deleteOne( selectedCustID );
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if( successCount < 1 ) { // 실패
			dialogOpen("고객 정보 삭제 실패");
		} else { // 성공
			this.clearUpFrm();
			this.readAll();
		}
	} // deleteOne

	private void clearUpFrm() {
		custUpFrm.tfName.setText("");
		custUpFrm.tfAddress.setText("");
		custUpFrm.tfPhone.setText("");
		custUpFrm.setVisible(false);
	} // clearUpFrm

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		if( command.equals( "목록 조회" ) ) {
			this.readAll();
		} else if( command.equals( "고객 추가" ) ) {
			custInsFrm.setVisible(true);
		} else if( command.equals( "고객 저장" ) ) {
			this.insertOne();
		} else if( command.equals( "입력 취소" ) ) {
			custInsFrm.setVisible(false);
		} else if( command.equals( "고객 수정" ) ) {
			this.updateOne();
		} else if( command.equals( "고객 삭제" ) ) {
			this.deleteOne();
		} else if( command.equals( "수정 취소" ) ) {
			custUpFrm.setVisible(false);
		}

	} // actionPerformed

	@Override
	public void mouseClicked(MouseEvent e) {
		int rowIndex = table.getSelectedRow();
		Vector<String> readOne = readAllData.get( rowIndex );
		selectedCustID = readOne.get(0);

		custUpFrm.tfName.setText( readOne.get(1) );
		custUpFrm.tfAddress.setText( readOne.get(2) );
		custUpFrm.tfPhone.setText( readOne.get(3) );
		custUpFrm.setVisible(true);
	} // mouseClicked

} // class
