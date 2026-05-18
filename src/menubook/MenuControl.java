package menubook;

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

		String command = e.getActionCommand();

		if( command.equals( "도서 관리" ) ) {
			card.show( frm.getContentPane(), "BookList" );
		} else if( command.equals( "고객 관리" ) ) {
			card.show( frm.getContentPane(), "CustList" );
		} else if( command.equals( "주문 관리" ) ) {
			card.show( frm.getContentPane(), "OrdrList" );
		}

	} // actionPerformed

} // class
