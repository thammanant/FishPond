package services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {
    private static final String MULTICAST_ADDRESS = "230.0.0.0";

    // for testing
    public static void main(String[] args) {
        int portNumber = 12345;

        String receivedMessage = runServer(portNumber);
        System.out.println("Server: Received message: " + receivedMessage);
    }

    private static String runServer(int portNumber) {
        StringBuilder receivedMessage = new StringBuilder();

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
                System.out.println("Server: Received message: " + received);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receivedMessage.toString();
    }
}
