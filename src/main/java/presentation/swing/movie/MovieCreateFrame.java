package presentation.swing.movie;

import java.awt.BorderLayout;
import javax.swing.*;

public class MovieCreateFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JTextField tfTitle, tfDirector, tfRating, tfRuntime;
	private JButton btnSave, btnCancel;

	public MovieCreateFrame() {
		panInsert  = new JPanel();
		panButton  = new JPanel();
		tfTitle    = new JTextField();
		tfDirector = new JTextField();
		tfRating   = new JTextField();
		tfRuntime  = new JTextField();
		btnSave    = new JButton("저장");
		btnCancel  = new JButton("취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("영화 추가");
		setSize(500, 280);
		add(panInsert, BorderLayout.CENTER);
		panInsert.setLayout(null);

		JLabel lTitle   = new JLabel("제목");		JLabel lDir     = new JLabel("감독");     
		JLabel lRate    = new JLabel("관람등급");	JLabel lRuntime = new JLabel("러닝타임(분)");


		panInsert.add(lTitle);     lTitle.setBounds(10, 20, 80, 30);
		panInsert.add(lDir);       lDir.setBounds(10, 60, 80, 30);
		panInsert.add(lRate);      lRate.setBounds(10, 100, 80, 30);
		panInsert.add(lRuntime);   lRuntime.setBounds(10, 140, 90, 30);

		panInsert.add(tfTitle);    tfTitle.setBounds(105, 20, 335, 30);
		panInsert.add(tfDirector); tfDirector.setBounds(105, 60, 335, 30);
		panInsert.add(tfRating);   tfRating.setBounds(105, 100, 335, 30);
		panInsert.add(tfRuntime);  tfRuntime.setBounds(105, 140, 100, 30);

		add(panButton, BorderLayout.SOUTH);
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnSave); panButton.add(btnCancel);
	}

	public void addEvent(MovieController c) {
		c.setMovieCreateFrame(this);
		btnSave.addActionListener(c); btnCancel.addActionListener(c);
	}

}
