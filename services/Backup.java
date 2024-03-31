package services;

public class Backup {
    /*
       1. have a backup file, that stores database information (in here)
       2. have backup function(in here) that writes to the backup file and deletes the log file(log class)
       3. everytime the server starts, check if the backup file exists, if it does, read from it(startup class)
       4. monitor the main program, if it crashes, restart it and read from the backup file (in here, run parallel to the main program)
       5.
     */
}
