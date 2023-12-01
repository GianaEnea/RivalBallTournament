package RivalBallTournament.server;

import java.util.Random;

public class PowerUp {
    public int id;
    public int spownedBy;
    public int type;
    public int x, y;
    public int speed = 1;
    public static final int SIZE = 20;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getSpownedBy() {
        return spownedBy;
    }

    public void setSpownedBy(int spownedBy) {
        this.spownedBy = spownedBy;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public PowerUp(int type, int spownedBy, int x, int y) {
        this.type = type;
        this.spownedBy = spownedBy;
        this.x = x;
        this.y = y;
    }

    //fa cadere il powerUp verso chi ha rotto il brick
    public void fall() {
        if (spownedBy == 0) {
            y += speed;
        }
        else {
            //forse
            y -= speed;
        }
        y += speed;
    }

    //restituisce la posizione del powerUp nell'array dei powerUp
    public static int RollaPowerup(){
        Random r = new Random();
        if (r.nextBoolean()) {
            return 0;
        }
        return (r.nextInt(fatherHandler.powerUpsList.length-1))+1;
    }
}