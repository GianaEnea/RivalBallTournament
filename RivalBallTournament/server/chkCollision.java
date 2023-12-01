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
        for (Ball b : f.balls) {
            new Thread(() -> {
                while (!f.gameOverFlag) {
                    Ball tempB = new Ball(b.getX()+b.getxSpeed(), b.getY()+b.getySpeed());
                    for (Paddle p : f.paddles) {
                        if (tempB.getBounds().intersects(p.getBounds())) {
                            boolean collisionOnX = false;
                            boolean collisionOnY = false;
                            Rectangle r = tempB.getBounds().intersection(p.getBounds());
                            //se il rettangolo dell'intersezione ha la larghezza più grande dell'altezza è una collisione orizzontale
                            if (r.width > r.height) {
                                collisionOnX = true;
                                b.reverseY();
                            }else if(r.width < r.height){
                                collisionOnY = true;
                                b.reverseX();
                            }else if (r.width == r.height) {
                                collisionOnX = true;
                                collisionOnY = true;
                                b.reverseY();
                                b.reverseX();
                            }

                            if (collisionOnX || collisionOnY) {
                                if (b.getOwner() != p.getId()) {
                                    p.setScore(20);
                                }
                            }
                        }
                    }
                    
                    
                    if (tempB.getX() <= 0 || tempB.getX() >= f.WIDTH - b.getSize())
                        b.reverseX();
                    
                    if (tempB.getY() <= 0) {
                        b.reverseY();
                        //b.reset();
                        for (Paddle p : f.paddles) {
                            if (p.getId() == 0)
                                p.setScore(100);
                        }
                    }
                    
                    if (tempB.getY() >= f.HEIGHT- b.getSize()) {
                        b.reverseY();
                        //b.reset();
                        for (Paddle p : f.paddles) {
                            if (p.getId() == 1)
                                p.setScore(100);
                        }
                    }
                    /*
                    if (f.powerUps.size() != 0) {
                        new Thread(() -> {
                            for (PowerUp pow : f.powerUps) {
                                if(f.paddles.get(pow.getSpownedBy()).getBounds().intersects(pow.getBounds())) {
                                    f.applyPowerUp(pow);
                                    f.powerUps.remove(pow);
                                }
                            }

                        }).start();
                    }*/
                }
            }).start();
        }
        
        Thread chkBricks = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Ball b : f.balls) {
                    new Thread(() -> {
                        while (!f.gameOverFlag) {
                            Ball tempB = new Ball(b.getX()+b.getxSpeed(), b.getY()+b.getySpeed());
                            String[] bricksS = f.useBricksList(0, null, null).split(";");
                            for (String brick : bricksS) {
                                String[] bricklet = brick.split(",");
                                Brick brk = new Brick(Integer.parseInt(bricklet[1]), Integer.parseInt(bricklet[2]), Integer.parseInt(bricklet[3]));
                                if(tempB.getBounds().intersects(brk.getBounds())) {
                                    boolean collisionOnX = false;
                                    boolean collisionOnY = false;

                                    Rectangle r = tempB.getBounds().intersection(brk.getBounds());
                                    if (r.width > r.height) {
                                        collisionOnX = true;
                                        b.reverseY();
                                    }else if(r.width < r.height){
                                        collisionOnY = true;
                                        b.reverseX();
                                    }else if (r.width == r.height) {
                                        collisionOnX = true;
                                        collisionOnY = true;
                                        b.reverseY();
                                        b.reverseX();
                                    }

                                    if (collisionOnX || collisionOnY) {
                                        new Thread(() -> {
                                            f.useBricksList(1, brk, b);
                                        }).start();
                                        f.paddles.get(b.getOwner()).setScore(10);
                                        break;
                                    }
                                }
                            }
                        }
                    }).start();
                }
                
            }
        });

        // Verifica se è finitio il gioco
        Thread chkEnding = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!f.gameOverFlag) {
                    if (f.bricks.size() == 0) {
                        f.gameOverFlag = true;
                    }
                    try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
                }
            }
        });
        
        chkBricks.start();
        chkEnding.start();
    }
}