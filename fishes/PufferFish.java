package fishes;

import services.Database;

import java.util.Random;

public class PufferFish extends Fish{

            public PufferFish(int x, int y, int id) {
                this.x = x;
                this.y = y;
                this.type = 5;
                this.id = id;
                this.imageLeft = "";
                this.imageRight = "";
            }

            @Override
            public void draw() {
                System.out.println("     .");
                System.out.println("                          A       ;");
                System.out.println("                |   ,--,-/ \\---,-/|  ,");
                System.out.println("               _|\\,'. /|      /|   `/|-. ");
                System.out.println("           \\`.'    /|      ,            `;.");
                System.out.println("          ,\\   A     A         A   A _ /| `.;");
                System.out.println("        ,/  _              A       _  / _   /|  ;");
                System.out.println("       /\\  / \\   ,  ,           A  /    /     `/|");
                System.out.println("      /_| | _ \\         ,     ,             ,/  \\");
                System.out.println("     // | |/ `.\\  ,-      ,       ,   ,/ ,/      \\ /");
                System.out.println("     / @| |@  / /'   \\  \\      ,              >  /|    ,--.");
                System.out.println("    |\\_/   \\_/ /      |  |           ,  ,/        \\  ./' __:..");
                System.out.println("    |  __ __  |       |  | .--.  ,         >  >   |-'   /     `");
                System.out.println("  ,/| /  '  \\ |       |  |     \\      ,           |    /");
                System.out.println(" /  |<--.__,->|       |  | .    `.        >  >    /   (");
                System.out.println("/_| \\  ^  /  \\     /  /   `.    >--            /^\\   |");
                System.out.println("      \\___/    \\   /  /      \\__'     \\   \\   \\/   \\  |");
                System.out.println("       `.   |/          ,  ,                  /`\\    \\  )");
                System.out.println("         \\  '  |/    ,       V    \\          /        `-\\");
                System.out.println("          `|/-.      \\ /   \\ /,---`\\            ");
                System.out.println("                /   `._____V_____V'");
                System.out.println("                           '     '");
            }

            @Override
            public void create(int pondID) {
                Random random = new Random();
                while (Database.check_fish_id(this.id)) {
                    this.id = random.nextInt(100);
                }
                Database.add_fish_toDB(this.id, this.type, pondID);
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
