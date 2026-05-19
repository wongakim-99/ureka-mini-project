package presentation.swing.customer;

import java.awt.BorderLayout;
import javax.swing.*;

public class CustomerUpdateFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	protected JTextField tfName, tfPhone, tfEmail;
	private JButton btnUpdate, btnDelete, btnCancel;

	public CustomerUpdateFrame() {
		panInsert = new JPanel(); panButton = new JPanel();
		tfName = new JTextField(); tfPhone = new JTextField(); tfEmail = new JTextField();
		btnUpdate = new JButton("수정"); btnDelete = new JButton("삭제"); btnCancel = new JButton("취소");
		makeGui();
	}

	private void makeGui() {
		setTitle("고객 수정/삭제"); setSize(450, 240);
		add(panInsert, BorderLayout.CENTER); panInsert.setLayout(null);
		JLabel lName = new JLabel("이름"); JLabel lPhone = new JLabel("전화번호"); JLabel lEmail = new JLabel("이메일");
		panInsert.add(lName);  lName.setBounds(10, 20, 70, 30);
		panInsert.add(lPhone); lPhone.setBounds(10, 60, 70, 30);
		panInsert.add(lEmail); lEmail.setBounds(10, 100, 70, 30);
		panInsert.add(tfName);  tfName.setBounds(90, 20, 300, 30);
		panInsert.add(tfPhone); tfPhone.setBounds(90, 60, 300, 30);
		panInsert.add(tfEmail); tfEmail.setBounds(90, 100, 300, 30);
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnUpdate); panButton.add(btnDelete); panButton.add(btnCancel);
	}

	public void addEvent(CustomerController c) {
		c.setCustomerUpdateFrame(this);
		btnUpdate.addActionListener(c); btnDelete.addActionListener(c); btnCancel.addActionListener(c);
	}

}
