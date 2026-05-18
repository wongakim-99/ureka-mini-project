package cinema.reservation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import cinema.util.ComboItem;

public class ReservControl extends MouseAdapter implements ActionListener {

	private ReservDAO dao;
	private Vector<Vector<String>> readAllData;
	private Vector<String> columnNames;

	private JDialog dialog;
	private JLabel dialogLabel;
	private JTable table;

	private ReservInsFrm reservInsFrm;
	private ReservUpFrm reservUpFrm;

	private String selectedReservID;

	public ReservControl(JDialog dialog, JLabel dialogLabel) {
		dao = new ReservDAO();
		columnNames = new Vector<>();
		columnNames.add("ReservID"); columnNames.add("고객");
		columnNames.add("영화");     columnNames.add("상영관");
		columnNames.add("상영시간"); columnNames.add("좌석");
		columnNames.add("가격");     columnNames.add("예약일");
		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable table)              { this.table = table; }
	public void setReservInsFrm(ReservInsFrm frm)   { this.reservInsFrm = frm; }
	public void setReservUpFrm(ReservUpFrm frm)     { this.reservUpFrm = frm; }

	private void dialogOpen(String message) {
		dialogLabel.setText(message);
		dialog.setVisible(true);
	}

	private void readAll() {
		try {
			readAllData = dao.readAll();
		} catch (SQLException e) {
			readAllData = new Vector<>();
			dialogOpen("예약 목록 조회 실패");
		}
		// index 0~7 만 표시 (8=custid, 9=screenid 는 숨김)
		Vector<Vector<String>> displayData = new Vector<>();
		for (Vector<String> row : readAllData) {
			displayData.add(new Vector<>(row.subList(0, 8)));
		}
		table.setModel(new DefaultTableModel(displayData, columnNames));
	}

	private void loadComboOptions(JComboBox<ComboItem> cbCust, JComboBox<ComboItem> cbScreen) throws SQLException {
		cbCust.removeAllItems();
		for (ComboItem item : dao.readCustomerOptions()) cbCust.addItem(item);

		cbScreen.removeAllItems();
		for (ComboItem item : dao.readScreeningOptions()) cbScreen.addItem(item);
	}

	private void insertOne() {
		ComboItem cust   = (ComboItem) reservInsFrm.cbCustomer.getSelectedItem();
		ComboItem screen = (ComboItem) reservInsFrm.cbScreening.getSelectedItem();
		if (cust == null || screen == null) { dialogOpen("고객과 상영일정을 선택해주세요."); return; }

		int result = 0;
		try {
			result = dao.insertOne(cust.id, screen.id, reservInsFrm.tfSeatNo.getText());
		} catch (SQLException e) { e.printStackTrace(); }

		if (result < 1) {
			dialogOpen("예약 추가 실패");
		} else {
			reservInsFrm.tfSeatNo.setText("");
			reservInsFrm.setVisible(false);
			readAll();
		}
	}

	private void updateOne() {
		ComboItem cust   = (ComboItem) reservUpFrm.cbCustomer.getSelectedItem();
		ComboItem screen = (ComboItem) reservUpFrm.cbScreening.getSelectedItem();
		if (cust == null || screen == null) { dialogOpen("고객과 상영일정을 선택해주세요."); return; }

		int result = 0;
		try {
			result = dao.updateOne(cust.id, screen.id, reservUpFrm.tfSeatNo.getText(), selectedReservID);
		} catch (SQLException e) { e.printStackTrace(); }

		if (result < 1) { dialogOpen("예약 수정 실패"); } else { clearUpFrm(); readAll(); }
	}

	private void deleteOne() {
		int result = 0;
		try { result = dao.deleteOne(selectedReservID); }
		catch (SQLException e) { e.printStackTrace(); }
		if (result < 1) { dialogOpen("예약 삭제 실패"); } else { clearUpFrm(); readAll(); }
	}

	private void clearUpFrm() {
		reservUpFrm.tfSeatNo.setText("");
		reservUpFrm.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "목록 조회":
				readAll();
				break;
			case "예약 추가":
				try { loadComboOptions(reservInsFrm.cbCustomer, reservInsFrm.cbScreening); }
				catch (SQLException ex) { dialogOpen("옵션 로드 실패"); return; }
				reservInsFrm.setVisible(true);
				break;
			case "저장":  insertOne(); break;
			case "취소":  reservInsFrm.setVisible(false); break;
			case "수정":  updateOne(); break;
			case "삭제":  deleteOne(); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int rowIndex = table.getSelectedRow();
		Vector<String> row = readAllData.get(rowIndex);
		selectedReservID = row.get(0);

		try { loadComboOptions(reservUpFrm.cbCustomer, reservUpFrm.cbScreening); }
		catch (SQLException ex) { dialogOpen("옵션 로드 실패"); return; }

		String currentCustId   = row.get(8);
		String currentScreenId = row.get(9);
		for (int i = 0; i < reservUpFrm.cbCustomer.getItemCount(); i++) {
			if (reservUpFrm.cbCustomer.getItemAt(i).id.equals(currentCustId)) {
				reservUpFrm.cbCustomer.setSelectedIndex(i); break;
			}
		}
		for (int i = 0; i < reservUpFrm.cbScreening.getItemCount(); i++) {
			if (reservUpFrm.cbScreening.getItemAt(i).id.equals(currentScreenId)) {
				reservUpFrm.cbScreening.setSelectedIndex(i); break;
			}
		}
		reservUpFrm.tfSeatNo.setText(row.get(5));
		reservUpFrm.setVisible(true);
	}

}
