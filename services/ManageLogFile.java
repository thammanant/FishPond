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

    public static boolean redo_task() {
        try {
            FileReader fileReader = new FileReader(logFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Get the number of lines in the file
            long lineCount = bufferedReader.lines().count();

            int currentLine = 1;
            Integer redo = (int) lineCount;

            // Perform binary search on clock values
            redo = binary_search(1, (int) lineCount - 1, redo);

            // Reset bufferedReader to the start of the file
            bufferedReader.close();
            fileReader = new FileReader(logFile);
            bufferedReader = new BufferedReader(fileReader);

            // Skip lines till the redo starting point
            for (int i = 0; i < redo; i++) {
                bufferedReader.readLine();
            }

            // Redo tasks from startLine to the end of the file
            String line;
            while ((line = bufferedReader.readLine()) != null) {

                String[] fields = line.split(", ");

                String command = fields[0];
                Integer fishID = Integer.parseInt(fields[1]);
                Integer pondID = Integer.parseInt(fields[2]);
                String msg = fields[4];

                Integer fishType = Communicate.get_fish_info(fishID)[0];
                Integer genesisPondID = Communicate.get_fish_info(fishID)[1];


                if ("move".equals(command)) {
                    Communicate.move(fishID, fishType, genesisPondID, pondID, 12345);
                } else if ("ack".equals(command) && "acpt".equals(msg)) {
                    Communicate.ack_fish(fishID, fishType, genesisPondID, pondID, 12345, "acpt");
                }

                currentLine++;
            }

            bufferedReader.close();
            return true; // Successfully processed the log file
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to read the log file
        }
    }


    private static Integer binary_search(Integer start, Integer end, Integer redo) throws IOException {
        while (start <= end) {
            Integer mid = start + (end - start) / 2;

            // Read the line at the mid point
            String line = read_line_at(mid);

            // Process the log entry
            if (check(line)) {
                // Update the redo position
                redo = mid;

                // Search in the right half for the least line number not done
                end = mid - 1;
            } else {
                // If the current entry is done, search in the left half
                start = mid + 1;
            }
        }
        return redo;
    }


    private static String read_line_at(long lineNumber) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile))) {
            String line;
            long lineCount = 0;

            while ((line = bufferedReader.readLine()) != null) {
                lineCount++;
                if (lineCount == lineNumber) {
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean check(String line) {
        if (line == null) {
            return false; // Exit or handle end of file
        }

        String[] fields = line.split(", ");

        String command = fields[0];
        Integer fishID = Integer.parseInt(fields[1]);
        String msg = fields[3];
        if ("move".equals(command)) {
            return !is_fish_in_pond(fishID);
        } else if ("ack".equals(command) && ("acpt".equals(msg))) {
            return is_fish_in_pond(fishID);
        }
        return false;
    }

    private static boolean is_fish_in_pond(int fishID) {
        // Your logic to check whether the fish is still in the pond
        JSONArray fishList = Database.read_fish_fromDB();
        for (Object o : fishList) {
            JSONObject fish = (JSONObject) o;
            int storedFishID = Integer.parseInt((String) fish.get("fishid"));
            if (storedFishID == fishID) {
                return true;
            }
        }
        return false;
    }
}
