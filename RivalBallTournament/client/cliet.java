package RivalBallTournament.client;

import javax.swing.*;

import java.awt.MouseInfo;
import java.awt.Point;
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
            //while (! entrambi connessi) {...}    forse
            
            //"posizioneMouse;saltoPaddle"
            int paddlePosition = WIDTH / 2 - 100 / 2;
            boolean paddleJump = false;

            Point p = MouseInfo.getPointerInfo().getLocation();
            String inputLine;
            boolean gameOverFlag = false;
            boolean isFirst = true;
            while (!gameOverFlag) {
                writer.println(paddlePosition+";"+Boolean.toString(paddleJump));
                p = MouseInfo.getPointerInfo().getLocation();
                paddlePosition = (int)p.getX();
                inputLine = reader.readLine();
                String[] obj = inputLine.split(";");
                game.setData(obj);
                inputLine = "";

                if (isFirst) {
                    //new Thread(() -> game.Start()).start();
                    game.Start();
                    isFirst = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}