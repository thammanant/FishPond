package services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Objects;

import static services.ManageLogFile.write_to_log;
import static services.StartUp.getCurrentClock;

public class Communicate {
    public static void move(int fishID, int pondID, int port){
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

    public static void ack_fish(int fishID, int pondID, int port, String status){
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
}
