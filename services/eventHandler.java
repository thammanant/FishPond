package services;

import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class eventHandler {
    public static boolean writeToLog(String command, Integer fishID, Integer pondID, Integer clock) {
        return writeToLog(command, fishID, pondID, clock, ""); // Call the main function with default msg
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
            FileReader fileReader = new FileReader("log.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Count the number of lines in the file
            String line;
            long lineCount = bufferedReader.lines().count();
            int currentLine = 1;
            Integer redo = (int) lineCount - 1;

            // Perform binary search on clock values
            redo = binarySearch(bufferedReader, 0, (int) lineCount - 1, redo);
            while ((line = bufferedReader.readLine()) != null && currentLine < redo) {
                currentLine++;
            }

            // Redo tasks from startLine to the end of the file
            while (line != null) {
                // Call functions or process tasks here (replace this with your logic)
                String[] fields = line.split(", ");

                String command = fields[0];
                Integer fishID = Integer.parseInt(fields[1]);
                Integer pondID = Integer.parseInt(fields[2]);
                String msg = fields[3];

                if ("move".equals(command)) {
                    // Move the fish to the pond
                    System.out.println("move");
//                    database.addFishToDB(fishID, pondID);
                } else if ("ack".equals(command) && ("acpt".equals(msg))) {
                    // Remove the fish from the pond
                    System.out.println("ack");
//                    database.removeFishFromDB(fishID);
                }

                // Read the next line
                line = bufferedReader.readLine();
                currentLine++;
            }
            bufferedReader.close();
            return true; // Successfully processed the log file
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to read the log file
        }
    }

    private static Integer binarySearch(BufferedReader bufferedReader, Integer start, Integer end, Integer redo) throws IOException {
        if (start < end) {
            Integer mid = start + (end - start) / 2;

            // Read the line at the mid point
            String line = readLineAt(bufferedReader, mid);

            // Process the log entry
            if (!check(line)) {

                // Update the redo position
                redo = mid;

                // Search in the left half
                return binarySearch(bufferedReader, start, mid - 1, redo);
            } else {
                // If the current entry is done, search in the right half
                return binarySearch(bufferedReader, mid + 1, end, redo);
            }
        }
        return redo;
    }


    private static String readLineAt(BufferedReader bufferedReader, long lineNumber) throws IOException {
        // Reset the reader to the beginning
        bufferedReader.reset();

        // Skip lines until the desired line number
        for (long i = 0; i < lineNumber; i++) {
            bufferedReader.readLine();
        }

        // Read the desired line
        return bufferedReader.readLine();
    }

    private static boolean check(String line) {
        String[] fields = line.split(", ");

        String command = fields[0];
        Integer fishID = Integer.parseInt(fields[1]);
        String msg = fields[3];

        if ("move".equals(command)) {
            if(isFishInPond(fishID)){
                return true;
            }
        } else if ("ack".equals(command) && ("acpt".equals(msg))) {
            if(!isFishInPond(fishID)){
                return true;
            }
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


