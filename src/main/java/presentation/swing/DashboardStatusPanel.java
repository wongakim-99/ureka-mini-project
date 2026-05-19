package presentation.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

final class DashboardStatusPanel extends JPanel {

	private final JLabel clockLabel;
	private final JLabel movieCountLabel;

	DashboardStatusPanel(ActionListener syncAction) {
		setLayout(null);
		setBackground(new Color(229, 9, 20));
		setBounds(150, 0, 260, 600);

		JLabel branchLabel = new JLabel("SEOLLEUNG");
		branchLabel.setBounds(20, 30, 200, 20);
		branchLabel.setFont(new Font("Arial", Font.BOLD, 12));
		branchLabel.setForeground(new Color(255, 192, 192));
		add(branchLabel);

		JLabel managerLabel = new JLabel("SYSTEM MANAGER", SwingConstants.RIGHT);
		managerLabel.setBounds(100, 30, 140, 20);
		managerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		managerLabel.setForeground(Color.WHITE);
		add(managerLabel);

		clockLabel = new JLabel("", SwingConstants.CENTER);
		clockLabel.setForeground(Color.WHITE);
		clockLabel.setBounds(0, 55, 260, 130);
		add(clockLabel);

		JSeparator separator = new JSeparator();
		separator.setBounds(30, 195, 200, 5);
		add(separator);

		JLabel infoTitle = new JLabel("TOTAL MOVIES");
		infoTitle.setBounds(20, 250, 220, 20);
		infoTitle.setFont(new Font("Arial", Font.BOLD, 16));
		infoTitle.setForeground(Color.WHITE);
		infoTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(infoTitle);

		movieCountLabel = new JLabel("0", SwingConstants.CENTER);
		movieCountLabel.setBounds(0, 290, 260, 90);
		movieCountLabel.setFont(new Font("Arial", Font.BOLD, 74));
		movieCountLabel.setForeground(Color.WHITE);
		add(movieCountLabel);

		JButton syncButton = new JButton("데이터 동기화");
		syncButton.setBounds(50, 450, 160, 40);
		syncButton.setBackground(new Color(229, 9, 20));
		syncButton.setForeground(Color.WHITE);
		syncButton.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
		syncButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		syncButton.setFocusPainted(false);
		syncButton.addActionListener(syncAction);
		add(syncButton);
	}

	JLabel getClockLabel() {
		return clockLabel;
	}

	JLabel getMovieCountLabel() {
		return movieCountLabel;
	}
}
