package RivalBallTournament.server;

import java.awt.*;

public class Paddle {
    public static final int WIDTH = 150;
    public static final int HEIGHT = 15;
    private int id;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int x, y;

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int mouseX) {
        x = mouseX - WIDTH / 2;

        // Limita il movimento del paddle all'interno della finestra
        if (x < 0) {
            x = 0;
        } else if (x > Server.WIDTH - WIDTH) {
            x = Server.WIDTH - WIDTH;
        }
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    
}
