package RivalBallTournament.server;

import java.net.Socket;
import java.util.ArrayList;

public class fatherHandler {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static ArrayList<Paddle> paddles;
    public static ArrayList<Ball> balls;
    public static ArrayList<Brick> bricks;
    public static ArrayList<PowerUp> powerUps;

    //lista dei powerUp
    public static String[] Powerups = {"BallBig", "BallSmall", "PaddleBig", "PaddleSmall", "BallDamage", "BallSlow", "BallFast"};

    public Socket[] playersSockets;

    public static boolean gameOverFlag = false;

    public fatherHandler(Socket[] sockets){
        playersSockets= new Socket[2];
        paddles = new ArrayList<>();
        balls = new ArrayList<>();
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();

        //creazione dei bricks
        int countID = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                bricks.add(new Brick(countID, i * 80 + 15, j * 40 + 200));
                countID++;
            }
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
        tchk.start();
    }

    public static synchronized String getPaddles() {
        String output = "";
        for (Paddle p : paddles) {
            output += "1,"+p.getId()+","+p.getX()+","+p.getY()+","+p.WIDTH+","+p.HEIGHT+";";
        }
        return output;
    }

    public static synchronized String getBalls() {
        String output = "";
        for (Ball b : balls) {
            output += "2,"+b.getId()+","+b.getX()+","+b.getY()+","+b.SIZE+","+b.getOwner()+";";
        }
        return output;
    }

    //crea la stringa con le informazioni dei brick da mandare al client per la stampa
    public static synchronized String getBricks() {
        String output = "";
        for (Brick b : bricks) {
            if (b.getHp() != 0) {
                output += "3,"+b.getId()+","+b.getX()+","+b.getY()+","+b.WIDTH+","+b.HEIGHT+","+b.getHp()+";";
            }
        }
        return output;
    }

    public static synchronized String getPowerUps() {
        String output = "";
        int count = 0;
        if (powerUps.size() != 0) {
            for (PowerUp p : powerUps) {
                count++;
                output += "2,"+p.getId()+","+p.getX()+","+p.getY()+","+p.SIZE+","+p.getSpownedBy()+";";
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
    public static synchronized void update(String inputLine, int id) {
        String[] input = inputLine.split(";");
        paddles.get(id).move(Integer.parseInt(input[0]));
        balls.get(id).move();
    }
}
