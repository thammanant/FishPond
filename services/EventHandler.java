package services;

import java.util.Date;
import java.util.List;

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

public class EventHandler {

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
            String log = ManageLogFile.read_log();

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
                    new Date(),
                    javaVersion,
                    osName + " " + osVersion,
                    processor,
                    ramSize,
                    userAction,
                    log
            );
        }
    }

    public static class ExtendedSystemReport {

        public static void generate_system_report() {
            print_system_details();
            print_analysis_report();
        }

        public static void print_system_details() {
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

            System.out.println("\nSystem Report:");
            System.out.println("--------------");
            System.out.println("Java Version: " + runtimeMXBean.getSpecVersion());
            System.out.println("Java Vendor: " + runtimeMXBean.getVmVendor());
            System.out.println("Java Runtime: " + runtimeMXBean.getVmName());
            System.out.println("Operating System: " + osMXBean.getName() + " " + osMXBean.getVersion() + " " + osMXBean.getArch());
            System.out.println("Available Processors: " + osMXBean.getAvailableProcessors());
            System.out.println("Total Memory: " + format_bytes(memoryMXBean.getHeapMemoryUsage().getMax()));
            System.out.println("Used Memory: " + format_bytes(memoryMXBean.getHeapMemoryUsage().getUsed()));
            System.out.println("Thread Count: " + threadMXBean.getThreadCount());
            System.out.println("Loaded Classes: " + classLoadingMXBean.getLoadedClassCount());
        }

        public static void print_analysis_report() {
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
            System.out.println("Heap Memory: Max=" + format_bytes(heapMemoryUsage.getMax()) +
                    ", Used=" + format_bytes(heapMemoryUsage.getUsed()) +
                    ", Free=" + format_bytes(heapMemoryUsage.getCommitted() - heapMemoryUsage.getUsed()) +
                    ", Committed=" + format_bytes(heapMemoryUsage.getCommitted()));
            System.out.println("Non-Heap Memory: Max=" + format_bytes(nonHeapMemoryUsage.getMax()) +
                    ", Used=" + format_bytes(nonHeapMemoryUsage.getUsed()) +
                    ", Free=" + format_bytes(nonHeapMemoryUsage.getCommitted() - nonHeapMemoryUsage.getUsed()) +
                    ", Committed=" + format_bytes(nonHeapMemoryUsage.getCommitted()));
            System.out.print("\n");
        }

        private static String format_bytes(long bytes) {
            long kilobytes = bytes / 1024;
            long megabytes = kilobytes / 1024;
            return megabytes + " MB";
        }
    }

    public static class FishPondReport {

        public static void generate_fishpond_report() {
            // Read fish pond data from the database
            JSONArray fishList = Database.read_fish_fromDB();

            // Print the fish pond report header
            System.out.println("""
                                    
                    Fish Pond Report
                    =================== 
                    """);

            // Print information for each fish
            for (Object o : fishList) {
                //TODO change this after the d=fish class update
                JSONObject fish = (JSONObject) o;
                int fishType = Integer.parseInt((String) fish.get("fishType"));
                int fishid = Integer.parseInt((String) fish.get("fishid"));
                String fishTypeString = null;
                String fishGallery = null;

                switch (fishType) {
                    case 1:
                        fishTypeString = "bubbleFish";
                        break;
                    case 2:
                        fishTypeString = "shark";
                        break;
                    case 3:
                        fishTypeString = "triangleFish";
                        break;
                    case 4:
                        fishTypeString = "seahorse";
                        break;
                    case 5:
                        fishTypeString = "pufflefish";
                        break;
                }

                // Print fish details
                System.out.printf("""
                                Fish ID: %d
                                Fish Type: %s
                                ------------------
                                """,
                        fishid,
                        fishTypeString
                );

                System.out.printf("""
                        Fish Gallery:                    
                        """
                );

                switch (fishType) {
                    case 1:
                        FishAnimation.bubble_fish();
                        break;
                    case 2:
                        FishAnimation.shark();
                        break;
                    case 3:
                        FishAnimation.triangle_fish();
                        break;
                    case 4:
                        FishAnimation.seahorse();
                        break;
                    case 5:
                        FishAnimation.puffle_fish();
                        break;
                }

            }
            System.out.printf("""
                            Pond ID: %d
                            Port Number: %d
                            Multicast Subscription Group: %s
                            """,
                    StartUp.getPondID(),
                    StartUp.getPortNumber(),
                    MulticastServer.get_multi_address()
                    );
            System.out.println();
        }
    }
}


