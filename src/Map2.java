import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class Map2 extends JPanel {

    private Dimension d;
    private List<Covid> covids;
    private Player player;
    private Dispense dispense;

    private int direction = -1;
    private int deaths = 0;
    int Life = 0;



    private boolean inGame = true;
    private String message = "Game Over";
    String explImg = "src/images/Explosion.png";
    private Timer timer;
    long start = System.currentTimeMillis();
    String name = JOptionPane.showInputDialog("Please enter Player Name");



    public Map2(){
        initMap();
        gameInit();
    }

    private void initMap() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Variables.BoardWidth, Variables.BoardHeight);
        setBackground(Color.black);



        timer = new Timer(Variables.Delay, new GameCycle());
        timer.start();


        gameInit();

    }
    private void gameInit() {

        covids = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 11; j++) { //This determines how many rows and columns of Covid Spawn

                var CovidActor = new Covid(Variables.CovidInitX + 70 * j, //this determines the gap between each covid
                        Variables.CovidInitY + 50 * i);
                covids.add(CovidActor);
            }
        }

        player = new Player();
        dispense = new Dispense();
    }

    private void drawCovids(Graphics g) {

        for (Covid covid : covids) {

            if (covid.isVisible()) {

                g.drawImage(covid.getImage(), covid.getX(), covid.getY(), this);
            }

            if (covid.isDying()) {

                covid.die();
            }
        }
    }
    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {

        if (dispense.isVisible()) {

            g.drawImage(dispense.getImage(), dispense.getX(), dispense.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {

        for (Covid a : covids) {

            Covid.Germ b = a.getGerm();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }
    private void drawMask(Graphics g) {
        for (Covid a : covids) {

            Covid.Mask b = a.getMask();

            if (!b.isDestroyed2()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }





    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       /* final BufferedImage image;
        try {
            image = ImageIO.read(new File("src/Images/BG3.png"));
            g.drawImage(image, 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

*/
            doDrawing(g);

    }

    private void doDrawing(Graphics g) {


        g.setColor(Color.black);
        //g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Life : " + Life, 100, 950);



        if (inGame) {

            g.drawLine(0, Variables.FloorLevel,
                    Variables.BoardWidth, Variables.FloorLevel);

            drawCovids(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
            drawMask(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

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





     private void update() {

        long TimeElapsed = 0;
        if (deaths == Variables.AmountOfCovidToKill) {

            inGame = false;
            timer.stop();
            message = "Game won!";
            long end = System.currentTimeMillis();
            TimeElapsed = (end - start) / 1000;
            //System.out.println("It took you " +(end-start)/1000 + " Seconds to destroy the Covid Invasion!");
            JOptionPane.showMessageDialog(null, "It took you " + TimeElapsed + " Seconds to destroy the Covid Invasion!");
            File myFile = new File("highscore.txt");
            FileWriter fw;
            try {
                fw = new FileWriter(myFile, true);
                fw.write( name + ": " + Long.toString(TimeElapsed)+"\n");
                fw.close();



            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Person> persons;
            try {
                persons = Files.lines(Path.of("highscore.txt"))
                        .map(Person::parseLine)
                        .collect(Collectors.toList());

                persons.sort(Comparator.comparingInt(Person::getScore));

                List<String> lines = persons.stream()
                        .map(Person::toLine)
                        .collect(Collectors.toList());
                Files.write(Path.of("highscore.txt"), lines);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                HighScore();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        // player
        player.act();

        // shot

        if (dispense.isVisible()) {

            int shotX = dispense.getX();
            int shotY = dispense.getY();

            for (Covid CovidActors : covids) {

                int CovidActorX = CovidActors.getX();
                int CovidActorY = CovidActors.getY();

                if (CovidActors.isVisible() && dispense.isVisible()) {
                    if (shotX >= (CovidActorX)
                            && shotX <= (CovidActorX + Variables.CovidWidth)
                            && shotY >= (CovidActorY)
                            && shotY <= (CovidActorY + Variables.CovidHeight)) {

                        var ii = new ImageIcon(explImg);
                        CovidActors.setImage(ii.getImage());
                        CovidActors.setDying(true);
                        deaths++;
                        dispense.die();
                    }
                }
            }

            int y = dispense.getY();
            if(Life <3) {
                y -= 10; //the speed of the dispenser shot
            }
            if(Life == 3){
                y -= 20; //the speed of the dispenser shot
            }

            if (y < 0) {
                dispense.die();
            } else {
                dispense.setY(y);
            }
        }

        // Covid Actor movement

        for (Covid covid : covids) {

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

                if (y > Variables.FloorLevel - Variables.CovidHeight) {
                    inGame = false;
                    message = "Invasion!";
                }

                covid.act(direction);
            }
        }

        // Germ generation
        var generator = new Random();

        for (Covid covid : covids) {

            int shot = generator.nextInt(1000);
            Covid.Germ germ = covid.getGerm();

            if (shot == Variables.Chance && covid.isVisible() && germ.isDestroyed()) {

                germ.setDestroyed(false);
                germ.setX(covid.getX());
                germ.setY(covid.getY());
            }

            int germX = germ.getX();
            int germY = germ.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !germ.isDestroyed()) {

                if (germX >= (playerX) && germX <= (playerX + Variables.PlayerWidth)
                        && germY >= (playerY)
                        && germY <= (playerY + Variables.PlayerHeight)) {
                    germ.setDestroyed(true);

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


            if (!germ.isDestroyed()) {

                germ.setY(germ.getY() + 1);

                if (germ.getY() >= Variables.FloorLevel - Variables.GermHeight) {

                    germ.setDestroyed(true);
                }
            }
        }
        var generator2 = new Random();
        for (Covid covid : covids) {
            int shoot = generator2.nextInt(10000); //determines the chance of a mask spawnin
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
                if (maskX >= (playerX) && maskX <= (playerX + Variables.PlayerWidth)
                        && maskY >= (playerY)
                        && maskY <= (playerY + Variables.PlayerHeight)) {
                    mask.setDestroyed2(true);
                    if (Life <= 2) {
                        Life = Life + 1;
                        System.out.println(Life);
                    }else if(Life == 3){


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

    public static void HighScore() throws IOException {
        String input = "";

        BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            //Add the line and then "\n" indicating a new line
            input += line + "\n";
        }
        reader.close();
        JOptionPane.showMessageDialog(null,input);
    }

    private void doGameCycle() {

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
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if (inGame) {

                    if (!dispense.isVisible()) {

                        dispense = new Dispense(x, y);
                    }
                }
            }
        }
    }



}
