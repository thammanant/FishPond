import services.eventHandler;
import services.startup;
import services.shutdown;
import services.fish;
public class main {
    public static void main(String[] args) {
        startup start = new startup(0);
        eventHandler.redoList();
        start.start();
    }
}
