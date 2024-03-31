package services;

import java.io.*;

public class Backup {
    public static final String backupFile = "backup.txt";

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
            if(new File(backupFile).exists())
            {
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
                String[] backupParts = backup.toString().split(",");
                // write from the first continuously until the second last element
                for (int i = 0; i < backupParts.length - 1; i++) {
                    bufferedWriter.write(backupParts[i]);
                }
                bufferedReader.close();

                // redo the tasks in the log file
                ManageLogFile.redo_task(Integer.parseInt(backupParts[backupParts.length - 1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

