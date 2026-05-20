package presentation.swing.maindashboard;

import java.awt.Color;
import java.awt.Font;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

final class DashboardSidebar extends JPanel {

	DashboardSidebar(Consumer<String> menuHandler) {
		setLayout(null);
		setBackground(new Color(34, 34, 34));
		setBounds(0, 0, 150, 600);

		JLabel logoText = new JLabel("CGV 선릉점", SwingConstants.CENTER);
		logoText.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
		logoText.setForeground(new Color(229, 9, 20));
		logoText.setBounds(0, 20, 150, 30);
		add(logoText);

		String[] menus = {"영화 관리", "예약 관리", "상영 일정 관리", "상영관 관리", "고객 관리", "수입 관리"};
		int yOffset = 100;
		for (String menu : menus) {
			JButton menuBtn = new JButton(menu);
			menuBtn.setBounds(0, yOffset, 150, 45);
			menuBtn.setForeground(Color.WHITE);
			menuBtn.setBackground(new Color(34, 34, 34));
			menuBtn.setBorderPainted(false);
			menuBtn.setFocusPainted(false);
			menuBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
			menuBtn.addActionListener(e -> menuHandler.accept(menu));
			add(menuBtn);
			yOffset += 50;
		}
	}
}
