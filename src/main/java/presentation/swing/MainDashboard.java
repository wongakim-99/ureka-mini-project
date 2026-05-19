package presentation.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import application.AppFactory;
import presentation.swing.movie.*;
import presentation.swing.reservation.*;
import presentation.swing.screening.*;
import presentation.swing.customer.*;
import presentation.swing.theater.*;
import presentation.swing.revenue.RevenueController;
import infrastructure.db.DBUtil;
import infrastructure.db.SchemaManager;
import infrastructure.kobis.KobisImporter;

public class MainDashboard extends JFrame {

    private JLabel menuTitleLabel;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnRefresh, btnDelete, btnUpdate;
    private JTextField searchField;
    private JButton btnSearch;
    private JLabel bigCount;

    private JLabel liveClockLabel;

    private JDialog dialog;
    private JLabel dialogLabel;
    private JButton btnDialogClose;

    private MovieController movieController;
    private MovieCreateFrame movieCreateFrame;
    private MovieUpdateFrame movieUpdateFrame;
    private ReservationController reservationController;
    private ReservationCreateFrame reservationCreateFrame;
    private ReservationUpdateFrame reservationUpdateFrame;
    private ScreeningController screeningController;
    private ScreeningCreateFrame screeningCreateFrame;
    private ScreeningUpdateFrame screeningUpdateFrame;
    private CustomerController customerController;
    private CustomerCreateFrame customerCreateFrame;
    private CustomerUpdateFrame customerUpdateFrame;
    private RevenueController revenueController;
    private JLabel totalRevenueLabel;

    public MainDashboard() {
        setTitle("CGV Seolleung Management Tool");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initControls();
        initUI();
        startClock();

        setVisible(true);
    }

