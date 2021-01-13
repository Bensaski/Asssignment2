import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

public class Covid extends Sprite { //This creates the Covid Enemy

    private Germ germ;
    private Mask mask;

    public Covid(int x, int y) {
        initCovid(x, y); //initialise covid
    }

    private void initCovid(int x, int y) {

        this.x = x;
        this.y = y;

        germ = new Germ(x, y); //germ class
        mask = new Mask(x, y); //mask class

        var CovidImg = "src/Images/covid4.png"; //gets the covid4 png for the enemy image
        var ii = new ImageIcon(CovidImg);


        setImage(ii.getImage());
    }

    public void act(int direction) {

        this.x += direction; //the direction for which the covid needs to move. eg left or right

    }

    public Germ getGerm() {
        return germ;
    }

    public class Germ extends Sprite { //creates the Germ projectile. This spawns from the UKCovid and Covid enemies

        private boolean destroyed;

        public Germ(int x, int y) {
            initGerm(x, y); //initalise the germ with the covid x and y coordinates
        }

        private void initGerm(int x, int y) {
            setDestroyed(true);

            this.x = x;
            this.y = y;

            var germImg = "src/Images/CovidGerm.png";
            var ii = new ImageIcon(germImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) { //The destroyed methods are
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }

    public Mask getMask() {
        return mask;
    }

    public class Mask extends Sprite {
        private boolean destroyed;

        public Mask(int x, int y) {
            initMask(x, y); // initliase the mask with the covid x and y coordinates
        }

        private void initMask(int x, int y) {
            setDestroyed2(true);
            this.x = x;
            this.y = y;

            var maskImg = "src/Images/mask.png";
            var ii = new ImageIcon(maskImg);
            setImage(ii.getImage());
        }

        public void setDestroyed2(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed2() {
            return destroyed;
        }
    }

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class CovidInvasion extends JFrame {

    public CovidInvasion() {


        BoardCreation(); //Creates the board

    }

    private void BoardCreation() {
        add(new Map2()); //adds the map to the screen

        setTitle("Covid Invasion - Ben Sadler BS19624");
        setSize(Variables.BoardWidth, Variables.BoardHeight); //sets the size of the map to the boardwidth and boardheight variable in the variables class

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false); //we dont want the window to be resizable as that will affect the difficulty of the game. A wider screen would mean the covid enemies will take longer to invade, which will affect high scores.
        setLocationRelativeTo(null);


    }


    public static void main(String[] args) {

        //This message dialog shows the aim of the game and the rules. At the bottom is a link to the gov.uk website for coronavirus advice and guidelines.
        JOptionPane.showMessageDialog(null, "COVID-19 has infested various regions in the world and it is your job to keep it at bay" +
                "\nBy using your flashy soap dispensing gun, blast away the virus. Prevent it from coming within two meters of you or you will be infected too!" +
                "\n Masks can occasionally spawn that can prevent the virus from entering your lungs. If a virus comes in contact with it, the mask will be taken away and prevents you from getting infected." +
                "\n \nRules : \n" +
                "1. Press SPACEBAR to shoot your Soap Dispensing Gun" +
                "\n2. Press Left and Right Arrow Keys to move Left and Right" +
                "\n3. Destroy all Covid Viruses to win the game before they reach the floor level, or you will lose the game" +
                "\n4. Avoid the Blue germs or they will take a life away. If you run out of lives and get hit again, you will lose the game" +
                "\n5. Pick up the White Masks to gain a Life up to 3 Maximum Lives, once at 3 Masks/Lives, you gain courage against the virus and your soap dispensing gun will now shoot faster." +
                "\n\n Coronavirus is a REAL threat in our daily lives and should be taken seriously, practice social distancing like in the game and make sure to wash your hands often. For more information against the virus, please check out the following link :\nhttps://www.gov.uk/coronavirus");

        Variables var = new Variables();
        var.Choices();


        EventQueue.invokeLater(() -> {
            var ex = new CovidInvasion();
            ex.setVisible(true);

        });
        String BG = "src/BG.wav"; //begins playing the background music for the game. The music was downloaded from : https://www.youtube.com/watch?v=5Eys2CYYU0w. The user states that the music is fine to be downloaded as long as it is credited
        musicStuff musicObject = new musicStuff();
        musicObject.playSound(BG);


    }


}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Dispense extends Sprite { //This class is for when the player shoots the projectile by pressing spacebar


    public Dispense() {
    }

    public Dispense(int x, int y) {

        initDispense(x, y); //sets the dispenser projectile where the player x and y coordinates are
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

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Map2 extends JPanel {


    private Dimension d;
    private java.util.List<Covid> covids; //the list of covid enemies
    private java.util.List<UKCovid> ukcovids; //the list of uk covid enemies
    private Player player;
    private Dispense dispense;

    private int direction = -1; //the initial direction of the covid enemies
    private int direction2 = 2;
    private int deaths = 0;
    int Life = 0; //the amount of life the player has depending on how many masks they have picked up (Max will be 3)
    BufferedImage img;
    Variables Variables = new Variables();


    private boolean inGame = true;
    private String message = "Game Over";
    String explImg = "src/images/Explosion.png";
    private Timer timer;
    long start = System.currentTimeMillis(); //this starts the timer based on the current system time. This will be used to score the user on how fast they kill the covid invasion
    String name = Variables.name; //This will call the code to enter the players name in the Variables class


    public Map2() {

        initMap(); //initialises the map
        gameInit();
        bgimage();
    }

    private void initMap() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Variables.BoardWidth, Variables.BoardHeight); //this is the dimensions for the board that the player and covid enemies will move around
        setBackground(Color.black);


        timer = new Timer(Variables.Delay, new GameCycle()); //this starts the timer and gamecycle method
        timer.start();


        gameInit();

    }

    private void gameInit() { //This Body of code will create a list of covid and ukcovid enemies depending on what difficulty the player chose.

        covids = new ArrayList<>();

        for (int i = 0; i < Variables.CovidRow; i++) {
            for (int j = 0; j < Variables.CovidColumn; j++) { //This determines how many rows and columns of Covid Spawn

                var CovidActor = new Covid(Variables.CovidInitX + 70 * j, //this determines the gap between each covid
                        Variables.CovidInitY + 50 * i);
                covids.add(CovidActor);
            }
        }

        ukcovids = new ArrayList<>();

        for (int i = 0; i < Variables.UKCovidRow; i++) {
            for (int j = 0; j < Variables.UKCovidColumn; j++) { //This determines how many rows and columns of Covid Spawn

                var UKCovidActor = new UKCovid(Variables.UKCovidInitX + 50 * j, //this determines the gap between each covid
                        Variables.UKCovidInitY + 50 * i);
                ukcovids.add(UKCovidActor);
            }
        }

        player = new Player();
        dispense = new Dispense();
    }

    private void drawCovids(Graphics g) { // this body of code draws the covid and uk covid lists of enemies to the screen

        for (Covid covid : covids) {

            if (covid.isVisible()) {

                g.drawImage(covid.getImage(), covid.getX(), covid.getY(), this); //draw to screen
            }

            if (covid.isDying()) {

                covid.die(); //if the covid enemy is set to isDying, then set covid.die and remove the covid enemy
            }
        }

        for (UKCovid ukcovid : ukcovids) {

            if (ukcovid.isVisible()) {

                g.drawImage(ukcovid.getImage(), ukcovid.getX(), ukcovid.getY(), this);
            }

            if (ukcovid.isDying()) {

                ukcovid.die();
            }
        }
    }

    private void drawPlayer(Graphics g) { //this body of code draws the player on to the screen

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this); //only drawn if the player is set to visible
        }

        if (player.isDying()) {

            player.die();
            inGame = false; //leave the game if the player has died
        }
    }

