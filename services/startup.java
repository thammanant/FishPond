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
        boolean messageReceived = false;
        while (true) {

            String messages = MulticastServer.getReceivedMessages();
            if (!messages.isEmpty()) {
                //decompose message

                System.out.println("Received request:\n" + messages.toLowerCase());
                //handle request
                if(messages.toLowerCase().contains("move")) {

                    String[] request = messages.toLowerCase().split("\\s*,\\s*");
                    //replace all whitespace
                    for (int i = 0; i < request.length; i++) {
                        request[i] = request[i].replaceAll("\\s+", "");
                        System.out.println(request[i]);
                    }
                    //check if message is for this pond
                    if (request[1].matches("[0-9]+") && request[2].matches("[0-9]+") && Integer.parseInt(request[2]) == pondID && "move".equals(request[0])) {
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
                messageReceived = true;
                MulticastServer.clearReceivedMessages();
            }
            if (messageReceived) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // Handle the exception if needed
                    e.printStackTrace();
                }
                messageReceived = false;
                continue;
            }
            if(!messageReceived){
                System.out.println("1: Add fish");
                System.out.println("2: Remove fish");
                System.out.println("3: Draw pond");
                System.out.println("4: Move fish");
                System.out.println("5: Shutdown");
                System.out.print("Please enter your choice: ");
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
            }
            catch (Exception e) {
                System.out.println("Invalid input");
                input.nextLine();
            }
            finally {
                messageReceived = false;

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
