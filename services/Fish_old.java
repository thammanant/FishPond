//package services;
//import java.io.IOException;
//import java.util.Objects;
//import java.util.Scanner;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//
//import java.util.Random;
//
//import static services.ManageLogFile.write_to_log;
//import static services.StartUp.getCurrentClock;
//
//public class Fish_old {
//    public int fishid;
//    public int fishType;
//
//
//    public static Fish_old create_fish(int fishType){
//        Fish_old newFish = new Fish_old();
//        Random random = new Random();
//        int randomNum = random.nextInt(10);
//        //conditional for checking if fishid is exist
//        newFish.fishid = randomNum;
//        newFish.fishType = fishType;
//        return newFish;
//    }
//
//
//    public static void move_fish(int fishID, int pondID, int port){
//        // call multicast client
//        MulticastClient client = new MulticastClient(port);
//        try {
//            while (true) {
//                client.send_multicast_message("move," + fishID + "," + pondID);
//                // get response from server
//                String response = MulticastServer.get_received_messages();
//                ManageLogFile.write_to_log("move", fishID, pondID, getCurrentClock());
//                if (response.equals("ack," + fishID + "," + pondID + "acpt")){ // if accepted
//                    Database.remove_fish_fromDB(fishID);
//                    break;
//                }
//                else if (response.equals("ack," + fishID + "," + pondID + "rej")){ // if rejected
//                    System.out.println("Fish rejected");
//                    break;
//                }
//                Thread.sleep(1000);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void ack_fish(int fishID, int pondID, int port, String status){
//        // status must only be acpt for accept or rej for reject
//        if (!Objects.equals(status, "acpt") && !Objects.equals(status, "rej")){
//            System.out.println("Invalid status");
//            return;
//        }
//        // call multicast client
//        MulticastClient client = new MulticastClient(port);
//        try {
//            if(status.equals("acpt")) {
//                write_to_log("ack", fishID, pondID, getCurrentClock(), status);
//                //check fish within database
//                JSONArray fishList = Database.read_fish_fromDB();
//                for (Object o : fishList) {
//                    JSONObject fish = (JSONObject) o;
//                    int fishType = 4;
//                    int fishid = Integer.parseInt((String) fish.get("fishid"));
//                    if (fishid == fishID) {
//                        System.out.println("Fish already added");
//                    } else {
//                        Database.add_fish_from_other_pond(fishID, fishType);
//                        System.out.println("Fish added");
//                    }
//                }
//            }
//            else{
//                System.out.println("Fish rejected");
//            }
//            client.send_multicast_message("ack," + fishID + "," + pondID + "," + status);
//            System.out.println("Ack sent");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
////    public static void add_fish_from_other_pond(int fishID, int fishType){
////        Database.add_fish_toDB(fishID, fishType);
////    }
//
//    public static void add_fish(){
//
//        System.out.print("Please select fish type: \n");
//        System.out.println("Type 1)\n");
//        FishAnimation.bubbleFish();
//        System.out.println("Type 2)\n");
//        FishAnimation.shark();
//        System.out.println("Type 3)\n");
//        FishAnimation.triangleFish();
//        System.out.println("Type 4)\n");
//        FishAnimation.seahorse();
//        System.out.println("Type 5)\n");
//        FishAnimation.pufflefish();
//        Scanner input = new Scanner(System.in);
//        System.out.print("Enter fish type: ");
//        String userChoice = input.nextLine();
//
//        switch(userChoice){
//            case "1":
//                System.out.println("You have selected bubble fish");
//                FishAnimation.bubbleFish();
//                break;
//            case "2":
//                System.out.println("You have selected shark");
//                FishAnimation.shark();
//                break;
//            case "3":
//                System.out.println("You have selected triangle fish");
//                FishAnimation.triangleFish();
//                break;
//            case "4":
//                System.out.println("You have selected seahorse");
//                FishAnimation.seahorse();
//                break;
//            case "5":
//                System.out.println("You have selected pufflefish");
//                FishAnimation.pufflefish();
//                break;
//            case "exit":
//                System.out.println("Quitting...");
//                break;
//            default:
//                System.out.println("Invalid input, please type number between 1-5");
//                return;
//        }
//
//        Fish_old newFish = create_fish(Integer.parseInt(userChoice));
//        Database.add_fish_toDB(newFish.fishid, newFish.fishType);
//    }
//
//    public static void draw_fish_fromDB(){
//        JSONArray fishList = Database.read_fish_fromDB();
//        for (Object o : fishList) {
//            JSONObject fish = (JSONObject) o;
//            int fishType = Integer.parseInt((String) fish.get("fishType"));
//            int fishid = Integer.parseInt((String) fish.get("fishid"));
//            System.out.println("id: " + fishid);
//            switch(fishType){
//                case 1:
//                    FishAnimation.bubbleFish();
//                    break;
//                case 2:
//                    FishAnimation.shark();
//                    break;
//                case 3:
//                    FishAnimation.triangleFish();
//                    break;
//                case 4:
//                    FishAnimation.seahorse();
//                    break;
//                case 5:
//                    FishAnimation.pufflefish();
//                    break;
//                default:
//                    System.out.println("Invalid input, please type number between 1-5");
//                    break;
//            }
//        }
//    }
//
//    public static void remove_fish(int fishid){
//        while(true){
//            Database.remove_fish_fromDB(fishid);
//            System.out.println("Fish id: " + fishid + " removed");
//            break;
//        }
//    }
//}
//
