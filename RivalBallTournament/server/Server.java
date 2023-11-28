package RivalBallTournament.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server{
    static final int portNumber = 5555;
    private static final ScheduledExecutorService scheduledThreadPoolExecutor = Executors.newScheduledThreadPool(10);

    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    private static ArrayList<Paddle> paddles;
    private static ArrayList<Ball> balls;
    private static ArrayList<Brick> bricks;
    private static ArrayList<PowerUp> Powerups;

    private static boolean gameOverFlag = false;

    public static void main(String[] args) {
        paddles = new ArrayList<>();
        balls = new ArrayList<>();
        bricks = new ArrayList<>();
        Powerups = new ArrayList<>();

        //creazione dei bricks
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                bricks.add(new Brick(i * 80 + 15, j * 40 + 200));
            }
        }

        try (
            //creo la socket tcp
            ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            //coda per capire in numero di giocatori
            int queue = 0;
            Socket[] playersSockets= new Socket[2];
            //numero di giocatori da lasciare connettere prima di iniziare i gioco (max 2)
            while (queue < 2) {
                playersSockets[queue] = serverSocket.accept();
                //System.out.println("Client connected: " + playersSockets[queue].getInetAddress().getHostAddress());
                queue++;
            }
            //fa pertire la gestione del gioco client una volta connesse due socket
            boolean isFirst = true;
            //crea un thread per inviare i dati a ogni player connesso
            for (Socket socket : playersSockets) {
                if (isFirst) {
                    new Thread(() -> handleClient(socket, 0)).start();
                    isFirst = false;
                }
                else {
                    new Thread(() -> handleClient(socket, 1)).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleClient(Socket clientSocket, int id) {
        //creo la paddle e la inizializzo in base al player a cui va assegnata
        Paddle paddle;
        if (id == 0) {
            paddle = new Paddle(WIDTH / 2 - Paddle.WIDTH / 2, HEIGHT - 50);
        }
        else {
            paddle = new Paddle(WIDTH / 2 - Paddle.WIDTH / 2, 50);
        }
        paddle.setId(id);
        //setto i punteggi dei due player, 0 il primo e 1 il secondo
        paddle.setScore(0);
        paddles.add(paddle);

        //creo la palla e la inizializzo in base al player a cui va assegnata
        Ball ball;
        if (id == 0) {
            ball = new Ball(WIDTH / 2, HEIGHT / 2);
        }
        else {
            ball = new Ball(WIDTH / 2, HEIGHT / 2);
        }
        ball.setOwner(id);
        balls.add(ball);
        

        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            //messaggio del client
            String inputLine;
            while (!gameOverFlag) {
                inputLine = reader.readLine();
                //modifico le posizioni della paddle e della palla di un player in base alle informazioni del client
                update(inputLine, paddle, ball);
                // messaggio da mandare al client
                String output = "";
                //se la stringa inizia con 1 sono informazioni Paddle
                for (Paddle p : paddles) {
                    output += "1,"+p.getId()+","+p.getX()+","+p.getY()+","+p.WIDTH+","+p.HEIGHT+";";
                }
                //se la stringa inizia con 2 sono informazioni Ball
                for (Ball b : balls) {
                    output += "2,"+b.getId()+","+b.getX()+","+b.getY()+","+b.SIZE+","+b.getOwner()+";";
                }
                //se la stringa inizia con 3 sono informazioni Brick
                output += getBricks();

                for (PowerUp p : Powerups) {
                    //TODO: madare le informazioni dei powerup nell'ordine giusto
                    //output += "4,"+p.getId()+","+p.getX()+","+p.getY()+","+p.SIZE+","+p.spownedBy";";
                }

                writer.println(output);
                inputLine = "";
                doPause(15);
            }
            //se finiscono i mattoni finisce la partita
            if (checkMattoni()) {
                //TODO: manda stats, vanno mandate le stats di entrambe le Paddle
                //se la stringa inizia con 9 finisce il gioco
                writer.println("9,"+paddle.getId()+","+paddle.getScore());
            }
            

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //finito chiudo connessione
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //aggiorna le posizioni di paddle e palla
    private static synchronized void update(String inputLine, Paddle paddle, Ball ball) {
        String[] input = inputLine.split(";");
        paddle.move(Integer.parseInt(input[0]));
        ball.move();
        checkCollision(paddle, ball);
        //System.out.println(paddles.get(1).getX());
    }
    //controlla tutte le collisioni necessarie
    private static void checkCollision(Paddle paddle, Ball ball) {
        
        /*if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseY();
            if (ball.getOwner() != paddle.getId()) {
                ball.changeOwner();
            }
        }*/
        
        // Verifica collisione con il paddle
        if (ball.getBounds().intersects(paddle.getBounds())) {
            boolean collisionOnX = Math.abs(ball.getBounds().getCenterX() - paddle.getBounds().getCenterX()) 
                                  < (ball.getBounds().getWidth() + paddle.getBounds().getWidth()) / 2;
            boolean collisionOnY = Math.abs(ball.getBounds().getCenterY() - paddle.getBounds().getCenterY()) 
                                  < (ball.getBounds().getHeight() + paddle.getBounds().getHeight()) / 2;
        
            if (collisionOnX) {
                // Collisione lungo l'asse X
                ball.reverseX();
            }
        
            if (collisionOnY) {
                // Collisione lungo l'asse Y
                ball.reverseY();
            }
        
            if (collisionOnX || collisionOnY) {
                if (ball.getOwner() != paddle.getId()) {
                    ball.changeOwner();
                }
            }
        }
        
        /*for (Brick brick : bricks) {
            if (ball.getBounds().intersects(brick.getBounds())) {
                bricks.remove(brick);
                ball.reverseY();
                break;
            }
        }*/
        // Verifica collisione con i mattoni
        if (ball.getBounds().intersects(paddle.getBounds())) {
            for (Brick brick : bricks) {
                boolean collisionOnX = Math.abs(ball.getBounds().getCenterX() - brick.getBounds().getCenterX()) 
                                    < (ball.getBounds().getWidth() + brick.getBounds().getWidth()) / 2;
                boolean collisionOnY = Math.abs(ball.getBounds().getCenterY() - brick.getBounds().getCenterY()) 
                                    < (ball.getBounds().getHeight() + brick.getBounds().getHeight()) / 2;
            
                if (collisionOnX) {
                    // Collisione lungo l'asse X
                    brick.setHp(brick.getHp()-1);
                    ball.reverseX();
                }
            
                if (collisionOnY) {
                    // Collisione lungo l'asse Y
                    brick.setHp(brick.getHp()-1);
                    ball.reverseY();
                }
                //TODO : Decidere punteggio
                if (collisionOnX || collisionOnY) {
                if (brick.getHp() == 0) {
                    paddle.setScore(paddle.getScore()+ 100);
                    String powerUp = PowerUp.RollaPowerup();
                    Powerups.add(new PowerUp(powerUp, paddle.getId(), brick.getX(), brick.getY()));
                }
            }
            }
        }

        // Verifica collisione con i bordi della finestra
        if (ball.getX() <= 0 || ball.getX() >= WIDTH - Ball.SIZE) {
            ball.reverseX();
        }

        if (ball.getY() <= 0) {
            ball.reverseY();
            //bisogna modificarlo per il multiplayer
            if (ball.getOwner() != paddle.getId()) {
                ball.changeOwner();
            }
        }

        // Verifica se il giocatore ha perso palla
        if (ball.getY() >= HEIGHT) {
            ball.reverseY();
            ball.changeOwner();
            //TODO : Decidere punti da dare all'avversario
        }

        //verifica se la partita è finita
        if (bricks.size() == 0) {
            gameOverFlag = true;
        }
    }

    //serve per bloccare l'esecuzione del thread in modo da mandare le informazioni al client non troppo veloci
    private static void doPause(int ms) {
        try {
            scheduledThreadPoolExecutor.schedule(() -> {
            }, ms, TimeUnit.MILLISECONDS).get();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    //crea la stringa con le informazioni dei brick da mandare al client per la stampa
    private static synchronized String getBricks() {
        String output = "";
        int count = 0;
        for (Brick b : bricks) {
            if (b.getHp() != 0) {
                count++;
                output += "3,"+b.getId()+","+b.getX()+","+b.getY()+","+b.WIDTH+","+b.HEIGHT+","+b.getHp();
                if (count != bricks.size()) 
                    output += ";";
            }
            
        }
        return output;
    }
    //se almeno un mattone ha vita puoi continuare a giocare, se nessun mattone ha vita finisce il gioco
    private static boolean checkMattoni(){
        boolean fineGioco = false;
        for (Brick b : bricks) {
            if (b.getHp() != 0) {
                fineGioco = true;
            }
        }
        return fineGioco;
    }
}