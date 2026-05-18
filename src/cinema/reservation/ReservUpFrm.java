package cinema.reservation;

import java.awt.BorderLayout;
import javax.swing.*;
import cinema.util.ComboItem;

public class ReservUpFrm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JComboBox<ComboItem> cbCustomer, cbScreening;
	protected JTextField tfSeatNo;
	private JButton btnUpdate, btnDelete, btnCancel;

	public ReservUpFrm() {
		panInsert   = new JPanel();
		panButton   = new JPanel();
		cbCustomer  = new JComboBox<>();
		cbScreening = new JComboBox<>();
		tfSeatNo    = new JTextField();
		btnUpdate   = new JButton("수정");
		btnDelete   = new JButton("삭제");
		btnCancel   = new JButton("취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("예약 수정/삭제");
		setSize(560, 240);
		add(panInsert, BorderLayout.CENTER);
		panInsert.setLayout(null);

		JLabel lCustomer  = new JLabel("고객");
		JLabel lScreening = new JLabel("상영일정");
		JLabel lSeatNo    = new JLabel("좌석번호");

		panInsert.add(lCustomer);   lCustomer.setBounds(10, 20, 70, 30);
		panInsert.add(lScreening);  lScreening.setBounds(10, 60, 70, 30);
		panInsert.add(lSeatNo);     lSeatNo.setBounds(10, 100, 70, 30);
		panInsert.add(cbCustomer);  cbCustomer.setBounds(90, 20, 420, 30);
		panInsert.add(cbScreening); cbScreening.setBounds(90, 60, 420, 30);
		panInsert.add(tfSeatNo);    tfSeatNo.setBounds(90, 100, 420, 30);

		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnUpdate);
		panButton.add(btnDelete);
		panButton.add(btnCancel);
	}

	public void addEvent(ReservControl reservControl) {
		reservControl.setReservUpFrm(this);
		btnUpdate.addActionListener(reservControl);
		btnDelete.addActionListener(reservControl);
		btnCancel.addActionListener(reservControl);
	}

}
