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

        @Override
        public void create() {
            Random random = new Random();
            while (Database.check_fish_id(this.id)) {
                this.id = random.nextInt(10);
            }
            Database.add_fish_toDB(this.id, this.type);
        }

        @Override
        public void set_coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void set_fish_id(int id) {
            this.id = id;

        }

        @Override
        public void set_fish_image(String imageLeft, String imageRight) {
            this.imageLeft = imageLeft;
            this.imageRight = imageRight;
        }

        @Override
        public int[] get_coordinates() {
            return new int[]{this.x, this.y};
        }

        @Override
        public int get_fish_id() {
            return this.id;

        }

        @Override
        public int get_fish_type() {
            return this.type;

        }

        @Override
        public String[] get_fish_image() {
            return new String[]{this.imageLeft, this.imageRight};

        }

}
