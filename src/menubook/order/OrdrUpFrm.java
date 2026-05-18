package menubook.order;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class OrdrUpFrm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panInsert, panButton;
	private JLabel labelName, labelBookName, labelSalePrice;
	protected JTextField tfName, tfBookName, tfSalePrice;
	private JButton btnUpdate, btnDelete, btnCancel;
	//======================================================
	protected JTable custTable, bookTable;
	private JScrollPane custScrollPan, bookScrollPan;
	private JButton custSearch, bookSearch;

	public OrdrUpFrm() {
		panInsert = new JPanel();
		panButton = new JPanel();
		labelName = new JLabel("Name");
		labelBookName = new JLabel("BookName");
		labelSalePrice = new JLabel("SalePrice");
		tfName = new JTextField();
		tfBookName = new JTextField();
		tfSalePrice = new JTextField();
		btnUpdate = new JButton("주문 수정");
		btnDelete = new JButton("주문 삭제");
		btnCancel = new JButton("수정 취소");
		//=======================================
		custTable = new JTable();
		custTable.setName("ordrUpCust");
		custScrollPan = new JScrollPane(custTable);
		bookTable = new JTable();
		bookTable.setName("ordrUpBook");
		bookScrollPan = new JScrollPane(bookTable);
		custSearch = new JButton("고객 검색");
		bookSearch = new JButton("도서 검색");
		custSearch.setName("ordrUpCustBtn");
		bookSearch.setName("ordrUpBookBtn");
		//=======================================
		makeGui();
	}

	private void makeGui() {
		setTitle("주문 정보 수정");
		setSize(500, 500);
		// ====================
		add(panInsert, BorderLayout.CENTER);
		panInsert.setLayout(null);
		panInsert.add(labelName);			labelName.setBounds(10, 20, 100, 35);
		panInsert.add(labelBookName);		labelBookName.setBounds(10, 180, 100, 35);
		panInsert.add(labelSalePrice);		labelSalePrice.setBounds(10, 340, 100, 35);
		panInsert.add(tfName);				tfName.setBounds(120, 20, 240, 35);
		panInsert.add(tfBookName);			tfBookName.setBounds(120, 180, 240, 35);
		panInsert.add(tfSalePrice);			tfSalePrice.setBounds(120, 340, 350, 35);
		// ====================
		panInsert.add(custSearch);			custSearch.setBounds(370, 20, 100, 35);
		panInsert.add(custScrollPan);		custScrollPan.setBounds(10, 60, 460, 100);
		panInsert.add(bookSearch);			bookSearch.setBounds(370, 180, 100, 35);
		panInsert.add(bookScrollPan);		bookScrollPan.setBounds(10, 220, 460, 100);
		// ====================
		add(panButton, BorderLayout.SOUTH);
		panButton.add(btnUpdate);
		panButton.add(btnDelete);
		panButton.add(btnCancel);
	} // makeGui

	public void addEvent(OrdrControl ordrControl) {
		ordrControl.setOrdrUpFrm( this );
		btnUpdate.addActionListener( ordrControl );
		btnDelete.addActionListener( ordrControl );
		btnCancel.addActionListener( ordrControl );
		// =========================================
		custSearch.addActionListener( ordrControl );
		bookSearch.addActionListener( ordrControl );
		custTable.addMouseListener( ordrControl );
		bookTable.addMouseListener( ordrControl );
	} // addEvent

} // class
