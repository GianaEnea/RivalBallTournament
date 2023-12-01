import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class myPanel extends JPanel{

    public boolean gameOverFlag = false;
    //lista di oggetti mandati del server da stampare
    private String[] oggetiDaStampare;

    Image[] image = new Image[7];
    
    public myPanel() {
        this.setPreferredSize(new Dimension(client.WIDTH, client.HEIGHT));
        this.setBackground(Color.WHITE);
        //immagini de applicare agli oggetti
        image[0] = new ImageIcon("./img/BallBig.png").getImage();
        image[1] = new ImageIcon("./img/BallSmall.png").getImage();
        image[2] = new ImageIcon("./img/PaddleBig.png").getImage();
        image[3] = new ImageIcon("./img/PaddleSmall.png").getImage();
        image[4] = new ImageIcon("./img/BallDamage.png").getImage();
        image[5] = new ImageIcon("./img/BallSlow.png").getImage();
        image[6] = new ImageIcon("./img/BallFast.png").getImage();
    }
    
    public void setData(String[] data){
        oggetiDaStampare = data;
    }
    //crea un thread che si occupa di creare i frame da stampare
    public void Start(){
        new Thread(() -> draw()).start();
    }
    //disegno ogni 30 millisendi
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
        //scorro la lista degli oggetti da stampare e in base al primo campo capisco che oggetto è o lo stampo
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
                        drawPowerUps(g2D, data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
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
        else {
            drawWhaitRoom(g2D);
        }
    }
    //tutti i metodi per disegnare i vari oggetti del gioco


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

    public void drawPowerUps(Graphics2D g, String type, int x, int y, int size) {
        //"BallBig", "BallSmall", "PaddleBig", "PaddleSmall", "BallDamage", "BallSlow", "BallFast"
        switch (type) {
            case "BallBig"://Paddle
                g.drawImage(image[0], x, y, null);
                break;
            case "BallSmall"://Ball
                g.drawImage(image[1], x, y, null);
                break;

            case "PaddleBig"://Brick
                g.drawImage(image[2], x, y, null);
                break;
                
            case "PaddleSmall"://PowerUp
                g.drawImage(image[3], x, y, null);
                break;

            case "BallDamage": //GameOver
                g.drawImage(image[4], x, y, null);
                break;

            case "BallSlow": //GameOver
                g.drawImage(image[5], x, y, null);
                break;
            
            case "BallFast": //GameOver
                g.drawImage(image[6], x, y, null);
                break;

            default:
            //nulla
                break;
        }
        g.fillRect(x, y, size, size);
    }

    public void gameOver(Graphics2D g, int id1, int score1, int id2, int score2) {
        g.setColor(Color.BLUE);
        g.drawString(String.valueOf(score1), 100, 100);
        g.setColor(Color.RED);
        g.drawString(String.valueOf(score2), 100, 200);
        gameOverFlag = true;
    }

    public void drawWhaitRoom(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf("Whaiting for adversary..."), 400, 400);
    }
}
