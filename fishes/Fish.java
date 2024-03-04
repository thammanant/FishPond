package fishes;

import services.Database;


public abstract class Fish {

    int x;
    int y;
    int type;
    int id;

    String imageLeft;
    String imageRight;


    abstract void draw();

    abstract void create(int pondID);

    abstract void set_coordinates(int x, int y);

    abstract void set_fish_id(int id);

    abstract void set_fish_image(String imageLeft, String imageRight);

    abstract int[] get_coordinates();

    abstract int get_fish_id();

    abstract int get_fish_type();

    abstract String[] get_fish_image();

    
}
