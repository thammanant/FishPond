package services;
import java.util.Scanner;
public class shutdown {
    public void loop(){
        Scanner input = new Scanner(System.in);
        while(true){
            System.out.println("Enter text(Type Q to quit): ");
            String text = input.nextLine();

            if(text.equalsIgnoreCase("Q")){
                System.out.println("Quitting...");
                break;
            }

            System.out.println("You typed: " + text);
        }
        input.close();
    }
}
