package fishes;

import services.Database;

import java.util.Random;

public class Seahorse extends Fish{

        public Seahorse(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.type = 4;
            this.id = id;
            this.imageLeft = "";
            this.imageRight = "";
        }

        @Override
        public void draw() {
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

}
