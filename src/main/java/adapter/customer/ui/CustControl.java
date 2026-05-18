package adapter.customer.ui;

import domain.customer.Customer;
import domain.customer.CustomerService;
import adapter.customer.CustDAO;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustControl extends MouseAdapter implements ActionListener {

	private final CustomerService service;
	private List<Customer> custList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog; private JLabel dialogLabel;
	private JTable table;
	private CustInsFrm custInsFrm;
	private CustUpFrm custUpFrm;
	private int selectedCustId;

	public CustControl(JDialog dialog, JLabel dialogLabel) {
		service = new CustomerService(new CustDAO());
		columnNames = new Vector<>();
		columnNames.add("CustID"); columnNames.add("이름"); columnNames.add("전화번호"); columnNames.add("이메일");
		this.dialog = dialog; this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)        { this.table = t; }
	public void setCustInsFrm(CustInsFrm f) { this.custInsFrm = f; }
	public void setCustUpFrm(CustUpFrm f)   { this.custUpFrm = f; }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		try { custList = service.findAll(); }
		catch (SQLException e) { custList = new ArrayList<>(); dialogOpen("고객 목록 조회 실패"); }

		Vector<Vector<String>> data = new Vector<>();
		for (Customer c : custList) {
			Vector<String> row = new Vector<>();
			row.add(String.valueOf(c.getCustId())); row.add(c.getName()); row.add(c.getPhone()); row.add(c.getEmail());
			data.add(row);
		}
		table.setModel(new DefaultTableModel(data, columnNames));
	}

	private void insertOne() {
		try {
			service.save(new Customer(0, custInsFrm.tfName.getText(), custInsFrm.tfPhone.getText(), custInsFrm.tfEmail.getText()));
			custInsFrm.tfName.setText(""); custInsFrm.tfPhone.setText(""); custInsFrm.tfEmail.setText("");
			custInsFrm.setVisible(false); readAll();
		} catch (SQLException e) { dialogOpen("고객 추가 실패"); }
	}

	private void updateOne() {
		try {
			service.update(new Customer(selectedCustId, custUpFrm.tfName.getText(), custUpFrm.tfPhone.getText(), custUpFrm.tfEmail.getText()));
			clearUpFrm(); readAll();
		} catch (SQLException e) { dialogOpen("고객 수정 실패"); }
	}

	private void deleteOne() {
		try { service.delete(selectedCustId); clearUpFrm(); readAll(); }
		catch (SQLException e) { dialogOpen("고객 삭제 실패"); }
	}

	private void clearUpFrm() {
		custUpFrm.tfName.setText(""); custUpFrm.tfPhone.setText(""); custUpFrm.tfEmail.setText("");
		custUpFrm.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "목록 조회": readAll(); break;
			case "고객 추가": custInsFrm.setVisible(true); break;
			case "저장":      insertOne(); break;
			case "취소":      custInsFrm.setVisible(false); break;
			case "수정":      updateOne(); break;
			case "삭제":      deleteOne(); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Customer c = custList.get(table.getSelectedRow());
		selectedCustId = c.getCustId();
		custUpFrm.tfName.setText(c.getName()); custUpFrm.tfPhone.setText(c.getPhone()); custUpFrm.tfEmail.setText(c.getEmail());
		custUpFrm.setVisible(true);
	}

}
