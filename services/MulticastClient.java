package services;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MulticastClient implements Runnable {

    private static final String MULTICAST_ADDRESS = "230.0.0.0";
    private final int port;
    private MulticastSocket socket;
    private InetAddress group;

    // for testing
    public static void main(String[] args) {
        int port = 12345; // Default port
        Thread t = new Thread(new MulticastClient(port));
        t.start();
    }

    public MulticastClient(int port) {
        this.port = port;
        try {
            socket = new MulticastSocket(port);
            group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(new InetSocketAddress(group, port), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMulticastMessage(String message) throws IOException {
        DatagramSocket senderSocket = new DatagramSocket();
        byte[] msg = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, group, port);
        senderSocket.send(packet);
        senderSocket.close();
    }

    public void handleShutdown() {
        try 
        {
            socket.leaveGroup(new InetSocketAddress(group, port), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
            socket.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Enter (Q) to exit or message to send: ");
                String command = scanner.nextLine().toUpperCase();
                if (command.equals("Q")) {
                    handleShutdown();
                    break;
                } else {
                    sendMulticastMessage(command);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
