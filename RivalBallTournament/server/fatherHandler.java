package RivalBallTournament.server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class fatherHandler {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final double MAXBOUNCEANGLE = (Math.PI*5)/12;

    public ArrayList<Paddle> paddles;
    public ArrayList<Ball> balls;
    public ArrayList<Brick> bricks;
    public ArrayList<PowerUp> powerUps;

    //lista dei powerUp
    public static String[] powerUpsList = {"NULL","BallBig", "BallSmall", "PaddleBig", "PaddleSmall", "BallDamage", "BallSlow", "BallFast"};

    public Socket[] playersSockets;

    public boolean gameOverFlag = false;

    public fatherHandler(Socket[] sockets){
        playersSockets= new Socket[2];
        paddles = new ArrayList<>();
        balls = new ArrayList<>();
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();

        String map = "";
        //creazione dei bricks
        try {
            FileReader reader = new FileReader("map.txt");
            int data = reader.read();
            while (data != -1) {
                map += (char) data;
                data = reader.read();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            // TODO: handle exception
        } catch (IOException e) {
            // TODO: handle exception
        }
        
        String[] map2D = new String[50];
        map2D = map.split("\r\n");

        int countID = 0;
        int j = 0;
        for (String s : map2D) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '#') {
                    bricks.add(new Brick(countID, i * 80 + 15, j * 40 + 200));
                    countID++;
                }
            }
            j++;
        }

        playersSockets = sockets;


        handleClient h1 = new handleClient(playersSockets[0], 0, this);
        Thread t1 = new Thread(h1);
        handleClient h2 = new handleClient(playersSockets[1], 1, this);
        Thread t2 = new Thread(h2);

        chkCollision chk = new chkCollision(this);
        Thread tchk = new Thread(chk);

        t1.start();
        t2.start();
        try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
        tchk.start();
    }

    public synchronized String getPaddles() {
        String output = "";
        for (Paddle p : paddles) {
            output += "1,"+p.getId()+","+p.getX()+","+p.getY()+","+p.WIDTH+","+p.HEIGHT+";";
        }
        return output;
    }

    public synchronized String getBalls() {
        String output = "";
        ArrayList<Ball> tempBalls = balls;
        for (Ball b : tempBalls) {
            output += "2,"+b.getId()+","+b.getX()+","+b.getY()+","+b.getSize()+","+b.getOwner()+";";
        }
        return output;
    }

    //crea la stringa con le informazioni dei brick da mandare al client per la stampa
    private synchronized String getBricks() {
        String output = "";
        for (Brick b : bricks) {
            if (b.getHp() > 0) {
                output += "3,"+b.getId()+","+b.getX()+","+b.getY()+","+b.WIDTH+","+b.HEIGHT+","+b.getHp()+";";
            }
        }
        return output;
    }

    private synchronized String getPowerUps() {
        String output = "";
        int count = 0;
        if (!powerUps.isEmpty()) {
            for (PowerUp p : powerUps) {
                count++;
                output += "4,"+p.getId()+","+powerUpsList[p.getType()]+","+p.getX()+","+p.getY()+","+p.SIZE+","+p.getSpownedBy()+";";
                if (count != powerUps.size()) 
                        output += ";";
            }
        }
        else {
            return "";
        }
        return output;
    }

    //aggiorna le posizioni di paddle e palla
    public synchronized void update(String inputLine, int id) {
        String[] input = inputLine.split(";");
        paddles.get(id).move(Integer.parseInt(input[0]));
        balls.get(id).move();
        if (!powerUps.isEmpty()) {
            usePowerUpsList(1, null);
        }
    }
    
    public synchronized String usePowerUpsList(int useCase, PowerUp p) {
        switch (useCase) {
            case 0:
                return getPowerUps();
        
            case 1:
                updatePowerUps();
                return "ok";

            case 2:
                addPowerUps(p);
                return "ok";

            case 3:
                applyPowerUp(p);
                return "ok";

            default:
                return "null";
        }
    }

    public synchronized String useBricksList(int useCase, Brick brk, Ball b) {
        switch (useCase) {
            case 0:
                return getBricks();
        
            case 1:
                modifyBrick(brk, b);
                return "ok";

            default:
                return "null";
        }
    }

    private synchronized void addPowerUps(PowerUp p) {
        powerUps.add(p);
    }

    private synchronized void updatePowerUps() {
        if (!powerUps.isEmpty()) {
            for (PowerUp p : powerUps) {
                p.fall();
            }
        }
    }

    private synchronized void modifyBrick(Brick brk, Ball b) {
        if (!bricks.isEmpty()) {
            bricks.get(brk.getId()).setHp(bricks.get(brk.getId()).getHp() - b.getDamage());
            if (bricks.get(brk.getId()).getHp() == 0) {
                int powerUp = PowerUp.RollaPowerup();
                if (powerUp != 0) {
                    usePowerUpsList(2, new PowerUp(powerUp, b.getOwner(), brk.getX(), brk.getY()));
                }
                bricks.remove(bricks.get(brk.getId()));
                for (Brick brick : bricks) {
                    if (brick.getId() >= brk.getId()) {
                        brick.setId(brick.getId()-1);
                    }
                }
            }
        }
    }

    //applica lo stato del powerUp
    private synchronized void applyPowerUp(PowerUp p){
        switch (powerUpsList[p.getType()]) {
            case "BallBig":
                for (Ball ball : balls) {
                    if (ball.getOwner() == p.getSpownedBy()) {
                        ball.setSize(ball.getSize()+10);
                    }
                }
                break;
            
            case "BallSmall":
                for (Ball ball : balls) {
                    if (ball.getOwner() == p.getSpownedBy()) {
                        ball.setSize(ball.getSize()-10);
                    }
                }
                break;
            
            case "PaddleBig":
                for (Paddle paddle : paddles) {
                    if (paddle.getId() == p.getSpownedBy()) {
                        paddle.WIDTH += 10;
                    }
                }
                break;

            case "PaddleSmall":
                for (Paddle paddle : paddles) {
                    if (paddle.getId() == p.getSpownedBy()) {
                        paddle.WIDTH -= 10;
                    }
                }
                break;
            
            case "BallDamage":
                for (Ball ball : balls) {
                    if (ball.getOwner() == p.getSpownedBy()) {
                        ball.setDamage(3);
                    }
                }
                break;

            case "BallSlow":
                for (Ball ball : balls) {
                    if (ball.getOwner() == p.getSpownedBy()) {
                        ball.setSpeedMultiplyer(ball.getSpeedMultiplyer()-1);
                    }
                }
                break;
            
            case "BallFast":
                for (Ball ball : balls) {
                    if (ball.getOwner() == p.getSpownedBy()) {
                        ball.setSpeedMultiplyer(ball.getSpeedMultiplyer()+1);
                    }
                }
                break;
            default:
                break;
        }
        for (int i = 0; i < powerUps.size(); i++) {
            if(powerUps.get(i).getBounds().intersects(p.getBounds()))
                powerUps.remove(i);
        }
    }
}
