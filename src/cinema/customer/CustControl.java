package cinema.customer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustControl extends MouseAdapter implements ActionListener {

	private CustDAO dao;
	private Vector<Vector<String>> readAllData;
	private Vector<String> columnNames;

	private JDialog dialog;
	private JLabel dialogLabel;
	private JTable table;

	private CustInsFrm custInsFrm;
	private CustUpFrm custUpFrm;

	private String selectedCustID;

	public CustControl(JDialog dialog, JLabel dialogLabel) {
		dao = new CustDAO();
		columnNames = new Vector<>();
		columnNames.add("CustID"); columnNames.add("이름");
		columnNames.add("전화번호"); columnNames.add("이메일");
		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable table)          { this.table = table; }
	public void setCustInsFrm(CustInsFrm frm)   { this.custInsFrm = frm; }
	public void setCustUpFrm(CustUpFrm frm)     { this.custUpFrm = frm; }

	private void dialogOpen(String message) {
		dialogLabel.setText(message);
		dialog.setVisible(true);
	}

	private void readAll() {
		try {
			readAllData = dao.readAll();
		} catch (SQLException e) {
			readAllData = new Vector<>();
			dialogOpen("고객 목록 조회 실패");
		}
		table.setModel(new DefaultTableModel(readAllData, columnNames));
	}

	private void insertOne() {
		int result = 0;
		try {
			result = dao.insertOne(custInsFrm.tfName.getText(), custInsFrm.tfPhone.getText(), custInsFrm.tfEmail.getText());
		} catch (SQLException e) { e.printStackTrace(); }

		if (result < 1) {
			dialogOpen("고객 추가 실패");
		} else {
			custInsFrm.tfName.setText(""); custInsFrm.tfPhone.setText(""); custInsFrm.tfEmail.setText("");
			custInsFrm.setVisible(false);
			readAll();
		}
	}

	private void updateOne() {
		int result = 0;
		try {
			result = dao.updateOne(custUpFrm.tfName.getText(), custUpFrm.tfPhone.getText(), custUpFrm.tfEmail.getText(), selectedCustID);
		} catch (SQLException e) { e.printStackTrace(); }
		if (result < 1) { dialogOpen("고객 수정 실패"); } else { clearUpFrm(); readAll(); }
	}

	private void deleteOne() {
		int result = 0;
		try { result = dao.deleteOne(selectedCustID); }
		catch (SQLException e) { e.printStackTrace(); }
		if (result < 1) { dialogOpen("고객 삭제 실패"); } else { clearUpFrm(); readAll(); }
	}

	private void clearUpFrm() {
		custUpFrm.tfName.setText(""); custUpFrm.tfPhone.setText(""); custUpFrm.tfEmail.setText("");
		custUpFrm.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "목록 조회": readAll(); break;
			case "고객 추가": custInsFrm.setVisible(true); break;
			case "저장":      insertOne(); break;
			case "취소":      custInsFrm.setVisible(false); break;
			case "수정":      updateOne(); break;
			case "삭제":      deleteOne(); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int rowIndex = table.getSelectedRow();
		Vector<String> row = readAllData.get(rowIndex);
		selectedCustID = row.get(0);
		custUpFrm.tfName.setText(row.get(1));
		custUpFrm.tfPhone.setText(row.get(2));
		custUpFrm.tfEmail.setText(row.get(3));
		custUpFrm.setVisible(true);
	}

}
