package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyPanel extends JPanel implements ActionListener {
    
    final int Panel_Width = 1024;
    final int Panel_Height = 768;
    Image fish;
    Image background;
    Timer timer;
    int xVelocity = 5;
    int yVelocity = 1;
    int x = 1;
    int y = 1;


    MyPanel(){
        this.setPreferredSize(new Dimension(Panel_Width, Panel_Height));
        this.setBackground(Color.black);
        fish = new ImageIcon("C:\\Users\\brigh\\OneDrive\\Desktop\\year3 sem2\\DC\\FishPond\\test\\testFish.png").getImage();
        background = new ImageIcon("C:\\Users\\brigh\\OneDrive\\Desktop\\year3 sem2\\DC\\FishPond\\test\\aqua.jpg").getImage();
        timer = new Timer(10,this);
        timer.start();
    }

    public void paint(Graphics g){
        super.paint(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(background,0,0,null); 
        g2D.drawImage(fish,x,y,null); 
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(x>=Panel_Width-fish.getWidth(null)){
            xVelocity = xVelocity * -1;
            fish = new ImageIcon("C:\\Users\\brigh\\OneDrive\\Desktop\\year3 sem2\\DC\\FishPond\\test\\testFishLeft.png").getImage();
        } else if (x<=0){
            xVelocity = xVelocity * -1;
            fish = new ImageIcon("C:\\Users\\brigh\\OneDrive\\Desktop\\year3 sem2\\DC\\FishPond\\test\\testFish.png").getImage();
        }

        x += xVelocity;
        repaint();
    }
}