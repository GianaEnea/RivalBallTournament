package RivalBallTournament.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class myPanel extends JPanel{

    public boolean gameOverFlag = false;

    private String[] oggetiDaStampare;

    public myPanel() {
        this.setPreferredSize(new Dimension(client.WIDTH, client.HEIGHT));
        this.setBackground(Color.WHITE);
    }

    public void setData(String[] data){
        oggetiDaStampare = data;
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

    @Override
    public void paint(Graphics g) {
        super.paint(g); //colora lo sfondo
        Graphics2D g2D = (Graphics2D) g;

        if (oggetiDaStampare != null) {
            String[] data;
            for (String obj : oggetiDaStampare) {
                data = obj.split(",");
                switch (data[0]) {
                    case "1"://Paddle
                        drawPaddle(g2D, Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[1]));
                        break;
                    case "2"://Ball
                        drawBall(g2D, Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                        break;

                    case "3"://Brick
                        drawBricks(g2D, Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]));
                        break;
                        
                    case "4"://PowerUp
                        
                        break;

                    case "9": //GameOver
                        gameOver(g2D, Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                        break;

                    default:
                    //nulla
                        break;
                }
            }
        }
    }

    public void drawPaddle(Graphics2D g, int x, int y, int width, int height, int id) {
        if (id == 0)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.RED);

        g.fillRect(x, y, width, height);
    }

    public void drawBall(Graphics2D g, int x, int y, int size, int owner) {
        if (owner == 0)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.RED);
        g.fillOval(x, y, size, size);
    }

    public void drawBricks(Graphics2D g, int x, int y, int width, int height, int hp) {
        if (hp == 3)
            g.setColor(Color.GREEN);
        else if (hp == 2)
            g.setColor(Color.YELLOW);
        else
            g.setColor(Color.ORANGE);

        g.fillRect(x, y, width, height);
    }

    public void gameOver(Graphics2D g, int id1, int score1, int id2, int score2) {
        g.setColor(Color.BLUE);
        g.drawString(String.valueOf(score1), 100, 100);
        g.setColor(Color.RED);
        g.drawString(String.valueOf(score2), 100, 200);
        gameOverFlag = true;
    }
}
