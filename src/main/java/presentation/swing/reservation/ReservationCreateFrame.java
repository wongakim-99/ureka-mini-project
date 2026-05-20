package presentation.swing.reservation;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import domain.common.OptionItem;

public class ReservationCreateFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JTextField tfCustomerSearch;
	protected JTable tblCustomer;
	protected JScrollPane spCustomerTable;
	protected JLabel lblSelectedCustomer;
	protected JComboBox<OptionItem> cbMovie, cbScreening;
	protected JSpinner spPersonCount;
	protected JButton btnSelectSeats;
	protected JLabel lblSelectedSeats;
	private JButton btnSave, btnCancel;

	public ReservationCreateFrame() {
		panInsert = new JPanel(); panButton = new JPanel();
		tfCustomerSearch = new JTextField();
		tblCustomer = new JTable();
		spCustomerTable = new JScrollPane(tblCustomer);
		lblSelectedCustomer = new JLabel("선택된 고객 없음");
		cbMovie = new JComboBox<>();
		cbScreening = new JComboBox<>();
		// seat selection is handled via seat picker dialog
		spPersonCount = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		btnSelectSeats = new JButton("좌석 선택");
		lblSelectedSeats = new JLabel("선택된 좌석 없음");
		btnSave = new JButton("저장"); btnCancel = new JButton("취소");
		makeGui();
	}

	// seat selectors removed; seat selection uses SeatSelectionDialog

	private void makeGui() {
		setTitle("예약 추가"); setSize(560, 420);
		add(panInsert, BorderLayout.CENTER); panInsert.setLayout(null);
		JLabel lC = new JLabel("고객");    JLabel lM = new JLabel("영화");
		JLabel lS = new JLabel("상영일정"); JLabel lN = new JLabel("인원수");
		panInsert.add(lC); lC.setBounds(10, 20,  70, 30);
		panInsert.add(tfCustomerSearch); tfCustomerSearch.setBounds(90, 20, 420, 30);
		panInsert.add(lblSelectedCustomer); lblSelectedCustomer.setBounds(90, 50, 420, 20);
		panInsert.add(spCustomerTable); spCustomerTable.setBounds(90, 75, 420, 120);
		panInsert.add(lM); lM.setBounds(10, 210,  70, 30);
		panInsert.add(lS); lS.setBounds(10, 250, 70, 30);
		panInsert.add(lN); lN.setBounds(10, 290, 70, 30);
		panInsert.add(cbMovie);     cbMovie.setBounds(90,    210,  420, 30);
		panInsert.add(cbScreening); cbScreening.setBounds(90, 250, 420, 30);
		panInsert.add(spPersonCount); spPersonCount.setBounds(90, 290, 80, 30);
		panInsert.add(btnSelectSeats); btnSelectSeats.setBounds(180, 290, 120, 30);
		panInsert.add(lblSelectedSeats); lblSelectedSeats.setBounds(310, 290, 200, 30);
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnSave); panButton.add(btnCancel);
	}

	public void addEvent(ReservationController c) {
		c.setReservationCreateFrame(this);
		btnSave.addActionListener(c); btnCancel.addActionListener(c);
		tfCustomerSearch.getDocument().addDocumentListener(c.getCustomerSearchListener());
		tblCustomer.addMouseListener(c.getCustomerTableClickListener());
		cbMovie.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				OptionItem selected = (OptionItem) cbMovie.getSelectedItem();
				if (selected != null) c.reloadScreenings(selected.id, cbScreening);
			}
		});
		btnSelectSeats.addActionListener(e -> c.openSeatSelectionDialog());
	}

	public String getCustomerSearchKeyword() {
		return tfCustomerSearch.getText().trim();
	}

	public void setSelectedCustomerText(String text) {
		lblSelectedCustomer.setText(text);
	}

	public JTable getCustomerTable() {
		return tblCustomer;
	}

	public void clearCustomerSearch() {
		tfCustomerSearch.setText("");
		setSelectedCustomerText("선택된 고객 없음");
	}

	public String getSelectedSeatText() {
		String t = lblSelectedSeats.getText();
		if (t == null) return "";
		if (t.startsWith("선택된 좌석:")) return t.substring("선택된 좌석:".length()).trim();
		if (t.equals("선택된 좌석 없음")) return "";
		return t;
	}

}
