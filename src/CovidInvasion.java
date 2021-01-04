import javax.swing.*;
import java.awt.*;


public class CovidInvasion extends JFrame {

    public CovidInvasion() {

        BoardCreation();

    }

    private void BoardCreation() {
        add(new Map2());

        setTitle("Covid Invasion");
        setSize(Variables.BoardWidth, Variables.BoardHeight);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }



    public static void main(String[] args)  {



        JOptionPane.showMessageDialog(null,"COVID-19 has infested various regions in the world and it is your job to keep it at bay" +
                "\nBy using your flashy soap dispensing gun, blast away the virus. Prevent it from coming within two meters of you or you will be infected too!" +
                "\n Masks can occasionally spawn that can prevent the virus from entering your lungs. If a virus comes in contact with it, the mask will be taken away and prevents you from getting infected.");


        EventQueue.invokeLater(() -> {
            var ex = new CovidInvasion();
            ex.setVisible(true);

        });
        String BG = "src/BG.wav";
        musicStuff musicObject = new musicStuff();
        musicObject.playSound(BG);


    }



    }



