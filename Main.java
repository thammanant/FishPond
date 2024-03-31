import services.Backup;
import services.Pond;
import services.StartUp;

public class Main {
    public static void main(String[] args) {
        Pond pond = new Pond(5);
        pond.start();
    }
}
