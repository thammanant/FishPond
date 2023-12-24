package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class Restart {
    private static String clockFilePath = "clock.txt";
    private static long lastModifiedTime;

    public static void main(String[] args) {
        // Set up a timer to check the clock every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Check if the clock is still running
                if (isClockRunning()) {
                    System.out.println("Clock is still running.");
                } else {
                    System.out.println("Clock has stopped. Restarting...");
                    //...
                }
            }
        }, 1000, 1000); // Delay 1 second, repeat every 1 second
    }

    private static boolean isClockRunning() {
        Path filePath = Paths.get(clockFilePath);

        try {
            if (Files.exists(filePath)) {
                // Check if the file was modified within the last second
                long currentModifiedTime = Files.getLastModifiedTime(filePath).toMillis();
                boolean isRunning = currentModifiedTime > lastModifiedTime;
                lastModifiedTime = currentModifiedTime;
                return isRunning;
            } else {
                // Clock file does not exist
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
