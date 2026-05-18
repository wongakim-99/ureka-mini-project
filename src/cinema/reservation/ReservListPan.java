package cinema.reservation;

import java.awt.BorderLayout;
import javax.swing.*;

public class ReservListPan extends JPanel {

	private static final long serialVersionUID = 1L;

	protected JTable table;
	private JScrollPane scrollPan;
	private JPanel pan;
	private JButton btnAdd, btnReadAll;

	public ReservListPan() {
		table      = new JTable();
		scrollPan  = new JScrollPane(table);
		pan        = new JPanel();
		btnAdd     = new JButton("예약 추가");
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

	public void addEvent(ReservControl reservControl) {
		reservControl.setTable(table);
		btnReadAll.addActionListener(reservControl);
		btnAdd.addActionListener(reservControl);
		table.addMouseListener(reservControl);
	}

}
