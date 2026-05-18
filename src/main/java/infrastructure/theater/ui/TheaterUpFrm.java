package infrastructure.theater.ui;

import java.awt.BorderLayout;
import javax.swing.*;

public class TheaterUpFrm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JTextField tfName, tfTotalSeats;
	private JButton btnUpdate, btnDelete, btnCancel;

	public TheaterUpFrm() {
		panInsert = new JPanel(); panButton = new JPanel();
		tfName = new JTextField(); tfTotalSeats = new JTextField();
		btnUpdate = new JButton("수정"); btnDelete = new JButton("삭제"); btnCancel = new JButton("취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("상영관 수정/삭제"); setSize(450, 200);
		add(panInsert, BorderLayout.CENTER); panInsert.setLayout(null);
		JLabel lName = new JLabel("상영관명"); JLabel lSeats = new JLabel("총 좌석수");
		panInsert.add(lName);   lName.setBounds(10, 20, 70, 30);
		panInsert.add(lSeats);  lSeats.setBounds(10, 60, 70, 30);
		panInsert.add(tfName);        tfName.setBounds(90, 20, 300, 30);
		panInsert.add(tfTotalSeats);  tfTotalSeats.setBounds(90, 60, 300, 30);
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnUpdate); panButton.add(btnDelete); panButton.add(btnCancel);
	}

	public void addEvent(TheaterControl c) {
		c.setTheaterUpFrm(this);
		btnUpdate.addActionListener(c); btnDelete.addActionListener(c); btnCancel.addActionListener(c);
	}

}
