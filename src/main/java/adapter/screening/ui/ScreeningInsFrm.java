package adapter.screening.ui;

import common.ComboItem;
import java.awt.BorderLayout;
import java.util.Date;
import javax.swing.*;

public class ScreeningInsFrm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JComboBox<ComboItem> cbMovie, cbTheater;
	protected JSpinner spinShowtime;
	protected JTextField tfPrice;
	private JButton btnSave, btnCancel;

	public ScreeningInsFrm() {
		panInsert  = new JPanel(); panButton = new JPanel();
		cbMovie    = new JComboBox<>(); cbTheater = new JComboBox<>();
		spinShowtime = new JSpinner(new SpinnerDateModel());
		spinShowtime.setEditor(new JSpinner.DateEditor(spinShowtime, "yyyy-MM-dd HH:mm:ss"));
		tfPrice    = new JTextField();
		btnSave    = new JButton("저장"); btnCancel = new JButton("취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("상영일정 추가"); setSize(520, 250);
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
		panButton.add(btnSave); panButton.add(btnCancel);
	}

	public void addEvent(ScreeningControl c) {
		c.setScreeningInsFrm(this);
		btnSave.addActionListener(c); btnCancel.addActionListener(c);
	}

}
