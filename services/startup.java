package services;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.InterruptedException;
import java.util.Scanner;


public class startup {
    private static int pondID;
    private static int portNumber = 12345;

    private static boolean messageReceived = false;

    public startup(int pondID) {
        this.pondID = pondID;
    }

    public void start() {
        Scanner input = new Scanner(System.in);
        Scanner removeId = new Scanner(System.in);
        Scanner FishIdFormove = new Scanner(System.in);
        Scanner PondIdFormove = new Scanner(System.in);
        Scanner PortForMove = new Scanner(System.in);
        Scanner ansForRequest = new Scanner(System.in);
        System.out.println("Pond ID: " + pondID);
        System.out.println("Starting server" );
        Thread serverThread = new Thread(() -> MulticastServer.runServer(portNumber));
        serverThread.start();
        while (true) {
            handleReceivedMessages(ansForRequest);

            if (!messageReceived) {
                displayMenu();
            }

            try {
                int userChoice = input.nextInt();
                processUserChoice(userChoice, removeId, FishIdFormove, PortForMove, ansForRequest);
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

            for (int i = 0; i < request.length; i++) {
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
        System.out.print("Please enter your choice: ");
    }

    private static void processUserChoice(int userChoice, Scanner removeId, Scanner fishIdForMove,
                                          Scanner pondIdForMove, Scanner ansForRequest) {
        switch (userChoice) {
            case 1:
                fish.addFish();
                break;
            case 2:
                System.out.println("Enter ID of fish to remove: ");
                int idForRemove = removeId.nextInt();
                fish.removeFish(idForRemove);
                break;
            case 3:
                fish.drawFishFromDB();
                break;
            case 4:
                fish.moveFish(fishIdForMove.nextInt(), pondIdForMove.nextInt(), ansForRequest.nextInt());
                break;
            case 5:
                shutdown.shutdownMenu();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
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
