package RivalBallTournament.client;

import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.awt.event.*;

//work in progress
public class Game extends JFrame{
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    String[] oggetiDaStampare; 
    private BufferedImage buffer;
    private Graphics bufferGraphics;
    public boolean isMouseClicked = false;
    public int mousePosition = WIDTH / 2 - 100 / 2;;

    public void Start(){
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

        


        this.add(panel);

        setVisible(true);
        setTitle("Rival Ball Tournament");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bufferGraphics = buffer.getGraphics();

        repaint();
        
        Timer timer = new Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                repaint();
            }
        });

        timer.start();

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

    }
}
