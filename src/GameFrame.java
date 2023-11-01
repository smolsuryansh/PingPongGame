import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame{

    GamePanel panel;

    // Constructor
    GameFrame(){

        panel = new GamePanel();
        this.add(panel);
        this.setTitle("PingPong");
        this.setResizable(false);
        this.setBackground(new Color(34,34,34));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
}

