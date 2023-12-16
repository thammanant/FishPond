package services;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class database {
    private static final String path = "/Users/chananonkanunghet/Desktop/Work/Distributed Computing/FishPond/fish.json";


    public static void addFishToDB(int fishid, int fishType) {
        JSONArray fishList = readFishFromDB();
        JSONObject obj = new JSONObject();
        obj.put("fishid", String.valueOf(fishid));
        obj.put("fishType", String.valueOf(fishType));
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

    public static JSONArray readFishFromDB() {
        try (FileReader reader = new FileReader(path)) {
            JSONParser jsonParser = new JSONParser();
            return (JSONArray) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            // Return an empty array in case of an error
            return new JSONArray();
        }
    }

    public static void removeFishFromDB(int fishid) {
        JSONArray fishList = readFishFromDB();
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
}
