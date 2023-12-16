package services;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.InterruptedException;

public class startup {
    private final int pondID;
    private final int portNumber = 12345;

    public startup(int pondID) {
        this.pondID = pondID;
    }

    public void start() {
        System.out.println("Pond ID: " + pondID);
        System.out.println("Starting server" );
        Thread serverThread = new Thread(() -> MulticastServer.runServer(portNumber));
        serverThread.start();

        while (true) {
            String messages = MulticastServer.getReceivedMessages();
            if (!messages.isEmpty()) {
                System.out.println("Received messages:\n" + messages);
                // Optionally, you can clear the received messages
                MulticastServer.clearReceivedMessages();
            }
        }
    }
    public void receiveRequest() {
        System.out.println("Receiving message");
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
        int timeout = 5000;
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
