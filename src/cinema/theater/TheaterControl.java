package cinema.theater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TheaterControl extends MouseAdapter implements ActionListener {

	private TheaterDAO dao;
	private Vector<Vector<String>> readAllData;
	private Vector<String> columnNames;

	private JDialog dialog;
	private JLabel dialogLabel;
	private JTable table;

	private TheaterInsFrm theaterInsFrm;
	private TheaterUpFrm theaterUpFrm;

	private String selectedTheaterID;

	public TheaterControl(JDialog dialog, JLabel dialogLabel) {
		dao = new TheaterDAO();
		columnNames = new Vector<>();
		columnNames.add("TheaterID"); columnNames.add("상영관명"); columnNames.add("총 좌석수");
		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable table)                { this.table = table; }
	public void setTheaterInsFrm(TheaterInsFrm frm)  { this.theaterInsFrm = frm; }
	public void setTheaterUpFrm(TheaterUpFrm frm)    { this.theaterUpFrm = frm; }

	private void dialogOpen(String message) {
		dialogLabel.setText(message);
		dialog.setVisible(true);
	}

	private void readAll() {
		try {
			readAllData = dao.readAll();
		} catch (SQLException e) {
			readAllData = new Vector<>();
			dialogOpen("상영관 목록 조회 실패");
		}
		table.setModel(new DefaultTableModel(readAllData, columnNames));
	}

	private void insertOne() {
		int result = 0;
		try {
			result = dao.insertOne(theaterInsFrm.tfName.getText(), theaterInsFrm.tfTotalSeats.getText());
		} catch (SQLException e) { e.printStackTrace(); }

		if (result < 1) {
			dialogOpen("상영관 추가 실패");
		} else {
			theaterInsFrm.tfName.setText(""); theaterInsFrm.tfTotalSeats.setText("");
			theaterInsFrm.setVisible(false);
			readAll();
		}
	}

	private void updateOne() {
		int result = 0;
		try {
			result = dao.updateOne(theaterUpFrm.tfName.getText(), theaterUpFrm.tfTotalSeats.getText(), selectedTheaterID);
		} catch (SQLException e) { e.printStackTrace(); }

		if (result < 1) { dialogOpen("상영관 수정 실패"); } else { clearUpFrm(); readAll(); }
	}

	private void deleteOne() {
		int result = 0;
		try { result = dao.deleteOne(selectedTheaterID); }
		catch (SQLException e) { e.printStackTrace(); }
		if (result < 1) { dialogOpen("상영관 삭제 실패"); } else { clearUpFrm(); readAll(); }
	}

	private void clearUpFrm() {
		theaterUpFrm.tfName.setText(""); theaterUpFrm.tfTotalSeats.setText("");
		theaterUpFrm.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "목록 조회":  readAll(); break;
			case "상영관 추가": theaterInsFrm.setVisible(true); break;
			case "저장":        insertOne(); break;
			case "취소":        theaterInsFrm.setVisible(false); break;
			case "수정":        updateOne(); break;
			case "삭제":        deleteOne(); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int rowIndex = table.getSelectedRow();
		Vector<String> row = readAllData.get(rowIndex);
		selectedTheaterID = row.get(0);
		theaterUpFrm.tfName.setText(row.get(1));
		theaterUpFrm.tfTotalSeats.setText(row.get(2));
		theaterUpFrm.setVisible(true);
	}

}
