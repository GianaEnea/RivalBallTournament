import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

public class myFrame extends JFrame{

    public myPanel panel;
    public boolean isMouseClicked = false;
    public int mousePosition = client.WIDTH / 2 - 100 / 2;

    public myFrame() {
        panel = new myPanel();
        //listener per la posizione del mouse e il click
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {}
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {}
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {}
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                isMouseClicked = false;
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                isMouseClicked = true;
            }
        });
        panel.addMouseMotionListener(new MouseMotionListener() {
           @Override
           public void mouseDragged(java.awt.event.MouseEvent e) {} 
           @Override
               public void mouseMoved(java.awt.event.MouseEvent e) {
                    mousePosition = e.getX();
               }
        });

        //settaggio delle inpostazioni della finestra
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setTitle("Rival Ball Tournament");
    }
}
