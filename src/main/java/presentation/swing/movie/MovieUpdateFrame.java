package presentation.swing.movie;

import java.awt.BorderLayout;
import javax.swing.*;

public class MovieUpdateFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JTextField tfTitle, tfGenre, tfDirector, tfRating, tfRuntime;
	private JButton btnUpdate, btnDelete, btnCancel;

	public MovieUpdateFrame() {
		panInsert  = new JPanel();
		panButton  = new JPanel();
		tfTitle    = new JTextField();
		tfGenre    = new JTextField();
		tfDirector = new JTextField();
		tfRating   = new JTextField();
		tfRuntime  = new JTextField();
		btnUpdate  = new JButton("수정");
		btnDelete  = new JButton("삭제");
		btnCancel  = new JButton("취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("영화 수정/삭제");
		setSize(500, 330);
		add(panInsert, BorderLayout.CENTER);
		panInsert.setLayout(null);

		JLabel lTitle   = new JLabel("제목");     JLabel lGenre   = new JLabel("장르");
		JLabel lDir     = new JLabel("감독");     JLabel lRate    = new JLabel("관람등급");
		JLabel lRuntime = new JLabel("러닝타임(분)");

		panInsert.add(lTitle);     lTitle.setBounds(10, 20, 80, 30);
		panInsert.add(lGenre);     lGenre.setBounds(10, 60, 80, 30);
		panInsert.add(lDir);       lDir.setBounds(10, 100, 80, 30);
		panInsert.add(lRate);      lRate.setBounds(10, 140, 80, 30);
		panInsert.add(lRuntime);   lRuntime.setBounds(10, 180, 90, 30);
		panInsert.add(tfTitle);    tfTitle.setBounds(105, 20, 335, 30);
		panInsert.add(tfGenre);    tfGenre.setBounds(105, 60, 335, 30);
		panInsert.add(tfDirector); tfDirector.setBounds(105, 100, 335, 30);
		panInsert.add(tfRating);   tfRating.setBounds(105, 140, 335, 30);
		panInsert.add(tfRuntime);  tfRuntime.setBounds(105, 180, 100, 30);
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnUpdate); panButton.add(btnDelete); panButton.add(btnCancel);
	}

	public void addEvent(MovieController c) {
		c.setMovieUpdateFrame(this);
		btnUpdate.addActionListener(c); btnDelete.addActionListener(c); btnCancel.addActionListener(c);
	}

}
