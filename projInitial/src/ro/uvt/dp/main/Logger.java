package ro.uvt.dp.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Logger {
    private static Logger instance;
    private PrintWriter writer;

    private Logger(String logFileName) {
        try {
            writer = new PrintWriter(new FileWriter(logFileName, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getInstance(String logFileName) {
        if (instance == null) {
            instance = new Logger(logFileName);
        }
        return instance;
    }

    public void log(String message) {
        String logMessage = new Date() + ": " + message;
        System.out.println(logMessage);
        writer.println(logMessage);
        writer.flush();
    }
}
