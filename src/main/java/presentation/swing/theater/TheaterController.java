package presentation.swing.theater;

import domain.theater.Theater;
import domain.theater.TheaterService;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TheaterController extends MouseAdapter implements ActionListener {

	private final TheaterService service;
	private List<Theater> theaterList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog; private JLabel dialogLabel;
	private JTable table;
	private TheaterCreateFrame theaterCreateFrame;
	private TheaterUpdateFrame theaterUpdateFrame;
	private int selectedTheaterId;

	public TheaterController(TheaterService service, JDialog dialog, JLabel dialogLabel) {
		this.service = service;
		columnNames = new Vector<>();
		columnNames.add("TheaterID"); columnNames.add("상영관명"); columnNames.add("총 좌석수");
		this.dialog = dialog; this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)              { this.table = t; }
	public void setTheaterCreateFrame(TheaterCreateFrame f) { this.theaterCreateFrame = f; }
	public void setTheaterUpdateFrame(TheaterUpdateFrame f)   { this.theaterUpdateFrame = f; }

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
			service.save(new Theater(0, theaterCreateFrame.tfName.getText().trim(),
					Integer.parseInt(theaterCreateFrame.tfTotalSeats.getText().trim())));
			theaterCreateFrame.tfName.setText(""); theaterCreateFrame.tfTotalSeats.setText("");
			theaterCreateFrame.setVisible(false); readAll();
		} catch (Exception e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "상영관 추가 실패"); }
	}

	private void updateOne() {
		try {
			service.update(new Theater(selectedTheaterId, theaterUpdateFrame.tfName.getText().trim(),
					Integer.parseInt(theaterUpdateFrame.tfTotalSeats.getText().trim())));
			clearUpdateFrame(); readAll();
		} catch (Exception e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "상영관 수정 실패"); }
	}

	private void deleteOne() {
		try { service.delete(selectedTheaterId); clearUpdateFrame(); readAll(); }
		catch (SQLException e) { dialogOpen("상영관 삭제 실패"); }
	}

	private void clearUpdateFrame() {
		theaterUpdateFrame.tfName.setText(""); theaterUpdateFrame.tfTotalSeats.setText("");
		theaterUpdateFrame.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "목록 조회":   readAll(); break;
			case "상영관 추가": theaterCreateFrame.setVisible(true); break;
			case "저장":        insertOne(); break;
			case "취소":        theaterCreateFrame.setVisible(false); theaterUpdateFrame.setVisible(false); break;
			case "수정":        updateOne(); break;
			case "삭제":        deleteOne(); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Theater t = theaterList.get(table.getSelectedRow());
		selectedTheaterId = t.getTheaterId();
		theaterUpdateFrame.tfName.setText(t.getName());
		theaterUpdateFrame.tfTotalSeats.setText(String.valueOf(t.getTotalSeats()));
		theaterUpdateFrame.setVisible(true);
	}

}
