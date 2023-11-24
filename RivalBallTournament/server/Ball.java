package RivalBallTournament.server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ball {
    private int id;
    public static final int SIZE = 20;
    private int x, y;
    private int owner;
    private int xSpeed = 2;
    private int ySpeed = 2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        x += xSpeed;
        y += ySpeed;
    }

    public void changeOwner() {
        
    }

    public void reverseX() {
        xSpeed = -xSpeed;
    }

    public void reverseY() {
        ySpeed = -ySpeed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    
}