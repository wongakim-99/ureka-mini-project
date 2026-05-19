package presentation.swing.customer;

import domain.customer.Customer;
import domain.customer.CustomerService;
import adapter.customer.CustDAO;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.ListSelectionModel;
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
	private JButton btnDeleteTop, btnUpdateBottom;
	private String lastKeyword = "";

	public CustControl(JDialog dialog, JLabel dialogLabel) {
		service = new CustomerService(new CustDAO());
		columnNames = new Vector<>();
		columnNames.add("선택"); columnNames.add("CustID"); columnNames.add("이름"); columnNames.add("전화번호"); columnNames.add("이메일");
		this.dialog = dialog; this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)        { this.table = t; }
	public void setCustInsFrm(CustInsFrm f) { this.custInsFrm = f; }
	public void setCustUpFrm(CustUpFrm f)   { this.custUpFrm = f; }
	public void setDeleteBtn(JButton btn)   { this.btnDeleteTop = btn; }
	public void setUpdateBtn(JButton btn)   { this.btnUpdateBottom = btn; }
	public void load() { lastKeyword = ""; readAll(); }
	public void search(String keyword) { lastKeyword = keyword.trim(); readAll(); }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		try { custList = service.findAll(); }
		catch (SQLException e) { custList = new ArrayList<>(); dialogOpen("고객 목록 조회 실패"); }

		String keyword = lastKeyword.toLowerCase();
		Vector<Vector<Object>> data = new Vector<>();
		for (Customer c : custList) {
			// 이름이나 전화번호에 검색어가 포함되어 있는지 필터링
			if (!keyword.isEmpty() && !c.getName().toLowerCase().contains(keyword) && 
				!c.getPhone().toLowerCase().contains(keyword)) continue;

			Vector<Object> row = new Vector<>();
			row.add(Boolean.FALSE); // 체크박스 초기값
			row.add(String.valueOf(c.getCustId())); row.add(c.getName()); row.add(c.getPhone()); row.add(c.getEmail());
			data.add(row);
		}
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override public Class<?> getColumnClass(int col) { return col == 0 ? Boolean.class : String.class; }
			@Override public boolean isCellEditable(int row, int col) { return false; }
		};
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		
		// 체크박스 상태 변화 감지 리스너
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

	private void insertOne() {
		try {
			service.save(new Customer(0, custInsFrm.tfName.getText(), custInsFrm.tfPhone.getText(), custInsFrm.tfEmail.getText()));
			custInsFrm.tfName.setText(""); custInsFrm.tfPhone.setText(""); custInsFrm.tfEmail.setText("");
			custInsFrm.setVisible(false); readAll();
		} catch (SQLException e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "고객 추가 실패"); }
	}

	private void updateOne() {
		try {
			service.update(new Customer(selectedCustId, custUpFrm.tfName.getText(), custUpFrm.tfPhone.getText(), custUpFrm.tfEmail.getText()));
			clearUpFrm(); readAll();
		} catch (SQLException e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "고객 수정 실패"); }
	}

	private void deleteOne() {
		try { service.delete(selectedCustId); clearUpFrm(); readAll(); }
		catch (SQLException e) { dialogOpen("고객 삭제 실패"); }
	}

	private void clearUpFrm() {
		custUpFrm.tfName.setText(""); custUpFrm.tfPhone.setText(""); custUpFrm.tfEmail.setText("");
		custUpFrm.setVisible(false);
	}

	private void openUpdateFrmChecked() {
		for (int i = 0; i < table.getRowCount(); i++) {
			if ((Boolean) table.getValueAt(i, 0)) {
				selectedCustId = Integer.parseInt(table.getValueAt(i, 1).toString());
				Customer c = custList.stream().filter(item -> item.getCustId() == selectedCustId).findFirst().orElse(null);
				if (c != null) {
					custUpFrm.tfName.setText(c.getName());
					custUpFrm.tfPhone.setText(c.getPhone());
					custUpFrm.tfEmail.setText(c.getEmail());
					custUpFrm.setVisible(true);
				}
				break;
			}
		}
	}

	private void deleteChecked() {
		int opt = JOptionPane.showConfirmDialog(null, "선택한 고객 정보를 정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
		if (opt != JOptionPane.YES_OPTION) return;

		int count = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			if ((Boolean) table.getValueAt(i, 0)) {
				int id = Integer.parseInt(table.getValueAt(i, 1).toString());
				try { service.delete(id); count++; } catch (SQLException ignored) {}
			}
		}
		if (count > 0) { dialogOpen(count + "건의 고객 정보가 삭제되었습니다."); readAll(); }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		// 검색창에서 엔터를 치거나 [목록 조회] 버튼을 눌렀을 때 실행
		if (!"목록 조회".equals(cmd)) {
			switch (cmd) {
				case "고객 추가": custInsFrm.setVisible(true); break;
				case "저장":      insertOne(); break;
				case "취소":      custInsFrm.setVisible(false); custUpFrm.setVisible(false); break;
				case "수정":      if (e.getSource() == btnUpdateBottom) openUpdateFrmChecked(); else updateOne(); break;
				case "삭제":      deleteChecked(); break;
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
