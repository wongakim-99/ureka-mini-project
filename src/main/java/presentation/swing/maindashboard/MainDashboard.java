package presentation.swing.maindashboard;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import infrastructure.AppLogger;
import infrastructure.db.DBUtil;
import infrastructure.db.SchemaManager;
import infrastructure.kobis.KobisImporter;
import java.util.logging.Logger;

public class MainDashboard extends JFrame {

    private static final Logger log = AppLogger.get(MainDashboard.class);

    private JLabel bigCount;

    private JLabel liveClockLabel;

    private JDialog dialog;
    private JLabel dialogLabel;
    private JButton btnDialogClose;

    private DashboardControls controls;
    private DashboardMenuRouter menuRouter;

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

        controls = new DashboardControls(dialog, dialogLabel);
    }

    private void initUI() {
        DashboardWorkspace mainPanel = new DashboardWorkspace();
        DashboardStatusPanel statusPanel = new DashboardStatusPanel(this::syncMovies);
        liveClockLabel = statusPanel.getClockLabel();
        bigCount = statusPanel.getMovieCountLabel();
        menuRouter = new DashboardMenuRouter(controls, mainPanel, bigCount);
        DashboardSidebar sideNav = new DashboardSidebar(menuRouter::load);

        mainPanel.add(sideNav);
        mainPanel.add(statusPanel);
        add(mainPanel);
        menuRouter.load("영화 관리");
    }

    private void syncMovies(ActionEvent ev) {
        JButton actionBtn = (JButton) ev.getSource();
        new Thread(() -> {
            SwingUtilities.invokeLater(() -> actionBtn.setEnabled(false));
            try {
                new KobisImporter().importMovies();
                SwingUtilities.invokeLater(() -> {
                    dialogLabel.setText("영화 데이터 동기화 완료");
                    dialog.setVisible(true);
                    actionBtn.setEnabled(true);
                    if (menuRouter.isMovieMenu()) {
                        controls.movieController.load();
                        bigCount.setText(String.valueOf(controls.movieController.getMovieCount()));
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    dialogLabel.setText("API 연동 실패: " + ex.getMessage());
                    dialog.setVisible(true);
                    actionBtn.setEnabled(true);
                });
            }
        }).start();
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

    private void seedFromKobisIfEmpty() {
        try {
            boolean isEmpty;
            try (var stmt = DBUtil.getConnection().createStatement();
                 var rs = stmt.executeQuery("SELECT COUNT(*) FROM movie")) {
                rs.next();
                isEmpty = rs.getInt(1) == 0;
            }
            if (isEmpty) {
                log.info("영화 데이터가 없어 박스오피스 데이터를 가져옵니다...");
                new KobisImporter().importMovies();
                log.info("영화 데이터 초기 로드 완료");
                SwingUtilities.invokeLater(() -> {
                    controls.movieController.load();
                    bigCount.setText(String.valueOf(controls.movieController.getMovieCount()));
                });
            }
        } catch (Exception e) {
            log.warning("영화 초기 데이터 로드 실패: " + e.getMessage());
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
