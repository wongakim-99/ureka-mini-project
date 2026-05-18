package adapter.screening.ui;

import common.ComboItem;
import java.awt.BorderLayout;
import java.util.Date;
import javax.swing.*;

public class ScreeningUpFrm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JComboBox<ComboItem> cbMovie, cbTheater;
	protected JSpinner spinShowtime;
	protected JTextField tfPrice;
	private JButton btnUpdate, btnDelete, btnCancel;

	public ScreeningUpFrm() {
		panInsert  = new JPanel(); panButton = new JPanel();
		cbMovie    = new JComboBox<>(); cbTheater = new JComboBox<>();
		spinShowtime = new JSpinner(new SpinnerDateModel());
		spinShowtime.setEditor(new JSpinner.DateEditor(spinShowtime, "yyyy-MM-dd HH:mm:ss"));
		tfPrice    = new JTextField();
		btnUpdate  = new JButton("수정"); btnDelete = new JButton("삭제"); btnCancel = new JButton("취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("상영일정 수정/삭제"); setSize(520, 250);
		add(panInsert, BorderLayout.CENTER); panInsert.setLayout(null);
		JLabel lM = new JLabel("영화");    JLabel lT = new JLabel("상영관");
		JLabel lS = new JLabel("상영시간"); JLabel lP = new JLabel("가격");
		panInsert.add(lM); lM.setBounds(10, 20,  70, 30);
		panInsert.add(lT); lT.setBounds(10, 60,  70, 30);
		panInsert.add(lS); lS.setBounds(10, 100, 70, 30);
		panInsert.add(lP); lP.setBounds(10, 140, 70, 30);
		panInsert.add(cbMovie);      cbMovie.setBounds(90,      20,  380, 30);
		panInsert.add(cbTheater);    cbTheater.setBounds(90,    60,  380, 30);
		panInsert.add(spinShowtime); spinShowtime.setBounds(90, 100, 380, 30);
		panInsert.add(tfPrice);      tfPrice.setBounds(90,      140, 380, 30);
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnUpdate); panButton.add(btnDelete); panButton.add(btnCancel);
	}

	public void addEvent(ScreeningControl c) {
		c.setScreeningUpFrm(this);
		btnUpdate.addActionListener(c); btnDelete.addActionListener(c); btnCancel.addActionListener(c);
	}

}
