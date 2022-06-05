import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class All extends JPanel {
    public All() {
        this.setPreferredSize(new Dimension(500, 500));
        JFrame frame = new JFrame();
        frame.setTitle("Genetic-Simulator");
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new All();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        repaint();
    }
}
