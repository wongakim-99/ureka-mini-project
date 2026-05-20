package presentation.swing.maindashboard;

import javax.swing.JLabel;

public final class DashboardMenuRouter {

	private static final String MOVIE_MENU = "영화 관리";

	private final DashboardControls controls;
	private final DashboardWorkspace workspace;
	private final JLabel movieCountLabel;
	private String currentMenu = "";

	DashboardMenuRouter(DashboardControls controls, DashboardWorkspace workspace, JLabel movieCountLabel) {
		this.controls = controls;
		this.workspace = workspace;
		this.movieCountLabel = movieCountLabel;
	}

	void load(String menuName) {
		currentMenu = menuName;
		workspace.clearMenuBindings();

		if (MOVIE_MENU.equals(menuName)) {
			loadMovies();
		} else if ("예약 관리".equals(menuName)) {
			loadReservations();
		} else if ("상영 일정 관리".equals(menuName)) {
			loadScreenings();
		} else if ("고객 관리".equals(menuName)) {
			loadCustomers();
		} else if ("상영관 관리".equals(menuName)) {
			loadTheaters();
		} else if ("수입 관리".equals(menuName)) {
			loadRevenue();
			return;
		}

		workspace.refreshTable();
	}

	boolean isMovieMenu() {
		return MOVIE_MENU.equals(currentMenu);
	}

	private void loadMovies() {
		workspace.menuTitleLabel().setText("영화 관리 (Movie Master)");
		workspace.refreshButton().setVisible(false);
		workspace.searchResetButton().setVisible(true);
		workspace.addButton().setText("영화 추가");
		workspace.searchResetButton().addActionListener(e -> {
			workspace.searchField().setText("");
			controls.movieController.load();
			updateMovieCount();
		});
		workspace.searchButton().addActionListener(e -> controls.movieController.search(workspace.searchField().getText()));
		workspace.searchField().addActionListener(e -> controls.movieController.search(workspace.searchField().getText()));
		workspace.addButton().addActionListener(controls.movieController);
		workspace.deleteButton().addActionListener(controls.movieController);
		workspace.updateButton().addActionListener(controls.movieController);
		controls.movieController.setTable(workspace.dataTable());
		controls.movieController.setDeleteBtn(workspace.deleteButton());
		controls.movieController.setUpdateBtn(workspace.updateButton());
		workspace.dataTable().addMouseListener(controls.movieController);
		controls.movieController.load();
		updateMovieCount();
	}

	public void updateMovieCount() {
		movieCountLabel.setText(String.valueOf(controls.movieController.getMovieCount()));
	}

	private void loadReservations() {
		workspace.menuTitleLabel().setText("예약 관리 (Reservation Info)");
		workspace.refreshButton().setVisible(false);
		workspace.searchResetButton().setVisible(true);
		workspace.addButton().setText("예약 추가");
		workspace.searchResetButton().addActionListener(e -> {
			workspace.searchField().setText("");
			controls.reservationController.load();
		});
		workspace.searchButton().addActionListener(e -> controls.reservationController.search(workspace.searchField().getText()));
		workspace.searchField().addActionListener(e -> controls.reservationController.search(workspace.searchField().getText()));
		workspace.addButton().addActionListener(controls.reservationController);
		workspace.deleteButton().addActionListener(controls.reservationController);
		workspace.updateButton().addActionListener(controls.reservationController);
		controls.reservationController.setTable(workspace.dataTable());
		controls.reservationController.setDeleteBtn(workspace.deleteButton());
		controls.reservationController.setUpdateBtn(workspace.updateButton());
		workspace.dataTable().addMouseListener(controls.reservationController);
		controls.reservationController.load();
	}

	private void loadScreenings() {
		workspace.menuTitleLabel().setText("상영 일정 관리 (Schedules)");
		workspace.refreshButton().setVisible(false);
		workspace.searchResetButton().setVisible(true);
		workspace.addButton().setText("일정 추가");
		workspace.searchResetButton().addActionListener(e -> {
			workspace.searchField().setText("");
			controls.screeningController.load();
		});
		workspace.searchButton().addActionListener(e -> controls.screeningController.search(workspace.searchField().getText()));
		workspace.searchField().addActionListener(e -> controls.screeningController.search(workspace.searchField().getText()));
		workspace.addButton().addActionListener(controls.screeningController);
		workspace.deleteButton().addActionListener(controls.screeningController);
		workspace.updateButton().addActionListener(controls.screeningController);
		controls.screeningController.setTable(workspace.dataTable());
		controls.screeningController.setDeleteBtn(workspace.deleteButton());
		controls.screeningController.setUpdateBtn(workspace.updateButton());
		workspace.dataTable().addMouseListener(controls.screeningController);
		controls.screeningController.load();
	}

	private void loadCustomers() {
		workspace.menuTitleLabel().setText("고객 관리 (Customer Base)");
		workspace.refreshButton().setVisible(false);
		workspace.searchResetButton().setVisible(true);
		workspace.addButton().setText("고객 추가");
		workspace.searchResetButton().addActionListener(e -> {
			workspace.searchField().setText("");
			controls.customerController.load();
		});
		workspace.searchButton().addActionListener(e -> controls.customerController.search(workspace.searchField().getText()));
		workspace.searchField().addActionListener(e -> controls.customerController.search(workspace.searchField().getText()));
		workspace.deleteButton().addActionListener(controls.customerController);
		workspace.updateButton().addActionListener(controls.customerController);
		controls.customerController.setTable(workspace.dataTable());
		controls.customerController.setDeleteBtn(workspace.deleteButton());
		controls.customerController.setUpdateBtn(workspace.updateButton());
		workspace.addButton().addActionListener(controls.customerController);
		workspace.dataTable().addMouseListener(controls.customerController);
		controls.customerController.load();
	}

	private void loadTheaters() {
		workspace.menuTitleLabel().setText("상영관 관리 (Theater Master)");
		workspace.refreshButton().setVisible(false);
		workspace.searchResetButton().setVisible(false);
		workspace.addButton().setText("상영관 추가");
		workspace.searchField().setVisible(false);
		workspace.searchButton().setVisible(false);
		workspace.addButton().addActionListener(controls.theaterController);
		workspace.deleteButton().addActionListener(controls.theaterController);
		workspace.updateButton().addActionListener(controls.theaterController);
		controls.theaterController.setTable(workspace.dataTable());
		controls.theaterController.setDeleteBtn(workspace.deleteButton());
		controls.theaterController.setUpdateBtn(workspace.updateButton());
		workspace.dataTable().addMouseListener(controls.theaterController);
		controls.theaterController.load();
	}

	private void loadRevenue() {
		workspace.menuTitleLabel().setText("수입 관리 (Revenue)");
		workspace.refreshButton().setVisible(false);
		workspace.searchResetButton().setVisible(false);
		workspace.addButton().setText("추가");
		workspace.addButton().setEnabled(false);
		workspace.deleteButton().setEnabled(false);
		workspace.updateButton().setEnabled(false);
		workspace.searchField().setVisible(false);
		workspace.searchButton().setVisible(false);
		controls.revenueController.setTable(workspace.dataTable());
		controls.revenueController.setTotalLabel(workspace.totalRevenueLabel());
		controls.revenueController.load();
		workspace.totalRevenueLabel().setVisible(true);
	}
}
