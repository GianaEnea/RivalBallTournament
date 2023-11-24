package RivalBallTournament.client;

import java.util.ArrayList;

import javax.swing.*;

import RivalBallTournament.server.Brick;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


//work in progress
public class Game extends JFrame{
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    String[] oggetiDaStampare; 
    private BufferedImage buffer;
    private Graphics bufferGraphics;

    public void Start(){
        setVisible(true);
        setTitle("Rival Ball Tournament");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bufferGraphics = buffer.getGraphics();

        
        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                update();
                repaint();
            }
        });

        timer.start();

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                paddle.move(e.getX());
                repaint();
            }
        });

        setFocusable(true);
    }
    public void setData(String[] data){
        oggetiDaStampare = data;
    }

    @Override
    public void paint(Graphics g) {

        
        // Disegna sul buffer
        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.fillRect(0, 0, WIDTH, HEIGHT);

        ball.draw(bufferGraphics);

        for (Brick brick : bricks) {
            brick.draw(bufferGraphics);
        }
        String[] data;
        for (String obj : oggetiDaStampare) {
            data = obj.split(",");
            switch (data[0]) {
                case '1'://Paddle
                    drawPaddle(bufferGraphics, data[2], data[3], data[4], data[5], data[1]);
                    break;
                case '2'://Ball
                    drawBall(bufferGraphics, data[2], data[3], data[4], data[5]);
                    break;

                case '3'://Brick
                    drawBricks(bufferGraphics, data[2], data[3], data[4], data[5], data[6]);
                    break;
                    
                case '4'://PowerUp
                    
                    break;

                case '9': //GameOver
                    
                    break;

                default:
                //nulla
                    break;
            }
        }
        
        
        // Copia il buffer sulla finestra
        g.drawImage(buffer, 0, 0, this);
    }

    public void drawPaddle(Graphics g, int x, int y, int width, int height, int id) {
        if (id == 0)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.RED);

        g.fillRect(x, y, width, height);
    }

    public void drawBall(Graphics g, int x, int y, int size, int owner) {
        if (owner == 0)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.RED);
        g.fillOval(x, y, size, size);
    }

    public void drawBricks(Graphics g, int x, int y, int width, int height, int hp) {
        if (ho == 3)
            g.setColor(Color.GREEN);
        else if (ho == 2)
            g.setColor(Color.YELLOW);
        else if (ho == 1)
            g.setColor(Color.ORANGE);

        g.fillRect(x, y, width, height);
    }
}
