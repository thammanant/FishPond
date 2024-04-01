package services;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {
    public static Integer clock = 0;

    private static Timer timer;

    public static void start_clock() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update the clock and display the new content
                clock = clock + 1;
            }
        }, 1000, 1000); // Delay 1 second, repeat every 1 second
    }

    public static Integer get_current_clock() {
        return clock;
    }

    //stop running clock
    public static void stop_clock() {
        if (timer != null) {
            timer.cancel();
            System.out.println("Clock stopped");
        }
    }
}
