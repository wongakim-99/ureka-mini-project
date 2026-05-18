package adapter.screening.ui;

import common.ComboItem;
import domain.screening.Screening;
import domain.screening.ScreeningService;
import adapter.screening.ScreeningDAO;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ScreeningControl extends MouseAdapter implements ActionListener {

	private final ScreeningService service;
	private List<Screening> screeningList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog; private JLabel dialogLabel;
	private JTable table;
	private ScreeningInsFrm screeningInsFrm;
	private ScreeningUpFrm screeningUpFrm;
	private int selectedScreenId;

	public ScreeningControl(JDialog dialog, JLabel dialogLabel) {
		service = new ScreeningService(new ScreeningDAO());
		columnNames = new Vector<>();
		columnNames.add("ScreenID"); columnNames.add("영화"); columnNames.add("상영관");
		columnNames.add("상영시간"); columnNames.add("가격"); columnNames.add("잔여석");
		this.dialog = dialog; this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)                  { this.table = t; }
	public void setScreeningInsFrm(ScreeningInsFrm f) { this.screeningInsFrm = f; }
	public void setScreeningUpFrm(ScreeningUpFrm f)   { this.screeningUpFrm = f; }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		try { screeningList = service.findAll(); }
		catch (SQLException e) { screeningList = new ArrayList<>(); dialogOpen("상영일정 조회 실패"); }

		Vector<Vector<String>> data = new Vector<>();
		for (Screening s : screeningList) {
			Vector<String> row = new Vector<>();
			row.add(String.valueOf(s.getScreenId())); row.add(s.getMovieTitle());
			row.add(s.getTheaterName()); row.add(s.getShowtime());
			row.add(String.valueOf(s.getPrice())); row.add(String.valueOf(s.getRemainSeats()));
			data.add(row);
		}
		table.setModel(new DefaultTableModel(data, columnNames));
	}

	private void loadOptions(JComboBox<ComboItem> cbM, JComboBox<ComboItem> cbT) throws SQLException {
		cbM.removeAllItems(); service.getMovieOptions().forEach(cbM::addItem);
		cbT.removeAllItems(); service.getTheaterOptions().forEach(cbT::addItem);
	}

	private void insertOne() {
		ComboItem movie = (ComboItem) screeningInsFrm.cbMovie.getSelectedItem();
		ComboItem theater = (ComboItem) screeningInsFrm.cbTheater.getSelectedItem();
		if (movie == null || theater == null) { dialogOpen("영화와 상영관을 선택해주세요."); return; }
		try {
			Screening s = new Screening();
			s.setMovieId(movie.id); s.setTheaterId(theater.id);
			s.setShowtime(screeningInsFrm.tfShowtime.getText());
			s.setPrice(Integer.parseInt(screeningInsFrm.tfPrice.getText()));
			service.save(s);
			screeningInsFrm.tfShowtime.setText(""); screeningInsFrm.tfPrice.setText("");
			screeningInsFrm.setVisible(false); readAll();
		} catch (Exception e) { dialogOpen("상영일정 추가 실패"); }
	}

	private void updateOne() {
		ComboItem movie = (ComboItem) screeningUpFrm.cbMovie.getSelectedItem();
		ComboItem theater = (ComboItem) screeningUpFrm.cbTheater.getSelectedItem();
		if (movie == null || theater == null) { dialogOpen("영화와 상영관을 선택해주세요."); return; }
		try {
			Screening s = new Screening();
			s.setScreenId(selectedScreenId); s.setMovieId(movie.id); s.setTheaterId(theater.id);
			s.setShowtime(screeningUpFrm.tfShowtime.getText());
			s.setPrice(Integer.parseInt(screeningUpFrm.tfPrice.getText()));
			service.update(s); clearUpFrm(); readAll();
		} catch (Exception e) { dialogOpen("상영일정 수정 실패"); }
	}

	private void deleteOne() {
		try { service.delete(selectedScreenId); clearUpFrm(); readAll(); }
		catch (SQLException e) { dialogOpen("상영일정 삭제 실패"); }
	}

	private void clearUpFrm() {
		screeningUpFrm.tfShowtime.setText(""); screeningUpFrm.tfPrice.setText("");
		screeningUpFrm.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "목록 조회":
				readAll(); break;
			case "상영일정 추가":
				try { loadOptions(screeningInsFrm.cbMovie, screeningInsFrm.cbTheater); }
				catch (SQLException ex) { dialogOpen("옵션 로드 실패"); return; }
				screeningInsFrm.setVisible(true); break;
			case "저장":  insertOne(); break;
			case "취소":  screeningInsFrm.setVisible(false); screeningUpFrm.setVisible(false); break;
			case "수정":  updateOne(); break;
			case "삭제":  deleteOne(); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Screening s = screeningList.get(table.getSelectedRow());
		selectedScreenId = s.getScreenId();
		try { loadOptions(screeningUpFrm.cbMovie, screeningUpFrm.cbTheater); }
		catch (SQLException ex) { dialogOpen("옵션 로드 실패"); return; }

		for (int i = 0; i < screeningUpFrm.cbMovie.getItemCount(); i++)
			if (screeningUpFrm.cbMovie.getItemAt(i).id == s.getMovieId()) { screeningUpFrm.cbMovie.setSelectedIndex(i); break; }
		for (int i = 0; i < screeningUpFrm.cbTheater.getItemCount(); i++)
			if (screeningUpFrm.cbTheater.getItemAt(i).id == s.getTheaterId()) { screeningUpFrm.cbTheater.setSelectedIndex(i); break; }

		screeningUpFrm.tfShowtime.setText(s.getShowtime());
		screeningUpFrm.tfPrice.setText(String.valueOf(s.getPrice()));
		screeningUpFrm.setVisible(true);
	}

}
