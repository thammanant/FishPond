package fishes;

import services.Database;

import java.util.Random;

public class TriangleFish extends Fish{

        public TriangleFish(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.type = 3;
            this.id = id;
            this.imageLeft = "";
            this.imageRight = "";
        }

        @Override
        public void draw() {
            System.out.println("     |\\    o");
            System.out.println("    |  \\    o");
            System.out.println("|\\ /    .\\ o");
            System.out.println("| |       (");
            System.out.println("|/ \\     /");
            System.out.println("    |  /");
            System.out.println("     |/");
            System.out.println("\n");
        }

}
