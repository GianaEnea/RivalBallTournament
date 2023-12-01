package RivalBallTournament.server;

import java.awt.Rectangle;

public class Ball {
    private int id;
    public static int SIZE = 20;
    private int x, y;
    //player che ha colpito la pallina per ultimo
    private int owner;
    private int xSpeed = 3;
    private int ySpeed = 3;
    private int damage = 1;
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
    

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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
        xSpeed = -xSpeed;
    }
    //cambio direzione
    public void reverseY() {
        ySpeed = -ySpeed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    //ritorna la posizione della pallina
    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public void resetPallina(){
        this.damage = 1;
        this.xSpeed = 3;
        this.ySpeed = 3;
        this.SIZE = 20;
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