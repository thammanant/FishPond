package services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static services.ManageLogFile.write_to_log;
import static services.StartUp.getCurrentClock;

public class Communicate {

    private static boolean messageReceived = false;

    int pondID;

    int portNumber;

    public Communicate(int pornID, int portNumber) {
        this.pondID = pondID;
        this.portNumber = portNumber;
    }
    public void move(int fishID, int pondID, int port){
        // call multicast client
        MulticastClient client = new MulticastClient(port);
        try {
            while (true) {
                client.send_multicast_message("move," + fishID + "," + pondID);
                // get response from server
                String response = MulticastServer.get_received_messages();
                ManageLogFile.write_to_log("move", fishID, pondID, getCurrentClock());
                if (response.equals("ack," + fishID + "," + pondID + "acpt")){ // if accepted
                    Database.remove_fish_fromDB(fishID);
                    break;
                }
                else if (response.equals("ack," + fishID + "," + pondID + "rej")){ // if rejected
                    System.out.println("Fish rejected");
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void ack_fish(int fishID, int pondID, int port, String status){
        // status must only be acpt for accept or rej for reject
        if (!Objects.equals(status, "acpt") && !Objects.equals(status, "rej")){
            System.out.println("Invalid status");
            return;
        }
        // call multicast client
        MulticastClient client = new MulticastClient(port);
        try {
            if(status.equals("acpt")) {
                write_to_log("ack", fishID, pondID, getCurrentClock(), status);
                //check fish within database
                JSONArray fishList = Database.read_fish_fromDB();
                for (Object o : fishList) {
                    JSONObject fish = (JSONObject) o;
                    int fishType = 4;
                    int fishid = Integer.parseInt((String) fish.get("fishid"));
                    if (fishid == fishID) {
                        System.out.println("Fish already added");
                    } else {
                        Database.add_fish_from_other_pond(fishID, fishType);
                        System.out.println("Fish added");
                    }
                }
            }
            else{
                System.out.println("Fish rejected");
            }
            client.send_multicast_message("ack," + fishID + "," + pondID + "," + status);
            System.out.println("Ack sent");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleReceivedMessages(Scanner ansForRequest) {
        String messages = MulticastServer.get_received_messages();
        if (!messages.isEmpty()) {
            processReceivedMessages(messages, ansForRequest);
            messageReceived = true;
            MulticastServer.clear_received_messages();
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

    public void processReceivedMessages(String messages, Scanner ansForRequest) {
        // Process received messages
        System.out.println("Received request:\n" + messages.toLowerCase());

        if (messages.toLowerCase().contains("move")) {
            String[] request = messages.toLowerCase().split("\\s*,\\s*");

            for (Integer i = 0; i < request.length; i++) {
                request[i] = request[i].replaceAll("\\s+", "");
                System.out.println(request[i]);
            }

            if (isValidMoveRequest(request,this.pondID)) {
                System.out.println("New incoming fish");
                System.out.println("Would you like to accept? (Y/N)");

                String ans = ansForRequest.nextLine();
                if (ans.equalsIgnoreCase("Y")) {
                    Fish_old.ack_fish(Integer.parseInt(request[1]), Integer.parseInt(request[2]), this.portNumber, "acpt");
                } else if (ans.equalsIgnoreCase("N")) {
                    Fish_old.ack_fish(Integer.parseInt(request[1]), Integer.parseInt(request[2]), this.portNumber, "rej");
                }
            } else {
                System.out.println("Message not for this pond");
            }
        }
    }

    public boolean isValidMoveRequest(String[] request, int pondID) {
        return request.length == 3 && request[0].equals("move") &&
                request[1].matches("[0-9]+") && request[2].matches("[0-9]+") &&
                Integer.parseInt(request[2]) == pondID;
    }
}
