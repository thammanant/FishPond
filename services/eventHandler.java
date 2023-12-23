package services;

import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class eventHandler {
    public static boolean writeToLog(String command, Integer fishID, Integer pondID, Integer clock) {
        return writeToLog(command, fishID, pondID, clock, null); // Call the main function with default msg
    }

    public static boolean writeToLog(String command, Integer fishID, Integer pondID, Integer clock, String msg) {
        try {
            FileWriter fileWriter = new FileWriter("log.txt", true); // true for appending to the file
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Construct the log message
            String logMessage = String.format("%s, %d, %d, %d, %s\n", command, fishID, pondID, clock, msg);

            bufferedWriter.write(logMessage);
            bufferedWriter.close();

            return true; // Successfully wrote to the log file
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to write to the log file
        }
    }

    public static boolean redoList() {
        try {
            String filePath = "log.txt";
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Get the number of lines in the file
            long lineCount = bufferedReader.lines().count();

            int currentLine = 1;
            Integer redo = (int) lineCount;

            // Perform binary search on clock values
            redo = binarySearch(1, (int) lineCount - 1, redo);

            // Reset bufferedReader to the start of the file
            bufferedReader.close();
            fileReader = new FileReader(filePath);
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
                System.out.println("fishID: " + fishID);
                Integer pondID = Integer.parseInt(fields[2]);
                String msg = fields[4];
                System.out.println("command: " + line);

                if ("move".equals(command)) {
                    // Move the fish to the pond
                    System.out.println("move");
                    // Perform the logic for moving fish to the pond
                } else if ("ack".equals(command) && "acpt".equals(msg)) {
                    // Remove the fish from the pond
                    System.out.println("ack");
                    // Perform the logic for removing fish from the pond
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


    private static Integer binarySearch(Integer start, Integer end, Integer redo) throws IOException {
        while (start <= end) {
            Integer mid = start + (end - start) / 2;

            // Read the line at the mid point
            String line = readLineAt(mid);

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



    private static String readLineAt(long lineNumber) {
        String filePath = "log.txt";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
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
            return !isFishInPond(fishID);
        } else if ("ack".equals(command) && ("acpt".equals(msg))) {
            return isFishInPond(fishID);
        }
        return false;
    }

    private static boolean isFishInPond(int fishID) {
        // Your logic to check whether the fish is still in the pond
        JSONArray fishList = database.readFishFromDB();
        for (Object o : fishList) {
            JSONObject fish = (JSONObject) o;
            int storedFishID = Integer.parseInt((String) fish.get("fishid"));
            if (storedFishID == fishID) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Test the redoList function
        redoList();
    }
}


