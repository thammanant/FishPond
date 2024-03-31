package fishes;


import services.Database;

import java.util.Random;

public class Shark extends Fish {
        public Shark(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.type = 2;
            this.id = id;
            this.imageLeft = "";
            this.imageRight = "";
        }

        @Override
        public void draw() {
            System.out.println("      .            ");
            System.out.println("\\_____\\)\\_____");
            System.out.println("/--v____ __`<       ");
            System.out.println("        )/           ");
            System.out.println("        '            ");
            System.out.println("\n");
        }

}
