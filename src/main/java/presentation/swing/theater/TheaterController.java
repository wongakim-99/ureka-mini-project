package presentation.swing.theater;

import infrastructure.AppLogger;
import java.util.logging.Logger;
import domain.theater.Theater;
import domain.theater.TheaterService;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class TheaterController extends MouseAdapter implements ActionListener {

	private static final Logger log = AppLogger.get(TheaterController.class);

	private final TheaterService service;
	private List<Theater> theaterList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog; private JLabel dialogLabel;
	private JTable table;
	private TheaterCreateFrame theaterCreateFrame;
	private TheaterUpdateFrame theaterUpdateFrame;
	private int selectedTheaterId;
	private JButton btnDeleteTop, btnUpdateBottom;

	public TheaterController(TheaterService service, JDialog dialog, JLabel dialogLabel) {
		this.service = service;
		columnNames = new Vector<>();
		columnNames.add("선택"); columnNames.add("TheaterID"); columnNames.add("상영관명"); columnNames.add("총 좌석수");
		this.dialog = dialog; this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)                        { this.table = t; }
	public void setTheaterCreateFrame(TheaterCreateFrame f) { this.theaterCreateFrame = f; }
	public void setTheaterUpdateFrame(TheaterUpdateFrame f) { this.theaterUpdateFrame = f; }
	public void setDeleteBtn(JButton btn)                 { this.btnDeleteTop = btn; }
	public void setUpdateBtn(JButton btn)                 { this.btnUpdateBottom = btn; }
	public void load()                                    { readAll(); }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		long start = System.currentTimeMillis();
		try { theaterList = service.findAll(); }
		catch (SQLException e) { theaterList = new ArrayList<>(); dialogOpen("상영관 목록 조회 실패"); }
		log.fine(String.format("DB 조회 완료 (%d건, %dms)", theaterList.size(), System.currentTimeMillis() - start));

		Vector<Vector<Object>> data = new Vector<>();
		for (Theater t : theaterList) {
			Vector<Object> row = new Vector<>();
			row.add(Boolean.FALSE);
			row.add(String.valueOf(t.getTheaterId())); row.add(t.getName()); row.add(String.valueOf(t.getTotalSeats()));
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

	private void insertOne() {
		try {
			int seats = Integer.parseInt(theaterCreateFrame.tfTotalSeats.getText().trim());
			service.save(new Theater(0, theaterCreateFrame.tfName.getText().trim(), seats));
			theaterCreateFrame.tfName.setText(""); theaterCreateFrame.tfTotalSeats.setText("");
			theaterCreateFrame.setVisible(false); readAll();
		} catch (NumberFormatException e) {
			dialogOpen("좌석 수는 숫자로 입력해주세요.");
		} catch (SQLException e) {
			dialogOpen(e.getMessage() != null ? e.getMessage() : "상영관 추가 실패");
		}
	}

	private void updateOne() {
		try {
			int seats = Integer.parseInt(theaterUpdateFrame.tfTotalSeats.getText().trim());
			service.update(new Theater(selectedTheaterId, theaterUpdateFrame.tfName.getText().trim(), seats));
			clearUpdateFrame(); readAll();
		} catch (NumberFormatException e) {
			dialogOpen("좌석 수는 숫자로 입력해주세요.");
		} catch (SQLException e) {
			dialogOpen(e.getMessage() != null ? e.getMessage() : "상영관 수정 실패");
		}
	}

	private void deleteChecked() {
		int opt = JOptionPane.showConfirmDialog(null, "선택한 상영관 정보를 정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
		if (opt != JOptionPane.YES_OPTION) return;

		int count = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			if ((Boolean) table.getValueAt(i, 0)) {
				int id = Integer.parseInt(table.getValueAt(i, 1).toString());
				try { service.delete(id); count++; } catch (SQLException ignored) {}
			}
		}
		if (count > 0) { dialogOpen(count + "건의 상영관 정보가 삭제되었습니다."); readAll(); }
	}

	private void openCheckedUpdateFrame() {
		for (int i = 0; i < table.getRowCount(); i++) {
			if ((Boolean) table.getValueAt(i, 0)) {
				selectedTheaterId = Integer.parseInt(table.getValueAt(i, 1).toString());
				Theater t = theaterList.stream().filter(item -> item.getTheaterId() == selectedTheaterId).findFirst().orElse(null);
				if (t != null) {
					theaterUpdateFrame.tfName.setText(t.getName());
					theaterUpdateFrame.tfTotalSeats.setText(String.valueOf(t.getTotalSeats()));
					theaterUpdateFrame.setVisible(true);
				}
				break;
			}
		}
	}

	private void clearUpdateFrame() {
		theaterUpdateFrame.tfName.setText(""); theaterUpdateFrame.tfTotalSeats.setText("");
		theaterUpdateFrame.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (!"목록 조회".equals(cmd)) {
			switch (cmd) {
				case "상영관 추가": theaterCreateFrame.setVisible(true); break;
				case "저장":        insertOne(); break;
				case "취소":        theaterCreateFrame.setVisible(false); theaterUpdateFrame.setVisible(false); break;
				case "수정":        if (e.getSource() == btnUpdateBottom) openCheckedUpdateFrame(); else updateOne(); break;
				case "삭제":        deleteChecked(); break;
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
