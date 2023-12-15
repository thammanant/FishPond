package services;

import java.util.Scanner;
public class shutdown {
    private int voteCount = 0;
    private final int maxCount = 3; 
    public void loop(){
        Scanner input = new Scanner(System.in);
        while(true){
            System.out.println("Enter text(Type Q to quit): ");
            System.out.println("press V to vote for shutdown");
            String text = input.nextLine();
            
            

            if(text.equalsIgnoreCase("Q")){
                System.out.println("Quitting...");
                break;
            }
            if (text.equalsIgnoreCase("V")) {
                voteCount++;
                System.out.println("Vote count: " + voteCount);
                if (voteCount == maxCount) {
                    System.out.println("every client voted for shutdown");
                    System.out.println("Shutting down...");
                    break;
                }
                
            }
            

            System.out.println("You typed: " + text);
        }
        input.close();
    }
}

