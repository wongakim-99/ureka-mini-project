package cinema.customer;

import java.awt.BorderLayout;
import javax.swing.*;

public class CustListPan extends JPanel {

	private static final long serialVersionUID = 1L;

	protected JTable table;
	private JScrollPane scrollPan;
	private JPanel pan;
	private JButton btnAdd, btnReadAll;

	public CustListPan() {
		table      = new JTable();
		scrollPan  = new JScrollPane(table);
		pan        = new JPanel();
		btnAdd     = new JButton("고객 추가");
		btnReadAll = new JButton("목록 조회");
		makeGui();
	}

	private void makeGui() {
		setLayout(new BorderLayout());
		add(scrollPan, BorderLayout.CENTER);
		add(pan, BorderLayout.SOUTH);
		pan.add(btnAdd);
		pan.add(btnReadAll);
	}

	public void addEvent(CustControl custControl) {
		custControl.setTable(table);
		btnReadAll.addActionListener(custControl);
		btnAdd.addActionListener(custControl);
		table.addMouseListener(custControl);
	}

}
