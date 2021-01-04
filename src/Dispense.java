import javax.swing.ImageIcon;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class Dispense extends Sprite {

    private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
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
        playSound2("Laser.wav");
    }
private void playSound2(String filename){
    String strFilename = filename;

    try {
        soundFile = new File(strFilename);
    } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
    }

    try {
        audioStream = AudioSystem.getAudioInputStream(soundFile);
    } catch (Exception e){
        e.printStackTrace();
        System.exit(1);
    }

    audioFormat = audioStream.getFormat();

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
    byte[] abData = new byte[BUFFER_SIZE];
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

