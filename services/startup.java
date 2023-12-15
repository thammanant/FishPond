package services;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.InterruptedException;
public class startup {
    private final int port;
    private int clock;
    private final int timeout = 5000;

    public startup(int port) {
        this.port = port;
        this.clock = 0;

    }

    public void start() {
        System.out.println("Starting server on port " + port);
    }
    public void receiveMessage() {
        System.out.println("Receiving message");
    }

    public void listenForReplies(){
        System.out.println("Listening for replies");
    }

    public boolean performTask() {
        //TODO: Implement task
        boolean success = false;
        try {
            System.out.println("Performing task");
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return success;
    }


    public void timeoutCounter(){
        System.out.println("Timeout counter");
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            public void run(){
                System.out.println("Timeout");
            }
        }, timeout);
        if (performTask()){
            timer.cancel();
            System.out.println("Task completed");
        }
        else {
            timer.cancel();
            System.out.println("Timeout 1234");
        }


    }

}
