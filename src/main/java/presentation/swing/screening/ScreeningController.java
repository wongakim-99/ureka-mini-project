package presentation.swing.screening;

import infrastructure.AppLogger;
import java.util.logging.Logger;
import domain.common.OptionItem;
import domain.screening.Screening;
import domain.screening.ScreeningService;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ScreeningController extends MouseAdapter implements ActionListener {

	private static final Logger log = AppLogger.get(ScreeningController.class);

	private final ScreeningService service;
	private List<Screening> screeningList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog; private JLabel dialogLabel;
	private JTable table;
	private ScreeningCreateFrame screeningCreateFrame;
	private ScreeningUpdateFrame screeningUpdateFrame;
	private int selectedScreenId;
	private JButton btnDeleteTop, btnUpdateBottom;
	private String lastKeyword = "";

	public ScreeningController(ScreeningService service, JDialog dialog, JLabel dialogLabel) {
		this.service = service;
		columnNames = new Vector<>();
		columnNames.add("선택"); columnNames.add("ScreenID"); columnNames.add("영화"); columnNames.add("상영관");
		columnNames.add("상영시간"); columnNames.add("가격"); columnNames.add("잔여석");
		this.dialog = dialog; this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)                  { this.table = t; }
	public void setScreeningCreateFrame(ScreeningCreateFrame f) { this.screeningCreateFrame = f; }
	public void setScreeningUpdateFrame(ScreeningUpdateFrame f)   { this.screeningUpdateFrame = f; }
	public void setDeleteBtn(JButton btn)             { this.btnDeleteTop = btn; }
	public void setUpdateBtn(JButton btn)             { this.btnUpdateBottom = btn; }
	public void load() { lastKeyword = ""; readAll(); }
	public void search(String keyword) { lastKeyword = keyword.trim(); readAll(); }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		long t = System.currentTimeMillis();
		try { screeningList = service.findAll(); }
		catch (SQLException e) { screeningList = new ArrayList<>(); dialogOpen("상영일정 조회 실패"); }
		log.fine(String.format("DB 조회 완료 (%d건, %dms)", screeningList.size(), System.currentTimeMillis() - t));

		String keyword = lastKeyword.toLowerCase();
		Vector<Vector<Object>> data = new Vector<>();
		for (Screening s : screeningList) {
			// 영화 제목 또는 상영관 이름으로 검색 필터링
			if (!keyword.isEmpty() && !s.getMovieTitle().toLowerCase().contains(keyword) && 
				!s.getTheaterName().toLowerCase().contains(keyword)) continue;

			Vector<Object> row = new Vector<>();
			row.add(Boolean.FALSE);
			row.add(String.valueOf(s.getScreenId())); row.add(s.getMovieTitle());
			row.add(s.getTheaterName()); row.add(s.getShowtime());
			row.add(String.valueOf(s.getPrice())); row.add(String.valueOf(s.getRemainSeats()));
			data.add(row);
		}
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override public Class<?> getColumnClass(int col) { return col == 0 ? Boolean.class : String.class; }
			@Override public boolean isCellEditable(int row, int col) { return false; }
		};
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		if (table.getColumnModel().getColumnCount() > 1) {
			table.removeColumn(table.getColumnModel().getColumn(1));
		}

		model.addTableModelListener(e -> {
			int checkCount = 0;
			for (int i = 0; i < table.getRowCount(); i++) {
				if ((Boolean) table.getValueAt(i, 0)) checkCount++;
			}
			if (btnDeleteTop != null) btnDeleteTop.setEnabled(checkCount > 0);
			if (btnUpdateBottom != null) btnUpdateBottom.setEnabled(checkCount == 1);
		});
		if (btnDeleteTop != null) btnDeleteTop.setEnabled(false);
		if (btnUpdateBottom != null) btnUpdateBottom.setEnabled(false);
	}

	private void loadOptions(JComboBox<OptionItem> cbM, JComboBox<OptionItem> cbT) throws SQLException {
		cbM.removeAllItems(); service.getMovieOptions().forEach(cbM::addItem);
		cbT.removeAllItems(); service.getTheaterOptions().forEach(cbT::addItem);
	}

	private String getFormattedShowtime(JTextField tfDate, JComboBox<String> cbH, JComboBox<String> cbM) {
		String date = tfDate.getText().trim();
		String hour = (String) cbH.getSelectedItem();
		String min  = (String) cbM.getSelectedItem();
		return date + " " + hour + ":" + min + ":00";
	}

	private void insertOne() {
		OptionItem movie = (OptionItem) screeningCreateFrame.cbMovie.getSelectedItem();
		OptionItem theater = (OptionItem) screeningCreateFrame.cbTheater.getSelectedItem();
		if (movie == null || theater == null) { dialogOpen("영화와 상영관을 선택해주세요."); return; }
		try {
			Screening s = new Screening();
			s.setMovieId(movie.id); s.setTheaterId(theater.id);
			s.setShowtime(getFormattedShowtime(screeningCreateFrame.tfDate, screeningCreateFrame.cbHour, screeningCreateFrame.cbMin));
			s.setPrice(Integer.parseInt(screeningCreateFrame.tfPrice.getText().trim()));
			service.save(s);
			screeningCreateFrame.tfPrice.setText("");
			screeningCreateFrame.setVisible(false); readAll();
		} catch (Exception e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "상영일정 추가 실패"); }
	}

	private void updateOne() {
		OptionItem movie = (OptionItem) screeningUpdateFrame.cbMovie.getSelectedItem();
		OptionItem theater = (OptionItem) screeningUpdateFrame.cbTheater.getSelectedItem();
		if (movie == null || theater == null) { dialogOpen("영화와 상영관을 선택해주세요."); return; }
		try {
			Screening s = new Screening();
			s.setScreenId(selectedScreenId); s.setMovieId(movie.id); s.setTheaterId(theater.id);
			s.setShowtime(getFormattedShowtime(screeningUpdateFrame.tfDate, screeningUpdateFrame.cbHour, screeningUpdateFrame.cbMin));
			s.setPrice(Integer.parseInt(screeningUpdateFrame.tfPrice.getText().trim()));
			service.update(s); clearUpdateFrame(); readAll();
		} catch (Exception e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "상영일정 수정 실패"); }
	}

	private void deleteOne() {
		try { service.delete(selectedScreenId); clearUpdateFrame(); readAll(); }
		catch (SQLException e) { dialogOpen("상영일정 삭제 실패"); }
	}

	private void clearUpdateFrame() {
		screeningUpdateFrame.tfPrice.setText("");
		screeningUpdateFrame.setVisible(false);
	}

	private void openCheckedUpdateFrame() {
		for (int i = 0; i < table.getRowCount(); i++) {
			int modelRow = table.convertRowIndexToModel(i);
			if ((Boolean) table.getModel().getValueAt(modelRow, 0)) {
				selectedScreenId = Integer.parseInt(table.getModel().getValueAt(modelRow, 1).toString());
				Screening s = screeningList.stream().filter(item -> item.getScreenId() == selectedScreenId).findFirst().orElse(null);
				if (s != null) {
					try { loadOptions(screeningUpdateFrame.cbMovie, screeningUpdateFrame.cbTheater); } catch (SQLException ignored) {}
					for (int j = 0; j < screeningUpdateFrame.cbMovie.getItemCount(); j++)
						if (screeningUpdateFrame.cbMovie.getItemAt(j).id == s.getMovieId()) { screeningUpdateFrame.cbMovie.setSelectedIndex(j); break; }
					screeningUpdateFrame.tfDate.setText(s.getShowtime().substring(0, 10));
					screeningUpdateFrame.tfPrice.setText(String.valueOf(s.getPrice()));
					screeningUpdateFrame.setVisible(true);
				}
				break;
			}
		}
	}

	private void deleteChecked() {
		int opt = JOptionPane.showConfirmDialog(null, "선택한 상영일정을 정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
		if (opt != JOptionPane.YES_OPTION) return;

		int count = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			int modelRow = table.convertRowIndexToModel(i);
			if ((Boolean) table.getModel().getValueAt(modelRow, 0)) {
				int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 1).toString());
				try { service.delete(id); count++; } catch (SQLException ignored) {}
			}
		}
		if (count > 0) { dialogOpen(count + "건의 상영일정이 삭제되었습니다."); readAll(); }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (!"목록 조회".equals(cmd)) {
			switch (cmd) {
			case "일정 추가":
				try { loadOptions(screeningCreateFrame.cbMovie, screeningCreateFrame.cbTheater); }
				catch (SQLException ex) { dialogOpen("옵션 로드 실패"); return; }
				screeningCreateFrame.setVisible(true); break;
			case "저장":  insertOne(); break;
			case "취소":  screeningCreateFrame.setVisible(false); screeningUpdateFrame.setVisible(false); break;
			case "수정":  if (e.getSource() == btnUpdateBottom) openCheckedUpdateFrame(); else updateOne(); break;
			case "삭제":  deleteChecked(); break;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int col = table.columnAtPoint(e.getPoint());
		int row = table.rowAtPoint(e.getPoint());
		log.fine(String.format("click col=%d row=%d", col, row));
		if (col == 0 && row >= 0) {
			boolean curr = (Boolean) table.getValueAt(row, 0);
			table.setValueAt(!curr, row, 0);
		}
	}

}
