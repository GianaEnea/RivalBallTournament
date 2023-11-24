package RivalBallTournament.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


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

        repaint();
        
        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                repaint();
            }
        });

        timer.start();

        setFocusable(true);

        /*while (true) {
            Thread.sleep(10);
            repaint();
        }*/
    }
    
    public void setData(String[] data){
        oggetiDaStampare = data;
    }

    @Override
    public void paint(Graphics g) {
        // Disegna sul buffer
        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.fillRect(0, 0, WIDTH, HEIGHT);

        String[] data;
        for (String obj : oggetiDaStampare) {
            data = obj.split(",");
            switch (data[0]) {
                case "1"://Paddle
                    drawPaddle(bufferGraphics, Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[1]));
                    break;
                case "2"://Ball
                    drawBall(bufferGraphics, Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                    break;

                case "3"://Brick
                    drawBricks(bufferGraphics, Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]));
                    break;
                    
                case "4"://PowerUp
                    
                    break;

                case "9": //GameOver
                    
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
        if (hp == 3)
            g.setColor(Color.GREEN);
        else if (hp == 2)
            g.setColor(Color.YELLOW);
        else
            g.setColor(Color.ORANGE);

        g.fillRect(x, y, width, height);
    }
}
