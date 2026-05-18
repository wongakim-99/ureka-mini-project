package menubook.customer;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CustInsFrm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	private JLabel labelName, labelAddress, labelPhone;
	protected JTextField tfName, tfAddress, tfPhone;
	private JButton btnSave, btnCancel;

	public CustInsFrm() {
		panInsert = new JPanel();
		panButton = new JPanel();
		labelName = new JLabel("Name");
		labelAddress = new JLabel("Address");
		labelPhone = new JLabel("Phone");
		tfName = new JTextField();
		tfAddress = new JTextField();
		tfPhone = new JTextField();
		btnSave = new JButton("고객 저장");
		btnCancel = new JButton("입력 취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("고객 정보 입력");
		setSize(500, 500);
		// ====================
		add(panInsert, BorderLayout.CENTER);
		panInsert.setLayout(null);
		panInsert.add(labelName);	labelName.setBounds(10, 20, 100, 35);
		panInsert.add(labelAddress);	labelAddress.setBounds(10, 70, 100, 35);
		panInsert.add(labelPhone);		labelPhone.setBounds(10, 120, 100, 35);
		panInsert.add(tfName);		tfName.setBounds(120, 20, 300, 35);
		panInsert.add(tfAddress);		tfAddress.setBounds(120, 70, 300, 35);
		panInsert.add(tfPhone);			tfPhone.setBounds(120, 120, 300, 35);
		// ====================
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnSave);
		panButton.add(btnCancel);
	} // makeGui

	public void addEvent(CustControl custControl) {
		custControl.setCustInsFrm( this );
		btnSave.addActionListener( custControl );
		btnCancel.addActionListener( custControl );
	} // addEvent

} // class
