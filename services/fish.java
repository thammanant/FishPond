package services;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import services.fishAnimation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Random;


import static services.eventHandler.writeToLog;
import static services.startup.getCurrentClock;

public class fish {
    public int fishid;
    public int fishType;
    
    
    public static fish createFish(int fishType){
        fish newFish = new fish();
        Random random = new Random();
        int randomNum = random.nextInt(10);
        //conditional for checking if fishid is exist
        newFish.fishid = randomNum;
        newFish.fishType = fishType;
        return newFish;
    }

    


    public static void moveFish(int fishID, int pondID, int port){
        // call multicast client
        MulticastClient client = new MulticastClient(port);
        try {
            while (true) {
                client.sendMulticastMessage("move," + fishID + "," + pondID);
                // get response from server
                String response = MulticastServer.getReceivedMessages();
                writeToLog("move", fishID, pondID, getCurrentClock());
                if (response.equals("ack," + fishID + "," + pondID + "acpt")){ // if accepted
                    database.removeFishFromDB(fishID);
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

    public static void ackFish(int fishID, int pondID, int port, String status){
        // status must only be acpt for accept or rej for reject
        if (!Objects.equals(status, "acpt") && !Objects.equals(status, "rej")){
            System.out.println("Invalid status");
            return;
        }
        // call multicast client
        MulticastClient client = new MulticastClient(port);
        try {
            if(status.equals("acpt")) {
                writeToLog("ack", fishID, pondID, getCurrentClock(), status);
                //check fish within database
                JSONArray fishList = database.readFishFromDB();
                for (Object o : fishList) {
                    JSONObject fish = (JSONObject) o;
                    int fishType = 4;
                    int fishid = Integer.parseInt((String) fish.get("fishid"));
                    if (fishid == fishID) {
                        System.out.println("Fish already added");
                    } else {
                        addFishFromOtherPond(fishID, fishType);
                        System.out.println("Fish added");
                    }
                }
            }
            else{
                System.out.println("Fish rejected");
            }
            client.sendMulticastMessage("ack," + fishID + "," + pondID + "," + status);
            System.out.println("Ack sent");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addFishFromOtherPond(int fishID, int fishType){
        database.addFishToDB(fishID, fishType);
    }

    public static void addFish(){

        System.out.print("Please select fish type: \n");
        System.out.println("Type 1)\n");
        fishAnimation.bubbleFish();
        System.out.println("Type 2)\n");
        fishAnimation.shark();
        System.out.println("Type 3)\n");
        fishAnimation.triangleFish();
        System.out.println("Type 4)\n");
        fishAnimation.seahorse();
        System.out.println("Type 5)\n");
        fishAnimation.pufflefish();
        Scanner input = new Scanner(System.in);
        System.out.print("Enter fish type: ");
        String userChoice = input.nextLine();

        switch(userChoice){
            case "1":
                System.out.println("You have selected bubble fish");
                fishAnimation.bubbleFish();
                break;
            case "2":
                System.out.println("You have selected shark");
                fishAnimation.shark();
                break;
            case "3":
                System.out.println("You have selected triangle fish");
                fishAnimation.triangleFish();
                break;
            case "4":
                System.out.println("You have selected seahorse");
                fishAnimation.seahorse();
                break;
            case "5":
                System.out.println("You have selected pufflefish");
                fishAnimation.pufflefish();
                break;
            case "exit":
                System.out.println("Quitting...");
                break;
            default:
                System.out.println("Invalid input, please type number between 1-5");
                return;
        }

        fish newFish = createFish(Integer.parseInt(userChoice));
        database.addFishToDB(newFish.fishid, newFish.fishType);
    }

    public static void drawFishFromDB(){
        JSONArray fishList = database.readFishFromDB();
        for (Object o : fishList) {
            JSONObject fish = (JSONObject) o;
            int fishType = Integer.parseInt((String) fish.get("fishType"));
            int fishid = Integer.parseInt((String) fish.get("fishid"));
            System.out.println("id: " + fishid);
            switch(fishType){
                case 1:
                    fishAnimation.bubbleFish();
                    break;
                case 2:
                    fishAnimation.shark();
                    break;
                case 3:
                    fishAnimation.triangleFish();
                    break;
                case 4:
                    fishAnimation.seahorse();
                    break;
                case 5:
                    fishAnimation.pufflefish();
                    break;
                default:
                    System.out.println("Invalid input, please type number between 1-5");
                    break;
            }
        }
    }

    public static void removeFish(int fishid){
        while(true){
            database.removeFishFromDB(fishid);
            System.out.println("Fish id: " + fishid + " removed");
            break;
        }

    }
}

