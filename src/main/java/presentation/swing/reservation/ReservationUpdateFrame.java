package presentation.swing.reservation;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.common.OptionItem;

public class ReservationUpdateFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JComboBox<OptionItem> cbCustomer, cbMovie, cbScreening;
	protected JComboBox<String> cbSeatRow, cbSeatColumn;
	private JButton btnUpdate, btnDelete, btnCancel;

	public ReservationUpdateFrame() {
		panInsert = new JPanel(); panButton = new JPanel();
		cbCustomer = new JComboBox<>(); cbMovie = new JComboBox<>();
		cbScreening = new JComboBox<>();
		cbSeatRow = new JComboBox<>(); cbSeatColumn = new JComboBox<>();
		initializeSeatSelectors();
		btnUpdate = new JButton("수정"); btnDelete = new JButton("삭제"); btnCancel = new JButton("취소");
		makeGui();
	}

	private void initializeSeatSelectors() {
		for (char row = 'A'; row <= 'K'; row++) cbSeatRow.addItem(String.valueOf(row));
		for (int col = 1; col <= 16; col++) cbSeatColumn.addItem(String.valueOf(col));
	}

	private void makeGui() {
		setTitle("예약 수정/삭제"); setSize(560, 280);
		add(panInsert, BorderLayout.CENTER); panInsert.setLayout(null);
		JLabel lC = new JLabel("고객");    JLabel lM = new JLabel("영화");
		JLabel lS = new JLabel("상영일정"); JLabel lN = new JLabel("좌석번호");
		panInsert.add(lC); lC.setBounds(10, 20,  70, 30);
		panInsert.add(lM); lM.setBounds(10, 60,  70, 30);
		panInsert.add(lS); lS.setBounds(10, 100, 70, 30);
		panInsert.add(lN); lN.setBounds(10, 140, 70, 30);
		panInsert.add(cbCustomer);  cbCustomer.setBounds(90, 20,  420, 30);
		panInsert.add(cbMovie);     cbMovie.setBounds(90,    60,  420, 30);
		panInsert.add(cbScreening); cbScreening.setBounds(90, 100, 420, 30);
		panInsert.add(cbSeatRow);   cbSeatRow.setBounds(90,  140, 160, 30);
		panInsert.add(cbSeatColumn);cbSeatColumn.setBounds(260, 140, 120, 30);
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnUpdate); panButton.add(btnDelete); panButton.add(btnCancel);
	}

	public void addEvent(ReservationController c) {
		c.setReservationUpdateFrame(this);
		btnUpdate.addActionListener(c); btnDelete.addActionListener(c); btnCancel.addActionListener(c);
		cbMovie.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				OptionItem selected = (OptionItem) cbMovie.getSelectedItem();
				if (selected != null) c.reloadScreenings(selected.id, cbScreening);
			}
		});
	}

}
