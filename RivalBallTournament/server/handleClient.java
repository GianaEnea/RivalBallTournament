package RivalBallTournament.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class handleClient implements Runnable{
    Socket socket;
    fatherHandler f;
    int player;

    public  handleClient(Socket clientSocket, int id, fatherHandler fin) {
        socket = clientSocket;
        f = fin;
        player = id;

        //creo la paddle e la inizializzo in base al player a cui va assegnata
        Paddle paddle;
        if (player == 0) {
            paddle = new Paddle(f.WIDTH / 2 - Paddle.WIDTH / 2, f.HEIGHT - 50);
        }
        else {
            paddle = new Paddle(f.WIDTH / 2 - Paddle.WIDTH / 2, 50);
        }
        paddle.setId(player);
        //setto i punteggi dei due player, 0 il primo e 1 il secondo
        paddle.setScore(0);
        f.paddles.add(paddle);

        //creo la palla e la inizializzo in base al player a cui va assegnata
        Ball ball;
        if (player == 0) {
            ball = new Ball(f.WIDTH / 2, f.HEIGHT - 100);
        }
        else {
            ball = new Ball(f.WIDTH / 2, 100);
        }
        ball.setOwner(player);
        f.balls.add(ball);
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            //messaggio del client
            String inputLine;
            while (!f.gameOverFlag) {

                inputLine = reader.readLine();
                //modifico le posizioni della paddle e della palla di un player in base alle informazioni del client
                f.update(inputLine, player);
                inputLine = "";

                // messaggio da mandare al client
                String output = "";
                //se la stringa inizia con 1 sono informazioni Paddle
                output += f.getPaddles();
                //se la stringa inizia con 2 sono informazioni Ball
                output += f.getBalls();
                //se la stringa inizia con 3 sono informazioni Brick
                output += f.getBricks();
                //se la stringa inizia con 4 sono informazioni PowerUp
                output+= f.getPowerUps();
                writer.println(output);
                
                try {Thread.sleep(15);} catch (Exception e) {throw new RuntimeException();}
            }
            //se la stringa inizia con 9 finisce il gioco
            String results = "9";
            for (Paddle p : f.paddles) {
                results += ","+p.getId()+","+p.getScore();
            }
            writer.println(results);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //finito chiudo connessione
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
