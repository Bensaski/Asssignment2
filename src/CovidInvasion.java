import javax.swing.*;
import java.awt.*;


public class CovidInvasion extends JFrame {

    public CovidInvasion() {


        BoardCreation();

    }

    private void BoardCreation() {
        add(new Map2());

        setTitle("Covid Invasion - Ben Sadler BS19624");
        setSize(Variables.BoardWidth, Variables.BoardHeight);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);


    }



    public static void main(String[] args)  {


        JOptionPane.showMessageDialog(null,"COVID-19 has infested various regions in the world and it is your job to keep it at bay" +
                "\nBy using your flashy soap dispensing gun, blast away the virus. Prevent it from coming within two meters of you or you will be infected too!" +
                "\n Masks can occasionally spawn that can prevent the virus from entering your lungs. If a virus comes in contact with it, the mask will be taken away and prevents you from getting infected."+
                "\n \nRules : \n" +
                "1. Press SPACEBAR to shoot your Soap Dispensing Gun" +
                "\n2. Press Left and Right Arrow Keys to move Left and Right"+
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
        String BG = "src/BG.wav";
        musicStuff musicObject = new musicStuff();
        musicObject.playSound(BG);


    }



    }



