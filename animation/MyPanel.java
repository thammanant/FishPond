package animation;

import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import services.Database;

public class MyPanel extends JPanel implements ActionListener {
    
    final int Panel_Width = 1024;
    final int Panel_Height = 768;
    Image bubble_fish_right;
    Image bubble_fish_left;
    Image shark_right;
    Image shark_left;
    Image triangle_fish_right;
    Image triangle_fish_left;
    Image seahorse_right;
    Image seahorse_left;
    Image pufflefish_right;
    Image pufflefish_left;
    Image background;
    Timer timer;
    int xVelocity = 10;
    int yVelocity = 1;
    // random x value in range of 0 to 1024
    int x = 0;
    int y = 0;
    int count = 0;
    int fishPosition[][];

    MyPanel(){
        this.setPreferredSize(new Dimension(Panel_Width, Panel_Height));
        this.setBackground(Color.black);
        bubble_fish_right = new ImageIcon("FishImage\\bubbleFish_right.png").getImage();
        shark_right = new ImageIcon("FishImage\\shark_right.png").getImage();
        triangle_fish_right = new ImageIcon("FishImage\\Triangle_fish_right.png").getImage();
        seahorse_right = new ImageIcon("FishImage\\sea_horse_right.png").getImage();
        pufflefish_right = new ImageIcon("FishImage\\Puffer_fish_right.png").getImage();

        background = new ImageIcon("animation\\aqua.jpg").getImage();
        timer = new Timer(10,this);
        timer.start();
        JSONArray fishlist = Database.read_fish_fromDB();
        fishPosition = new int[fishlist.size()][3];
        Random random = new Random();
        for (int i = 0; i < fishlist.size(); i++) {
            fishPosition[i][0] = random.nextInt(1024); // Random pos x within 1024
            fishPosition[i][1] = random.nextInt(750);  // Random pos y within 768
            fishPosition[i][2] = 0; // Set type to 0
        }
        
    }

    public void paint(Graphics g){
        super.paint(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(background,0,0,null);
        JSONArray fishlist = Database.read_fish_fromDB();
        int i = 0;
        for(Object o : fishlist){
            JSONObject fish = (JSONObject) o;
            int fishType = Integer.parseInt((String) fish.get("fishType"));
            fishPosition[i][2] = fishType;
            switch (fishType) {
                case 1:
                    g2D.drawImage(bubble_fish_right,fishPosition[i][0],fishPosition[i][1],null);

                    break;
                case 2:
                    g2D.drawImage(shark_right,fishPosition[i][0],fishPosition[i][1],null);

                    break;
                case 3:
                    g2D.drawImage(triangle_fish_right,fishPosition[i][0],fishPosition[i][1],null);

                    break;
                case 4:
                    g2D.drawImage(seahorse_right,fishPosition[i][0],fishPosition[i][1],null);

                    break;
                case 5:
                    g2D.drawImage(pufflefish_right,fishPosition[i][0],fishPosition[i][1],null);

                    break;
            
                default:
                    break;
                
                
            }
            
             i++;
            
        } 
        
    }

   

    @Override
    public void actionPerformed(ActionEvent e) {
        if(x > Panel_Width - 90 || x < 0){
            xVelocity = xVelocity * -1;
        }

        for(int i = 0; i < fishPosition.length; i++){
            if(fishPosition[i][0] > Panel_Width - 90){
                xVelocity = xVelocity * -1;
                bubble_fish_right = new ImageIcon("FishImage\\bubbleFish_left.png").getImage();
                shark_right = new ImageIcon("FishImage\\shark_left.png").getImage();
                triangle_fish_right = new ImageIcon("FishImage\\Triangle_fish_left.png").getImage();
                seahorse_right = new ImageIcon("FishImage\\sea_horse_left.png").getImage();
                pufflefish_right = new ImageIcon("FishImage\\Puffer_fish_left.png").getImage();
                
            }
            else if (fishPosition[i][0]<0){
                xVelocity = xVelocity * -1;
                bubble_fish_right = new ImageIcon("FishImage\\bubbleFish_right.png").getImage();
                shark_right = new ImageIcon("FishImage\\shark_right.png").getImage();
                triangle_fish_right = new ImageIcon("FishImage\\Triangle_fish_right.png").getImage();
                seahorse_right = new ImageIcon("FishImage\\sea_horse_right.png").getImage();
                pufflefish_right = new ImageIcon("FishImage\\Puffer_fish_right.png").getImage();
            }
            else if(fishPosition[i][1] > Panel_Height || fishPosition[i][1] < 30){
                yVelocity = yVelocity * -1;
            }
            fishPosition[i][0] += xVelocity;
            fishPosition[i][1] += yVelocity;
        }
        repaint();
    }
}