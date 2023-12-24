package services;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Random;

import static services.RunClock.readClockFile;
import static services.eventHandler.writeToLog;

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

    public static void bubbleFish(){
        
        System.out.println("               O  o");
        System.out.println("          _\\_   o");
        System.out.println("       \\/  o\\ .");
        System.out.println("       //\\___=");
        System.out.println("          ''");
        System.out.println("\n");
    }

    public static void shark(){
        
        System.out.println("      .            ");
        System.out.println("\\_____\\)\\_____");
        System.out.println("/--v____ __`<       ");
        System.out.println("        )/           ");
        System.out.println("        '            ");
        System.out.println("\n");
    }

    public static void triangleFish(){
        
        System.out.println("     |\\    o");
        System.out.println("    |  \\    o");
        System.out.println("|\\ /    .\\ o");
        System.out.println("| |       (");
        System.out.println("|/ \\     /");
        System.out.println("    |  /");
        System.out.println("     |/");
        System.out.println("\n");
    }

    public static void seahorse(){
        System.out.println("     \\/)/)");
        System.out.println("    _'  oo(_.-.");
        System.out.println("  /'.     .---'");
        System.out.println("/'-./    (");
        System.out.println(")     ; __\\");
        System.out.println("\\_.'\\ : __|");
        System.out.println("     )  _/");
        System.out.println("    (  (,.");
        System.out.println("     '-.-'");
        System.out.println("\n");
    }

    public static void pufflefish(){
        
        System.out.println("     .");
        System.out.println("                          A       ;");
        System.out.println("                |   ,--,-/ \\---,-/|  ,");
        System.out.println("               _|\\,'. /|      /|   `/|-. ");
        System.out.println("           \\`.'    /|      ,            `;.");
        System.out.println("          ,\\   A     A         A   A _ /| `.;");
        System.out.println("        ,/  _              A       _  / _   /|  ;");
        System.out.println("       /\\  / \\   ,  ,           A  /    /     `/|");
        System.out.println("      /_| | _ \\         ,     ,             ,/  \\");
        System.out.println("     // | |/ `.\\  ,-      ,       ,   ,/ ,/      \\ /");
        System.out.println("     / @| |@  / /'   \\  \\      ,              >  /|    ,--.");
        System.out.println("    |\\_/   \\_/ /      |  |           ,  ,/        \\  ./' __:..");
        System.out.println("    |  __ __  |       |  | .--.  ,         >  >   |-'   /     `");
        System.out.println("  ,/| /  '  \\ |       |  |     \\      ,           |    /");
        System.out.println(" /  |<--.__,->|       |  | .    `.        >  >    /   (");
        System.out.println("/_| \\  ^  /  \\     /  /   `.    >--            /^\\   |");
        System.out.println("      \\___/    \\   /  /      \\__'     \\   \\   \\/   \\  |");
        System.out.println("       `.   |/          ,  ,                  /`\\    \\  )");
        System.out.println("         \\  '  |/    ,       V    \\          /        `-\\");
        System.out.println("          `|/-.      \\ /   \\ /,---`\\            ");
        System.out.println("                /   `._____V_____V'");
        System.out.println("                           '     '");
    }


    public static void moveFish(int fishID, int pondID, int port){
        // call multicast client
        MulticastClient client = new MulticastClient(port);
        try {
            while (true) {
                client.sendMulticastMessage("move," + fishID + "," + pondID);
                // get response from server
                String response = MulticastServer.getReceivedMessages();
                Integer clock = readClockFile();
                writeToLog("move", fishID, pondID, clock);
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
                Integer clock = readClockFile();
                writeToLog("ack", fishID, pondID, clock, status);
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
        bubbleFish();
        System.out.println("Type 2)\n");
        shark();
        System.out.println("Type 3)\n");
        triangleFish();
        System.out.println("Type 4)\n");
        seahorse();
        System.out.println("Type 5)\n");
        pufflefish();
        Scanner input = new Scanner(System.in);
        System.out.print("Enter fish type: ");
        String userChoice = input.nextLine();

        switch(userChoice){
            case "1":
                System.out.println("You have selected bubble fish");
                bubbleFish();
                break;
            case "2":
                System.out.println("You have selected shark");
                shark();
                break;
            case "3":
                System.out.println("You have selected triangle fish");
                triangleFish();
                break;
            case "4":
                System.out.println("You have selected seahorse");
                seahorse();
                break;
            case "5":
                System.out.println("You have selected pufflefish");
                pufflefish();
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
                    bubbleFish();
                    break;
                case 2:
                    shark();
                    break;
                case 3:
                    triangleFish();
                    break;
                case 4:
                    seahorse();
                    break;
                case 5:
                    pufflefish();
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

