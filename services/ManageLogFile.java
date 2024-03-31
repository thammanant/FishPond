package services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.Objects;

public class ManageLogFile {
    private static final String logFile = "log.txt";

    public static String read_log() {
        try {
            FileReader fileReader = new FileReader(logFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Read the log file line by line
            String line;
            StringBuilder log = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line).append("\n");
            }

            bufferedReader.close();
            return log.toString(); // Successfully read the log file
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Failed to read the log file
        }
    }
    public static boolean write_to_log(String command, Integer fishID, Integer fishType, Integer genesisPondID, Integer pondID, Integer clock) {
        return write_to_log(command, fishID, fishType,genesisPondID ,pondID, clock, null); // Call the main function with default msg
    }

    public static boolean write_to_log(String command, Integer fishID, Integer fishType, Integer genesisPondID, Integer pondID, Integer clock, String msg) {
        try {
            FileWriter fileWriter = new FileWriter(logFile, true); // true for appending to the file
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Construct the log message
            String logMessage = String.format("%s, %d, %d, %d, %d, %d", command, fishID, fishType, genesisPondID, pondID, clock);

            bufferedWriter.write(logMessage);
            bufferedWriter.close();

            return true; // Successfully wrote to the log file
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to write to the log file
        }
    }

    public static void clear_log() {
        try {
            FileWriter fileWriter = new FileWriter(logFile);
            fileWriter.write(""); // Clear the log file
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void redo_task(Integer clock){
        // Read the log file
        String log = read_log();
        if (log == null) {
            return;
        }

        // Split the log file into individual log entries
        String[] logEntries = log.split("\n");

        // Use binary search to go through the log entries
        Integer left = 0;
        Integer right = logEntries.length - 1;
        Integer index = binary_search(left, right, clock);

        // redo the tasks from the index
        for (int i = index; i < logEntries.length; i++) {
            String[] logEntry = logEntries[i].split(", ");
            String command = logEntry[0];
            Integer fishID = Integer.parseInt(logEntry[1]);
            Integer fishType = Integer.parseInt(logEntry[2]);
            Integer genesisPondID = Integer.parseInt(logEntry[3]);
            Integer pondID = Integer.parseInt(logEntry[4]);
            Integer newClock = Integer.parseInt(logEntry[5]);
            String msg = logEntry.length == 7 ? logEntry[6] : null;

            if (command.equals("move")) {
                Communicate.move(fishID, fishType, genesisPondID, pondID, newClock);
            } else if (command.equals("ack")) {
                assert msg != null;
                if (msg.equals("acpt")) {
                    Database.add_fish_toDB(fishID, fishType, pondID);
                }
            }
        }
        ManageLogFile.clear_log();
    }


    private static Integer binary_search(Integer left, Integer right, Integer target) {
        if (left > right) {
            return -1;
        }

        int mid = left + (right - left) / 2;
        String[] logEntries = Objects.requireNonNull(read_log()).split("\n");
        String[] logEntry = logEntries[mid].split(", ");
        Integer clock = Integer.parseInt(logEntry[5]);

        if (clock.equals(target)) {
            return mid;
        } else if (clock < target) {
            return binary_search(mid + 1, right, target);
        } else {
            return binary_search(left, mid - 1, target);
        }
    }
}
