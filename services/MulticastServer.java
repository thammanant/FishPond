package services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {
    private static final String MULTICAST_ADDRESS = "230.0.0.0";
    private static final StringBuilder receivedMessages = new StringBuilder();

    // for testing
    public static void main(String[] args) {
        int portNumber = 12345;

        // Start the server in a separate thread
        Thread serverThread = new Thread(() -> runServer(portNumber));
        serverThread.start();

        while (true) {
            String messages = getReceivedMessages();
            if (!messages.isEmpty()) {
                System.out.println("Received messages:\n" + messages);
                // Optionally, you can clear the received messages
                clearReceivedMessages();
            }
        }
    }

    public static void clearReceivedMessages() {
        receivedMessages.setLength(0); // Clear the string builder
    }


    public static void runServer(int portNumber) {
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            MulticastSocket socket = new MulticastSocket(portNumber);
            socket.joinGroup(group);

            System.out.println("Server: Waiting for connection...");

            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                receivedMessages.append(received).append("\n"); // Append received message to the variable
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getReceivedMessages() {
        return receivedMessages.toString(); // Return accumulated messages
    }

}
