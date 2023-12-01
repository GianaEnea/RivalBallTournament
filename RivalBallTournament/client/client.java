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
        //ip del server
        final String serverAddress = "localhost";// da cambiare con ip del server
        final int portNumber = 5555;
        //creazione della socket TCP
        try (
            Socket socket = new Socket(serverAddress, portNumber);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);           
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
        ) {
            //creazione di un frame
            myFrame frame = new myFrame();
            
            int paddlePosition = WIDTH / 2 - 100 / 2;
            //NON implementato
            boolean paddleJump = false;
            //informazioni ricevute dal server
            String inputLine;
            boolean gameOverFlag = false;
            boolean isFirst = true;
            while (!gameOverFlag) {
                //invio le informazioni dei listener al server
                writer.println(paddlePosition+";"+Boolean.toString(paddleJump));
                paddlePosition = frame.mousePosition;
                //leggo le informazioni ricevute dal server
                inputLine = reader.readLine();
                if (inputLine != null) {
                    String[] obj = inputLine.split(";");
                    //metto le informazioni del server dentro una lista
                    frame.panel.setData(obj);
                }
                //pulisco la stringa;
                inputLine = "";
                //genera il panel ma non lo aggiorna fino a che non inizia il gico
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