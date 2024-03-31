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

}
