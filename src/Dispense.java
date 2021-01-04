import javax.swing.ImageIcon;


public class Dispense extends Sprite {


    public Dispense() {
    }

    public Dispense(int x, int y) {

        initDispense(x, y);
    }

    private void initDispense(int x, int y) {

        var shotImg = "src/Images/water.png"; //setting the image for the water projectile
        var ii = new ImageIcon(shotImg);
        setImage(ii.getImage());

        int H_SPACE = 25; //these two small blocks of code determine where the water projectile initially spawns in comparison to the player coordinates
        setX(x + H_SPACE);

        int V_SPACE = 10;
        setY(y - V_SPACE);

        musicStuff Music = new musicStuff(); //call the music method

 //We run this play sound method in a new thread otherwise the game would pause until the sound has been played, which we dont want
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            Music.playSound("src/Laser.wav"); // This plays the shooting sound for the gun when the player presses space bar
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }
}