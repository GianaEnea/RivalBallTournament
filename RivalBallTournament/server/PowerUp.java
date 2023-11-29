package RivalBallTournament.server;

public class PowerUp {
    public String id;
    public int spownedBy;
    public int x, y;
    public int speed = 1;
    public static final int SIZE = 20;
    //lista dei powerUp
    public static String[] Powerups = {"BallBig", "BallSmall", "PaddleBig", "PaddleSmall", "BallDamage", "BallSlow", "BallFast"};

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

    public PowerUp(String id, int spownedBy, int x, int y) {
        this.id = id;
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
    //restituisce la stringa con il nome di un powerUp
    public static String RollaPowerup(){
        int max = Powerups.length;
        int i = (int) (Math.random() * max);
        return Powerups[i];
    }
}