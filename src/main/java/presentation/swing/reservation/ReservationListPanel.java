package presentation.swing.reservation;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

public class ReservationListPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	protected JTable table;
	private JScrollPane scrollPan;
	private JPanel pan;
	private JButton btnAdd, btnReadAll;

	public ReservationListPanel() {
		table = new JTable(); scrollPan = new JScrollPane(table); pan = new JPanel();
		btnAdd = new JButton("예약 추가"); btnReadAll = new JButton("목록 조회");
		makeGui();
	}

	private void makeGui() {
		setLayout(new BorderLayout());
		add(scrollPan, BorderLayout.CENTER); add(pan, BorderLayout.SOUTH);
		pan.add(btnAdd); pan.add(btnReadAll);
	}

	public void addEvent(ReservationController c) {
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
