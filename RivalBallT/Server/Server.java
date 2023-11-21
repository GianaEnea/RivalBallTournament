package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Server extends JFrame {
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;

    private BufferedImage buffer;
    private Graphics bufferGraphics;

    public Server() {
        setTitle("Brick Breaker");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bufferGraphics = buffer.getGraphics();

        ball = new Ball(WIDTH / 2, HEIGHT / 2);
        paddle = new Paddle(WIDTH / 2 - Paddle.WIDTH / 2, HEIGHT - 50);
        bricks = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 5; j++) {
                bricks.add(new Brick(i * 50 + 30, j * 40 + 50));
            }
        }

        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                update();
                repaint();
            }
        });

        timer.start();

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                paddle.move(e.getX());
                repaint();
            }
        });

        setFocusable(true);
    }

    private void update() {
        ball.move();
        checkCollision();
    }

    private void checkCollision() {
        // Verifica collisione con il paddle
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseY();
        
            int paddleCenterX = paddle.getX() + Paddle.WIDTH / 2;
            int ballCenterX = ball.getX() + Ball.SIZE / 2;

            // Calcola la posizione relativa della palla rispetto al centro della paddle
            int relativePosition = ballCenterX - paddleCenterX;

            if (relativePosition < -Paddle.WIDTH / 3) {
                // La palla ha toccato la parte sinistra della paddle
                ball.setAngle(ball.getAngle()-20);
            } else if (relativePosition > Paddle.WIDTH / 3) {
                // La palla ha toccato la parte destra della paddle
                ball.setAngle(ball.getAngle()+20);
            } else {
                // La palla ha toccato la parte centrale della paddle
                // la palla rimbalza normale senza modificare l'algolo
            }
        }
        // Verifica collisione con i mattoni
        for (Brick brick : bricks) {
            if (ball.getBounds().intersects(brick.getBounds())) {
                bricks.remove(brick);
                ball.reverseY();
                break;
            }
        }

        // Verifica collisione con i bordi della finestra
        if (ball.getX() <= 0 || ball.getX() >= WIDTH - Ball.SIZE) {
            ball.reverseX();
        }

        if (ball.getY() <= 0) {
            ball.reverseY();
        }

        // Verifica se il giocatore ha perso
        if (ball.getY() >= HEIGHT) {
            JOptionPane.showMessageDialog(this, "Hai perso!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    @Override
    public void paint(Graphics g) {
        // Disegna sul buffer
        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.fillRect(0, 0, WIDTH, HEIGHT);

        ball.draw(bufferGraphics);
        paddle.draw(bufferGraphics);

        for (Brick brick : bricks) {
            brick.draw(bufferGraphics);
        }

        // Copia il buffer sulla finestra
        g.drawImage(buffer, 0, 0, this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Server().setVisible(true);
            }
        });
    }
}