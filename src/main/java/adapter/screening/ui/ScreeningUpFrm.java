package adapter.screening.ui;

import common.ComboItem;
import common.ui.DatePickerDialog;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;

public class ScreeningUpFrm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JComboBox<ComboItem> cbMovie, cbTheater;
	protected JTextField tfDate;
	protected JComboBox<String> cbHour, cbMin;
	protected JTextField tfPrice;
	private JButton btnUpdate, btnDelete, btnCancel;

	public ScreeningUpFrm() {
		panInsert  = new JPanel(); panButton = new JPanel();
		cbMovie    = new JComboBox<>(); cbTheater = new JComboBox<>();
		
		tfDate = new JTextField();
		cbHour = new JComboBox<>(new String[]{"08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","00","01"});
		String[] mins = {"00","05","10","15","20","25","30","35","40","45","50","55"};
		cbMin = new JComboBox<>(mins);

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
		panInsert.add(cbMovie);   cbMovie.setBounds(90, 20, 380, 30);
		panInsert.add(cbTheater); cbTheater.setBounds(90, 60, 380, 30);

		JButton btnDatePick = new JButton("▼");
		btnDatePick.addActionListener(e -> {
			String picked = DatePickerDialog.show(this, tfDate.getText());
			if (picked != null) tfDate.setText(picked);
		});
		panInsert.add(tfDate);      tfDate.setBounds(90, 100, 120, 30);
		panInsert.add(btnDatePick); btnDatePick.setBounds(215, 100, 30, 30);
		panInsert.add(cbHour);      cbHour.setBounds(250, 100, 70, 30);
		panInsert.add(new JLabel("시")).setBounds(325, 100, 20, 30);
		panInsert.add(cbMin);       cbMin.setBounds(350, 100, 70, 30);
		panInsert.add(new JLabel("분")).setBounds(425, 100, 20, 30);

		panInsert.add(tfPrice);   tfPrice.setBounds(90, 140, 380, 30);
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnUpdate); panButton.add(btnDelete); panButton.add(btnCancel);
	}

	public void addEvent(ScreeningControl c) {
		c.setScreeningUpFrm(this);
		btnUpdate.addActionListener(c); btnDelete.addActionListener(c); btnCancel.addActionListener(c);
	}

}
