package services;

import java.util.Scanner;
public class shutdown {
    
    public static void shutdownMenu(){
        Scanner input = new Scanner(System.in);
        while(true){
            System.out.println("press Q to quit");
            System.out.println("press B to go back to main menu");
            System.out.print("Please enter your choice: ");
            String text = input.nextLine();
            
            

            if(text.equalsIgnoreCase("Q")){
                System.out.println("Quitting...");
                System.exit(0);
                break;
            }
            
            if (text.equalsIgnoreCase("B")) {
                System.out.println("Going back to main menu...");
                break;
            }
            

            System.out.println("You typed: " + text);
        }
        
    }
}