    private void initControls() {
        dialog = new JDialog(this, "알림", true);
        dialog.setSize(300, 150);
        dialog.setLayout(null);
        dialogLabel = new JLabel("", SwingConstants.CENTER);
        dialogLabel.setBounds(10, 20, 260, 30);
        btnDialogClose = new JButton("확인");
        btnDialogClose.setBounds(100, 70, 80, 30);
        dialog.add(dialogLabel);
        dialog.add(btnDialogClose);
        btnDialogClose.addActionListener(e -> dialog.setVisible(false));

        movieCreateFrame = new MovieCreateFrame(); movieUpdateFrame = new MovieUpdateFrame();
        movieController = AppFactory.createMovieController(dialog, dialogLabel);
        movieCreateFrame.addEvent(movieController); movieUpdateFrame.addEvent(movieController);

        reservationCreateFrame = new ReservationCreateFrame(); reservationUpdateFrame = new ReservationUpdateFrame();
        reservationController = AppFactory.createReservationController(dialog, dialogLabel);
        reservationCreateFrame.addEvent(reservationController); reservationUpdateFrame.addEvent(reservationController);

        screeningCreateFrame = new ScreeningCreateFrame(); screeningUpdateFrame = new ScreeningUpdateFrame();
        screeningController = AppFactory.createScreeningController(dialog, dialogLabel);
        screeningCreateFrame.addEvent(screeningController); screeningUpdateFrame.addEvent(screeningController);

        customerCreateFrame = new CustomerCreateFrame(); customerUpdateFrame = new CustomerUpdateFrame();
        customerController = AppFactory.createCustomerController(dialog, dialogLabel);
        customerCreateFrame.addEvent(customerController); customerUpdateFrame.addEvent(customerController);

        revenueController = AppFactory.createRevenueController(dialog, dialogLabel);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 247, 251));

        // 좌측 네비게이션 바
        JPanel sideNav = new JPanel();
        sideNav.setLayout(null);
        sideNav.setBackground(new Color(34, 34, 34));
        sideNav.setBounds(0, 0, 150, 600);

        JLabel logoText = new JLabel("CGV 선릉점", SwingConstants.CENTER);
        logoText.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        logoText.setForeground(new Color(229, 9, 20));
        logoText.setBounds(0, 20, 150, 30);
        sideNav.add(logoText);

        String[] menus = {"영화 관리", "예약 관리", "상영 일정 관리", "고객 관리", "수입 관리"};
        int yOffset = 100;
        for (String menu : menus) {
            JButton menuBtn = new JButton(menu);
            menuBtn.setBounds(0, yOffset, 150, 45);
            menuBtn.setForeground(Color.WHITE);
            menuBtn.setBackground(new Color(34, 34, 34));
            menuBtn.setBorderPainted(false);
            menuBtn.setFocusPainted(false);
            menuBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
            menuBtn.addActionListener(new MenuActionListener(menu));
            sideNav.add(menuBtn);
            yOffset += 50;
        }

        // 중간 현황판 영역
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(null);
        middlePanel.setBackground(new Color(229, 9, 20));
        middlePanel.setBounds(150, 0, 260, 600);

        JLabel branchLabel = new JLabel("SEOLLEUNG");
        branchLabel.setBounds(20, 30, 200, 20);
        branchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        branchLabel.setForeground(new Color(255, 192, 192));
        middlePanel.add(branchLabel);

        JLabel managerLabel = new JLabel("SYSTEM MANAGER", SwingConstants.RIGHT);
        managerLabel.setBounds(100, 30, 140, 20);
        managerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        managerLabel.setForeground(Color.WHITE);
        middlePanel.add(managerLabel);

        liveClockLabel = new JLabel("", SwingConstants.CENTER);
        liveClockLabel.setForeground(Color.WHITE);
        liveClockLabel.setBounds(0, 55, 260, 130);
        middlePanel.add(liveClockLabel);

        JSeparator separator = new JSeparator();
        separator.setBounds(30, 195, 200, 5);
        middlePanel.add(separator);

        JLabel infoTitle = new JLabel("TOTAL MOVIES");
        infoTitle.setBounds(20, 250, 220, 20);
        infoTitle.setFont(new Font("Arial", Font.BOLD, 16));
        infoTitle.setForeground(Color.WHITE);
        infoTitle.setHorizontalAlignment(SwingConstants.CENTER);
        middlePanel.add(infoTitle);

        bigCount = new JLabel("0", SwingConstants.CENTER);
        bigCount.setBounds(0, 290, 260, 90);
        bigCount.setFont(new Font("Arial", Font.BOLD, 74));
        bigCount.setForeground(Color.WHITE);
        middlePanel.add(bigCount);

        JButton actionBtn = new JButton("데이터 동기화");
        actionBtn.setBounds(50, 450, 160, 40);
        actionBtn.setBackground(new Color(229, 9, 20));
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
        actionBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        actionBtn.setFocusPainted(false);
        actionBtn.addActionListener(ev -> new Thread(() -> {
            SwingUtilities.invokeLater(() -> actionBtn.setEnabled(false));
            try {
                new KobisImporter().importMovies();
                SwingUtilities.invokeLater(() -> {
                    dialogLabel.setText("영화 데이터 동기화 완료");
                    dialog.setVisible(true);
                    actionBtn.setEnabled(true);
                    if (menuTitleLabel.getText().contains("영화")) {
                        movieController.load();
                        bigCount.setText(String.valueOf(movieController.getMovieCount()));
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    dialogLabel.setText("API 연동 실패: " + ex.getMessage());
                    dialog.setVisible(true);
                    actionBtn.setEnabled(true);
                });
            }
        }).start());
        middlePanel.add(actionBtn);

        // 우측 메인 데이터 테이블 영역
        menuTitleLabel = new JLabel("영화 관리 시스템");
        menuTitleLabel.setBounds(440, 30, 300, 30);
        menuTitleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        menuTitleLabel.setForeground(new Color(34, 34, 34));
        mainPanel.add(menuTitleLabel);

        JLabel subTitle = new JLabel("선릉점 데이터베이스의 ERD 기반 스키마 결과 조회입니다.");
        subTitle.setBounds(440, 65, 500, 20);
        subTitle.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        subTitle.setForeground(Color.GRAY);
        mainPanel.add(subTitle);

        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        dataTable.setRowHeight(30);
        dataTable.setGridColor(new Color(230, 235, 245));
        dataTable.getTableHeader().setBackground(new Color(240, 242, 245));
        dataTable.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 12));
        dataTable.setSelectionBackground(new Color(173, 214, 255));
        dataTable.setSelectionForeground(Color.BLACK);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.setRowSelectionAllowed(true);

        btnDelete = new JButton("삭제");
        btnDelete.setBounds(960, 30, 100, 35);
        btnDelete.setBackground(new Color(229, 9, 20));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        btnDelete.setFocusPainted(false);
        btnDelete.setOpaque(true);
        btnDelete.setBorderPainted(false);
        btnDelete.setEnabled(false);
        mainPanel.add(btnDelete);

        searchField = new JTextField();
        searchField.setBounds(440, 92, 390, 28);
        searchField.setFont(new Font("Malgun Gothic", Font.PLAIN, 13));
        mainPanel.add(searchField);

        btnSearch = new JButton("검색");
        btnSearch.setBounds(837, 92, 80, 28);
        btnSearch.setBackground(new Color(70, 70, 70));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
        btnSearch.setFocusPainted(false);
        btnSearch.setOpaque(true);
        btnSearch.setBorderPainted(false);
        mainPanel.add(btnSearch);

        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBounds(440, 130, 620, 330);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 225)));
        mainPanel.add(scrollPane);

        btnRefresh = new JButton("목록 조회");
        btnRefresh.setBounds(440, 480, 110, 35);
        btnRefresh.setBackground(new Color(70, 70, 70));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setOpaque(true);
        btnRefresh.setBorderPainted(false);
        mainPanel.add(btnRefresh);

        btnAdd = new JButton("추가");
        btnAdd.setBounds(560, 480, 110, 35);
        btnAdd.setBackground(new Color(34, 34, 34));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        btnAdd.setFocusPainted(false);
        btnAdd.setOpaque(true);
        btnAdd.setBorderPainted(false);
        mainPanel.add(btnAdd);

        btnUpdate = new JButton("수정");
        btnUpdate.setBounds(680, 480, 110, 35);
        btnUpdate.setBackground(new Color(34, 34, 34));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        btnUpdate.setFocusPainted(false);
        btnUpdate.setOpaque(true);
        btnUpdate.setBorderPainted(false);
        btnUpdate.setEnabled(false);
        mainPanel.add(btnUpdate);

        totalRevenueLabel = new JLabel("", SwingConstants.RIGHT);
        totalRevenueLabel.setBounds(440, 525, 620, 30);
        totalRevenueLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        totalRevenueLabel.setForeground(new Color(34, 34, 34));
        totalRevenueLabel.setVisible(false);
        mainPanel.add(totalRevenueLabel);

        loadMenuData("영화 관리");

        mainPanel.add(sideNav);
        mainPanel.add(middlePanel);
        add(mainPanel);
    }

    private void startClock() {
        SimpleDateFormat dateSdf    = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat dayTimeSdf = new SimpleDateFormat("EEE  HH:mm:ss", Locale.US);
        Timer timer = new Timer(1000, e -> {
            String date    = dateSdf.format(new Date());
            String dayTime = dayTimeSdf.format(new Date());
            liveClockLabel.setText(
                "<html><center>" +
                "<div style='font-size:28pt; font-family:Malgun Gothic;'>" + date + "</div>" +
                "<div style='font-size:20pt; font-family:Malgun Gothic;'>" + dayTime + "</div>" +
                "</center></html>"
            );
        });
        timer.start();
    }

    private void clearListeners() {
        for (ActionListener al : btnAdd.getActionListeners()) btnAdd.removeActionListener(al);
        for (ActionListener al : btnRefresh.getActionListeners()) btnRefresh.removeActionListener(al);
        for (ActionListener al : btnDelete.getActionListeners()) btnDelete.removeActionListener(al);
        for (ActionListener al : btnUpdate.getActionListeners()) btnUpdate.removeActionListener(al);
        for (ActionListener al : btnSearch.getActionListeners()) btnSearch.removeActionListener(al);
        for (ActionListener al : searchField.getActionListeners()) searchField.removeActionListener(al);
        for (MouseListener ml : dataTable.getMouseListeners()) dataTable.removeMouseListener(ml);
        searchField.setText("");
        btnAdd.setEnabled(true);
        searchField.setVisible(true);
        btnSearch.setVisible(true);
    }

    private void loadMenuData(String menuName) {
        clearListeners();

        if (menuName.equals("영화 관리")) {
            menuTitleLabel.setText("영화 관리 (Movie Master)");
            btnRefresh.setText("전체 조회");
            btnAdd.setText("영화 추가");
            btnRefresh.addActionListener(e -> { searchField.setText(""); movieController.load(); bigCount.setText(String.valueOf(movieController.getMovieCount())); });
            btnSearch.addActionListener(e -> movieController.search(searchField.getText()));
            searchField.addActionListener(e -> movieController.search(searchField.getText()));
            btnAdd.addActionListener(movieController);
            btnDelete.addActionListener(movieController);
            btnUpdate.addActionListener(movieController);
            movieController.setTable(dataTable);
            movieController.setDeleteBtn(btnDelete);
            movieController.setUpdateBtn(btnUpdate);
            dataTable.addMouseListener(movieController);
            movieController.load();
            bigCount.setText(String.valueOf(movieController.getMovieCount()));

        } else if (menuName.equals("예약 관리")) {
            menuTitleLabel.setText("예약 관리 (Reservation Info)");
            btnRefresh.setText("전체 조회");
            btnAdd.setText("예약 추가");
            btnRefresh.addActionListener(e -> { searchField.setText(""); reservationController.load(); });
            btnSearch.addActionListener(e -> reservationController.search(searchField.getText()));
            searchField.addActionListener(e -> reservationController.search(searchField.getText()));
            btnAdd.addActionListener(reservationController);
            btnDelete.addActionListener(reservationController);
            btnUpdate.addActionListener(reservationController);
            reservationController.setTable(dataTable);
            reservationController.setDeleteBtn(btnDelete);
            reservationController.setUpdateBtn(btnUpdate);
            dataTable.addMouseListener(reservationController);
            reservationController.load();

        } else if (menuName.equals("상영 일정 관리")) {
            menuTitleLabel.setText("상영 일정 관리 (Schedules)");
            btnRefresh.setText("전체 조회");
            btnAdd.setText("일정 추가");
            btnRefresh.addActionListener(e -> { searchField.setText(""); screeningController.load(); });
            btnSearch.addActionListener(e -> screeningController.search(searchField.getText()));
            searchField.addActionListener(e -> screeningController.search(searchField.getText()));
            btnAdd.addActionListener(screeningController);
            btnDelete.addActionListener(screeningController);
            btnUpdate.addActionListener(screeningController);
            screeningController.setTable(dataTable);
            screeningController.setDeleteBtn(btnDelete);
            screeningController.setUpdateBtn(btnUpdate);
            dataTable.addMouseListener(screeningController);
            screeningController.load();

        } else if (menuName.equals("고객 관리")) {
            menuTitleLabel.setText("고객 관리 (Customer Base)");
            btnRefresh.setText("전체 조회");
            btnAdd.setText("고객 추가");
            btnRefresh.addActionListener(e -> { searchField.setText(""); customerController.load(); });
            btnSearch.addActionListener(e -> customerController.search(searchField.getText()));
            searchField.addActionListener(e -> customerController.search(searchField.getText()));
            btnDelete.addActionListener(customerController);
            btnUpdate.addActionListener(customerController);
            customerController.setTable(dataTable);
            customerController.setDeleteBtn(btnDelete);
            customerController.setUpdateBtn(btnUpdate);
            btnAdd.addActionListener(customerController);
            dataTable.addMouseListener(customerController);
            customerController.load();

        } else if (menuName.equals("수입 관리")) {
            menuTitleLabel.setText("수입 관리 (Revenue)");
            btnRefresh.setText("전체 조회");
            btnAdd.setText("추가");
            btnAdd.setEnabled(false);
            btnDelete.setEnabled(false);
            btnUpdate.setEnabled(false);
            searchField.setVisible(false);
            btnSearch.setVisible(false);
            btnRefresh.addActionListener(e -> revenueController.load());
            revenueController.setTable(dataTable);
            revenueController.setTotalLabel(totalRevenueLabel);
            revenueController.load();
            totalRevenueLabel.setVisible(true);
            return;
        }

        totalRevenueLabel.setVisible(false);
        dataTable.revalidate();
        dataTable.repaint();
    }

    private class MenuActionListener implements ActionListener {
        private String menuName;
        public MenuActionListener(String menuName) { this.menuName = menuName; }
        @Override
        public void actionPerformed(ActionEvent e) { loadMenuData(menuName); }
    }

    private void seedFromKobisIfEmpty() {
        try {
            var rs = DBUtil.getConnection().createStatement()
                .executeQuery("SELECT COUNT(*) FROM movie");
            rs.next();
            if (rs.getInt(1) == 0) {
                System.out.println("[KOBIS] movie 테이블이 비어있어 박스오피스 데이터를 가져옵니다...");
                new KobisImporter().importMovies();
                System.out.println("[KOBIS] 데이터 삽입 완료");
                SwingUtilities.invokeLater(() -> {
                    movieController.load();
                    bigCount.setText(String.valueOf(movieController.getMovieCount()));
                });
            }
        } catch (Exception e) {
            System.err.println("[KOBIS] 초기 데이터 로드 실패: " + e.getMessage());
        }
    }

    public static void launch() {
        System.setProperty("java.awt.im.style", "below-the-spot");
        SchemaManager.run();
        SwingUtilities.invokeLater(() -> {
            MainDashboard app = new MainDashboard();
            new Thread(app::seedFromKobisIfEmpty).start();
        });
    }
}
