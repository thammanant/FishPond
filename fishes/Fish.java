package fishes;

import services.Database;

import java.util.Random;


public abstract class Fish{

    int x;
    int y;
    int type;
    int id;

    String imageLeft;
    String imageRight;


    public void create(int pondID){
        Random random = new Random();
        this.id = random.nextInt(100);
        while (Database.check_fish_id(this.id)) {
            this.id = random.nextInt(100);
        }
        Database.add_fish_toDB(this.id, this.type,pondID);
    }

}
