package fishes;

import services.Database;

import java.util.Random;

public class BubbleFish extends Fish {

        public BubbleFish(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.type = 1;
            this.id = id;
            this.imageLeft = "";
            this.imageRight = "";
        }

        @Override
        public void draw() {
            System.out.println("               O  o");
            System.out.println("          _\\_   o");
            System.out.println("       \\/  o\\ .");
            System.out.println("       //\\___=");
            System.out.println("          ''");
            System.out.println("\n");
        }

}
