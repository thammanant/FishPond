import services.startup;
import services.shutdown;
public class main {
    public static void main(String[] args) {
        shutdown terminate = new shutdown();
        terminate.loop();
       
    }
}
