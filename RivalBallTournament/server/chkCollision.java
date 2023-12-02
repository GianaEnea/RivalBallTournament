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
                    tempB.setSize(b.getSize());
                    for (Paddle p : f.paddles) {
                        if (tempB.getBounds().intersects(p.getBounds())) {
                            Rectangle r = tempB.getBounds().intersection(p.getBounds());

                            double relativeIntersectX = (p.getX()+(p.WIDTH/2)) - (r.x + (r.getWidth()/2));
                            double normalizedRelativeIntersectionX = (relativeIntersectX/(p.WIDTH/2));
                            double bounceAngle = normalizedRelativeIntersectionX * f.MAXBOUNCEANGLE;
                            b.setxSpeed((int)(b.getSpeedMultiplyer()*-Math.sin(bounceAngle)));
                            b.setySpeed((int)(b.getSpeedMultiplyer()*Math.cos(bounceAngle)));

                            if(b.getOwner() == 0)
                                b.reverseY();

                            if (b.getOwner() != p.getId()) {
                                p.setScore(20);
                                b.changeOwner();
                            }
                            /* 
                            boolean collisionOnX = false;
                            boolean collisionOnY = false;
                            
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
                                    b.changeOwner();
                                }
                            }*/
                        }
                    }
                    
                    
                    if (tempB.getX() <= 0 || tempB.getX() >= f.WIDTH - b.getSize())
                        b.reverseX();
                    
                    if (tempB.getY() <= 0) {
                        b.reset();
                        for (Paddle p : f.paddles) {
                            if (p.getId() == 0)
                                p.setScore(100);
                        }
                    }
                    
                    if (tempB.getY() >= f.HEIGHT- b.getSize()) {
                        b.reset();
                        for (Paddle p : f.paddles) {
                            if (p.getId() == 1)
                                p.setScore(100);
                        }
                    }
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

        
        Thread chkPowerUps = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!f.gameOverFlag) {
                    if (!f.powerUps.isEmpty()) {
                        String[] powerUpsS = f.usePowerUpsList(0, null).split(";");
                        for (String power : powerUpsS) {
                            if (power != "") {
                                String[] pow = power.split(",");
                                PowerUp p = new PowerUp(0, Integer.parseInt(pow[6]), Integer.parseInt(pow[3]), Integer.parseInt(pow[4]));
                                if (f.paddles.get(p.getSpownedBy()).getBounds().intersects(p.getBounds())) {
                                    p.setTypeString(pow[2]);
                                    f.usePowerUpsList(3, p);
                                }
                            }
                        }
                    }
                    try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
                }
            }
        });
        
        // Verifica se è finitio il gioco
        Thread chkEnding = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!f.gameOverFlag) {
                    if (f.bricks.isEmpty()) {
                        f.gameOverFlag = true;
                    }
                    try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
                }
            }
        });
        
        chkBricks.start();
        chkPowerUps.start();
        chkEnding.start();
    }
}