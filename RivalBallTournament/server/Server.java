package RivalBallTournament.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    static final int portNumber = 5555;

    public static void main(String[] args) {
        try (
            //creo la socket tcp
            ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            while (true) {
                //coda per capire in numero di giocatori
                int queue = 0;
                Socket[] playersSockets= new Socket[2];
                //numero di giocatori da lasciare connettere prima di iniziare i gioco (max 2)
                while (queue < 2) {
                    playersSockets[queue] = serverSocket.accept();
                    //System.out.println("Client connected: " + playersSockets[queue].getInetAddress().getHostAddress());
                    queue++;
                }
                
                fatherHandler f = new fatherHandler(playersSockets);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}