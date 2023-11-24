package RivalBallTournament.server;

public class PowerUp {
    public String id;
    public int x, y;
    public static final int SIZE = 20;
    String[] Powerups = {"BallBig", "BallSmall", "PaddleBig", "PaddleSmall", "BallDamage", "BallSlow", "BallFast"};

    public String RollaPowerup(){
        int max = Powerups.length;
        int min = 0;
        int range = max - min + 1;
        int i = (int)(Math.random() * range) + min;
        return Powerups[i];
    }
}