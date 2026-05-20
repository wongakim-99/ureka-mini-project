package presentation.swing.reservation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import domain.common.OptionItem;
import domain.reservation.Reservation;
import domain.reservation.ReservationService;
import infrastructure.AppLogger;

public class ReservationController extends MouseAdapter implements ActionListener {

	private static final Logger log = AppLogger.get(ReservationController.class);

	private final ReservationService service;
	private List<Reservation> reservationList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog; private JLabel dialogLabel;
	private JTable table;
	private ReservationCreateFrame reservationCreateFrame;
	private ReservationUpdateFrame reservationUpdateFrame;
	private int selectedReservationId;
	private JButton btnDeleteTop, btnUpdateBottom;
	private String lastKeyword = "";

	public ReservationController(ReservationService service, JDialog dialog, JLabel dialogLabel) {
		this.service = service;
		columnNames = new Vector<>();
		columnNames.add("선택"); columnNames.add("ReservID"); columnNames.add("고객"); columnNames.add("영화");
		columnNames.add("상영관"); columnNames.add("상영시간"); columnNames.add("좌석");
		columnNames.add("가격"); columnNames.add("예약일");
		this.dialog = dialog; this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)            { this.table = t; }
	public void setReservationCreateFrame(ReservationCreateFrame f) { this.reservationCreateFrame = f; }
	public void setReservationUpdateFrame(ReservationUpdateFrame f)   { this.reservationUpdateFrame = f; }
	public void setDeleteBtn(JButton btn)       { this.btnDeleteTop = btn; }
	public void setUpdateBtn(JButton btn)       { this.btnUpdateBottom = btn; }
	public void load() { lastKeyword = ""; readAll(); }
	public void search(String keyword) { lastKeyword = keyword.trim(); readAll(); }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		long t = System.currentTimeMillis();
		try { reservationList = service.findAll(); }
		catch (SQLException e) { reservationList = new ArrayList<>(); dialogOpen("예약 목록 조회 실패"); }
		log.fine(String.format("DB 조회 완료 (%d건, %dms)", reservationList.size(), System.currentTimeMillis() - t));

