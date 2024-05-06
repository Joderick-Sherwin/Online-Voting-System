package com.onlinevotingsystem.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_DIRECTORY = "C:/Online_Voting_System"; // Custom log directory
    private static final String LOG_FILE = LOG_DIRECTORY + "/application.log"; // Custom log filename

    static {
        // Create the log directory if it doesn't exist
        File logDir = new File(LOG_DIRECTORY);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }

    public static void log(String message) {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            LocalDateTime timestamp = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTimestamp = timestamp.format(formatter);

            printWriter.println("[" + formattedTimestamp + "] " + message);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle logging exceptions
        }
    }
}
