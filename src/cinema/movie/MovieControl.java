package cinema.movie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MovieControl extends MouseAdapter implements ActionListener {

	private MovieDAO dao;
	private Vector<Vector<String>> readAllData;
	private Vector<String> columnNames;

	private JDialog dialog;
	private JLabel dialogLabel;
	private JTable table;

	private MovieInsFrm movieInsFrm;
	private MovieUpFrm movieUpFrm;

	private String selectedMovieID;

	public MovieControl(JDialog dialog, JLabel dialogLabel) {
		dao = new MovieDAO();
		columnNames = new Vector<>();
		columnNames.add("MovieID"); columnNames.add("제목");
		columnNames.add("장르");    columnNames.add("감독");
		columnNames.add("관람등급");
		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable table)             { this.table = table; }
	public void setMovieInsFrm(MovieInsFrm frm)    { this.movieInsFrm = frm; }
	public void setMovieUpFrm(MovieUpFrm frm)      { this.movieUpFrm = frm; }

	private void dialogOpen(String message) {
		dialogLabel.setText(message);
		dialog.setVisible(true);
	}

	private void readAll() {
		try {
			readAllData = dao.readAll();
		} catch (SQLException e) {
			readAllData = new Vector<>();
			dialogOpen("영화 목록 조회 실패");
		}
		table.setModel(new DefaultTableModel(readAllData, columnNames));
	}

	private void insertOne() {
		int result = 0;
		try {
			result = dao.insertOne(
				movieInsFrm.tfTitle.getText(),
				movieInsFrm.tfGenre.getText(),
				movieInsFrm.tfDirector.getText(),
				movieInsFrm.tfRating.getText()
			);
		} catch (SQLException e) { e.printStackTrace(); }

		if (result < 1) {
			dialogOpen("영화 추가 실패");
		} else {
			movieInsFrm.tfTitle.setText(""); movieInsFrm.tfGenre.setText("");
			movieInsFrm.tfDirector.setText(""); movieInsFrm.tfRating.setText("");
			movieInsFrm.setVisible(false);
			readAll();
		}
	}

	private void updateOne() {
		int result = 0;
		try {
			result = dao.updateOne(
				movieUpFrm.tfTitle.getText(),
				movieUpFrm.tfGenre.getText(),
				movieUpFrm.tfDirector.getText(),
				movieUpFrm.tfRating.getText(),
				selectedMovieID
			);
		} catch (SQLException e) { e.printStackTrace(); }

		if (result < 1) { dialogOpen("영화 수정 실패"); } else { clearUpFrm(); readAll(); }
	}

	private void deleteOne() {
		int result = 0;
		try { result = dao.deleteOne(selectedMovieID); }
		catch (SQLException e) { e.printStackTrace(); }
		if (result < 1) { dialogOpen("영화 삭제 실패"); } else { clearUpFrm(); readAll(); }
	}

	private void clearUpFrm() {
		movieUpFrm.tfTitle.setText(""); movieUpFrm.tfGenre.setText("");
		movieUpFrm.tfDirector.setText(""); movieUpFrm.tfRating.setText("");
		movieUpFrm.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "목록 조회": readAll(); break;
			case "영화 추가": movieInsFrm.setVisible(true); break;
			case "저장":      insertOne(); break;
			case "취소":      movieInsFrm.setVisible(false); break;
			case "수정":      updateOne(); break;
			case "삭제":      deleteOne(); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int rowIndex = table.getSelectedRow();
		Vector<String> row = readAllData.get(rowIndex);
		selectedMovieID = row.get(0);
		movieUpFrm.tfTitle.setText(row.get(1));
		movieUpFrm.tfGenre.setText(row.get(2));
		movieUpFrm.tfDirector.setText(row.get(3));
		movieUpFrm.tfRating.setText(row.get(4));
		movieUpFrm.setVisible(true);
	}

}
