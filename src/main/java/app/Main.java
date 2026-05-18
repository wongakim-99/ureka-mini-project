package app;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import javax.swing.*;

import common.ui.DialogControl;
import common.ui.WindowControl;
import adapter.movie.ui.*;
import adapter.theater.ui.*;
import adapter.screening.ui.*;
import adapter.customer.ui.*;
import adapter.reservation.ui.*;

public class Main {

	private JFrame frm;
	private CardLayout card;
	private MenuBar menuBar;
	private Menu menu1;
	private MenuItem menuItemMovie, menuItemTheater, menuItemScreening, menuItemCust, menuItemResrv;
	private MenuControl menuControl;
	private JDialog dialog;
	private JLabel dialogLabel;
	private JButton btnDialogClose;
	private DialogControl dialogControl;
	private WindowControl windowControl;

	private MovieListPan movieListPan; private MovieInsFrm movieInsFrm; private MovieUpFrm movieUpFrm; private MovieControl movieControl;
	private TheaterListPan theaterListPan; private TheaterInsFrm theaterInsFrm; private TheaterUpFrm theaterUpFrm; private TheaterControl theaterControl;
	private ScreeningListPan screeningListPan; private ScreeningInsFrm screeningInsFrm; private ScreeningUpFrm screeningUpFrm; private ScreeningControl screeningControl;
	private CustListPan custListPan; private CustInsFrm custInsFrm; private CustUpFrm custUpFrm; private CustControl custControl;
	private ReservListPan reservListPan; private ReservInsFrm reservInsFrm; private ReservUpFrm reservUpFrm; private ReservControl reservControl;

	public Main() {
		frm = new JFrame("영화 예약 관리 시스템"); card = new CardLayout();
		menuBar = new MenuBar(); menu1 = new Menu("메 뉴");
		menuItemMovie = new MenuItem("영화 관리"); menuItemTheater = new MenuItem("상영관 관리");
		menuItemScreening = new MenuItem("상영일정 관리"); menuItemCust = new MenuItem("고객 관리");
		menuItemResrv = new MenuItem("예약 관리");
		dialog = new JDialog(frm, "알림창", true);
		dialogLabel = new JLabel(""); btnDialogClose = new JButton("닫기");
		movieListPan = new MovieListPan(); movieInsFrm = new MovieInsFrm(); movieUpFrm = new MovieUpFrm();
		theaterListPan = new TheaterListPan(); theaterInsFrm = new TheaterInsFrm(); theaterUpFrm = new TheaterUpFrm();
		screeningListPan = new ScreeningListPan(); screeningInsFrm = new ScreeningInsFrm(); screeningUpFrm = new ScreeningUpFrm();
		custListPan = new CustListPan(); custInsFrm = new CustInsFrm(); custUpFrm = new CustUpFrm();
		reservListPan = new ReservListPan(); reservInsFrm = new ReservInsFrm(); reservUpFrm = new ReservUpFrm();
	}

	private void makeGui() {
		frm.setSize(750, 500);
		frm.setMenuBar(menuBar); menuBar.add(menu1);
		menu1.add(menuItemMovie); menu1.addSeparator(); menu1.add(menuItemTheater); menu1.addSeparator();
		menu1.add(menuItemScreening); menu1.addSeparator(); menu1.add(menuItemCust); menu1.addSeparator();
		menu1.add(menuItemResrv);
		dialog.setSize(320, 100); dialog.setLayout(new GridLayout(2, 1));
		dialog.add(dialogLabel); dialog.add(btnDialogClose);
		frm.setLayout(card);
		frm.add(movieListPan, "MovieList"); frm.add(theaterListPan, "TheaterList");
		frm.add(screeningListPan, "ScreenList"); frm.add(custListPan, "CustList");
		frm.add(reservListPan, "ReservList");
		frm.setVisible(true);
	}

	private void addEvent() {
		menuControl = new MenuControl(frm, card);
		menuItemMovie.addActionListener(menuControl); menuItemTheater.addActionListener(menuControl);
		menuItemScreening.addActionListener(menuControl); menuItemCust.addActionListener(menuControl);
		menuItemResrv.addActionListener(menuControl);
		dialogControl = new DialogControl(dialog); btnDialogClose.addActionListener(dialogControl);
		windowControl = new WindowControl(dialog, dialogLabel); frm.addWindowListener(windowControl);

		movieControl = new MovieControl(dialog, dialogLabel);
		movieListPan.addEvent(movieControl); movieInsFrm.addEvent(movieControl); movieUpFrm.addEvent(movieControl);

		theaterControl = new TheaterControl(dialog, dialogLabel);
		theaterListPan.addEvent(theaterControl); theaterInsFrm.addEvent(theaterControl); theaterUpFrm.addEvent(theaterControl);

		screeningControl = new ScreeningControl(dialog, dialogLabel);
		screeningListPan.addEvent(screeningControl); screeningInsFrm.addEvent(screeningControl); screeningUpFrm.addEvent(screeningControl);

		custControl = new CustControl(dialog, dialogLabel);
		custListPan.addEvent(custControl); custInsFrm.addEvent(custControl); custUpFrm.addEvent(custControl);

		reservControl = new ReservControl(dialog, dialogLabel);
		reservListPan.addEvent(reservControl); reservInsFrm.addEvent(reservControl); reservUpFrm.addEvent(reservControl);
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.makeGui();
		main.addEvent();
	}

}
