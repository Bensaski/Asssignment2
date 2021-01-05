import javax.swing.*;

public class  Variables {
    public static int BoardHeight = 1000;
    public static int BoardWidth = 1000;
    public static int BorderLeft = 5;
    public static int BorderRight = 30;

    public static int FloorLevel = 900;
    public static int GermHeight = 20;
    public static int MaskHeight = 20;

    public static int CovidHeight = 50;
    public static int CovidWidth = 60;
    public static int CovidInitX = 150;
    public static int CovidInitY = 60;
    public static int UKCovidInitX = 150;
    public static int UKCovidInitY = 5;
    public static int UKCovidHeight = 50;
    public static int UKCovidWidth = 60;
    public static int GoDown = 50;
    public static int GoDown2 = 100;
    public static int AmountOfCovidToKill = 50;
    public static int Chance = 1;
    public static int Delay = 10;
    public static int PlayerWidth = 50;
    public static int PlayerHeight = 20;
    public static int CovidRow = 1;
    public static int CovidColumn = 5;
    public static int UKCovidRow = 5;
    public static int UKCovidColumn = 5;
    public static String Choice;

    public void Choices() {

        String Difficulty = JOptionPane.showInputDialog("Which Difficulty would you like? Easy,Hard or Extreme");
        Variables.Choice = Difficulty;

        if (Difficulty.equals("Easy")) {
            Variables.BoardHeight = 1000;
            Variables.BoardWidth = 1000;
            Variables.BorderLeft = 5;
            Variables.BorderRight = 30;
            Variables.FloorLevel = 900;
            Variables.GermHeight = 20;
            Variables.MaskHeight = 20;
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
        }
        if (Difficulty.equals("Hard")) {
            Variables.BoardHeight = 1000;
            Variables.BoardWidth = 1000;
            Variables.BorderLeft = 5;
            Variables.BorderRight = 30;

            Variables.FloorLevel = 900;
            Variables.GermHeight = 20;
            Variables.MaskHeight = 20;

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
            Variables.PlayerWidth = 50;
            Variables.PlayerHeight = 20;
            Variables.CovidRow = 8;
            Variables.CovidColumn = 10;
            Variables.UKCovidRow = 2;
            Variables.UKCovidColumn = 10;
        }
        if (Difficulty.equals("Extreme")) {
            Variables.BoardHeight = 1000;
            Variables.BoardWidth = 1000;
            Variables.BorderLeft = 5;
            Variables.BorderRight = 30;

            Variables.FloorLevel = 900;
            Variables.GermHeight = 20;
            Variables.MaskHeight = 20;

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
            Variables.AmountOfCovidToKill = 120;
            Variables.Chance = 1;
            Variables.Delay = 10;
            Variables.PlayerWidth = 50;
            Variables.PlayerHeight = 20;
            Variables.CovidRow = 10;
            Variables.CovidColumn = 10;
            Variables.UKCovidRow = 2;
            Variables.UKCovidColumn = 10;
        }

    }
}





