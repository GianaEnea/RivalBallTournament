package brickBraker.server;

import java.awt.*;

public class Brick {
    //id per multiplayer???
    public static final int WIDTH = 90;
    public static final int HEIGHT = 30;
    private  int x, y;
    //hp

    public Brick(int x, int y) {
        this.x = x;
        this.y = y;
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

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
}