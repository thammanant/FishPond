package services;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.lang.InterruptedException;



public class StartUp {
    private static Integer pondID;
    private static Integer portNumber = 12345;

    

    public static Integer clock = 0;
    public StartUp(Integer pondID) {
        this.pondID = pondID;
        createIfNotExists("fish.json");
        createIfNotExists("backup.txt");
        createIfNotExists("log.txt");
    }

    public void start() {
        System.out.println("Pond ID: " + pondID);
        System.out.println("Starting server" );
        Thread serverThread = new Thread(() -> MulticastServer.run_server(portNumber));
        serverThread.start();
        // new thread that will call backup every 5 minutes
        Thread backupThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                    Backup.backup();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        backupThread.start();
        Backup.recover();
    }

    

    public static void writeClockFile(int content) {
        try {
            Path filePath = Paths.get("clock.txt");
            // Write the updated content to the file
            Files.write(filePath, String.valueOf(content).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Integer getCurrentClock() {
        return clock;
    }

    public static Integer getPondID() {
        return pondID;
    }

    public static Integer getPortNumber() {
        return portNumber;
    }

    public static void createIfNotExists(String filename) {
        File file = new File(filename);
        // Check if the file doesn't exist
        if (!file.exists()) {
            try {
                // Create the file
                file.createNewFile();
                System.out.println(filename + " created.");
            } catch (Exception e) {
                // Handle file creation error (optional)
                e.printStackTrace();
            }
        } else {
            // Check if the file is empty (length is 0 bytes)
            if (file.length() == 0) {
                System.out.println(filename + " already exists but empty.");
            } else {
                System.out.println(filename + " already exists.");
            }
        }
    }
}
