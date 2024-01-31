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
import sun.misc.SignalHandler;


public class startup {
    private static Integer pondID;
    private static Integer portNumber = 12345;

    private static boolean messageReceived = false;

    public static Integer clock = 0;
    public startup(Integer pondID) {
        this.pondID = pondID;
    }

    public void start() {
        Scanner input = new Scanner(System.in);
        Scanner removeId = new Scanner(System.in);
        Scanner FishIdFormove = new Scanner(System.in);
        Scanner PondIdFormove = new Scanner(System.in);
//        Scanner PortForMove = new Scanner(System.in);
        Scanner ansForRequest = new Scanner(System.in);
        System.out.println("Pond ID: " + pondID);
        System.out.println("Starting server" );
        Thread serverThread = new Thread(() -> MulticastServer.runServer(portNumber));
        serverThread.start();
        // Set up a timer to update and write the clock every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update the clock and display the new content
                clock = clock + 1;
                // Write the updated content back to the file
                writeClockFile(clock);
            }
        }, 1000, 1000); // Delay 1 second, repeat every 1 second
        while (true) {
            // handle crash
            Signal.handle(new Signal("INT"), new eventHandler.ExSignalHandler());
            Signal.handle(new Signal("TERM"), new eventHandler.ExSignalHandler());
            Signal.handle(new Signal("HUP"), new eventHandler.ExSignalHandler());


            handleReceivedMessages(ansForRequest);

            if (!messageReceived) {
                displayMenu();
            }

            try {
                Integer userChoice = input.nextInt();
                if (userChoice == 1) {
                    fish.addFish();
                } else if (userChoice == 2) {
                    System.out.println("Enter ID of fish to remove: ");
                    Integer idForRemove = removeId.nextInt();
                    fish.removeFish(idForRemove);
                } else if (userChoice == 3) {
                    fish.drawFishFromDB();
                } else if (userChoice == 4) {
                    System.out.println("Enter ID of fish to move: ");
                    Integer idForMove = FishIdFormove.nextInt();
                    System.out.println("Enter ID of pond to move to: ");
                    Integer pondForMove = PondIdFormove.nextInt();
                    fish.moveFish(idForMove,pondForMove,portNumber);
                } else if (userChoice == 5) {
                    shutdown.shutdownMenu();
                }
                else if (userChoice == 6) {
                    eventHandler.ExtendedSystemReport.main(null);
                }
                else {
                    System.out.println("Invalid input");
                }
            } catch (Exception e) {
                System.out.println("Invalid input");
                input.nextLine(); // Clear the buffer
            } finally {
                messageReceived = false;
            }
        }
    }

    private static void handleReceivedMessages(Scanner ansForRequest) {
        String messages = MulticastServer.getReceivedMessages();
        if (!messages.isEmpty()) {
            processReceivedMessages(messages, ansForRequest);
            messageReceived = true;
            MulticastServer.clearReceivedMessages();
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
                    fish.ackFish(Integer.parseInt(request[1]), Integer.parseInt(request[2]), portNumber, "acpt");
                } else if (ans.equalsIgnoreCase("N")) {
                    fish.ackFish(Integer.parseInt(request[1]), Integer.parseInt(request[2]), portNumber, "rej");
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

}
