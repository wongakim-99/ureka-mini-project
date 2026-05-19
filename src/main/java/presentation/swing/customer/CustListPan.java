package presentation.swing.customer;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

public class CustListPan extends JPanel {

	private static final long serialVersionUID = 1L;

	protected JTable table;
	private JScrollPane scrollPan;
	private JPanel pan;
	private JButton btnAdd, btnReadAll;

	public CustListPan() {
		table = new JTable(); scrollPan = new JScrollPane(table); pan = new JPanel();
		btnAdd = new JButton("고객 추가"); btnReadAll = new JButton("목록 조회");
		makeGui();
	}

	private void makeGui() {
		setLayout(new BorderLayout());
		add(scrollPan, BorderLayout.CENTER); add(pan, BorderLayout.SOUTH);
		pan.add(btnAdd); pan.add(btnReadAll);
	}

	public void addEvent(CustControl c) {
		c.setTable(table);
		btnReadAll.addActionListener(c); btnAdd.addActionListener(c);
		table.addMouseListener(c);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				c.actionPerformed(new ActionEvent(btnReadAll, ActionEvent.ACTION_PERFORMED, "목록 조회"));
			}
		});
	}

}
