package presentation.swing.reservation;

import domain.common.OptionItem;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import javax.swing.*;

public class ReservationCreateFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JComboBox<OptionItem> cbCustomer, cbMovie, cbScreening;
	protected JTextField tfSeatNo;
	private JButton btnSave, btnCancel;

	public ReservationCreateFrame() {
		panInsert = new JPanel(); panButton = new JPanel();
		cbCustomer = new JComboBox<>(); cbMovie = new JComboBox<>();
		cbScreening = new JComboBox<>();
		tfSeatNo = new JTextField();
		btnSave = new JButton("저장"); btnCancel = new JButton("취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("예약 추가"); setSize(560, 280);
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
		panInsert.add(tfSeatNo);    tfSeatNo.setBounds(90,   140, 420, 30);
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnSave); panButton.add(btnCancel);
	}

	public void addEvent(ReservationController c) {
		c.setReservationCreateFrame(this);
		btnSave.addActionListener(c); btnCancel.addActionListener(c);
		cbMovie.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				OptionItem selected = (OptionItem) cbMovie.getSelectedItem();
				if (selected != null) c.reloadScreenings(selected.id, cbScreening);
			}
		});
	}

}
