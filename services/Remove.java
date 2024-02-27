package services;

public class Remove {
    public static void remove_fish_by_id(int id){
        Database.remove_fish_fromDB(id);
        System.out.println("Fish id: " + id + " removed");
    }
}
