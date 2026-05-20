package presentation.swing.revenue;

import domain.revenue.Revenue;
import domain.revenue.RevenueService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RevenueController implements ActionListener {

	private final RevenueService service;
	private List<Revenue> revenueList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog;
	private JLabel dialogLabel;
	private JTable table;
	private JLabel totalLabel;

	public RevenueController(RevenueService service, JDialog dialog, JLabel dialogLabel) {
		this.service = service;
		columnNames = new Vector<>();
		columnNames.add("MovieID");
		columnNames.add("제목");
		columnNames.add("총 수입 (원)");
		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)       { this.table = t; }
	public void setTotalLabel(JLabel l)  { this.totalLabel = l; }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	public void load() {
		try {
			revenueList = service.findAllByMovie();
			int total = service.calculateAll();
			if (totalLabel != null)
				totalLabel.setText("총 수입:  " + String.format("%,d", total) + " 원");
		} catch (SQLException e) {
			revenueList = new ArrayList<>();
			dialogOpen("수입 조회 실패");
		}

		Vector<Vector<Object>> data = new Vector<>();
		for (Revenue r : revenueList) {
			Vector<Object> row = new Vector<>();
			row.add(String.valueOf(r.getMovieId()));
			row.add(r.getMovieTitle());
			row.add(String.format("%,d", r.getTotalRevenue()));
			data.add(row);
		}
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override public boolean isCellEditable(int row, int col) { return false; }
		};
		table.setModel(model);
		if (table.getColumnModel().getColumnCount() > 0) {
			table.removeColumn(table.getColumnModel().getColumn(0));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("목록 조회".equals(e.getActionCommand())) load();
	}
}
