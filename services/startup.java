package services;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.InterruptedException;
import java.util.Scanner;


public class startup {
    private final int pondID;
    private final int portNumber = 12345;

    public startup(int pondID) {
        this.pondID = pondID;
    }

    public void start() {
        Scanner input = new Scanner(System.in);
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
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Handle the exception if needed
                e.printStackTrace();
            }
    
            System.out.println("1: Add fish");
            System.out.println("2: Remove fish");
            System.out.println("3: Draw pond");
            System.out.println("4: Move fish");
            System.out.println("5: Shutdown");
            System.out.print("Please enter your choice: ");
            Integer userChoice = input.nextInt();

            if(userChoice == 1){
                fish.addFish();
            }
            else if(userChoice == 2){
                //implemet later
            }
            else if(userChoice == 3){
                //implement later
            }
            else if(userChoice == 4){
                //implement later
            }
            else if(userChoice == 5){
                shutdown.shutdownMenu();
            }
            else{
                System.out.println("Invalid input");

            }
            
        }


        // input.close();  keep this at the end
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
