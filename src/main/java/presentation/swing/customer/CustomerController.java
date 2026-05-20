package presentation.swing.customer;

import infrastructure.AppLogger;
import java.util.logging.Logger;
import domain.customer.Customer;
import domain.customer.CustomerService;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class CustomerController extends MouseAdapter implements ActionListener {

	private static final Logger log = AppLogger.get(CustomerController.class);

	private final CustomerService service;
	private List<Customer> customerList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog; private JLabel dialogLabel;
	private JTable table;
	private CustomerCreateFrame customerCreateFrame;
	private CustomerUpdateFrame customerUpdateFrame;
	private int selectedCustomerId;
	private JButton btnDeleteTop, btnUpdateBottom;
	private String lastKeyword = "";

	public CustomerController(CustomerService service, JDialog dialog, JLabel dialogLabel) {
		this.service = service;
		columnNames = new Vector<>();
		columnNames.add("선택"); columnNames.add("CustID"); columnNames.add("이름"); columnNames.add("전화번호"); columnNames.add("이메일"); columnNames.add("나이");
		this.dialog = dialog; this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)        { this.table = t; }
	public void setCustomerCreateFrame(CustomerCreateFrame f) { this.customerCreateFrame = f; }
	public void setCustomerUpdateFrame(CustomerUpdateFrame f)   { this.customerUpdateFrame = f; }
	public void setDeleteBtn(JButton btn)   { this.btnDeleteTop = btn; }
	public void setUpdateBtn(JButton btn)   { this.btnUpdateBottom = btn; }
	public void load() { lastKeyword = ""; readAll(); }
	public void search(String keyword) { lastKeyword = keyword.trim(); readAll(); }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		long t = System.currentTimeMillis();
		try { customerList = service.findAll(); }
		catch (SQLException e) { customerList = new ArrayList<>(); dialogOpen("고객 목록 조회 실패"); }
		log.fine(String.format("DB 조회 완료 (%d건, %dms)", customerList.size(), System.currentTimeMillis() - t));

		String keyword = lastKeyword.toLowerCase();
		Vector<Vector<Object>> data = new Vector<>();
		for (Customer c : customerList) {
			// 이름이나 전화번호에 검색어가 포함되어 있는지 필터링
			if (!keyword.isEmpty() && !c.getName().toLowerCase().contains(keyword) && 
				!c.getPhone().toLowerCase().contains(keyword)) continue;

			Vector<Object> row = new Vector<>();
			row.add(Boolean.FALSE);
			row.add(String.valueOf(c.getCustId())); row.add(c.getName()); row.add(c.getPhone()); row.add(c.getEmail());
			row.add(c.getAge() != null ? String.valueOf(c.getAge()) : "");
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
		String name = customerCreateFrame.tfName.getText().trim();
		String phone = customerCreateFrame.tfPhone.getText().trim();
		String email = customerCreateFrame.tfEmail.getText().trim();
		Integer age = parseAge(customerCreateFrame.tfAge.getText());

		if (name.isEmpty()) { dialogOpen("이름을 입력해주세요."); return; }
		if (phone.isEmpty()) { dialogOpen("전화번호를 입력해주세요."); return; }
		if (email.isEmpty()) { dialogOpen("이메일을 입력해주세요."); return; }
		if (!isValidEmail(email)) { dialogOpen("올바른 이메일 형식을 입력해주세요. (예: user@example.com)"); return; }

		try {
			service.save(new Customer(0, name, phone, email, age));
			customerCreateFrame.tfName.setText(""); customerCreateFrame.tfPhone.setText("");
			customerCreateFrame.tfEmail.setText(""); customerCreateFrame.tfAge.setText("");
			customerCreateFrame.setVisible(false); readAll();
		} catch (SQLException e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "고객 추가 실패"); }
	}

	private boolean isValidEmail(String email) {
		return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	}

	private Integer parseAge(String text) {
		if (text == null || text.trim().isEmpty()) return null;
		try { return Integer.parseInt(text.trim()); } catch (NumberFormatException e) { return null; }
	}

	private void updateOne() {
		String name = customerUpdateFrame.tfName.getText().trim();
		String phone = customerUpdateFrame.tfPhone.getText().trim();
		String email = customerUpdateFrame.tfEmail.getText().trim();
		Integer age = parseAge(customerUpdateFrame.tfAge.getText());

		if (name.isEmpty()) { dialogOpen("이름을 입력해주세요."); return; }
		if (phone.isEmpty()) { dialogOpen("전화번호를 입력해주세요."); return; }
		if (email.isEmpty()) { dialogOpen("이메일을 입력해주세요."); return; }
		if (!isValidEmail(email)) { dialogOpen("올바른 이메일 형식을 입력해주세요. (예: user@example.com)"); return; }

		try {
			service.update(new Customer(selectedCustomerId, name, phone, email, age));
			clearUpdateFrame(); readAll();
		} catch (SQLException e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "고객 수정 실패"); }
	}

	private void deleteOne() {
		try { service.delete(selectedCustomerId); clearUpdateFrame(); readAll(); }
		catch (SQLException e) { dialogOpen("고객 삭제 실패"); }
	}

	private void clearUpdateFrame() {
		customerUpdateFrame.tfName.setText(""); customerUpdateFrame.tfPhone.setText("");
		customerUpdateFrame.tfEmail.setText(""); customerUpdateFrame.tfAge.setText("");
		customerUpdateFrame.setVisible(false);
	}

	private void openCheckedUpdateFrame() {
		for (int i = 0; i < table.getRowCount(); i++) {
			int modelRow = table.convertRowIndexToModel(i);
			if ((Boolean) table.getModel().getValueAt(modelRow, 0)) {
				selectedCustomerId = Integer.parseInt(table.getModel().getValueAt(modelRow, 1).toString());
				Customer c = customerList.stream().filter(item -> item.getCustId() == selectedCustomerId).findFirst().orElse(null);
				if (c != null) {
					customerUpdateFrame.tfName.setText(c.getName());
					customerUpdateFrame.tfPhone.setText(c.getPhone());
					customerUpdateFrame.tfEmail.setText(c.getEmail());
					customerUpdateFrame.tfAge.setText(c.getAge() != null ? String.valueOf(c.getAge()) : "");
					customerUpdateFrame.setVisible(true);
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
			int modelRow = table.convertRowIndexToModel(i);
			if ((Boolean) table.getModel().getValueAt(modelRow, 0)) {
				int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 1).toString());
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
				case "고객 추가": customerCreateFrame.setVisible(true); break;
				case "저장":      insertOne(); break;
				case "취소":      customerCreateFrame.setVisible(false); customerUpdateFrame.setVisible(false); break;
				case "수정":      if (e.getSource() == btnUpdateBottom) openCheckedUpdateFrame(); else updateOne(); break;
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
