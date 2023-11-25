package RivalBallTournament.server;

public class PowerUp {
    public String id;
    public int spownedBy;
    public int x, y;
    public int speed = 1;
    public static final int SIZE = 20;
    String[] Powerups = {"BallBig", "BallSmall", "PaddleBig", "PaddleSmall", "BallDamage", "BallSlow", "BallFast"};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String RollaPowerup(){
        int max = Powerups.length;
        int min = 0;
        int range = max - min + 1;
        int i = (int)(Math.random() * range) + min;
        return Powerups[i];
    }
}