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
            return null; // Failed to read the log file
        }
    }
    public static boolean write_to_log(String command, Integer fishID, Integer clock){
        return write_to_log(command, fishID, null, null, null, clock, null);
    }

    public static boolean write_to_log(String command, Integer fishID, Integer fishType, Integer genesisPondID, Integer clock){
        return write_to_log(command, fishID, fishType, genesisPondID, null, clock, null);
    }

    public static boolean write_to_log(String command, Integer fishID, Integer fishType, Integer genesisPondID, Integer pondID, Integer clock) {
        return write_to_log(command, fishID, fishType, genesisPondID ,pondID, clock, null); // Call the main function with default msg
    }

    public static boolean write_to_log(String command, Integer fishID, Integer fishType, Integer genesisPondID, Integer pondID, Integer clock, String msg) {
        try {
            FileWriter fileWriter = new FileWriter(logFile, true); // true for appending to the file
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Construct the log message
            String logMessage = String.format("%s, %d, %d, %d, %d, %d, %s \n", command, fishID, fishType, genesisPondID, pondID, clock, msg);

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
        if(log == null || log.equals("")) {
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
            int fishID = Integer.parseInt(logEntry[1]);
            int fishType = logEntry[2].equals("null") ? -1 : Integer.parseInt(logEntry[2]);
            int pondID = logEntry[3].equals("null") ? -1 : Integer.parseInt(logEntry[3]);
            String msg = logEntry.length == 7 ? logEntry[6] : null;

            switch (command) {
                case "move" ->
//                Communicate.move(fishID, fishType, genesisPondID, pondID, newClock);
                        System.out.println("Fish moved");
                case "ack" -> {
                    assert msg != null;
                    if (msg.equals("acpt")) {
                        Database.add_fish_toDB(fishID, fishType, pondID);
//                    System.out.println("Fish added to the database");
                    }
                }
                case "add" -> Database.add_fish_toDB(fishID, fishType, pondID);
                case "remove" -> Database.remove_fish_fromDB(fishID);
            }
        }
        ManageLogFile.clear_log();
        System.out.println("Redo tasks completed");
    }


    private static Integer binary_search(Integer left, Integer right, Integer target) {
        if (right >= left) {
            int mid = left + (right - left) / 2;
            String[] logEntry = Objects.requireNonNull(read_log()).split("\n")[mid].split(", ");
            int clock = Integer.parseInt(logEntry[5]);

            if (clock == target) {
                return mid;
            }

            if (clock > target) {
                return binary_search(left, mid - 1, target);
            }

            return binary_search(mid + 1, right, target);
        }

        return 0;
    }
}
