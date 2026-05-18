package cinema.screening;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import cinema.util.ComboItem;

public class ScreeningControl extends MouseAdapter implements ActionListener {

	private ScreeningDAO dao;
	private Vector<Vector<String>> readAllData;
	private Vector<String> columnNames;

	private JDialog dialog;
	private JLabel dialogLabel;
	private JTable table;

	private ScreeningInsFrm screeningInsFrm;
	private ScreeningUpFrm screeningUpFrm;

	private String selectedScreenID;

	public ScreeningControl(JDialog dialog, JLabel dialogLabel) {
		dao = new ScreeningDAO();
		columnNames = new Vector<>();
		columnNames.add("ScreenID"); columnNames.add("영화");
		columnNames.add("상영관");   columnNames.add("상영시간");
		columnNames.add("가격");     columnNames.add("잔여석");
		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable table)                    { this.table = table; }
	public void setScreeningInsFrm(ScreeningInsFrm frm)  { this.screeningInsFrm = frm; }
	public void setScreeningUpFrm(ScreeningUpFrm frm)    { this.screeningUpFrm = frm; }

	private void dialogOpen(String message) {
		dialogLabel.setText(message);
		dialog.setVisible(true);
	}

	private void readAll() {
		try {
			readAllData = dao.readAll();
		} catch (SQLException e) {
			readAllData = new Vector<>();
			dialogOpen("상영일정 조회 실패");
		}
		// index 0~5 만 표시 (6=movieid, 7=theaterid 는 숨김)
		Vector<Vector<String>> displayData = new Vector<>();
		for (Vector<String> row : readAllData) {
			displayData.add(new Vector<>(row.subList(0, 6)));
		}
		table.setModel(new DefaultTableModel(displayData, columnNames));
	}

	private void loadComboOptions(JComboBox<ComboItem> cbMovie, JComboBox<ComboItem> cbTheater) throws SQLException {
		cbMovie.removeAllItems();
		for (ComboItem item : dao.readMovieOptions()) cbMovie.addItem(item);

		cbTheater.removeAllItems();
		for (ComboItem item : dao.readTheaterOptions()) cbTheater.addItem(item);
	}

	private void insertOne() {
		ComboItem movie   = (ComboItem) screeningInsFrm.cbMovie.getSelectedItem();
		ComboItem theater = (ComboItem) screeningInsFrm.cbTheater.getSelectedItem();
		if (movie == null || theater == null) { dialogOpen("영화와 상영관을 선택해주세요."); return; }

		int result = 0;
		try {
			result = dao.insertOne(movie.id, theater.id,
				screeningInsFrm.tfShowtime.getText(), screeningInsFrm.tfPrice.getText());
		} catch (SQLException e) { e.printStackTrace(); }

		if (result < 1) {
			dialogOpen("상영일정 추가 실패");
		} else {
			screeningInsFrm.tfShowtime.setText(""); screeningInsFrm.tfPrice.setText("");
			screeningInsFrm.setVisible(false);
			readAll();
		}
	}

	private void updateOne() {
		ComboItem movie   = (ComboItem) screeningUpFrm.cbMovie.getSelectedItem();
		ComboItem theater = (ComboItem) screeningUpFrm.cbTheater.getSelectedItem();
		if (movie == null || theater == null) { dialogOpen("영화와 상영관을 선택해주세요."); return; }

		int result = 0;
		try {
			result = dao.updateOne(movie.id, theater.id,
				screeningUpFrm.tfShowtime.getText(), screeningUpFrm.tfPrice.getText(), selectedScreenID);
		} catch (SQLException e) { e.printStackTrace(); }

		if (result < 1) { dialogOpen("상영일정 수정 실패"); } else { clearUpFrm(); readAll(); }
	}

	private void deleteOne() {
		int result = 0;
		try { result = dao.deleteOne(selectedScreenID); }
		catch (SQLException e) { e.printStackTrace(); }
		if (result < 1) { dialogOpen("상영일정 삭제 실패"); } else { clearUpFrm(); readAll(); }
	}

	private void clearUpFrm() {
		screeningUpFrm.tfShowtime.setText(""); screeningUpFrm.tfPrice.setText("");
		screeningUpFrm.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "목록 조회":
				readAll();
				break;
			case "상영일정 추가":
				try { loadComboOptions(screeningInsFrm.cbMovie, screeningInsFrm.cbTheater); }
				catch (SQLException ex) { dialogOpen("옵션 로드 실패"); return; }
				screeningInsFrm.setVisible(true);
				break;
			case "저장":  insertOne(); break;
			case "취소":  screeningInsFrm.setVisible(false); break;
			case "수정":  updateOne(); break;
			case "삭제":  deleteOne(); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int rowIndex = table.getSelectedRow();
		Vector<String> row = readAllData.get(rowIndex);
		selectedScreenID = row.get(0);

		try { loadComboOptions(screeningUpFrm.cbMovie, screeningUpFrm.cbTheater); }
		catch (SQLException ex) { dialogOpen("옵션 로드 실패"); return; }

		// 현재 값 pre-select
		String currentMovieId   = row.get(6);
		String currentTheaterId = row.get(7);
		for (int i = 0; i < screeningUpFrm.cbMovie.getItemCount(); i++) {
			if (screeningUpFrm.cbMovie.getItemAt(i).id.equals(currentMovieId)) {
				screeningUpFrm.cbMovie.setSelectedIndex(i); break;
			}
		}
		for (int i = 0; i < screeningUpFrm.cbTheater.getItemCount(); i++) {
			if (screeningUpFrm.cbTheater.getItemAt(i).id.equals(currentTheaterId)) {
				screeningUpFrm.cbTheater.setSelectedIndex(i); break;
			}
		}
		screeningUpFrm.tfShowtime.setText(row.get(3));
		screeningUpFrm.tfPrice.setText(row.get(4));
		screeningUpFrm.setVisible(true);
	}

}
