package common.ui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class MenuControl implements ActionListener {

	private JFrame frm;
	private CardLayout card;

	public MenuControl(JFrame frm, CardLayout card) {
		this.frm = frm;
		this.card = card;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "영화 관리":     card.show(frm.getContentPane(), "MovieList");    break;
			case "상영관 관리":   card.show(frm.getContentPane(), "TheaterList");  break;
			case "상영일정 관리": card.show(frm.getContentPane(), "ScreenList");   break;
			case "고객 관리":     card.show(frm.getContentPane(), "CustList");     break;
			case "예약 관리":     card.show(frm.getContentPane(), "ReservList");   break;
		}
	}

}
