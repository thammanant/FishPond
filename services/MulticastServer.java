package services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {
    private static final int SERVER_PORT = 4446;
    private static final String MULTICAST_ADDRESS = "230.0.0.0";

    public static void main(String[] args) {
        runServer();
    }

    private static void runServer() {
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            MulticastSocket socket = new MulticastSocket(SERVER_PORT);
            socket.joinGroup(group);

            System.out.println("Server: Waiting for connection...");

            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Server received: " + received);

                if (received.equals("connected to the server")) {
                    System.out.println("Server: 1 user connected.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
