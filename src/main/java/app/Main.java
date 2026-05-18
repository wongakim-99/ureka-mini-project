package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.*;

import common.DBUtil;
import common.KobisImporter;
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
	private JPanel navPan;
	private JPanel contentPan;
	private JButton btnMovie, btnTheater, btnScreening, btnCust, btnResrv;
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
		navPan = new JPanel();
		contentPan = new JPanel(card);
		btnMovie = new JButton("영화 관리"); btnTheater = new JButton("상영관 관리");
		btnScreening = new JButton("상영일정 관리"); btnCust = new JButton("고객 관리");
		btnResrv = new JButton("예약 관리");
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
		navPan.add(btnMovie); navPan.add(btnTheater); navPan.add(btnScreening); navPan.add(btnCust); navPan.add(btnResrv);
		dialog.setSize(320, 100); dialog.setLayout(new GridLayout(2, 1));
		dialog.add(dialogLabel); dialog.add(btnDialogClose);
		frm.setLayout(new BorderLayout());
		frm.add(navPan, BorderLayout.NORTH);
		frm.add(contentPan, BorderLayout.CENTER);
		contentPan.add(movieListPan, "MovieList"); contentPan.add(theaterListPan, "TheaterList");
		contentPan.add(screeningListPan, "ScreenList"); contentPan.add(custListPan, "CustList");
		contentPan.add(reservListPan, "ReservList");
		frm.setVisible(true);
	}

	private void addEvent() {
		menuControl = new MenuControl(contentPan, card);
		btnMovie.addActionListener(menuControl); btnTheater.addActionListener(menuControl);
		btnScreening.addActionListener(menuControl); btnCust.addActionListener(menuControl);
		btnResrv.addActionListener(menuControl);
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

	private static void seedFromKobisIfEmpty() {
		try {
			var rs = DBUtil.getConnection().createStatement()
				.executeQuery("SELECT COUNT(*) FROM movie");
			rs.next();
			if (rs.getInt(1) == 0) {
				System.out.println("[KOBIS] movie 테이블이 비어있어 박스오피스 데이터를 가져옵니다...");
				new KobisImporter().importMovies();
				System.out.println("[KOBIS] 데이터 삽입 완료");
			}
		} catch (Exception e) {
			System.err.println("[KOBIS] 초기 데이터 로드 실패: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		seedFromKobisIfEmpty();
		Main main = new Main();
		main.makeGui();
		main.addEvent();
	}

}
