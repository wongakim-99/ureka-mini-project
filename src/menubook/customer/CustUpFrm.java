package menubook.customer;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CustUpFrm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	private JLabel labelName, labelAddress, labelPhone;
	protected JTextField tfName, tfAddress, tfPhone;
	private JButton btnUpdate, btnDelete, btnCancel;

	public CustUpFrm() {
		panInsert = new JPanel();
		panButton = new JPanel();
		labelName = new JLabel("Name");
		labelAddress = new JLabel("Address");
		labelPhone = new JLabel("Phone");
		tfName = new JTextField();
		tfAddress = new JTextField();
		tfPhone = new JTextField();
		btnUpdate = new JButton("고객 수정");
		btnDelete = new JButton("고객 삭제");
		btnCancel = new JButton("수정 취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("고객 정보 수정");
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
		panButton.add(btnUpdate);
		panButton.add(btnDelete);
		panButton.add(btnCancel);
	} // makeGui

	public void addEvent(CustControl custControl) {
		custControl.setCustUpFrm( this );
		btnUpdate.addActionListener( custControl );
		btnDelete.addActionListener( custControl );
		btnCancel.addActionListener( custControl );
	} // addEvent

} // class
