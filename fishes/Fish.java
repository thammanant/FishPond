package fishes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import services.Database;
import services.ManageLogFile;
import services.MulticastClient;
import services.MulticastServer;

import java.io.IOException;
import java.util.Objects;

import static services.ManageLogFile.write_to_log;
import static services.StartUp.getCurrentClock;

public abstract class Fish {

    int x;
    int y;
    int type;
    int id;

    String imageLeft;
    String imageRight;


    abstract void draw();

    abstract void create();

    abstract void set_coordinates(int x, int y);

    abstract void set_fish_id(int id);

    abstract void set_fish_image(String imageLeft, String imageRight);

    abstract int[] get_coordinates();

    abstract int get_fish_id();

    abstract int get_fish_type();

    abstract String[] get_fish_image();

    void remove(int id){
        Database.remove_fish_fromDB(id);
        System.out.println("Fish id: " + id + " removed");
    }
}
