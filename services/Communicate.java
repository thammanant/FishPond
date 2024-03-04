package services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static services.ManageLogFile.write_to_log;

public class Communicate {

    boolean messageReceived = false;

    int pondID;

    int portNumber;

    public Communicate(int pondID, int portNumber) {
        this.pondID = pondID;
        this.portNumber = portNumber;
    }
    public static void move(int fishID, int fishType, int genesisPondID, int pondID, int port){
        // call multicast client
        MulticastClient client = new MulticastClient(port);
        boolean moveLoop = true;
        try {
            while (moveLoop) {
                client.send_multicast_message("move," + fishID + "," + fishType + "," + genesisPondID + "," + pondID + "\n");
                // get response from server
                String response = MulticastServer.get_received_messages();
                String[] messagesParts = response.split("\n");
                ManageLogFile.write_to_log("move", fishID,fishType, genesisPondID, pondID, Clock.get_current_clock());
                for(String message : messagesParts){
                    if (message.equals("ack," + fishID + "," + fishType + "," + genesisPondID + "," + pondID + "," + "acpt")){ // if accepted
                        Database.remove_fish_fromDB(fishID);
                        System.out.println("Fish accepted");
                        moveLoop = false;
                        break;
                    }
                    else if (message.equals("ack," + fishID + "," + fishType + "," + genesisPondID + "," + pondID + "," + "rej")){ // if rejected
                        System.out.println("Fish rejected");
                        moveLoop = false;
                        break;
                    }
                    Thread.sleep(1000);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ack_fish(int fishID,int fishType, int genesisPondID , int pondID, int port, String status){
        // status must only be acpt for accept or rej for reject
        if (!Objects.equals(status, "acpt") && !Objects.equals(status, "rej")){
            System.out.println("Invalid status");
            return;
        }
        // call multicast client
        MulticastClient client = new MulticastClient(port);
        try {
            if(status.equals("acpt")) {
                write_to_log("ack", fishID, fishType, genesisPondID, pondID, Clock.get_current_clock(), status);
                //check fish within database
                JSONArray fishList = Database.read_fish_fromDB();
                for (Object o : fishList) {
                    JSONObject fish = (JSONObject) o;
                    int fishid = Integer.parseInt((String) fish.get("fishid"));
                    if (fishid == fishID) {
                        System.out.println("Fish already added");
                    }
                    else{
                        Database.add_fish_from_other_pond(fishID, fishType, genesisPondID);
                        System.out.println("Fish added");
                        client.send_multicast_message("ack," + fishID + "," + fishType + "," + genesisPondID + "," + pondID + "," + status + "\n");
                        System.out.println("Ack sent");
                        break;
                    }
                }
            }
            else{
                System.out.println("Fish rejected");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handle_received_messages(Scanner ansForRequest) {
        String messages = MulticastServer.get_received_messages();
        String[] messagesParts = messages.split("\n");
        if (!messages.isEmpty()) {
            for (String message : messagesParts) {
                this.process_received_messages(message, ansForRequest);
                this.messageReceived = true;
                MulticastServer.clear_received_messages();
            }
        }

        if (this.messageReceived) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace(); // Handle the exception if needed
            }
            this.messageReceived = false;
        }
    }

    private void process_received_messages(String messages, Scanner ansForRequest) {
        // Process received messages
        System.out.println("Received request:\n" + messages.toLowerCase());

        if (messages.toLowerCase().contains("move")) {
            String[] request = messages.toLowerCase().split("\\s*,\\s*");

            if (is_valid_move_request(request,this.pondID)) {
                System.out.println("New incoming fish");
                System.out.println("Would you like to accept? (Y/N)");

                String ans = ansForRequest.nextLine();
                if (ans.equalsIgnoreCase("Y")) {
                    ack_fish(Integer.parseInt(request[1]), Integer.parseInt(request[2]),Integer.parseInt(request[3]) , Integer.parseInt(request[4].trim()), this.portNumber, "acpt");
                } else if (ans.equalsIgnoreCase("N")) {
                    ack_fish(Integer.parseInt(request[1]),Integer.parseInt(request[2]),Integer.parseInt(request[3]) , Integer.parseInt(request[4].trim()),  this.portNumber, "rej");
                }
            }
        }
    }


    private boolean is_valid_move_request(String[] request, int pondID) {
        return ((request.length == 5) && (request[0].equals("move")) &&
                (request[1].matches("[0-9]+")) && (request[2].matches("[0-9]+")) &&
                (Integer.parseInt(request[4].trim()) == pondID));
    }

    public void set_message_received(boolean messageReceived) {
        this.messageReceived = messageReceived;
    }

    public static Integer[] get_fish_info(int fishid) {
        return Database.get_fish_info(fishid);
    }

}
