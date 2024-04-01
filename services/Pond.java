package services;

import org.json.simple.JSONArray;
import sun.misc.Signal;
import animation.MyFrame;

import java.util.Scanner;

public class Pond {

    private Integer pondID;

    private Integer portNumber = 12345;
    public Pond(Integer pondID) {
        this.pondID = pondID;
    }

    public void start(){
        StartUp startUp = new StartUp(this.pondID);
        startUp.start();
        Clock.start_clock();
        // new thread that will sent signal to backup every 1 second
        Thread signalThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    Backup.signal(Clock.get_current_clock());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        signalThread.start();
        Scanner ansForRequest = new Scanner(System.in);
        Scanner input = new Scanner(System.in);
        Scanner removeId = new Scanner(System.in);
        Scanner FishIdFormove = new Scanner(System.in);
        Scanner PondIdFormove = new Scanner(System.in);
        while (true) {
            // handle crash
            Signal.handle(new Signal("INT"), new EventHandler.ExSignalHandler());
            Signal.handle(new Signal("TERM"), new EventHandler.ExSignalHandler());
            // Signal.handle(new Signal("HUP"), new EventHandler.ExSignalHandler());
            Communicate communicate = new Communicate(this.pondID, 12345);
            communicate.handle_received_messages(ansForRequest);
            if (!communicate.messageReceived) {
                DisplayMenu.display_main_menu();
            }

            try {
                Integer userChoice = input.nextInt();
                if (userChoice == 1) {
                    DisplayMenu displayMenu = new DisplayMenu(this.pondID);
                    displayMenu.add_fish_menu();
                } else if (userChoice == 2) {
                    System.out.println("Enter ID of fish to remove: ");
                    Integer idForRemove = removeId.nextInt();
                    ManageLogFile.write_to_log("remove", idForRemove, Clock.get_current_clock());
                    Remove.remove_fish_by_id(idForRemove);
                } else if (userChoice == 3) {
                    new MyFrame();
                } else if (userChoice == 4) {
                    System.out.println("Enter ID of fish to move: ");
                    Integer idForMove = FishIdFormove.nextInt();
                    System.out.println("Enter ID of pond to move to: ");
                    Integer pondForMove = PondIdFormove.nextInt();
                    Integer[] fishInfo = Communicate.get_fish_info(idForMove);
                    Communicate.move(idForMove,fishInfo[0],fishInfo[1], pondForMove,portNumber);
                } else if (userChoice == 5) {
                    Shutdown.display_shutdown_menu();
                }
                else if (userChoice == 6) {
                    EventHandler.ExtendedSystemReport.generate_system_report();
                }
                else if (userChoice == 7) {
                    EventHandler.FishPondReport.generate_fishpond_report();
                }
                else {
                    System.out.println("Invalid input");
                    System.out.println();
                }
            } catch (Exception e) {
                System.out.println("Invalid input");
                input.nextLine(); // Clear the buffer
            } finally {
                communicate.set_message_received(false);
            }
        }
    }

    public Integer get_pond_ID() {
        return this.pondID;
    }

    public void set_pond_ID(Integer pondID) {
        this.pondID = pondID;
    }

    public static JSONArray get_all_fish() {
        return Database.read_fish_fromDB();
    }






}
