package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Timer;
import java.util.TimerTask;

public class RunClock {
    private static String clockFilePath = "clock.txt";


    public static void main(String[] args) {

        // Set up a timer to update and write the clock every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update the clock and display the new content
                int updatedContent = readClockFile() + 1;

                // Write the updated content back to the file
                writeClockFile(updatedContent);
            }
        }, 1000, 1000); // Delay 1 second, repeat every 1 second
    }

    public static int readClockFile() {
        try {
            Path filePath = Paths.get(clockFilePath);
            if (Files.exists(filePath)) {
                // Read the content of the clock file
                return Integer.parseInt(new String(Files.readAllBytes(filePath)));
            } else {
                System.err.println("Clock file not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void writeClockFile(int content) {
        try {
            Path filePath = Paths.get(clockFilePath);
            // Write the updated content to the file
            Files.write(filePath, String.valueOf(content).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

