package RivalBallTournament.server;

import java.awt.*;

public class Paddle {
    public static int WIDTH = 150;
    public static int HEIGHT = 15;
    //che player sta usando la paddle
    private int id;
    private int score = 0;
    private int x, y;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    //ritorna il rettangolo
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
    //aggiorna la posizione della paddle
    public void move(int mouseX) {
        x = mouseX - WIDTH / 2;

        // Limita il movimento del paddle all'interno della finestra
        if (x < 0) {
            x = 0;
        } else if (x > fatherHandler.WIDTH - WIDTH) {
            x = fatherHandler.WIDTH - WIDTH;
        }
    }
}
