import infrastructure.AppLogger;
import presentation.swing.maindashboard.MainDashboard;

public class Main {

    public static void main(String[] args) {
        AppLogger.configure();
        MainDashboard.launch();
    }
}
