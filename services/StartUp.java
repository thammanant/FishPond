package services;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.InterruptedException;
import java.util.Scanner;
import sun.misc.Signal;


public class StartUp {
    private static Integer pondID;
    private static Integer portNumber = 12345;

    private static boolean messageReceived = false;

    public static Integer clock = 0;
    public StartUp(Integer pondID) {
        this.pondID = pondID;
    }

    public void start() {
        System.out.println("Pond ID: " + pondID);
        System.out.println("Starting server" );
        Thread serverThread = new Thread(() -> MulticastServer.run_server(portNumber));
        serverThread.start();
        
    }

    private static void handleReceivedMessages(Scanner ansForRequest) {
        String messages = MulticastServer.get_received_messages();
        if (!messages.isEmpty()) {
            processReceivedMessages(messages, ansForRequest);
            messageReceived = true;
            MulticastServer.clear_received_messages();
        }

        if (messageReceived) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace(); // Handle the exception if needed
            }
            messageReceived = false;
        }
    }

    private static void processReceivedMessages(String messages, Scanner ansForRequest) {
        // Process received messages
        System.out.println("Received request:\n" + messages.toLowerCase());

        if (messages.toLowerCase().contains("move")) {
            String[] request = messages.toLowerCase().split("\\s*,\\s*");

            for (Integer i = 0; i < request.length; i++) {
                request[i] = request[i].replaceAll("\\s+", "");
                System.out.println(request[i]);
            }

            if (isValidMoveRequest(request)) {
                System.out.println("New incoming fish");
                System.out.println("Would you like to accept? (Y/N)");

                String ans = ansForRequest.nextLine();
                if (ans.equalsIgnoreCase("Y")) {
                    Fish_old.ack_fish(Integer.parseInt(request[1]), Integer.parseInt(request[2]), portNumber, "acpt");
                } else if (ans.equalsIgnoreCase("N")) {
                    Fish_old.ack_fish(Integer.parseInt(request[1]), Integer.parseInt(request[2]), portNumber, "rej");
                }
            } else {
                System.out.println("Message not for this pond");
            }
        }
    }

    private static boolean isValidMoveRequest(String[] request) {
        return request.length == 3 && request[0].equals("move") &&
                request[1].matches("[0-9]+") && request[2].matches("[0-9]+") &&
                Integer.parseInt(request[2]) == pondID;
    }

    private static void displayMenu() {
        System.out.println("1: Add fish");
        System.out.println("2: Remove fish");
        System.out.println("3: Draw pond");
        System.out.println("4: Move fish");
        System.out.println("5: Shutdown");
        System.out.println("6: System Report");
        System.out.println("7: Pond Report");
        System.out.print("Please enter your choice: ");
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
        Integer timeout = 5000;
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

    public static void writeClockFile(int content) {
        try {
            Path filePath = Paths.get("clock.txt");
            // Write the updated content to the file
            Files.write(filePath, String.valueOf(content).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Integer getCurrentClock() {
        return clock;
    }

    public static Integer getPondID() {
        return pondID;
    }

    public static Integer getPortNumber() {
        return portNumber;
    }
}
