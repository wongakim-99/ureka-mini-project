package cinema.movie;

import java.awt.BorderLayout;
import javax.swing.*;

public class MovieInsFrm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JTextField tfTitle, tfGenre, tfDirector, tfRating;
	private JButton btnSave, btnCancel;

	public MovieInsFrm() {
		panInsert  = new JPanel();
		panButton  = new JPanel();
		tfTitle    = new JTextField();
		tfGenre    = new JTextField();
		tfDirector = new JTextField();
		tfRating   = new JTextField();
		btnSave    = new JButton("저장");
		btnCancel  = new JButton("취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("영화 추가");
		setSize(500, 280);
		add(panInsert, BorderLayout.CENTER);
		panInsert.setLayout(null);

		JLabel lTitle    = new JLabel("제목");
		JLabel lGenre    = new JLabel("장르");
		JLabel lDirector = new JLabel("감독");
		JLabel lRating   = new JLabel("관람등급");

		panInsert.add(lTitle);     lTitle.setBounds(10, 20, 70, 30);
		panInsert.add(lGenre);     lGenre.setBounds(10, 60, 70, 30);
		panInsert.add(lDirector);  lDirector.setBounds(10, 100, 70, 30);
		panInsert.add(lRating);    lRating.setBounds(10, 140, 70, 30);
		panInsert.add(tfTitle);    tfTitle.setBounds(90, 20, 350, 30);
		panInsert.add(tfGenre);    tfGenre.setBounds(90, 60, 350, 30);
		panInsert.add(tfDirector); tfDirector.setBounds(90, 100, 350, 30);
		panInsert.add(tfRating);   tfRating.setBounds(90, 140, 350, 30);

		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnSave);
		panButton.add(btnCancel);
	}

	public void addEvent(MovieControl movieControl) {
		movieControl.setMovieInsFrm(this);
		btnSave.addActionListener(movieControl);
		btnCancel.addActionListener(movieControl);
	}

}
