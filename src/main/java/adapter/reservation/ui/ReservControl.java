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

	public ReservControl(JDialog dialog, JLabel dialogLabel) {
		service = new ReservationService(new ReservDAO());
		columnNames = new Vector<>();
		columnNames.add("ReservID"); columnNames.add("고객"); columnNames.add("영화");
		columnNames.add("상영관"); columnNames.add("상영시간"); columnNames.add("좌석");
		columnNames.add("가격"); columnNames.add("예약일");
		this.dialog = dialog; this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)            { this.table = t; }
	public void setReservInsFrm(ReservInsFrm f) { this.reservInsFrm = f; }
	public void setReservUpFrm(ReservUpFrm f)   { this.reservUpFrm = f; }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		try { reservList = service.findAll(); }
		catch (SQLException e) { reservList = new ArrayList<>(); dialogOpen("예약 목록 조회 실패"); }

		Vector<Vector<String>> data = new Vector<>();
		for (Reservation r : reservList) {
			Vector<String> row = new Vector<>();
			row.add(String.valueOf(r.getReservId())); row.add(r.getCustName()); row.add(r.getMovieTitle());
			row.add(r.getTheaterName()); row.add(r.getShowtime()); row.add(r.getSeatNo());
			row.add(String.valueOf(r.getPrice())); row.add(r.getReservDate());
			data.add(row);
		}
		table.setModel(new DefaultTableModel(data, columnNames));
	}

	private void loadOptions(JComboBox<ComboItem> cbC, JComboBox<ComboItem> cbS) throws SQLException {
		cbC.removeAllItems(); service.getCustomerOptions().forEach(cbC::addItem);
		cbS.removeAllItems(); service.getScreeningOptions().forEach(cbS::addItem);
	}

	private void insertOne() {
		ComboItem cust = (ComboItem) reservInsFrm.cbCustomer.getSelectedItem();
		ComboItem screen = (ComboItem) reservInsFrm.cbScreening.getSelectedItem();
		if (cust == null || screen == null) { dialogOpen("고객과 상영일정을 선택해주세요."); return; }
		try {
			Reservation r = new Reservation();
			r.setCustId(cust.id); r.setScreenId(screen.id); r.setSeatNo(reservInsFrm.tfSeatNo.getText());
			service.save(r);
			reservInsFrm.tfSeatNo.setText(""); reservInsFrm.setVisible(false); readAll();
		} catch (SQLException e) { dialogOpen("예약 추가 실패"); }
	}

	private void updateOne() {
		ComboItem cust = (ComboItem) reservUpFrm.cbCustomer.getSelectedItem();
		ComboItem screen = (ComboItem) reservUpFrm.cbScreening.getSelectedItem();
		if (cust == null || screen == null) { dialogOpen("고객과 상영일정을 선택해주세요."); return; }
		try {
			Reservation r = new Reservation();
			r.setReservId(selectedReservId); r.setCustId(cust.id); r.setScreenId(screen.id);
			r.setSeatNo(reservUpFrm.tfSeatNo.getText());
			service.update(r); clearUpFrm(); readAll();
		} catch (SQLException e) { dialogOpen("예약 수정 실패"); }
	}

	private void deleteOne() {
		try { service.delete(selectedReservId); clearUpFrm(); readAll(); }
		catch (SQLException e) { dialogOpen("예약 삭제 실패"); }
	}

	private void clearUpFrm() { reservUpFrm.tfSeatNo.setText(""); reservUpFrm.setVisible(false); }

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "목록 조회":
				readAll(); break;
			case "예약 추가":
				try { loadOptions(reservInsFrm.cbCustomer, reservInsFrm.cbScreening); }
				catch (SQLException ex) { dialogOpen("옵션 로드 실패"); return; }
				reservInsFrm.setVisible(true); break;
			case "저장":  insertOne(); break;
			case "취소":  reservInsFrm.setVisible(false); break;
			case "수정":  updateOne(); break;
			case "삭제":  deleteOne(); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Reservation r = reservList.get(table.getSelectedRow());
		selectedReservId = r.getReservId();
		try { loadOptions(reservUpFrm.cbCustomer, reservUpFrm.cbScreening); }
		catch (SQLException ex) { dialogOpen("옵션 로드 실패"); return; }

		for (int i = 0; i < reservUpFrm.cbCustomer.getItemCount(); i++)
			if (reservUpFrm.cbCustomer.getItemAt(i).id == r.getCustId()) { reservUpFrm.cbCustomer.setSelectedIndex(i); break; }
		for (int i = 0; i < reservUpFrm.cbScreening.getItemCount(); i++)
			if (reservUpFrm.cbScreening.getItemAt(i).id == r.getScreenId()) { reservUpFrm.cbScreening.setSelectedIndex(i); break; }

		reservUpFrm.tfSeatNo.setText(r.getSeatNo());
		reservUpFrm.setVisible(true);
	}

}
