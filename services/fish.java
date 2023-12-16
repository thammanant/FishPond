package services;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

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


    public void moveFish(int fishID, int pondID, int port){
        // call multicast client
        MulticastClient client = new MulticastClient(port);
        try {
            while (true) {
                client.sendMulticastMessage("move," + fishID + "," + pondID);
                // get response from server
                String response = MulticastServer.getReceivedMessages();
                if (response.equals("ack," + fishID + "," + pondID + "acpt")){ // if accepted
                    //TODO
                    // remove fish from DB
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

    public void ackFish(int fishID, int pondID, int port, String status){
        // status must only be acpt for accept or rej for reject
        // call multicast client
        MulticastClient client = new MulticastClient(port);
        try {
            client.sendMulticastMessage("ack," + fishID + "," + pondID + "," + status);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (status.equals("acpt")){
            //TODO
            // if fish already in DB, do nothing
            // else add fish to pond to DB
        }
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
                break;
        }

        fish newFish = createFish(Integer.parseInt(userChoice));


        // startup mainmenu = new startup(0);
        // mainmenu.start();


    }
}

