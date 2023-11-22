package RivalBallTournament.client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

//work in progress
public class cliet extends JFrame{
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    public static void main(String[] args) throws UnknownHostException, IOException {

        final String serverAddress = "localhost";// da cambiare con ip del server
        final int portNumber = 5555;
        try (
            Socket socket = new Socket(serverAddress, portNumber);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            Game game = new Game();
            game.Start();

            //"posizioneMouse;saltoPaddle"
            int paddlePosition = WIDTH / 2 - 100 / 2;
            boolean paddleJump = false;

            String inputLine;
            boolean gameOverFlag = false;
            while (!gameOverFlag) {
                writer.println(paddlePosition+";"+Boolean.toString(paddleJump));

                while ((inputLine = reader.readLine()) != null) {
                    String[] data;
                    String[] obj = inputLine.split(";");
                    for (String s : obj) {
                        switch (s.charAt(0)) {
                            case '1'://Paddle
                                data = s.split(",");
                                //manda al game per disegnare obj
                                //game.drawPaddle(data...);
                                break;
                            case '2'://Ball
                                
                                break;

                            case '3'://Brick
                                
                                break;
                                
                            case '4'://PowerUp
                                
                                break;

                            case '9': //GameOver
                                
                                break;

                            default:
                            //nulla
                                break;
                        }
                    }
                    inputLine = "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
