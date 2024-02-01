package fishes;

import services.Database;

import java.util.Random;

public class Seahorse extends Fish{

        public Seahorse(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.type = 3;
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
