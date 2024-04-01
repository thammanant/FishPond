package services;

import java.awt.*;
import java.io.*;

public class Backup {
    private static final String backupFile = "backup.txt";
    private static Integer clock = 0;

    // This method is called every 1 second to make sure that the main program is still running
    public static void signal(int currentClock) {
        if(currentClock > clock){
            System.out.println("clock from backup: "+clock);
            System.out.println("clock from main: "+currentClock);
            System.out.println("System is still running");
        } else {
            System.out.println("System is not running");
            System.out.println("Restarting the system");
            clock = 0;
            Pond pond = new Pond(5);
            pond.start();
        }
        clock+=1;
    }

    public static void backup() {
        try {
            FileWriter fileWriter = new FileWriter(backupFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Construct the log message
            String backupMessage = String.format("%s, %d", Database.read_fish_fromDB(), Clock.get_current_clock());

            bufferedWriter.write(backupMessage);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void recover() {
        try {
            // open the backup file
            File file = new File(backupFile);
            // check if the backup file is empty
            if(file.length() != 0)
            {
                System.out.println("Recovering from backup file");
                // write the backup file to the fish database(fish.json)
                FileWriter fileWriter = new FileWriter("fish.json");
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                // read the backup file and get only the fish database
                FileReader fileReader = new FileReader(backupFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                StringBuilder backup = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    backup.append(line).append("\n");
                }
                String[] backupParts = backup.toString().split("],");
                // add ] to the first element
                backupParts[0] += "]";
                // write from the first continuously until the second last element
                for (int i = 0; i < backupParts.length - 1; i++) {
                    System.out.println(backupParts[i]);
                    bufferedWriter.write(backupParts[i]);
                }
                bufferedReader.close();
                bufferedWriter.close();

                // get the last element
                String lastPart = backupParts[backupParts.length - 1];
                ManageLogFile.redo_task(Integer.parseInt(lastPart.strip()));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

