package services;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.InterruptedException;
import java.util.Scanner;


public class StartUp {
    private static Integer pondID;
    private static final Integer portNumber = 12345;

    private static boolean messageReceived = false;

    public static Integer clock = 0;
    public StartUp(Integer pondID) {
        StartUp.pondID = pondID;
        createIfNotExists("fish.json");
        createIfNotExists("backup.txt");
        createIfNotExists("log.txt");
    }

    public void start() {
        System.out.println("Pond ID: " + pondID);
        System.out.println("Starting server" );
        Thread serverThread = new Thread(() -> MulticastServer.run_server(portNumber));
        serverThread.start();
        // new thread that will call backup every 5 minutes
        Thread backupThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                    Backup.backup();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        backupThread.start();
        Backup.recover();
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

                Integer fishID = Communicate.get_fish_info(Integer.parseInt(request[1]))[0];
                Integer genesisPondID = Communicate.get_fish_info(Integer.parseInt(request[1]))[1];

                String ans = ansForRequest.nextLine();
                if (ans.equalsIgnoreCase("Y")) {
                    Communicate.ack_fish(Integer.parseInt(request[1]), fishID, genesisPondID, Integer.parseInt(request[2]), portNumber, "acpt");
                } else if (ans.equalsIgnoreCase("N")) {
                    Communicate.ack_fish(Integer.parseInt(request[1]), fishID, genesisPondID, Integer.parseInt(request[2]), portNumber, "rej");
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

    public static Integer get_pond_ID() {
        return pondID;
    }

    public static Integer get_port_number(){
        return portNumber;
    }

    public static Integer getPortNumber() {
        return portNumber;
    }

    public static void createIfNotExists(String filename) {
        File file = new File(filename);
        // Check if the file doesn't exist
        if (!file.exists()) {
            try {
                // Create the file
                file.createNewFile();
                System.out.println(filename + " created.");
            } catch (Exception e) {
                // Handle file creation error (optional)
                e.printStackTrace();
            }
        } else {
            // Check if the file is empty (length is 0 bytes)
            if (file.length() == 0) {
                System.out.println(filename + " already exists but empty.");
            } else {
                System.out.println(filename + " already exists.");
            }
        }
    }
}
