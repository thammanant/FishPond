package services;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MulticastClient implements Runnable {

    private static final String MULTICAST_ADDRESS = "230.0.0.0";
    private static final int PORT = 4446;
    private static final int TIMEOUT = 5000; // Timeout in milliseconds for waiting for replies
    private static final String LOG_FILE_PATH = "pond_log.txt";

    private MulticastSocket socket;
    private InetAddress group;
    private boolean isCoordinator = false;
    private long clock = 0;

    public static void main(String[] args) {
        Thread t = new Thread(new MulticastClient());
        t.start();
    }

    public MulticastClient() {
        try {
            socket = new MulticastSocket(PORT);
            group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(new InetSocketAddress(group, PORT), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMulticastMessage(String message) throws IOException {
        DatagramSocket senderSocket = new DatagramSocket();
        byte[] msg = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, group, PORT);
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
                System.out.println("Enter command (HELLO/EXIT): ");
                String command = scanner.nextLine().toUpperCase();

                if (command.equals("HELLO")) {
                    sendMulticastMessage("HELLO");
                    handleHelloMessage();
                } else if (command.equals("EXIT")) {
                    handleShutdownRequest();
                    saveStateToFile();
                    socket.leaveGroup(new InetSocketAddress(group, PORT), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
                    socket.close();
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
