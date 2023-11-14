package brickBraker;

import java.awt.*;

public class Paddle {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;
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
        } else if (x > BrickBreakerGame.WIDTH - WIDTH) {
            x = BrickBreakerGame.WIDTH - WIDTH;
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

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
}
