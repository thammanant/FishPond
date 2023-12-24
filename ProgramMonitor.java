import java.io.IOException;

public class ProgramMonitor {
    public static void main(String[] args) {
        String programPath = "path/to/your/program.exe"; // Change this to the path of your program
        boolean isRunning = false;

        while (true) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", programPath);
                Process process = processBuilder.start();
                isRunning = true;

                // Monitor the external program
                while (isRunning) {
                    try {
                        int exitCode = process.waitFor();
                        isRunning = false;
                    } catch (InterruptedException e) {
                        // Handle interruption
                    }
                }

                // If the program exited/crashed, restart it
                System.out.println("The program has crashed. Restarting...");
            } catch (IOException e) {
                // Handle IOException
                e.printStackTrace();
            }
        }
    }
}
