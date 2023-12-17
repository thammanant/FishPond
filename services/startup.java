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
            String messages = MulticastServer.getReceivedMessages();
            if (!messages.isEmpty()) {
                //decompose message
                String[] request = messages.split(",");
                System.out.println("Received request after split:\n");
                for (String s : request) {
                    System.out.println(s);
                }
                //check if message is for this pond
                if (Integer.parseInt(request[2]) == pondID && request[0].equals("move")) {
                    System.out.println("New incoming fish");
                    System.out.println("Would you like to accept? (Y/N)");
                    String ans = ansForRequest.nextLine();
                    if (ans.equalsIgnoreCase("Y")) {
                        fish.ackFish(Integer.parseInt(request[1]), Integer.parseInt(request[2]), portNumber, "acpt");
                    } else if (ans.equalsIgnoreCase("N")) {
                        fish.ackFish(Integer.parseInt(request[1]), Integer.parseInt(request[2]), portNumber, "rej");
                    }
                }
                else {
                    System.out.println("Message not for this pond");
                }

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
                    System.out.println("Enter port number: ");
                    Integer portForMove = PortForMove.nextInt();
                    fish.moveFish(idForMove,pondForMove,portForMove);
                } else if (userChoice == 5) {
                    shutdown.shutdownMenu();
                }
            }
            
            catch (Exception e) {
                System.out.println("Invalid input");
                input.nextLine();
            }
            finally {
                continue;
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
