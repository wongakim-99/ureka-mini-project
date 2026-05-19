package adapter.reservation.ui;

import common.ComboItem;
import domain.reservation.Reservation;
import domain.reservation.ReservationService;
import adapter.reservation.ReservDAO;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ReservControl extends MouseAdapter implements ActionListener {

	private final ReservationService service;
	private List<Reservation> reservList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog; private JLabel dialogLabel;
	private JTable table;
	private ReservInsFrm reservInsFrm;
	private ReservUpFrm reservUpFrm;
	private int selectedReservId;
	private JButton btnDeleteTop, btnUpdateBottom;
	private String lastKeyword = "";

	public ReservControl(JDialog dialog, JLabel dialogLabel) {
		service = new ReservationService(new ReservDAO());
		columnNames = new Vector<>();
		columnNames.add("선택"); columnNames.add("ReservID"); columnNames.add("고객"); columnNames.add("영화");
		columnNames.add("상영관"); columnNames.add("상영시간"); columnNames.add("좌석");
		columnNames.add("가격"); columnNames.add("예약일");
		this.dialog = dialog; this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)            { this.table = t; }
	public void setReservInsFrm(ReservInsFrm f) { this.reservInsFrm = f; }
	public void setReservUpFrm(ReservUpFrm f)   { this.reservUpFrm = f; }
	public void setDeleteBtn(JButton btn)       { this.btnDeleteTop = btn; }
	public void setUpdateBtn(JButton btn)       { this.btnUpdateBottom = btn; }
	public void load() { lastKeyword = ""; readAll(); }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		try { reservList = service.findAll(); }
		catch (SQLException e) { reservList = new ArrayList<>(); dialogOpen("예약 목록 조회 실패"); }

		String keyword = lastKeyword.toLowerCase();
		Vector<Vector<Object>> data = new Vector<>();
		for (Reservation r : reservList) {
			// 고객 이름 또는 영화 제목으로 검색 필터링
			if (!keyword.isEmpty() && !r.getCustName().toLowerCase().contains(keyword) && 
				!r.getMovieTitle().toLowerCase().contains(keyword)) continue;

			Vector<Object> row = new Vector<>();
			row.add(Boolean.FALSE);
			row.add(String.valueOf(r.getReservId())); row.add(r.getCustName()); row.add(r.getMovieTitle());
			row.add(r.getTheaterName()); row.add(r.getShowtime()); row.add(r.getSeatNo());
			row.add(String.valueOf(r.getPrice())); row.add(r.getReservDate());
			data.add(row);
		}
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override public Class<?> getColumnClass(int col) { return col == 0 ? Boolean.class : String.class; }
			@Override public boolean isCellEditable(int row, int col) { return false; }
		};
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);

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

	public void reloadScreenings(int movieId, JComboBox<ComboItem> cbScreening) {
		cbScreening.removeAllItems();
		try { service.getScreeningsByMovie(movieId).forEach(cbScreening::addItem); }
		catch (SQLException e) { dialogOpen("상영일정 로드 실패"); }
	}

	private void loadOptions(JComboBox<ComboItem> cbC, JComboBox<ComboItem> cbM) throws SQLException {
		cbC.removeAllItems(); service.getCustomerOptions().forEach(cbC::addItem);
		cbM.removeAllItems(); service.getMovieOptions().forEach(cbM::addItem);
	}

	private void insertOne() {
		ComboItem cust   = (ComboItem) reservInsFrm.cbCustomer.getSelectedItem();
		ComboItem screen = (ComboItem) reservInsFrm.cbScreening.getSelectedItem();
		if (cust == null || screen == null) { dialogOpen("고객과 영화/상영일정을 선택해주세요."); return; }
		String seatNo = reservInsFrm.tfSeatNo.getText().trim();
		if (seatNo.isEmpty()) { dialogOpen("좌석번호를 입력해주세요."); return; }
		try {
			Reservation r = new Reservation();
			r.setCustId(cust.id); r.setScreenId(screen.id); r.setSeatNo(seatNo);
			service.save(r);
			reservInsFrm.tfSeatNo.setText(""); reservInsFrm.setVisible(false); readAll();
		} catch (SQLException e) { dialogOpen(e.getMessage() == null ? "예약 추가 실패" : e.getMessage()); }
	}

	private void updateOne() {
		ComboItem cust = (ComboItem) reservUpFrm.cbCustomer.getSelectedItem();
		ComboItem screen = (ComboItem) reservUpFrm.cbScreening.getSelectedItem();
		if (cust == null || screen == null) { dialogOpen("고객과 상영일정을 선택해주세요."); return; }
		String seatNo = reservUpFrm.tfSeatNo.getText().trim();
		if (seatNo.isEmpty()) { dialogOpen("좌석번호를 입력해주세요."); return; }
		try {
			Reservation r = new Reservation();
			r.setReservId(selectedReservId); r.setCustId(cust.id); r.setScreenId(screen.id);
			r.setSeatNo(seatNo);
			service.update(r); clearUpFrm(); readAll();
		} catch (SQLException e) { dialogOpen(e.getMessage() == null ? "예약 수정 실패" : e.getMessage()); }
	}

	private void deleteOne() {
		try { service.delete(selectedReservId); clearUpFrm(); readAll(); }
		catch (SQLException e) { dialogOpen("예약 삭제 실패"); }
	}

	private void clearUpFrm() { reservUpFrm.tfSeatNo.setText(""); reservUpFrm.setVisible(false); }

	private void openUpdateFrmChecked() {
		for (int i = 0; i < table.getRowCount(); i++) {
			if ((Boolean) table.getValueAt(i, 0)) {
				selectedReservId = Integer.parseInt(table.getValueAt(i, 1).toString());
				Reservation r = reservList.stream().filter(item -> item.getReservId() == selectedReservId).findFirst().orElse(null);
				if (r != null) {
					try { loadOptions(reservUpFrm.cbCustomer, reservUpFrm.cbMovie); } catch (SQLException ignored) {}
					for (int j = 0; j < reservUpFrm.cbCustomer.getItemCount(); j++)
						if (reservUpFrm.cbCustomer.getItemAt(j).id == r.getCustId()) { reservUpFrm.cbCustomer.setSelectedIndex(j); break; }
					for (int j = 0; j < reservUpFrm.cbMovie.getItemCount(); j++)
						if (reservUpFrm.cbMovie.getItemAt(j).id == r.getMovieId()) { reservUpFrm.cbMovie.setSelectedIndex(j); break; }
					for (int j = 0; j < reservUpFrm.cbScreening.getItemCount(); j++)
						if (reservUpFrm.cbScreening.getItemAt(j).id == r.getScreenId()) { reservUpFrm.cbScreening.setSelectedIndex(j); break; }
					reservUpFrm.tfSeatNo.setText(r.getSeatNo());
					reservUpFrm.setVisible(true);
				}
				break;
			}
		}
	}

	private void deleteChecked() {
		int opt = JOptionPane.showConfirmDialog(null, "선택한 예약 정보를 정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
		if (opt != JOptionPane.YES_OPTION) return;

		int count = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			if ((Boolean) table.getValueAt(i, 0)) {
				int id = Integer.parseInt(table.getValueAt(i, 1).toString());
				try { service.delete(id); count++; } catch (SQLException ignored) {}
			}
		}
		if (count > 0) { dialogOpen(count + "건의 예약 정보가 삭제되었습니다."); readAll(); }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("목록 조회".equals(cmd)) {
			String input = JOptionPane.showInputDialog(null, "검색어를 입력하세요:", "예약 검색", JOptionPane.QUESTION_MESSAGE);
			if (input != null) { lastKeyword = input.trim(); readAll(); }
		} else {
			switch (cmd) {
			case "예약 추가":
				try { loadOptions(reservInsFrm.cbCustomer, reservInsFrm.cbMovie); }
				catch (SQLException ex) { dialogOpen("옵션 로드 실패"); return; }
				reservInsFrm.setVisible(true); break;
			case "저장":  insertOne(); break;
			case "취소":  reservInsFrm.setVisible(false); reservUpFrm.setVisible(false); break;
			case "수정":  if (e.getSource() == btnUpdateBottom) openUpdateFrmChecked(); else updateOne(); break;
			case "삭제":  deleteChecked(); break;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int col = table.columnAtPoint(e.getPoint());
		int row = table.rowAtPoint(e.getPoint());
		if (col == 0 && row >= 0) {
			boolean curr = (Boolean) table.getValueAt(row, 0);
			table.setValueAt(!curr, row, 0);
		}
	}

}