		String keyword = lastKeyword.toLowerCase();
		Vector<Vector<Object>> data = new Vector<>();
		for (Reservation r : reservationList) {
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

	public void reloadScreenings(int movieId, JComboBox<OptionItem> cbScreening) {
		cbScreening.removeAllItems();
		try { service.getScreeningsByMovie(movieId).forEach(cbScreening::addItem); }
		catch (SQLException e) { dialogOpen("상영일정 로드 실패"); }
	}

	private void loadOptions(JComboBox<OptionItem> cbC, JComboBox<OptionItem> cbM) throws SQLException {
		cbC.removeAllItems(); service.getCustomerOptions().forEach(cbC::addItem);
		cbM.removeAllItems(); service.getMovieOptions().forEach(cbM::addItem);
	}

	private String buildSeatNo(JComboBox<String> cbRow, JComboBox<String> cbColumn) {
		String row = (String) cbRow.getSelectedItem();
		String column = (String) cbColumn.getSelectedItem();
		if (row == null || row.isEmpty() || column == null || column.isEmpty()) return "";
		return row + column;
	}

	private void selectSeatNo(JComboBox<String> cbRow, JComboBox<String> cbColumn, String seatNo) {
		if (seatNo == null || seatNo.isEmpty()) {
			cbRow.setSelectedIndex(0);
			cbColumn.setSelectedIndex(0);
			return;
		}
		String row = seatNo.substring(0, 1);
		String column = seatNo.length() > 1 ? seatNo.substring(1) : "";
		for (int i = 0; i < cbRow.getItemCount(); i++) {
			if (cbRow.getItemAt(i).equals(row)) { cbRow.setSelectedIndex(i); break; }
		}
		for (int i = 0; i < cbColumn.getItemCount(); i++) {
			if (cbColumn.getItemAt(i).equals(column)) { cbColumn.setSelectedIndex(i); break; }
		}
	}

	private void insertOne() {
		OptionItem cust   = (OptionItem) reservationCreateFrame.cbCustomer.getSelectedItem();
		OptionItem screen = (OptionItem) reservationCreateFrame.cbScreening.getSelectedItem();
		if (cust == null || screen == null) { dialogOpen("고객과 영화/상영일정을 선택해주세요."); return; }
		String seatNo = buildSeatNo(reservationCreateFrame.cbSeatRow, reservationCreateFrame.cbSeatColumn);
		if (seatNo.isEmpty()) { dialogOpen("좌석번호를 선택해주세요."); return; }
		try {
			Reservation r = new Reservation();
			r.setCustId(cust.id); r.setScreenId(screen.id); r.setSeatNo(seatNo);
			service.save(r);
			reservationCreateFrame.cbSeatRow.setSelectedIndex(0);
			reservationCreateFrame.cbSeatColumn.setSelectedIndex(0);
			reservationCreateFrame.setVisible(false);
			readAll();
		} catch (SQLException e) { dialogOpen(e.getMessage() == null ? "예약 추가 실패" : e.getMessage()); }
	}

	private void updateOne() {
		OptionItem cust = (OptionItem) reservationUpdateFrame.cbCustomer.getSelectedItem();
		OptionItem screen = (OptionItem) reservationUpdateFrame.cbScreening.getSelectedItem();
		if (cust == null || screen == null) { dialogOpen("고객과 상영일정을 선택해주세요."); return; }
		String seatNo = buildSeatNo(reservationUpdateFrame.cbSeatRow, reservationUpdateFrame.cbSeatColumn);
		if (seatNo.isEmpty()) { dialogOpen("좌석번호를 선택해주세요."); return; }
		try {
			Reservation r = new Reservation();
			r.setReservId(selectedReservationId); r.setCustId(cust.id); r.setScreenId(screen.id);
			r.setSeatNo(seatNo);
			service.update(r); clearUpdateFrame(); readAll();
		} catch (SQLException e) { dialogOpen(e.getMessage() == null ? "예약 수정 실패" : e.getMessage()); }
	}

	private void deleteOne() {
		try { service.delete(selectedReservationId); clearUpdateFrame(); readAll(); }
		catch (SQLException e) { dialogOpen("예약 삭제 실패"); }
	}

	private void clearUpdateFrame() {
		reservationUpdateFrame.cbSeatRow.setSelectedIndex(0);
		reservationUpdateFrame.cbSeatColumn.setSelectedIndex(0);
		reservationUpdateFrame.setVisible(false);
	}

	private void openCheckedUpdateFrame() {
		for (int i = 0; i < table.getRowCount(); i++) {
			if ((Boolean) table.getValueAt(i, 0)) {
				selectedReservationId = Integer.parseInt(table.getValueAt(i, 1).toString());
				Reservation r = reservationList.stream().filter(item -> item.getReservId() == selectedReservationId).findFirst().orElse(null);
				if (r != null) {
					try { loadOptions(reservationUpdateFrame.cbCustomer, reservationUpdateFrame.cbMovie); } catch (SQLException ignored) {}
					for (int j = 0; j < reservationUpdateFrame.cbCustomer.getItemCount(); j++)
						if (reservationUpdateFrame.cbCustomer.getItemAt(j).id == r.getCustId()) { reservationUpdateFrame.cbCustomer.setSelectedIndex(j); break; }
					for (int j = 0; j < reservationUpdateFrame.cbMovie.getItemCount(); j++)
						if (reservationUpdateFrame.cbMovie.getItemAt(j).id == r.getMovieId()) { reservationUpdateFrame.cbMovie.setSelectedIndex(j); break; }
					for (int j = 0; j < reservationUpdateFrame.cbScreening.getItemCount(); j++)
						if (reservationUpdateFrame.cbScreening.getItemAt(j).id == r.getScreenId()) { reservationUpdateFrame.cbScreening.setSelectedIndex(j); break; }
					selectSeatNo(reservationUpdateFrame.cbSeatRow, reservationUpdateFrame.cbSeatColumn, r.getSeatNo());
					reservationUpdateFrame.setVisible(true);
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
		if (!"목록 조회".equals(cmd)) {
			switch (cmd) {
			case "예약 추가":
				try { loadOptions(reservationCreateFrame.cbCustomer, reservationCreateFrame.cbMovie); }
				catch (SQLException ex) { dialogOpen("옵션 로드 실패"); return; }
				reservationCreateFrame.cbSeatRow.setSelectedIndex(0);
			reservationCreateFrame.cbSeatColumn.setSelectedIndex(0);
			reservationCreateFrame.setVisible(true); break;
			case "저장":  insertOne(); break;
			case "취소":  reservationCreateFrame.setVisible(false); reservationUpdateFrame.setVisible(false); break;
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
