package brickBraker.client;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

//work in progress
public class cliet extends JFrame{

    public static void main(String[] args) throws UnknownHostException, IOException {

        final String serverAddress = "localhost";
        final int portNumber = 5555;
 
        Socket socket = new Socket(serverAddress, portNumber);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        

        /*BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in)) {
            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                writer.println(userInputLine);

                // Read and print the response from the server
                System.out.println("Server response: " + reader.readLine());
            }
        }*/

        
        

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Game game = new Game();
                game.Start();
            }
        });
    }
}
