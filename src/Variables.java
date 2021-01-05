import javax.swing.*;

public class Variables {
    static int BoardHeight = 1000;
    static int BoardWidth = 1000;
    static int BorderLeft = 5;
    static int BorderRight = 30;

    static int FloorLevel = 900;
    static int GermHeight = 20;
    static int MaskHeight = 20;

    static int CovidHeight = 50;
    static int CovidWidth = 60;
    static int CovidInitX = 150;
    static int CovidInitY = 60;
    static int UKCovidInitX = 150;
    static int UKCovidInitY = 5;
    static int UKCovidHeight = 50;
    static int UKCovidWidth = 60;

    static int GoDown = 50;
    static int GoDown2 = 100;
    static int AmountOfCovidToKill = 50;
    static int Chance = 1;
    static int Delay = 10;
    static int PlayerWidth = 50;
    static int PlayerHeight = 20;
    static int CovidRow = 1;
    static int CovidColumn = 5;
    static int UKCovidRow = 5;
    static int UKCovidColumn = 5;
    public void Doing(){

        String Difficulty = JOptionPane.showInputDialog("Which Difficulty would you like? Easy or Hard");

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
            Variables.UKCovidWidth = 60;

            Variables.GoDown = 50;
            Variables.GoDown2 = 100;
            Variables.AmountOfCovidToKill = 30;
            Variables.Chance = 1;
            Variables.Delay = 10;
            Variables.PlayerWidth = 50;
            Variables.PlayerHeight = 20;
            Variables.CovidRow = 5;

            Variables.CovidColumn = 5;
            Variables.UKCovidRow = 5;
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
            Variables.AmountOfCovidToKill = 60;
            Variables.Chance = 1;
            Variables.Delay = 10;
            Variables.PlayerWidth = 50;
            Variables.PlayerHeight = 20;


        }
    }
}


