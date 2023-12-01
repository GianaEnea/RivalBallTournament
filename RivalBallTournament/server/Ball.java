package RivalBallTournament.server;

import java.awt.Rectangle;

public class Ball {
    private int id;
    private int size = 20;
    private int x, y;
    private int damage = 1;
    //player che ha colpito la pallina per ultimo
    private int owner;
    private int xSpeed = 3;
    private int ySpeed = 3;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        x += xSpeed;
        y += ySpeed;
    }
    //se una paddle colpisce la pallina se ne appropria
    public void changeOwner() {
        if(owner == 1)
            this.setOwner(0);
        else
            this.setOwner(1);
    }
    //cambio direzione
    public void reverseX() {
        xSpeed *= (-1);
    }
    //cambio direzione
    public void reverseY() {
        ySpeed *= (-1);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    //ritorna la posizione della pallina
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public void reset(){
        this.damage = 1;
        this.xSpeed = 3;
        this.ySpeed = 3;
        this.size = 20;
        if (owner == 0) {
            this.x = fatherHandler.WIDTH / 2;
            this.y = fatherHandler.HEIGHT - 100;
        }
        else {
            this.x = fatherHandler.WIDTH / 2;
            this.y = 100;
        }
    }
}