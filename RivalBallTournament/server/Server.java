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

    private static boolean gameOverFlag = false;

    public static void main(String[] args) {
        paddles = new ArrayList<>();
        balls = new ArrayList<>();
        bricks = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                bricks.add(new Brick(i * 80 + 15, j * 40 + 200));
            }
        }

        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            int queue = 0;
            Socket[] playersSockets= new Socket[2];
            while (queue < 2) {
                playersSockets[queue] = serverSocket.accept();
                //System.out.println("Client connected: " + playersSockets[queue].getInetAddress().getHostAddress());
                queue++;
            }
            //fa pertite la gestione del gioco client una volta connesse due socket
            boolean isFirst = true;
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
        Paddle paddle;
        if (id == 0) {
            paddle = new Paddle(WIDTH / 2 - Paddle.WIDTH / 2, HEIGHT - 50);
        }
        else {
            paddle = new Paddle(WIDTH / 2 - Paddle.WIDTH / 2, 50);
        }
        paddle.setId(id);
        paddles.add(paddle);

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
            String inputLine;
            while (!gameOverFlag) {
                inputLine = reader.readLine();
                update(inputLine, paddle, ball);

                // Echo the message back to the client
                String output = "";
                for (Paddle p : paddles) {
                    output += "1,"+p.getId()+","+p.getX()+","+p.getY()+","+p.WIDTH+","+p.HEIGHT+";";
                }
                for (Ball b : balls) {
                    output += "2,"+b.getId()+","+b.getX()+","+b.getY()+","+b.SIZE+","+b.getOwner()+";";
                }
                output += addBricks();

                writer.println(output);
                inputLine = "";
                doPause(15);
            }
            //missing stats output
            //TO-DO: manda stats
            writer.println("9,"+paddle.getId()+","+paddle.getScore());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static synchronized void update(String inputLine, Paddle paddle, Ball ball) {
        String[] input = inputLine.split(";");
        paddle.move(Integer.parseInt(input[0]));
        ball.move();
        checkCollision(paddle, ball);
        //System.out.println(paddles.get(1).getX());
    }

    private static void checkCollision(Paddle paddle, Ball ball) {
        // Verifica collisione con il paddle
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseY();
            if (ball.getOwner() != paddle.getId()) {
                ball.changeOwner();
            }
        }

        // Verifica collisione con i mattoni
        for (Brick brick : bricks) {
            if (ball.getBounds().intersects(brick.getBounds())) {
                bricks.remove(brick);
                ball.reverseY();
                break;
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
        }

        //verifica se la partita è finita
        if (bricks.size() == 0) {
            gameOverFlag = true;
        }
    }
    
    private static void doPause(int ms) {
        try {
            scheduledThreadPoolExecutor.schedule(() -> {
            }, ms, TimeUnit.MILLISECONDS).get();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private static synchronized String addBricks() {
        String output = "";
        int count = 0;
        for (Brick b : bricks) {
            count++;
            output += "3,"+b.getId()+","+b.getX()+","+b.getY()+","+b.WIDTH+","+b.HEIGHT+","+b.getHp();
            if (count != bricks.size()) {
                output += ";";
            }
        }
        return output;
    }
}