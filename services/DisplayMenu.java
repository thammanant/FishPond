package services;

import java.util.Scanner;

import fishes.*;

public class DisplayMenu {

    private final int pondID;

    public DisplayMenu(int pondID) {
        this.pondID = pondID;
    }


    public static void display_main_menu() {
        System.out.println("1. Add fish");
        System.out.println("2. Remove fish");
        System.out.println("3. Draw fish pond");
        System.out.println("4. Move fish");
        System.out.println("5. Shutdown");
        System.out.println("6. System report");
        System.out.println("7. Pond report");

    }

    public void add_fish_menu() {
        System.out.print("Please select fish type: \n");
        System.out.println("Type 1)\n");
        FishAnimation.bubble_fish();
        System.out.println("Type 2)\n");
        FishAnimation.shark();
        System.out.println("Type 3)\n");
        FishAnimation.seahorse();
        System.out.println("Type 4)\n");
        FishAnimation.triangle_fish();
        System.out.println("Type 5)\n");
        FishAnimation.puffle_fish();
        Scanner input = new Scanner(System.in);
        System.out.print("Enter fish type: ");
        String userChoice = input.nextLine();

        switch(userChoice){
            case "1":
                System.out.println("You have selected bubble fish");
                BubbleFish bubbleFish = new BubbleFish(0, 0, 0);
                FishAnimation.bubble_fish();
                bubbleFish.create(this.pondID);
                break;
            case "2":
                System.out.println("You have selected shark");
                Shark shark = new Shark(0, 0, 0);
                FishAnimation.shark();
                shark.create(this.pondID);
                break;
            case "3":
                System.out.println("You have selected triangle fish");
                TriangleFish triangleFish = new TriangleFish(0, 0, 0);
                FishAnimation.triangle_fish();
                triangleFish.create(this.pondID);
                break;
            case "4":
                System.out.println("You have selected seahorse");
                Seahorse seahorse = new Seahorse(0, 0, 0);
                FishAnimation.seahorse();
                seahorse.create(this.pondID);
                break;
            case "5":
                System.out.println("You have selected pufflefish");
                PufferFish pufferFish = new PufferFish(0, 0, 0);
                FishAnimation.puffle_fish();
                pufferFish.create(this.pondID);
                break;
            case "exit":
                System.out.println("Quitting...");
                break;
            default:
                System.out.println("Invalid input, please type number between 1-5");
        }

    }


}
