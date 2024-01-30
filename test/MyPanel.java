package test;

import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.event.*;
import services.database;

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
    int fishPosition[];

    MyPanel(){
        this.setPreferredSize(new Dimension(Panel_Width, Panel_Height));
        this.setBackground(Color.black);
        bubble_fish_right = new ImageIcon("FishImage\\bubbleFish_right.png").getImage();
        bubble_fish_left = new ImageIcon("FishImage\\bubbleFish_left.png").getImage();
        shark_right = new ImageIcon("FishImage\\shark_right.png").getImage();
        shark_left = new ImageIcon("FishImage\\shark_left.png").getImage();
        triangle_fish_right = new ImageIcon("FishImage\\Triangle_fish_right.png").getImage();
        triangle_fish_left = new ImageIcon("FishImage\\Triangle_fish_left.png").getImage();
        seahorse_right = new ImageIcon("FishImage\\sea_horse_right.png").getImage();
        seahorse_left = new ImageIcon("FishImage\\sea_horse_left.png").getImage();
        pufflefish_right = new ImageIcon("FishImage\\Puffer_fish_right.png").getImage();
        pufflefish_left = new ImageIcon("FishImage\\Puffer_fish_left.png").getImage();

        background = new ImageIcon("test\\aqua.jpg").getImage();
        timer = new Timer(10,this);
        timer.start();
        JSONArray fishlist = database.readFishFromDB();
        fishPosition = new int[fishlist.size()];
        // random y value in range of 0 to 768 in array
        for(int i = 0; i < fishlist.size(); i++){
            fishPosition[i] = (int) (Math.random() * 768);
        }
        
    }

    public void paint(Graphics g){
        super.paint(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(background,0,0,null);
        JSONArray fishlist = database.readFishFromDB();
        int i = 0;
        for(Object o : fishlist){
            JSONObject fish = (JSONObject) o;
            int fishType = Integer.parseInt((String) fish.get("fishType"));
            switch (fishType) {
                case 1:
                    g2D.drawImage(bubble_fish_right,x,fishPosition[i],null);
                    System.out.println(x);
                    break;
                case 2:
                    g2D.drawImage(shark_right,x,fishPosition[i],null);
                    System.out.println(x);
                    break;
                case 3:
                    g2D.drawImage(triangle_fish_right,x,fishPosition[i],null);
                    System.out.println(x);
                    break;
                case 4:
                    g2D.drawImage(seahorse_right,x,fishPosition[i],null);
                    System.out.println(x);
                    break;
                case 5:
                    g2D.drawImage(pufflefish_right,x,fishPosition[i],null);
                    System.out.println(x);
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

        x += xVelocity;
        repaint();
    }
}