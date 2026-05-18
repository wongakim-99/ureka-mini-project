package menubook.order;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class OrdrListPan extends JPanel {

	private static final long serialVersionUID = 1L;

	private BorderLayout border;
	protected JTable table;
	private JScrollPane scrollPan;
	private JPanel pan;
	private JButton btnAdd, btnReadAll;

	public OrdrListPan() {
		border = new BorderLayout();
		table = new JTable();
		table.setName("ordrList");
		scrollPan = new JScrollPane(table);
		pan = new JPanel();
		btnAdd = new JButton("주문 추가");
		btnReadAll = new JButton("목록 조회");
		makeGui();
	}

	private void makeGui() {
		setSize(500, 500);
		setLayout(border);
		add(scrollPan, BorderLayout.NORTH);
		add(pan, BorderLayout.SOUTH);
		pan.add(btnAdd);
		pan.add(btnReadAll);
		setVisible(true);
	} // makeGui

	public void addEvent(OrdrControl ordrControl) {
		ordrControl.setTable( table );
		btnReadAll.addActionListener( ordrControl );
		btnAdd.addActionListener( ordrControl );
		table.addMouseListener( ordrControl );
	} // addEvent

} // class
