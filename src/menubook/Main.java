package menubook;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import menubook.book.BookControl;
import menubook.book.BookInsFrm;
import menubook.book.BookListPan;
import menubook.book.BookUpFrm;
import menubook.customer.CustControl;
import menubook.customer.CustInsFrm;
import menubook.customer.CustListPan;
import menubook.customer.CustUpFrm;
import menubook.order.OrdrControl;
import menubook.order.OrdrInsFrm;
import menubook.order.OrdrListPan;
import menubook.order.OrdrUpFrm;

public class Main {

	private JFrame frm;
	private CardLayout card;
	// =====================
	private MenuBar menuBar;
	private Menu menu1;
	private MenuItem menuItemBook, menuItemCust, menuItemOrdr;
	private MenuControl menuControl;
	// =====================
	private JDialog dialog;
	private JLabel dialogLabel;
	private JButton btnDialogClose;
	private DialogControl dialogControl;
	// =====================
	private WindowControl windowControl;
	// =====================
	private BookListPan bookListPan;
	private BookInsFrm bookInsFrm;
	private BookUpFrm bookUpFrm;
	private BookControl bookControl;
	// =====================
	private CustListPan custListPan;
	private CustInsFrm custInsFrm;
	private CustUpFrm custUpFrm;
	private CustControl custControl;
	// =====================
	private OrdrListPan ordrListPan;
	private OrdrInsFrm ordrInsFrm;
	private OrdrUpFrm ordrUpFrm;
	private OrdrControl ordrControl;

	public Main() {
		frm = new JFrame("도서 관리 프로그램");
		card = new CardLayout();
		// =====================
		menuBar = new MenuBar();
		menu1 = new Menu("선 택");
		menuItemBook = new MenuItem("도서 관리");
		menuItemCust = new MenuItem("고객 관리");
		menuItemOrdr = new MenuItem("주문 관리");
		// =====================
		dialog = new JDialog(frm, "알림창", true); // modal - true : 창을 닫기 전에 다른 작업 불가.
		dialogLabel = new JLabel("");
		btnDialogClose = new JButton("알림창 닫기");
		// =====================
		bookListPan = new BookListPan();
		bookInsFrm = new BookInsFrm();
		bookUpFrm = new BookUpFrm();
		// =====================
		custListPan = new CustListPan();
		custInsFrm = new CustInsFrm();
		custUpFrm = new CustUpFrm();
		// =====================
		ordrListPan = new OrdrListPan();
		ordrInsFrm = new OrdrInsFrm();
		ordrUpFrm = new OrdrUpFrm();
	}

	private void makeGui() {
		frm.setSize(500, 500);
		// =====================
		frm.setMenuBar(menuBar);
		menuBar.add(menu1);
		menu1.add(menuItemBook);	menu1.addSeparator();
		menu1.add(menuItemCust);	menu1.addSeparator();
		menu1.add(menuItemOrdr);
		// =====================
		dialog.setSize(300, 100);
		dialog.setLayout( new GridLayout(2,1) );
		dialog.add(dialogLabel);
		dialog.add(btnDialogClose);
		// =====================
		frm.setLayout(card);
		frm.add(bookListPan, "BookList");
		frm.add(custListPan, "CustList");
		frm.add(ordrListPan, "OrdrList");
		// =====================
		frm.setVisible(true);
	} // makeGui

	private void addEvent() {
		menuControl = new MenuControl( frm, card );
		menuItemBook.addActionListener( menuControl );
		menuItemCust.addActionListener( menuControl );
		menuItemOrdr.addActionListener( menuControl );
		// =====================
		dialogControl = new DialogControl( dialog );
		btnDialogClose.addActionListener( dialogControl );
		// =====================
		windowControl = new WindowControl( dialog, dialogLabel );
		frm.addWindowListener( windowControl );
		// =====================
		bookControl = new BookControl( dialog, dialogLabel );
		bookListPan.addEvent( bookControl );
		bookInsFrm.addEvent( bookControl );
		bookUpFrm.addEvent( bookControl );
		// =====================
		custControl = new CustControl( dialog, dialogLabel );
		custListPan.addEvent( custControl );
		custInsFrm.addEvent( custControl );
		custUpFrm.addEvent( custControl );
		// =====================
		ordrControl = new OrdrControl( dialog, dialogLabel );
		ordrListPan.addEvent( ordrControl );
		ordrInsFrm.addEvent( ordrControl );
		ordrUpFrm.addEvent( ordrControl );
	} // addEvent

	public static void main(String[] args) {
		Main main = new Main();
		main.makeGui();
		main.addEvent();
	} // main

} // class
