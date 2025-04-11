package ayaya;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

@SuppressWarnings("all")
public final class Log {

    private Log() {}
    private static final Logger LOGGER = Logger.getLogger("App Log");

    public static void severe(String message) {
        LOGGER.severe(message);
    }
    public static void warning(String message) {
        LOGGER.warning(message);
    }
    public static void info(String message) {
        LOGGER.info(message);
    }
    public static void config(String message) {
        LOGGER.config(message);
    }
    public static void fine(String message) {
        LOGGER.fine(message);
    }
    public static void finer(String message) {
        LOGGER.finer(message);
    }
    public static void finest(String message) {
        LOGGER.finest(message);
    }

    static {
        try {

            new File("logs").mkdir();

            FileHandler fileHandler = new FileHandler("logs/application.log", false);
            fileHandler.setFormatter(new SimpleFormatter());

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());

            LOGGER.addHandler(fileHandler);
            LOGGER.addHandler(consoleHandler);

            LOGGER.setLevel(Level.FINE);

        } catch (IOException e) { throw new RuntimeException(e); }
    }

}
