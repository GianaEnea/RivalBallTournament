package RivalBallTournament.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class client {
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
            myFrame frame = new myFrame();
            
            int paddlePosition = WIDTH / 2 - 100 / 2;
            boolean paddleJump = false;

            String inputLine;
            boolean gameOverFlag = false;
            boolean isFirst = true;
            while (!gameOverFlag) {
                writer.println(paddlePosition+";"+Boolean.toString(paddleJump));
                paddlePosition = frame.mousePosition;
                inputLine = reader.readLine();
                if (inputLine != null) {
                    String[] obj = inputLine.split(";");
                    frame.panel.setData(obj);
                }
                inputLine = "";

                if (isFirst) {
                    frame.panel.Start();
                    isFirst = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}