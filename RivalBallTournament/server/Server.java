package RivalBallTournament.server;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends JFrame {
    static final int portNumber = 5555;

    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    private static Ball ball;
    private static Paddle paddle;
    private static ArrayList<Brick> bricks;

    private static boolean gameOverFlag = false;

    public static void main(String[] args) {
        paddle = new Paddle(WIDTH / 2 - Paddle.WIDTH / 2, HEIGHT - 50);
        ball = new Ball(WIDTH / 2, HEIGHT / 2);
        bricks = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                bricks.add(new Brick(i * 100 + 50, j * 40 + 50));
            }
        }

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            int queue = 0;
            Socket[] playersSockets= new Socket[2];
            //2 per multiplayer, non implementato
            while (queue < 1) {
                playersSockets[queue] = serverSocket.accept();//new Socket(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort());
                System.out.println("Client connected: " + playersSockets[queue].getInetAddress().getHostAddress());
                queue++;
                // Handle communication with the client in a new thread
            }
            //fa pertite la gestione del gioco client una volta connesse due socket
            boolean isFirst = true;
            if (isFirst) {
                new Thread(() -> handleClient(playersSockets[0], 0)).start();
                isFirst = false;
            }
            else {
                new Thread(() -> handleClient(playersSockets[1], 1)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleClient(Socket clientSocket, int id) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            paddle.setId(id);
            ball.setOwner(id);
            String inputLine;
            while (!gameOverFlag) {
                inputLine = reader.readLine();
                update(inputLine);

                // Echo the message back to the client
                String output = "1,"+paddle.getId()+","+paddle.getX()+","+paddle.getY()+","+paddle.WIDTH+","+paddle.HEIGHT+";";
                output += "2,"+ball.getId()+","+ball.getX()+","+ball.getY()+","+ball.SIZE+","+ball.getOwner()+";";
                int count = 0;
                for (Brick brick : bricks) {
                    count++;
                    output = "3,"+brick.getId()+","+brick.getX()+","+brick.getY()+","+brick.WIDTH+","+brick.HEIGHT+","+brick.getHp();
                    if (count != bricks.size()) {
                        output += ";";
                    }
                }
                writer.println(output);
                inputLine = "";
                
            }
            //missing stats output
            //TO-DO: manda stats
            writer.println("9;");

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

    private static void update(String inputLine) {
        String[] input = inputLine.split(";");
        paddle.move(Integer.parseInt(input[0]));
        ball.move();
        checkCollision();
    }

    private static void checkCollision() {
        // Verifica collisione con il paddle
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseY();
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
}