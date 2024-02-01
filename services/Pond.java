package services;

import org.json.simple.JSONArray;

public class Pond {

    private Integer pondID;

    public Pond(Integer pondID) {
        this.pondID = pondID;
    }

    public void start(){
        StartUp startUp = new StartUp(this.pondID);
        startUp.start();
        Clock.start_clock();

    }

    public Integer get_pond_ID() {
        return this.pondID;
    }

    public void set_pond_ID(Integer pondID) {
        this.pondID = pondID;
    }

    public static JSONArray get_all_fish() {
        return Database.read_fish_fromDB();
    }






}
