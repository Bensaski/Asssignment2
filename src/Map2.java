import javax.imageio.ImageIO;
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
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class Map2 extends JPanel {



    private Dimension d;
    private List<Covid> covids;
    private List<UKCovid> ukcovids;
    private Player player;
    private Dispense dispense;

    private int direction = -1;
    private int direction2 = 2;
    private int deaths = 0;
    int Life = 0;
    BufferedImage img;
    Variables Variables = new Variables();



    private boolean inGame = true;
    private String message = "Game Over";
    String explImg = "src/images/Explosion.png";
    private Timer timer;
    long start = System.currentTimeMillis();
    String name = JOptionPane.showInputDialog("Please enter Player Name");






    public Map2(){
        Variables var = new Variables();
        var.Doing();
        initMap();
        gameInit();
        bgimage();
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

    private void drawCovids(Graphics g) {

        for (Covid covid : covids) {

            if (covid.isVisible()) {

                g.drawImage(covid.getImage(), covid.getX(), covid.getY(), this);
            }

            if (covid.isDying()) {

                covid.die();
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

    private void drawGerms(Graphics g) {

        for (Covid a : covids) {

            Covid.Germ b = a.getGerm();

            if (!b.isDestroyed()) {

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
    private void drawMask(Graphics g) {
        for (Covid a : covids) {

            Covid.Mask b = a.getMask();

            if (!b.isDestroyed2()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }



public void bgimage() {

    try {
        img = ImageIO.read(new File("src/Images/BGGame.jpg"));

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(img, 0, 0, null);


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
                 fw.write(name + ": " + TimeElapsed + "\n");
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
             for (UKCovid UkCovidActors : ukcovids) {

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
             if (Life < 3) {
                 y -= 10; //the speed of the dispenser shot
             }
             if (Life == 3) {
                 y -= 20; //the speed of the dispenser shot
             }

             if (y < 0) {
                 dispense.die();
             } else {
                 dispense.setY(y);
             }
         }
         class CovidMoveDown extends Thread{
             @Override
             public void run(){
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
             }
         }


         class UkCovidMoveDown extends Thread{
             @Override
             public void run(){

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
         var generator = new Random();

         for (Covid covid : covids) {

             int shot = generator.nextInt(5000);
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

         for (UKCovid ukcovid : ukcovids) {

             int shot2 = generator.nextInt(1000);
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



        var generator2 = new Random();
        for (Covid covid : covids) {
            int shoot = generator2.nextInt(15000); //determines the chance of a mask spawnin
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
                    musicStuff Music = new musicStuff(); //call the music method
                    if (Life <= 2) {
                        Life = Life + 1;
                        System.out.println(Life);


                        //We run this play sound method in a new thread otherwise the game would pause until the sound has been played, which we dont want
                        new Thread(
                                new Runnable() {
                                    public void run() {
                                        try {
                                            Music.playSound("src/Mask.wav"); // This plays the shooting sound for picking up the masks
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                        if(Life == 3){
                            new Thread(
                                    new Runnable() {
                                        public void run() {
                                            try {
                                                Music.playSound("src/PowerUp.wav"); // This plays power up sound when the player hits 3 lives and they begin shooting faster
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
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

    public static void HighScore() throws IOException {
        String input = "";

        BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            //Add the line and then "\n" indicating a new line
            input += line + "\n";
        }
        reader.close();
        JOptionPane.showMessageDialog(null,input,"High Scores", JOptionPane.INFORMATION_MESSAGE);
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
