package RivalBallTournament.server;

import java.awt.*;

public class Brick {
    private int id;
    public static final int WIDTH = 60;
    public static final int HEIGHT = 20;
    private  int x, y;
    private int hp = 3;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

}