import javax.swing.ImageIcon;

public class Dispense extends Sprite {
    public Dispense() {
    }

    public Dispense(int x, int y) {

        initDispense(x, y);
    }

    private void initDispense(int x, int y) {

        var shotImg = "src/Images/water.png";
        var ii = new ImageIcon(shotImg);
        setImage(ii.getImage());

        int H_SPACE = 25;
        setX(x + H_SPACE);

        int V_SPACE = 10;
        setY(y - V_SPACE);
    }
}
