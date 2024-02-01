package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {
    public static Integer clock = 0;

    public static void start_clock() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update the clock and display the new content
                clock = clock + 1;
                // Write the updated content back to the file
                write_clock_file(clock);
            }
        }, 1000, 1000); // Delay 1 second, repeat every 1 second
    }

    public static void write_clock_file(int content) {
        try {
            Path filePath = Paths.get("clock.txt");
            // Write the updated content to the file
            Files.write(filePath, String.valueOf(content).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Integer get_current_clock() {
        return clock;
    }
}
