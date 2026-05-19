package presentation.swing.theater;

import domain.theater.Theater;
import domain.theater.TheaterService;
import adapter.theater.TheaterDAO;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TheaterControl extends MouseAdapter implements ActionListener {

	private final TheaterService service;
	private List<Theater> theaterList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog; private JLabel dialogLabel;
	private JTable table;
	private TheaterInsFrm theaterInsFrm;
	private TheaterUpFrm theaterUpFrm;
	private int selectedTheaterId;

	public TheaterControl(JDialog dialog, JLabel dialogLabel) {
		service = new TheaterService(new TheaterDAO());
		columnNames = new Vector<>();
		columnNames.add("TheaterID"); columnNames.add("상영관명"); columnNames.add("총 좌석수");
		this.dialog = dialog; this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)              { this.table = t; }
	public void setTheaterInsFrm(TheaterInsFrm f) { this.theaterInsFrm = f; }
	public void setTheaterUpFrm(TheaterUpFrm f)   { this.theaterUpFrm = f; }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		try { theaterList = service.findAll(); }
		catch (SQLException e) { theaterList = new ArrayList<>(); dialogOpen("상영관 목록 조회 실패"); }

		Vector<Vector<String>> data = new Vector<>();
		for (Theater t : theaterList) {
			Vector<String> row = new Vector<>();
			row.add(String.valueOf(t.getTheaterId())); row.add(t.getName()); row.add(String.valueOf(t.getTotalSeats()));
			data.add(row);
		}
		table.setModel(new DefaultTableModel(data, columnNames));
	}

	private void insertOne() {
		try {
			service.save(new Theater(0, theaterInsFrm.tfName.getText().trim(),
					Integer.parseInt(theaterInsFrm.tfTotalSeats.getText().trim())));
			theaterInsFrm.tfName.setText(""); theaterInsFrm.tfTotalSeats.setText("");
			theaterInsFrm.setVisible(false); readAll();
		} catch (Exception e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "상영관 추가 실패"); }
	}

	private void updateOne() {
		try {
			service.update(new Theater(selectedTheaterId, theaterUpFrm.tfName.getText().trim(),
					Integer.parseInt(theaterUpFrm.tfTotalSeats.getText().trim())));
			clearUpFrm(); readAll();
		} catch (Exception e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "상영관 수정 실패"); }
	}

	private void deleteOne() {
		try { service.delete(selectedTheaterId); clearUpFrm(); readAll(); }
		catch (SQLException e) { dialogOpen("상영관 삭제 실패"); }
	}

	private void clearUpFrm() {
		theaterUpFrm.tfName.setText(""); theaterUpFrm.tfTotalSeats.setText("");
		theaterUpFrm.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "목록 조회":   readAll(); break;
			case "상영관 추가": theaterInsFrm.setVisible(true); break;
			case "저장":        insertOne(); break;
			case "취소":        theaterInsFrm.setVisible(false); theaterUpFrm.setVisible(false); break;
			case "수정":        updateOne(); break;
			case "삭제":        deleteOne(); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Theater t = theaterList.get(table.getSelectedRow());
		selectedTheaterId = t.getTheaterId();
		theaterUpFrm.tfName.setText(t.getName());
		theaterUpFrm.tfTotalSeats.setText(String.valueOf(t.getTotalSeats()));
		theaterUpFrm.setVisible(true);
	}

}
