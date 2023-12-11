package ro.uvt.dp.test.JUnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.uvt.dp.main.Logger;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerTest {
    private Logger logger;
    private File logFile;

    @BeforeEach
    void setUp() {
        // creating a new logger
        logger = Logger.getInstance("test.log");
        logFile = new File("test.log");
    }

    @Test
    void logMessage() {
        // logging a message
        logger.log("Test message");

        // checking if the log file exists and is not empty
        assertTrue(logFile.exists());
        assertTrue(logFile.length() > 0);
    }
}