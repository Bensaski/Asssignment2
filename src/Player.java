import javax.swing.*;
import java.awt.event.KeyEvent;

public class Player extends Sprite {

    private int width;
    public Player(){
        initPlayer();
    }

    private void initPlayer() {

        var playerImg = "src/Images/watergun.png";
        var ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());

        int Start_x = 270;
        setX(Start_x);

        int Start_y = 830; //the level at which the player spawns (just above floor level)
        setY(Start_y);
    }

    public void act() {
        x += dx;
        if (x <= 2) {
            x = 2;
        }
        if (x >= Variables.BoardWidth - 1 * width) {

            x = Variables.BoardWidth - 1 * width;
        }
    }
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = -4;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 4;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }
}
