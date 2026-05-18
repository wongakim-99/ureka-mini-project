package adapter.movie.ui;

import java.awt.BorderLayout;
import javax.swing.*;

public class MovieListPan extends JPanel {

	private static final long serialVersionUID = 1L;

	protected JTable table;
	private JScrollPane scrollPan;
	private JPanel pan;
	private JButton btnAdd, btnReadAll;

	public MovieListPan() {
		table      = new JTable();
		scrollPan  = new JScrollPane(table);
		pan        = new JPanel();
		btnAdd     = new JButton("영화 추가");
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

	public void addEvent(MovieControl c) {
		c.setTable(table);
		btnReadAll.addActionListener(c);
		btnAdd.addActionListener(c);
		table.addMouseListener(c);
	}

}
