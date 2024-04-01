package services;
import java.io.File;
import java.lang.InterruptedException;



public class StartUp {
    private static Integer pondID;
    private static final Integer portNumber = 12345;

    public StartUp(Integer pondID) {
        StartUp.pondID = pondID;
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
                    Thread.sleep(300000);
                    Backup.backup();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        backupThread.start();
        Backup.recover();
    }

    public static Integer get_pond_ID() {
        return pondID;
    }

    public static Integer get_port_number(){
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
