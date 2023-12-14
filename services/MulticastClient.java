package services;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MulticastClient implements Runnable {

    private static final String MULTICAST_ADDRESS = "230.0.0.0";
    private int port;
    private static final int TIMEOUT = 5000; // Timeout in milliseconds for waiting for replies
    private static final String LOG_FILE_PATH = "pond_log.txt";

    private MulticastSocket socket;
    private InetAddress group;
    private boolean isCoordinator = false;
    private long clock = 0;

    public static void main(String[] args) {
        int port = 4446; // Default port
        long initialClock = 0; // Default initial clock value
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }
        if (args.length >= 2) {
            initialClock = Long.parseLong(args[1]);
        }
        Thread t = new Thread(new MulticastClient(port, initialClock));
        t.start();
    }

    public MulticastClient(int port, long initialClock) {
        this.port = port;
        this.clock = initialClock;
        try {
            socket = new MulticastSocket(port);
            group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(new InetSocketAddress(group, port), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMulticastMessage(String message) throws IOException {
        DatagramSocket senderSocket = new DatagramSocket();
        byte[] msg = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, group, port);
        senderSocket.send(packet);
        senderSocket.close();
    }

    private void handleHelloMessage() throws IOException, InterruptedException {
        // Send state update request
        sendMulticastMessage("STATE_REQUEST");

        // Wait for replies
        Thread.sleep(TIMEOUT);
        if (!isCoordinator) {
            // No replies received, become the coordinator
            System.out.println("Coordinator: No replies received. Becoming the coordinator.");
            isCoordinator = true;
            // Set up necessary data structures and return to any saved state
            // (Not implemented in this example)
        }
    }

    private void handleStateUpdateRequest() throws IOException {
        // Respond to state update request with the current clock value
        sendMulticastMessage("STATE_UPDATE " + clock);
    }

    private void handleStateUpdate(String message) {
        // Parse the state update message and update the clock
        String[] parts = message.split(" ");
        if (parts.length == 2 && parts[0].equals("STATE_UPDATE")) {
            long receivedClock = Long.parseLong(parts[1]);
            clock = Math.max(clock, receivedClock);
        }
    }

    private void handleShutdownRequest() {
        // Implement a voting mechanism for shutting down the vivisystem
        // For simplicity, assume that all ponds agree to shut down
        System.out.println("Received shutdown request. Voting to shut down.");
        try {
            // Inform others about the decision
            sendMulticastMessage("SHUTDOWN_ACK");
            // Implement shutdown logic (Not implemented in this example)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStateToFile() {
        // Save pond state to a log file for persistent recovery
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH))) {
            writer.println("Clock: " + clock);
            // Save other pond state information as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recoverStateFromFile() {
        // Recover pond state from the log file for persistent recovery
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line = reader.readLine();
            while (line != null) {
                // Parse and apply saved state information as needed
                if (line.startsWith("Clock: ")) {
                    clock = Long.parseLong(line.substring("Clock: ".length()));
                }
                // Add more conditions for other saved state information
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            recoverStateFromFile();

            while (true) {
                System.out.println("Enter (EXIT) to exit or message to send: ");
                String command = scanner.nextLine().toUpperCase();
                if (command.equals("EXIT")) {
                    handleShutdownRequest();
                    saveStateToFile();
                    socket.leaveGroup(new InetSocketAddress(group, port), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
                    socket.close();
                    break;
                } else {
                    sendMulticastMessage(command);
                    handleHelloMessage();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