    private void drawShot(Graphics g) { //this body of code draws the dispense function which is what we use as a projectile

        if (dispense.isVisible()) {

            g.drawImage(dispense.getImage(), dispense.getX(), dispense.getY(), this);
        }
    }

    private void drawGerms(Graphics g) { //this body of code draws the germs that have a random chance of spawning from the enemies

        for (Covid a : covids) {

            Covid.Germ b = a.getGerm();

            if (!b.isDestroyed()) { //only if the germ is not destroyed

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }

        for (UKCovid a : ukcovids) {

            UKCovid.Germ b = a.getGerm();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    private void drawMask(Graphics g) { //this body of code draws the masks that have a random chance of spawning from the enemies
        for (Covid a : covids) {

            Covid.Mask b = a.getMask();

            if (!b.isDestroyed2()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }


    public void bgimage() { //this body of code sets the background image for the game

        try {
            img = ImageIO.read(new File("src/Images/BGGame.jpg")); //the background image for the game was downloaded from : https://www.desktopbackground.org/wallpaper/8-bit-retro-game-console-freebie-claudio-gomboli-dribbble-iphone-5-735953

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) { //creates the paint component
        super.paintComponent(g);

        g.drawImage(img, 0, 0, null);


        doDrawing(g);

    }

    private void doDrawing(Graphics g) { //Does the drawing for all the bodys of code that are called here.


        g.setColor(Color.black);
        //g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Life : " + Life, 100, 950);


        if (inGame) {


            g.drawLine(0, Variables.FloorLevel,
                    Variables.BoardWidth, Variables.FloorLevel); //droors the floor level that the player is on

            drawCovids(g);
            drawPlayer(g);
            drawShot(g);  //calls all the drawing methods
            drawGerms(g);
            drawMask(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) { // creates the game over screen when the player loses

        g.setColor(Color.black);
        g.fillRect(0, 0, Variables.BoardWidth, Variables.BoardHeight);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Variables.BoardWidth / 2 - 30, Variables.BoardWidth - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Variables.BoardWidth / 2 - 30, Variables.BoardWidth - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Variables.BoardWidth - fontMetrics.stringWidth(message)) / 2,
                Variables.BoardWidth / 2);
    }


    private void update() { //this body of code runs throughout the game

        long TimeElapsed = 0;
        if (deaths == Variables.AmountOfCovidToKill) {

            inGame = false;
            timer.stop();
            message = "Game won! The Vaccine has been distributed across the population whilst you were fighting off the Covid Invasion";
            long end = System.currentTimeMillis();
            TimeElapsed = (end - start) / 1000;
            //System.out.println("It took you " +(end-start)/1000 + " Seconds to destroy the Covid Invasion!");
            JOptionPane.showMessageDialog(null, "It took you " + TimeElapsed + " Seconds to destroy the Covid Invasion!"); ///This body of cody is ran when the player kills the covid invasion and wins the game, It will output the time it took for them to win and write it to the text file for the correct difficulty

            if (Variables.Choice.equals("Easy") || Variables.Choice.equals("easy")) {
                File myFile = new File("src/HighScoreEasy.txt");
                FileWriter fw;
                try {
                    fw = new FileWriter(myFile, true);
                    fw.write(name + ": " + TimeElapsed + "\n");
                    fw.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }

                java.util.List<Person> persons;
                try {//this sorts the highscore text files into order so that the lowest time spent is the top score.
                    persons = Files.lines(Path.of("src/HighScoreEasy.txt"))
                            .map(Person::parseLine)
                            .collect(Collectors.toList());

                    persons.sort(Comparator.comparingInt(Person::getScore));

                    java.util.List<String> lines = persons.stream()
                            .map(Person::toLine)
                            .collect(Collectors.toList());
                    Files.write(Path.of("src/HighScoreEasy.txt"), lines);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    HighScore();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (Variables.Choice.equals("Hard") || Variables.Choice.equals("hard")) {
                File myFile = new File("src/HighScoreHard.txt");
                FileWriter fw;
                try {
                    fw = new FileWriter(myFile, true);
                    fw.write(name + ": " + TimeElapsed + "\n");
                    fw.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }

                java.util.List<Person> persons;
                try {
                    persons = Files.lines(Path.of("src/HighScoreHard.txt"))
                            .map(Person::parseLine)
                            .collect(Collectors.toList());

                    persons.sort(Comparator.comparingInt(Person::getScore));

                    java.util.List<String> lines = persons.stream()
                            .map(Person::toLine)
                            .collect(Collectors.toList());
                    Files.write(Path.of("src/HighScoreHard.txt"), lines);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    HighScore();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (Variables.Choice.equals("Endless") || Variables.Choice.equals("endless")) {
                File myFile = new File("src/HighScoreEndless.txt");
                FileWriter fw;
                try {
                    fw = new FileWriter(myFile, true);
                    fw.write(name + ": " + TimeElapsed + "\n");
                    fw.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }

                java.util.List<Person> persons;
                try {
                    persons = Files.lines(Path.of("src/HighScoreEndless.txt"))
                            .map(Person::parseLine)
                            .collect(Collectors.toList());

                    persons.sort(Comparator.comparingInt(Person::getScore));

                    java.util.List<String> lines = persons.stream()
                            .map(Person::toLine)
                            .collect(Collectors.toList());
                    Files.write(Path.of("src/HighScoreEndless.txt"), lines);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    HighScore();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        // player
        player.act();

        // shot

        if (dispense.isVisible()) { //checks wether a shot is already on the screen

            int shotX = dispense.getX();
            int shotY = dispense.getY();

            for (Covid CovidActors : covids) {

                int CovidActorX = CovidActors.getX();
                int CovidActorY = CovidActors.getY();

                if (CovidActors.isVisible() && dispense.isVisible()) { //this checks wether the the dispense projectile and the covid actor have collided with eachother. If they have, then set the covid actor to die and destroy the dispense projectile
                    if (shotX >= (CovidActorX)
                            && shotX <= (CovidActorX + Variables.CovidWidth)
                            && shotY >= (CovidActorY)
                            && shotY <= (CovidActorY + Variables.CovidHeight)) {

                        var ii = new ImageIcon(explImg); //shows an explosion image when the covid actor gets hit and dies
                        CovidActors.setImage(ii.getImage());
                        CovidActors.setDying(true);
                        deaths++; // increases death counter which is checked against the CovidAmountToKill variable and when they are the same the game is won.
                        dispense.die(); //remove dispense projectile
                    }
                }
            }
            for (UKCovid UkCovidActors : ukcovids) { //same as above but for the ukcovid enemies

                int UkCovidActorX = UkCovidActors.getX();
                int UkCovidActorY = UkCovidActors.getY();

                if (UkCovidActors.isVisible() && dispense.isVisible()) {
                    if (shotX >= (UkCovidActorX)
                            && shotX <= (UkCovidActorX + Variables.CovidWidth)
                            && shotY >= (UkCovidActorY)
                            && shotY <= (UkCovidActorY + Variables.CovidHeight)) {

                        var ii = new ImageIcon(explImg);
                        UkCovidActors.setImage(ii.getImage());
                        UkCovidActors.setDying(true);
                        deaths++;
                        dispense.die();
                    }
                }
            }

            int y = dispense.getY();
            if (Life < 3) { //if life is less than 3, set the dispenser to the default speed
                y -= 20; //the speed of the dispenser shot
            }
            if (Life == 3) { //if life is 3 then increase dispenser speed
                y -= 40; //the speed of the dispenser shot
            }

            if (y < 0) {
                dispense.die();
            } else {
                dispense.setY(y);
            }
        }
        class CovidMoveDown extends Thread { //the class for the covid variable moving down
            @Override
            public void run() {
                for (Covid covid : covids) { //changing directions when the covid enemies hit the edge
                    int x = covid.getX();
                    if (x >= Variables.BoardWidth - Variables.BorderRight && direction != -1) {
                        direction = -1;
                        for (Covid a2 : covids) {
                            a2.setY(a2.getY() + Variables.GoDown);
                        }
                    }
                    if (x <= Variables.BorderLeft && direction != 1) {
                        direction = 1;
                        for (Covid a : covids) {
                            a.setY(a.getY() + Variables.GoDown);
                        }
                    }
                }
                for (Covid covid : covids) {
                    if (covid.isVisible()) {
                        int y = covid.getY();
                        if (y > Variables.FloorLevel - Variables.CovidHeight) { //if the covid enemy reaches the floor level, then the game is lost and covid has invaded
                            inGame = false;
                            message = "Invasion!";
                        }
                        covid.act(direction);
                    }
                }
            }
        }


        class UkCovidMoveDown extends Thread { //same as above but for the uk covid
            @Override
            public void run() {

                for (UKCovid ukcovid : ukcovids) {

                    int x2 = ukcovid.getX();
                    if (x2 >= Variables.BoardWidth - Variables.BorderRight && direction2 != -2) {
                        direction2 = -2;
                        for (UKCovid a2 : ukcovids) {


                            a2.setY(a2.getY() + Variables.GoDown2);
                        }
                    }
                    if (x2 <= Variables.BorderLeft && direction2 != 2) {
                        direction2 = 2;
                        for (UKCovid a2 : ukcovids) {

                            a2.setY(a2.getY() + Variables.GoDown2);

                        }
                    }
                }
                for (UKCovid ukcovid : ukcovids) {
                    if (ukcovid.isVisible()) {
                        int y = ukcovid.getY();
                        if (y > Variables.FloorLevel - Variables.UKCovidHeight) {
                            inGame = false;
                            message = "Invasion!";
                        }
                        ukcovid.UKAct(direction2);
                    }
                }
            }
        }
        CovidMoveDown threadA = new CovidMoveDown();
        threadA.start();
        UkCovidMoveDown threadB = new UkCovidMoveDown();
        threadB.start();

        // Germ generation
        var generator = new Random(); //creates a random number generator for determining when to spawn a germ

        for (Covid covid : covids) {

            int shot = generator.nextInt(10000);
            Covid.Germ germ = covid.getGerm();

            if (shot == Variables.Chance && covid.isVisible() && germ.isDestroyed()) { //if the shot generator is the same as the chance variable, then spawn a germ

                germ.setDestroyed(false);
                germ.setX(covid.getX()); //spawns where the covid enemy is
                germ.setY(covid.getY());
            }


            int germX = germ.getX();
            int germY = germ.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !germ.isDestroyed()) {

                if (germX >= (playerX) && germX <= (playerX + Variables.PlayerWidth)
                        && germY >= (playerY)
                        && germY <= (playerY + Variables.PlayerHeight)) { //checks if the germ coordinates are within the player hitbox/collision coordinates
                    germ.setDestroyed(true);


                    if (Life <= 0) { //if the player life is less than 0 then the player dies and looses the game
                        System.out.println(Life);
                        player.setDying(true);

                    } else { //the player looses a life if they have more than 1 life
                        Life = Life - 1;
                        germ.setDestroyed(true);
                    }
                }
            }


            if (!germ.isDestroyed()) {

                germ.setY(germ.getY() + 1);

                if (germ.getY() >= Variables.FloorLevel - Variables.GermHeight - 10) { //if the germ  gets to just above the floor level, destroy it

                    germ.setDestroyed(true);
                }
            }
        }

        for (UKCovid ukcovid : ukcovids) { //same as above but for the uk covid

            int shot2 = generator.nextInt(2000);
            UKCovid.Germ germ2 = ukcovid.getGerm();

            if (shot2 == Variables.Chance && ukcovid.isVisible() && germ2.isDestroyed()) {

                germ2.setDestroyed(false);
                germ2.setX(ukcovid.getX());
                germ2.setY(ukcovid.getY());
            }

            int germX = germ2.getX();
            int germY = germ2.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !germ2.isDestroyed()) {

                if (germX >= (playerX) && germX <= (playerX + Variables.PlayerWidth)
                        && germY >= (playerY)
                        && germY <= (playerY + Variables.PlayerHeight)) {
                    germ2.setDestroyed(true);

                    // var ii = new ImageIcon(explImg);
                    //player.setImage(ii.getImage());
                    if (Life <= 0) {
                        System.out.println(Life);
                        player.setDying(true);

                    } else {
                        Life = Life - 1;
                    }
                }
            }


            if (!germ2.isDestroyed()) {

                germ2.setY(germ2.getY() + 1);

                if (germ2.getY() >= Variables.FloorLevel - Variables.GermHeight) {

                    germ2.setDestroyed(true);
                }
            }
        }


        var generator2 = new Random(); //the same method for generating the germ but for the mask
        for (Covid covid : covids) {
            int shoot = generator2.nextInt(10000); //determines the chance of a mask spawning
            Covid.Mask mask = covid.getMask();

            if (shoot == Variables.Chance && covid.isVisible() && mask.isDestroyed2()) {
                mask.setDestroyed2(false);
                mask.setX(covid.getX());
                mask.setY(covid.getY());
            }
            int maskX = mask.getX();
            int maskY = mask.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !mask.isDestroyed2()) {
                if (maskX >= (playerX) && maskX <= (playerX + Variables.PlayerWidth + 20)
                        && maskY >= (playerY)
                        && maskY <= (playerY + Variables.PlayerHeight + 20)) {
                    mask.setDestroyed2(true);
                    musicStuff Music = new musicStuff(); //call the music method
                    if (Life <= 2) {
                        Life = Life + 1;
                        System.out.println(Life);


                        //We run this play sound method in a new thread otherwise the game would pause until the sound has been played, which we dont want
                        new Thread(
                                () -> {
                                    try {
                                        Music.playSound("src/Mask.wav"); // This plays the shooting sound for picking up the masks
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }).start();
                        if (Life == 3) {
                            new Thread(
                                    () -> {
                                        try {
                                            Music.playSound("src/PowerUp.wav"); // This plays power up sound when the player hits 3 lives and they begin shooting faster
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }).start();
                        }


                    }

                }
            }
            if (!mask.isDestroyed2()) {

                mask.setY(mask.getY() + 1);

                if (mask.getY() >= Variables.FloorLevel - Variables.MaskHeight) {

                    mask.setDestroyed2(true);
                }
            }


        }
    }

    public void HighScore() throws IOException { //This method shows the highscore box
        String input = "";
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        if (Variables.Choice.equals("Easy") || Variables.Choice.equals("easy")) {
            BufferedReader reader = new BufferedReader(new FileReader("src/HighScoreEasy.txt")); //Depending on the difficulty chosen, it will write to the file that corresponds to that difficulty


            String line = null;
            while ((line = reader.readLine()) != null & i < 5) { //This reads the first 5 lines of the text file since the assignment brief only wants to show the first 5 high scores

                //Add the line and then "\n" indicating a new line
                input += line + " Seconds\n";
                i += 1;


            }

            reader.close();
            JOptionPane.showMessageDialog(null, input, "High Scores Easy", JOptionPane.INFORMATION_MESSAGE); //Then show a message dialog box with the 5 high scores sorted. This is the same for the other difficulties
        }
        if (Variables.Choice.equals("Hard") || Variables.Choice.equals("hard")) {
            BufferedReader reader = new BufferedReader(new FileReader("src/HighScoreHard.txt"));


            String line = null;
            while ((line = reader.readLine()) != null & i2 < 5) {
                //Add the line and then "\n" indicating a new line
                input += line + " Seconds \n";
                i2 += 1;
            }
            reader.close();
            JOptionPane.showMessageDialog(null, input, "High Scores Hard", JOptionPane.INFORMATION_MESSAGE);

        }
        if (Variables.Choice.equals("Endless") || Variables.Choice.equals("endless")) {
            BufferedReader reader = new BufferedReader(new FileReader("src/HighScoreEndless.txt"));


            String line = null;
            while ((line = reader.readLine()) != null & i2 < 5) {
                //Add the line and then "\n" indicating a new line
                input += line + " Seconds\n";
                i2 += 1;
            }
            reader.close();
            JOptionPane.showMessageDialog(null, input, "High Scores Endless", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private void doGameCycle() { //this runs constantly

        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) { //for shooting the dispense projectile on spacebar press

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY(); //gets the players coordinates

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) { //when spacebar is pressed, if the game is running and a dispense projectile isnt on the screen, shoot the projectile. You can only have one dispense projectile on the screen at a time

                if (inGame) {

                    if (!dispense.isVisible()) {

                        dispense = new Dispense(x, y);
                    }
                }
            }
        }
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class musicStuff { //this class holds the method for playing a music file

    private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;

    /**
     * @param filename the name of the file that is going to be played
     */
    public void playSound(String filename) {

        String strFilename = filename;

        try {
            soundFile = new File(strFilename); //sets the soundFile variables to the sound file name
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile); //gets the audioinputstream of the sound file
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat(); //determines the audio format eg wav,mp3 etc

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE]; //sets the buffer size
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Person { //this class is for splitting the highscore file out to then write the players score to the file and sort it

    String name;
    int score;

    public Person(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String toLine() {
        return name + ": " + score;
    }

    public int getScore() {
        return score;
    }

    public static Person parseLine(String line) {
        String[] data = line.split(": "); //splits the elements from ": "
        String name = data[0];
        int score = Integer.parseInt(data[1]);
        return new Person(name, score);
    }


}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Player extends Sprite { //the class for the player

    private int width;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {

        var playerImg = "src/Images/Dispenser.png";
        var ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());

        int Start_x = 500; // this is where the player spawns in x
        setX(Start_x);

        int Start_y = 830; //the level at which the player spawns (just above floor level)
        setY(Start_y);
    }

    public void act() {
        x += dx;
        if (x <= 2) {
            x = 2;
        }
        if (x >= Variables.BoardWidth - 1 * width) { //only does the movement if the player is not on the edge of the screen

            x = Variables.BoardWidth - 1 * width;
        }
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) { // when the player presses the left arrow, they move left by 4

            dx = -4;
        }

        if (key == KeyEvent.VK_RIGHT) { //when the player presses the right arrow, they move right by 4

            dx = 4;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) { //sets the direction movement to 0 when a key is released

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Sprite { //the sprite class is used and extended from the player and covids.
    private boolean visible;
    private Image image;
    private boolean dying;

    int x;
    int y;
    int dx;

    public Sprite() {

        visible = true; // sets the sprite to visible initialliy
    }

    public void die() {

        visible = false; //if the sprite has died, set the visibility to false
    }

    public boolean isVisible() {

        return visible; //checks visibility
    }

    protected void setVisible(boolean visible) {

        this.visible = visible; //sets visibility
    }

    public void setImage(Image image) {

        this.image = image; //sets the sprite to the image file
    }

    public Image getImage() {

        return image; //gets the image
    }

    public void setX(int x) {

        this.x = x; //gets the x coordinate
    }

    public void setY(int y) {

        this.y = y; //gets the y coordinate
    }

    public int getY() {

        return y;
    }

    public int getX() {

        return x;
    }

    public void setDying(boolean dying) {

        this.dying = dying; //sets dying
    }

    public boolean isDying() {

        return this.dying; //gets the dying state
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class UKCovid extends Sprite { // the class for the UkCovid, this is the same as the Covid class but slightly altered for UkCovid
    private UKCovid.Germ germ;
    private UKCovid.Mask mask;

    public UKCovid(int x, int y) {
        initUkCovid(x, y);
    }

    private void initUkCovid(int x, int y) {

        this.x = x;
        this.y = y;

        germ = new UKCovid.Germ(x, y);
        mask = new UKCovid.Mask(x, y);

        var CovidImg = "src/Images/UkCovid.png";
        var ii = new ImageIcon(CovidImg);


        setImage(ii.getImage());
    }

    public void UKAct(int direction) {

        this.x += direction;

    }

    public UKCovid.Germ getGerm() {
        return germ;
    }

    public class Germ extends Sprite {

        private boolean destroyed;

        public Germ(int x, int y) {
            initGerm(x, y);
        }

        private void initGerm(int x, int y) {
            setDestroyed(true);

            this.x = x;
            this.y = y;

            var germImg = "src/Images/CovidGerm.png";
            var ii = new ImageIcon(germImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }

    public UKCovid.Mask getMask() {
        return mask;
    }

    public class Mask extends Sprite {
        private boolean destroyed;

        public Mask(int x, int y) {
            initMask(x, y);
        }

        private void initMask(int x, int y) {
            setDestroyed2(true);
            this.x = x;
            this.y = y;

            var maskImg = "src/Images/mask.png";
            var ii = new ImageIcon(maskImg);
            setImage(ii.getImage());
        }

        public void setDestroyed2(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed2() {
            return destroyed;
        }
    }

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Variables { //The variable class holds the statistics for the game logic.
    public static int BoardHeight = 1000; //the board height
    public static int BoardWidth = 1000; //the board width
    public static int BorderLeft = 5;
    public static int BorderRight = 30;

    public static int FloorLevel = 900; //the floor level which the player plays on and the covid enemies try to invade to
    public static int GermHeight = 20; // germ height
    public static int MaskHeight = 20; //mask height

    public static int CovidHeight = 50; //Covid Height and Width determine the hitbox/collision for the covid enemies. This is the same for the UkCovid
    public static int CovidWidth = 60;
    public static int CovidInitX = 150; //These variables set the inital spawn of the covid enemies
    public static int CovidInitY = 60;
    public static int UKCovidInitX = 150; //where the uk covid spawn
    public static int UKCovidInitY = 5;
    public static int UKCovidHeight = 50;
    public static int UKCovidWidth = 60;
    public static int GoDown = 50; //This is how far down the y axis the covid enemy will go down after reaching the edge
    public static int GoDown2 = 100; //This is how far down the y axis the covid enemy will go down after reaching the edge
    public static int AmountOfCovidToKill = 50; //This is how many covid enemies need to be killed for the player to win the game
    public static int Chance = 1; //number which is used for the chance of spawning a germ or mask
    public static int Delay = 10;
    public static int PlayerWidth = 20; //the player hitbox/collision
    public static int PlayerHeight = 20;
    public static int CovidRow = 1; //how many rows of covid spawn
    public static int CovidColumn = 5; //how many columns of covid spawn
    public static int UKCovidRow = 5; // how many rows of ukcovid spawn
    public static int UKCovidColumn = 5; //how many columns of ukcovid spawn
    public static String Choice;
    public static String name;
    public static boolean chosen;


    public void Choices() {
        Variables.chosen = false;
        while (!chosen) {
            String Difficulty = JOptionPane.showInputDialog("Which Difficulty would you like? Easy,Hard or Extreme"); //gets the chosen difficulty for
            Variables.Choice = Difficulty;
            String name = JOptionPane.showInputDialog("Please enter Player Name");
            Variables.name = name;

            if (Difficulty.equals("Easy") || Difficulty.equals("easy")) {
                Variables.BoardHeight = 1000;
                Variables.BoardWidth = 1000;
                Variables.BorderLeft = 5;
                Variables.BorderRight = 30;
                Variables.FloorLevel = 900;
                Variables.GermHeight = 10;
                Variables.MaskHeight = 40;
                Variables.CovidHeight = 50;
                Variables.CovidWidth = 60;
                Variables.CovidInitX = 150;
                Variables.CovidInitY = 60;
                Variables.UKCovidInitX = 150;
                Variables.UKCovidInitY = 5;
                Variables.UKCovidHeight = 50;
                Variables.UKCovidWidth = 40;
                Variables.GoDown = 50;
                Variables.GoDown2 = 100;
                Variables.AmountOfCovidToKill = 40;
                Variables.Chance = 1;
                Variables.Delay = 10;
                Variables.PlayerWidth = 30;
                Variables.PlayerHeight = 20;
                Variables.CovidRow = 3;
                Variables.CovidColumn = 10;
                Variables.UKCovidRow = 2;
                Variables.UKCovidColumn = 5;
                Variables.chosen = true;
            } else if (Difficulty.equals("Hard") || Difficulty.equals("hard")) {
                Variables.BoardHeight = 1000;
                Variables.BoardWidth = 1000;
                Variables.BorderLeft = 5;
                Variables.BorderRight = 30;

                Variables.FloorLevel = 900;
                Variables.GermHeight = 10;
                Variables.MaskHeight = 40;

                Variables.CovidHeight = 50;
                Variables.CovidWidth = 60;
                Variables.CovidInitX = 150;
                Variables.CovidInitY = 60;
                Variables.UKCovidInitX = 150;
                Variables.UKCovidInitY = 5;
                Variables.UKCovidHeight = 50;
                Variables.UKCovidWidth = 60;

                Variables.GoDown = 50;
                Variables.GoDown2 = 100;
                Variables.AmountOfCovidToKill = 100;
                Variables.Chance = 1;
                Variables.Delay = 10;
                Variables.PlayerWidth = 30;
                Variables.PlayerHeight = 20;
                Variables.CovidRow = 8;
                Variables.CovidColumn = 10;
                Variables.UKCovidRow = 2;
                Variables.UKCovidColumn = 10;
                Variables.chosen = true;
            } else if (Difficulty.equals("Endless") || Difficulty.equals("endless")) {
                Variables.BoardHeight = 1000;
                Variables.BoardWidth = 1000;
                Variables.BorderLeft = 5;
                Variables.BorderRight = 30;

                Variables.FloorLevel = 900;
                Variables.GermHeight = 10;
                Variables.MaskHeight = 40;

                Variables.CovidHeight = 50;
                Variables.CovidWidth = 60;
                Variables.CovidInitX = 150;
                Variables.CovidInitY = -2500;
                Variables.UKCovidInitX = 150;
                Variables.UKCovidInitY = 5;
                Variables.UKCovidHeight = 50;
                Variables.UKCovidWidth = 60;

                Variables.GoDown = 50;
                Variables.GoDown2 = 100;
                Variables.AmountOfCovidToKill = 550;
                Variables.Chance = 1;
                Variables.Delay = 10;
                Variables.PlayerWidth = 30;
                Variables.PlayerHeight = 20;
                Variables.CovidRow = 50;
                Variables.CovidColumn = 10;
                Variables.UKCovidRow = 5;
                Variables.UKCovidColumn = 10;
                Variables.chosen = true;
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a correct difficulty");
            }
        }
    }
}
















