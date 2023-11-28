package RivalBallTournament.client;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

public class myFrame extends JFrame{







    /*
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    String[] oggetiDaStampare; 
    private BufferedImage buffer;
    private Graphics bufferGraphics;
    public boolean isMouseClicked = false;
    public int mousePosition = WIDTH / 2 - 100 / 2;

    private boolean gameOverFlag = false;

    public myFrame(){
        JPanel panel = new JPanel();
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {}
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {}
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {}
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                isMouseClicked = false;
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                isMouseClicked = true;
            }
        });
        panel.addMouseMotionListener(new MouseMotionListener() {
           @Override
           public void mouseDragged(java.awt.event.MouseEvent e) {} 
           @Override
               public void mouseMoved(java.awt.event.MouseEvent e) {
                    mousePosition = e.getX();
               }
        });
        panel.setSize(WIDTH, HEIGHT);
        //panel.setVisible(true);

        this.add(panel);

        setVisible(true);
        setTitle("Rival Ball Tournament");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bufferGraphics = buffer.createGraphics();
        setFocusable(true);
    }

    public void Start(){
        new Thread(() -> draw()).start();
    }

    public void draw() {
        while (!gameOverFlag) {
            repaint();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    
    public void setData(String[] data){
        oggetiDaStampare = data;
    }

    @Override
    public void paint(Graphics g) {
        // Disegna sul buffer
        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.fillRect(0, 0, WIDTH, HEIGHT);
        if (oggetiDaStampare == null) {
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
                        gameOver(g, Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                        break;

                    default:
                    //nulla
                        break;
                }
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

    public void gameOver(Graphics g, int id1, int score1, int id2, int score2) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLUE);
        g.drawString(String.valueOf(score1), 100, 100);
        g.setColor(Color.RED);
        g.drawString(String.valueOf(score2), 100, 200);
    }*/
}
