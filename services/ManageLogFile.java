package services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;

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

    public static void redo_task(){
        // TODO: Implement the redo task, use binary search to find the redo position
    }


    private static Integer binary_search(Integer left, Integer right, Integer target) {
        // TODO: Implement the binary search algorithm
        return null;
    }
}
