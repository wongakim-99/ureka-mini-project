package adapter.reservation.ui;

import common.ComboItem;
import java.awt.BorderLayout;
import javax.swing.*;

public class ReservInsFrm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JComboBox<ComboItem> cbCustomer, cbScreening;
	protected JTextField tfSeatNo;
	private JButton btnSave, btnCancel;

	public ReservInsFrm() {
		panInsert = new JPanel(); panButton = new JPanel();
		cbCustomer = new JComboBox<>(); cbScreening = new JComboBox<>();
		tfSeatNo = new JTextField();
		btnSave = new JButton("저장"); btnCancel = new JButton("취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("예약 추가"); setSize(560, 230);
		add(panInsert, BorderLayout.CENTER); panInsert.setLayout(null);
		JLabel lC = new JLabel("고객"); JLabel lS = new JLabel("상영일정"); JLabel lN = new JLabel("좌석번호");
		panInsert.add(lC); lC.setBounds(10, 20, 70, 30);
		panInsert.add(lS); lS.setBounds(10, 60, 70, 30);
		panInsert.add(lN); lN.setBounds(10, 100, 70, 30);
		panInsert.add(cbCustomer);  cbCustomer.setBounds(90, 20, 420, 30);
		panInsert.add(cbScreening); cbScreening.setBounds(90, 60, 420, 30);
		panInsert.add(tfSeatNo);    tfSeatNo.setBounds(90, 100, 420, 30);
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnSave); panButton.add(btnCancel);
	}

	public void addEvent(ReservControl c) {
		c.setReservInsFrm(this);
		btnSave.addActionListener(c); btnCancel.addActionListener(c);
	}

}
