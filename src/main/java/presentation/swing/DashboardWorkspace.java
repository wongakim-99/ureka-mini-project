package presentation.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

final class DashboardWorkspace extends JPanel {

	private final JLabel menuTitleLabel;
	private final JTable dataTable;
	private final JButton addButton;
	private final JButton refreshButton;
	private final JButton deleteButton;
	private final JButton updateButton;
	private final JTextField searchField;
	private final JButton searchButton;
	private final JLabel totalRevenueLabel;

	DashboardWorkspace() {
		setLayout(null);
		setBackground(new Color(245, 247, 251));

		menuTitleLabel = new JLabel("영화 관리 시스템");
		menuTitleLabel.setBounds(440, 30, 300, 30);
		menuTitleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
		menuTitleLabel.setForeground(new Color(34, 34, 34));
		add(menuTitleLabel);

		JLabel subTitle = new JLabel("선릉점 데이터베이스의 ERD 기반 스키마 결과 조회입니다.");
		subTitle.setBounds(440, 65, 500, 20);
		subTitle.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
		subTitle.setForeground(Color.GRAY);
		add(subTitle);

		dataTable = new JTable(new DefaultTableModel());
		dataTable.setRowHeight(30);
		dataTable.setGridColor(new Color(230, 235, 245));
		dataTable.getTableHeader().setBackground(new Color(240, 242, 245));
		dataTable.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 12));
		dataTable.setSelectionBackground(new Color(173, 214, 255));
		dataTable.setSelectionForeground(Color.BLACK);
		dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dataTable.setRowSelectionAllowed(true);

		deleteButton = new JButton("삭제");
		deleteButton.setBounds(960, 30, 100, 35);
		deleteButton.setBackground(new Color(229, 9, 20));
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		deleteButton.setFocusPainted(false);
		deleteButton.setOpaque(true);
		deleteButton.setBorderPainted(false);
		deleteButton.setEnabled(false);
		add(deleteButton);

		searchField = new JTextField();
		searchField.setBounds(440, 92, 390, 28);
		searchField.setFont(new Font("Malgun Gothic", Font.PLAIN, 13));
		add(searchField);

		searchButton = new JButton("검색");
		searchButton.setBounds(837, 92, 80, 28);
		searchButton.setBackground(new Color(70, 70, 70));
		searchButton.setForeground(Color.WHITE);
		searchButton.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
		searchButton.setFocusPainted(false);
		searchButton.setOpaque(true);
		searchButton.setBorderPainted(false);
		add(searchButton);

		JScrollPane scrollPane = new JScrollPane(dataTable);
		scrollPane.setBounds(440, 130, 620, 330);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 225)));
		add(scrollPane);

		refreshButton = new JButton("목록 조회");
		refreshButton.setBounds(440, 480, 110, 35);
		refreshButton.setBackground(new Color(70, 70, 70));
		refreshButton.setForeground(Color.WHITE);
		refreshButton.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		refreshButton.setFocusPainted(false);
		refreshButton.setOpaque(true);
		refreshButton.setBorderPainted(false);
		add(refreshButton);

		addButton = new JButton("추가");
		addButton.setBounds(560, 480, 110, 35);
		addButton.setBackground(new Color(34, 34, 34));
		addButton.setForeground(Color.WHITE);
		addButton.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		addButton.setFocusPainted(false);
		addButton.setOpaque(true);
		addButton.setBorderPainted(false);
		add(addButton);

		updateButton = new JButton("수정");
		updateButton.setBounds(680, 480, 110, 35);
		updateButton.setBackground(new Color(34, 34, 34));
		updateButton.setForeground(Color.WHITE);
		updateButton.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
		updateButton.setFocusPainted(false);
		updateButton.setOpaque(true);
		updateButton.setBorderPainted(false);
		updateButton.setEnabled(false);
		add(updateButton);

		totalRevenueLabel = new JLabel("", SwingConstants.RIGHT);
		totalRevenueLabel.setBounds(440, 525, 620, 30);
		totalRevenueLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
		totalRevenueLabel.setForeground(new Color(34, 34, 34));
		totalRevenueLabel.setVisible(false);
		add(totalRevenueLabel);
	}

	void clearMenuBindings() {
		for (ActionListener listener : addButton.getActionListeners()) {
			addButton.removeActionListener(listener);
		}
		for (ActionListener listener : refreshButton.getActionListeners()) {
			refreshButton.removeActionListener(listener);
		}
		for (ActionListener listener : deleteButton.getActionListeners()) {
			deleteButton.removeActionListener(listener);
		}
		for (ActionListener listener : updateButton.getActionListeners()) {
			updateButton.removeActionListener(listener);
		}
		for (ActionListener listener : searchButton.getActionListeners()) {
			searchButton.removeActionListener(listener);
		}
		for (ActionListener listener : searchField.getActionListeners()) {
			searchField.removeActionListener(listener);
		}
		for (MouseListener listener : dataTable.getMouseListeners()) {
			dataTable.removeMouseListener(listener);
		}

		searchField.setText("");
		addButton.setEnabled(true);
		deleteButton.setEnabled(false);
		updateButton.setEnabled(false);
		searchField.setVisible(true);
		searchButton.setVisible(true);
		totalRevenueLabel.setVisible(false);
	}

	void refreshTable() {
		dataTable.revalidate();
		dataTable.repaint();
	}

	JLabel menuTitleLabel() {
		return menuTitleLabel;
	}

	JTable dataTable() {
		return dataTable;
	}

	JButton addButton() {
		return addButton;
	}

	JButton refreshButton() {
		return refreshButton;
	}

	JButton deleteButton() {
		return deleteButton;
	}

	JButton updateButton() {
		return updateButton;
	}

	JTextField searchField() {
		return searchField;
	}

	JButton searchButton() {
		return searchButton;
	}

	JLabel totalRevenueLabel() {
		return totalRevenueLabel;
	}
}
