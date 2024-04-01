package fishes;

import services.Clock;
import services.Database;
import services.ManageLogFile;

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
        ManageLogFile.write_to_log("add", this.id, this.type, pondID, Clock.get_current_clock());
        Database.add_fish_toDB(this.id, this.type,pondID);
    }

    public void set_coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set_fish_id(int id) {
        this.id = id;
    }

    public void set_fish_image(String imageLeft, String imageRight) {
        this.imageLeft = imageLeft;
        this.imageRight = imageRight;
    }

    public int[] get_coordinates() {
        return new int[]{this.x, this.y};
    }

    public int get_fish_id() {
        return this.id;
    }
    public int get_fish_type() {
        return this.type;

    }
    public String[] get_fish_image() {
        return new String[]{this.imageLeft, this.imageRight};
    }

    
}
