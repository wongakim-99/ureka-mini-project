package infrastructure.theater.ui;

import java.awt.BorderLayout;
import javax.swing.*;

public class TheaterInsFrm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JTextField tfName, tfTotalSeats;
	private JButton btnSave, btnCancel;

	public TheaterInsFrm() {
		panInsert = new JPanel(); panButton = new JPanel();
		tfName = new JTextField(); tfTotalSeats = new JTextField();
		btnSave = new JButton("저장"); btnCancel = new JButton("취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("상영관 추가"); setSize(450, 200);
		add(panInsert, BorderLayout.CENTER); panInsert.setLayout(null);
		JLabel lName = new JLabel("상영관명"); JLabel lSeats = new JLabel("총 좌석수");
		panInsert.add(lName);   lName.setBounds(10, 20, 70, 30);
		panInsert.add(lSeats);  lSeats.setBounds(10, 60, 70, 30);
		panInsert.add(tfName);        tfName.setBounds(90, 20, 300, 30);
		panInsert.add(tfTotalSeats);  tfTotalSeats.setBounds(90, 60, 300, 30);
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnSave); panButton.add(btnCancel);
	}

	public void addEvent(TheaterControl c) {
		c.setTheaterInsFrm(this);
		btnSave.addActionListener(c); btnCancel.addActionListener(c);
	}

}
