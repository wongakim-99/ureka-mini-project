package presentation.swing.maindashboard;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

final class DashboardSidebar extends JPanel {

	private final List<JButton> menuButtons = new ArrayList<>();
	private final Color[] selectedColors = new Color[] {
		new Color(198, 33, 43),
		new Color(217, 44, 58),
		new Color(201, 45, 58),
		new Color(186, 20, 40),
		new Color(220, 64, 70),
		new Color(230, 80, 90)
	};

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
		for (int idx = 0; idx < menus.length; idx++) {
			String menu = menus[idx];
			JButton menuBtn = new JButton(menu);
			menuBtn.setBounds(0, yOffset, 150, 45);
			menuBtn.setForeground(Color.WHITE);
			menuBtn.setBackground(new Color(34, 34, 34));
			menuBtn.setBorderPainted(true);
			menuBtn.setFocusPainted(false);
			menuBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
			menuBtn.setOpaque(true);
			menuBtn.putClientProperty("selectedColor", selectedColors[idx % selectedColors.length]);
			menuBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 0, 0));
			// when clicked, notify handler and update selection visuals
			menuBtn.addActionListener(e -> {
				menuHandler.accept(menu);
				selectButton(menuBtn);
			});
			menuButtons.add(menuBtn);
			add(menuBtn);
			yOffset += 50;
		}

		// select first menu by default
		if (!menuButtons.isEmpty()) selectButton(menuButtons.get(0));
	}

	private void selectButton(JButton btn) {
		Color base = new Color(34, 34, 34);
		for (JButton b : menuButtons) {
			b.setBackground(base);
			b.setForeground(Color.WHITE);
			b.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 0, 0));
		}
		Color selected = (Color) btn.getClientProperty("selectedColor");
		if (selected == null) selected = new Color(229, 9, 20);
		btn.setBackground(selected.darker());
		btn.setForeground(Color.WHITE);
		btn.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 6, 0, 0, selected));
	}
}
