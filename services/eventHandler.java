package services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sun.misc.Signal;
import sun.misc.SignalHandler;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ThreadMXBean;
import java.lang.management.ClassLoadingMXBean;
import java.util.List;

public class eventHandler {
    public static boolean writeToLog(String command, Integer fishID, Integer pondID, Integer clock) {
        return writeToLog(command, fishID, pondID, clock, null); // Call the main function with default msg
    }

    public static boolean writeToLog(String command, Integer fishID, Integer pondID, Integer clock, String msg) {
        try {
            FileWriter fileWriter = new FileWriter("log.txt", true); // true for appending to the file
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Construct the log message
            String logMessage = String.format("%s, %d, %d, %d, %s\n", command, fishID, pondID, clock, msg);

            bufferedWriter.write(logMessage);
            bufferedWriter.close();

            return true; // Successfully wrote to the log file
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to write to the log file
        }
    }

    public static boolean redoList() {
        try {
            String filePath = "log.txt";
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Get the number of lines in the file
            long lineCount = bufferedReader.lines().count();

            int currentLine = 1;
            Integer redo = (int) lineCount;

            // Perform binary search on clock values
            redo = binarySearch(1, (int) lineCount - 1, redo);

            // Reset bufferedReader to the start of the file
            bufferedReader.close();
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);

            // Skip lines till the redo starting point
            for (int i = 0; i < redo; i++) {
                bufferedReader.readLine();
            }

            // Redo tasks from startLine to the end of the file
            String line;
            while ((line = bufferedReader.readLine()) != null) {

                String[] fields = line.split(", ");

                String command = fields[0];
                Integer fishID = Integer.parseInt(fields[1]);
                Integer pondID = Integer.parseInt(fields[2]);
                String msg = fields[4];

                if ("move".equals(command)) {
                    fish.moveFish(fishID, pondID, 12345);
                } else if ("ack".equals(command) && "acpt".equals(msg)) {
                    fish.ackFish(fishID, pondID, 12345, "acpt");
                }

                currentLine++;
            }

            bufferedReader.close();
            return true; // Successfully processed the log file
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to read the log file
        }
    }


    private static Integer binarySearch(Integer start, Integer end, Integer redo) throws IOException {
        while (start <= end) {
            Integer mid = start + (end - start) / 2;

            // Read the line at the mid point
            String line = readLineAt(mid);

            // Process the log entry
            if (check(line)) {
                // Update the redo position
                redo = mid;

                // Search in the right half for the least line number not done
                end = mid - 1;
            } else {
                // If the current entry is done, search in the left half
                start = mid + 1;
            }
        }
        return redo;
    }


    private static String readLineAt(long lineNumber) {
        String filePath = "log.txt";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            long lineCount = 0;

            while ((line = bufferedReader.readLine()) != null) {
                lineCount++;
                if (lineCount == lineNumber) {
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean check(String line) {
        if (line == null) {
            return false; // Exit or handle end of file
        }

        String[] fields = line.split(", ");

        String command = fields[0];
        Integer fishID = Integer.parseInt(fields[1]);
        String msg = fields[3];
        if ("move".equals(command)) {
            return !isFishInPond(fishID);
        } else if ("ack".equals(command) && ("acpt".equals(msg))) {
            return isFishInPond(fishID);
        }
        return false;
    }

    private static boolean isFishInPond(int fishID) {
        // Your logic to check whether the fish is still in the pond
        JSONArray fishList = database.readFishFromDB();
        for (Object o : fishList) {
            JSONObject fish = (JSONObject) o;
            int storedFishID = Integer.parseInt((String) fish.get("fishid"));
            if (storedFishID == fishID) {
                return true;
            }
        }
        return false;
    }

    public static class ExSignalHandler implements SignalHandler {
        @Override
        public void handle(Signal signal) {
            // Get system properties
            String osName = System.getProperty("os.name");
            String osVersion = System.getProperty("os.version");
            String javaVersion = System.getProperty("java.version");

            String processor = System.getProperty("os.arch");
            String ramSize = String.valueOf(Runtime.getRuntime().totalMemory());

            String userAction = "for testing";  // Replace with actual user action
            // read log.txt
            Path path = Paths.get("log.txt");
            List<String> lines = null;
            try {
                lines = Files.readAllLines(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Concatenate all lines into a single string with \n between each line
            String concatenatedString = lines.stream().collect(Collectors.joining("\n"));


            // Format and print the crash report
            System.out.printf("""
                            Crash Report
                            =============
                            Date: %s
                            Software Version: %s
                            Operating System: %s
                                                       
                            Environment:
                            -------------
                            Processor: %s
                            RAM: %s
                            ...
                                                
                            User Actions:
                            -------------
                            User was performing %s when the crash occurred.
                                                
                            Logs and Diagnostics:
                            ---------------------
                            %s
                            """,
                    new java.util.Date(),
                    javaVersion,
                    osName + " " + osVersion,
                    processor,
                    ramSize,
                    userAction,
                    concatenatedString
            );
        }
    }

    public class ExtendedSystemReport {

        public static void main(String[] args) {
            printSystemDetails();
            printAnalysisReport();
        }

        public static void printSystemDetails() {
            // Get the runtime bean
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

            // Get the operating system bean
            OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();

            // Get the memory bean
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

            // Get the thread bean
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

            // Get the class loading bean
            ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();

            System.out.println("System Report:");
            System.out.println("--------------");
            System.out.println("Java Version: " + runtimeMXBean.getSpecVersion());
            System.out.println("Java Vendor: " + runtimeMXBean.getVmVendor());
            System.out.println("Java Runtime: " + runtimeMXBean.getVmName());
            System.out.println("Operating System: " + osMXBean.getName() + " " + osMXBean.getVersion() + " " + osMXBean.getArch());
            System.out.println("Available Processors: " + osMXBean.getAvailableProcessors());
            System.out.println("Total Memory: " + formatBytes(memoryMXBean.getHeapMemoryUsage().getMax()));
            System.out.println("Used Memory: " + formatBytes(memoryMXBean.getHeapMemoryUsage().getUsed()));
            System.out.println("Thread Count: " + threadMXBean.getThreadCount());
            System.out.println("Loaded Classes: " + classLoadingMXBean.getLoadedClassCount());
        }

        public static void printAnalysisReport() {
            // Get the garbage collector beans
            List<GarbageCollectorMXBean> garbageCollectors = ManagementFactory.getGarbageCollectorMXBeans();

            // Get the memory bean for more detailed memory usage statistics
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

            System.out.println("\nAnalysis Report:");
            System.out.println("----------------");

            System.out.println("Garbage Collectors:");
            for (GarbageCollectorMXBean gcBean : garbageCollectors) {
                System.out.println(gcBean.getName() + " - Collections: " + gcBean.getCollectionCount() +
                        ", Time spent: " + gcBean.getCollectionTime() + " ms");
            }

            System.out.println("\nMemory Usage:");
            System.out.println("Heap Memory: Max=" + formatBytes(heapMemoryUsage.getMax()) +
                    ", Used=" + formatBytes(heapMemoryUsage.getUsed()) +
                    ", Free=" + formatBytes(heapMemoryUsage.getCommitted() - heapMemoryUsage.getUsed()) +
                    ", Committed=" + formatBytes(heapMemoryUsage.getCommitted()));
            System.out.println("Non-Heap Memory: Max=" + formatBytes(nonHeapMemoryUsage.getMax()) +
                    ", Used=" + formatBytes(nonHeapMemoryUsage.getUsed()) +
                    ", Free=" + formatBytes(nonHeapMemoryUsage.getCommitted() - nonHeapMemoryUsage.getUsed()) +
                    ", Committed=" + formatBytes(nonHeapMemoryUsage.getCommitted()));
            System.out.print("\n");
        }

        private static String formatBytes(long bytes) {
            long kilobytes = bytes / 1024;
            long megabytes = kilobytes / 1024;
            return megabytes + " MB";
        }
    }


}


