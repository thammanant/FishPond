package services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class eventHandler {
    public static boolean writeToLog(String command, Integer fishID, Integer pondID, Integer clock) {
        return writeToLog(command, fishID, pondID, clock, ""); // Call the main function with default msg
    }

    public static boolean writeToLog(String command, Integer fishID, Integer pondID, Integer clock, String msg) {
        try {
            FileWriter fileWriter = new FileWriter("log.txt", true); // true for appending to the file
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Construct the log message
            String logMessage = String.format("%s, %d, %d, %s, %d\n", command, fishID, pondID, msg, clock);

            bufferedWriter.write(logMessage);
            bufferedWriter.close();

            return true; // Successfully wrote to the log file
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to write to the log file
        }
    }
}
