package services;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Database {
    private static final String path = "fish.json";

    public static void add_fish_toDB(int fishid, int fishType, int genesisPondID) {
        // if the fish already exists in the database, return
        if (check_fish_id(fishid)) {
            return;
        }
        JSONArray fishList = read_fish_fromDB();
        JSONObject obj = new JSONObject();
        obj.put("fishid", String.valueOf(fishid));
        obj.put("fishType", String.valueOf(fishType));
        obj.put("genesisPondID", String.valueOf(genesisPondID));
        fishList.add(obj);

        try {
            FileWriter file = new FileWriter(path);
            file.write(fishList.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("JSON file created: " + obj);
    }

    public static void add_fish_from_other_pond(int fishID, int fishType, int genesisPondID){
        ManageLogFile.write_to_log("add", fishID, fishType, genesisPondID, Clock.get_current_clock());
        Database.add_fish_toDB(fishID, fishType, genesisPondID);
    }


    public static JSONArray read_fish_fromDB() {
        try (FileReader reader = new FileReader(path)) {
            JSONParser jsonParser = new JSONParser();
            return (JSONArray) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            // Return an empty array in case of an error
            return new JSONArray();
        }
    }

    public static void remove_fish_fromDB(int fishid) {
        JSONArray fishList = read_fish_fromDB();
        for (int i = 0; i < fishList.size(); i++) {
            JSONObject fish = (JSONObject) fishList.get(i);
            if (fish.get("fishid").equals(String.valueOf(fishid))) {
                fishList.remove(i);
                break;
            }
        }

        try {
            FileWriter file = new FileWriter(path);
            file.write(fishList.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean check_fish_id(int fishid) {
        JSONArray fishList = read_fish_fromDB();
        for (Object o : fishList) {
            JSONObject fish = (JSONObject) o;
            if (fish.get("fishid").equals(String.valueOf(fishid))) {
                return true;
            }
        }
        return false;
    }

    //get fish info by id as array
    public static Integer[] get_fish_info(int fishid) {
        JSONArray fishList = read_fish_fromDB();
        for (Object o : fishList) {
            JSONObject fish = (JSONObject) o;
            if (fish.get("fishid").equals(String.valueOf(fishid))) {
                return new Integer[]{Integer.parseInt((String) fish.get("fishType")), Integer.parseInt((String) fish.get("genesisPondID"))};
            }
        }
        return new Integer[]{};
    }

}
