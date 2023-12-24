package services;

import java.io.*;
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
                    try {
                        compileAndRunMain();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 1000, 1000); // Delay 1 second, repeat every 1 second
    }

    private static void compileAndRunMain() throws IOException, InterruptedException {
        // Get the current Java process and execute a new Java process
        String javaCommand = System.getProperty("java.home") + "/bin/java";
        String javaClassPath = System.getProperty("java.class.path");
        String className = "main"; // Adjust based on the actual package and class name

        // Use ProcessBuilder to compile and run main.java
        ProcessBuilder processBuilder = new ProcessBuilder(javaCommand, "-cp", javaClassPath, className);

        // Redirect error stream to the output stream
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        // Capture the output of the process
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        // Wait for the new process to finish
        int exitCode = process.waitFor();

        // Print the exit code for debugging
        System.out.println("Restarted process exited with code: " + exitCode);
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
