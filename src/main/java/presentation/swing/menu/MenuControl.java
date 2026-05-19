package presentation.swing.menu;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuControl implements ActionListener {

	private Container cardContainer;
	private CardLayout card;

	public MenuControl(Container cardContainer, CardLayout card) {
		this.cardContainer = cardContainer;
		this.card = card;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "영화 관리":     card.show(cardContainer, "MovieList");    break;
			case "상영관 관리":   card.show(cardContainer, "TheaterList");  break;
			case "상영일정 관리": card.show(cardContainer, "ScreenList");   break;
			case "고객 관리":     card.show(cardContainer, "CustList");     break;
			case "예약 관리":     card.show(cardContainer, "ReservList");   break;
		}
	}

}
