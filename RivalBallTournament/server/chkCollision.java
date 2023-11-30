package RivalBallTournament.server;

import java.awt.Rectangle;

public class chkCollision implements Runnable{
    fatherHandler f;

    public chkCollision(fatherHandler f) {
        this.f = f;
    }

    //controlla tutte le collisioni necessarie
    @Override
    public void run() {

        // Verifica collisione con il paddle
        Thread chkPaddles = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Ball b : f.balls) {
                    new Thread(() -> {
                        while (!f.gameOverFlag) {
                            for (Paddle p : f.paddles) {
                                if (b.getBounds().intersects(p.getBounds())) {
                                    boolean collisionPOnX = false;
                                    boolean collisionPOnY = false;
                                    //ritorna il rettangolo creato dall'intersezione
                                    Rectangle pr = b.getBounds().intersection(p.getBounds());
                                    //se il rettangolo dell'intersezione ha la larghezza più grande dell'altezza è una collisione orizzontale
                                    if (pr.width > pr.height && pr.width >= 0 && pr.height >= 0) {
                                        collisionPOnX = true;
                                    }else if(pr.width < pr.height && pr.width >= 0 && pr.height >= 0){
                                        collisionPOnY = true;
                                    }
                                
                                    if (collisionPOnX || collisionPOnY) {
                                        if (b.getOwner() != p.getId()) {
                                            b.changeOwner();
                                        }
                                    }
                                }
                            }
                            //Thread.sleep(2);
                        }
                    }).start();
                }
            }
        });

        // Verifica collisione con i mattoni
        Thread chkBricks = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Ball b : f.balls) {
                    new Thread(() -> {
                        while (!f.gameOverFlag) {
                            for (Brick brick : f.bricks) {
                                if (b.getBounds().intersects(brick.getBounds())) {
                                    boolean collisionOnX = Math.abs(b.getBounds().getCenterX() - brick.getBounds().getCenterX()) 
                                                        < (b.getBounds().getWidth() + brick.getBounds().getWidth()) / 2;
                                    boolean collisionOnY = Math.abs(b.getBounds().getCenterY() - brick.getBounds().getCenterY()) 
                                                        < (b.getBounds().getHeight() + brick.getBounds().getHeight()) / 2;
                                
                                    if (collisionOnX) {
                                        // Collisione lungo l'asse X
                                        brick.setHp(brick.getHp()-1);
                                        b.reverseX();
                                    }
                                
                                    if (collisionOnY) {
                                        // Collisione lungo l'asse Y
                                        brick.setHp(brick.getHp()-1);
                                        b.reverseY();
                                    }

                                    if (collisionOnX || collisionOnY) {
                                        f.paddles.get(b.getOwner()).setScore(10);
                                        if (brick.getHp() == 0) {
                                            f.bricks.remove(brick.getId());
                                            f.paddles.get(b.getOwner()).setScore(40);

                                            int powerUp = PowerUp.RollaPowerup();
                                            f.powerUps.add(new PowerUp(powerUp, f.paddles.get(b.getOwner()).getId(), brick.getX(), brick.getY()));
                                        }
                                    }
                                }
                            }
                            //Thread.sleep(2);
                        }
                    }).start();
                }
            }
        });

        Thread chkBorders = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Ball b : f.balls) {
                    new Thread(() -> {
                        while (!f.gameOverFlag) {
                            if (b.getX() <= 0 || b.getX() >= f.WIDTH - Ball.SIZE) {
                                b.reverseX();
                            }

                            if (b.getY() <= 0) {
                                b.reverseY();
                                if (b.getOwner() == f.paddles.get(1).getId()) {
                                    f.paddles.get(1).setScore(100);
                                }
                            }

                            if (b.getY() >= f.HEIGHT) {
                                b.reverseY();
                                if (b.getOwner() == f.paddles.get(0).getId()) {
                                    f.paddles.get(0).setScore(100);
                                }
                            }

                            //verifica se la partita è finita
                            if (f.bricks.size() == 0) {
                                f.gameOverFlag = true;
                            }
                            //Thread.sleep(2);
                        }
                    }).start();
                }
            }
        });
        
        chkPaddles.start();
        chkBricks.start();
        chkBorders.start();
    }
}
