package services;

import fishes.*;

public class DisplayMenu {

    public void display_main_menu() {
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
        BubbleFish bubbleFish = new BubbleFish(0, 0, 0);
        bubbleFish.draw();
        System.out.println("Type 2)\n");
        Shark shark = new Shark(0, 0, 0);
        shark.draw();
        System.out.println("Type 3)\n");
        Seahorse seahorse = new Seahorse(0, 0, 0);
        seahorse.draw();
        System.out.println("Type 4)\n");
        TriangleFish triangleFish = new TriangleFish(0, 0, 0);
        triangleFish.draw();
        System.out.println("Type 5)\n");
        PufferFish pufferFish = new PufferFish(0, 0, 0);
        pufferFish.draw();
    }


}
