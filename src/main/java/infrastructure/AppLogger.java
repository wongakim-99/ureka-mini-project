package infrastructure;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

public class AppLogger {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static void configure() {
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) root.removeHandler(h);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord r) {
                String time  = LocalTime.now().format(FMT);
                String name  = r.getLoggerName();
                int dot = name.lastIndexOf('.');
                if (dot >= 0) name = name.substring(dot + 1);
                return String.format("[%s] %-30s %s%n", time, name, r.getMessage());
            }
        });

        root.addHandler(handler);
        root.setLevel(Level.FINE);
    }

    public static Logger get(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}
