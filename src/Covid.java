import javax.swing.*;

public class Covid extends Sprite {

    private Germ germ;
    private Mask mask;

    public Covid(int x, int y) {
        initCovid(x, y);
    }

    private void initCovid(int x, int y) {

        this.x = x;
        this.y = y;

        germ = new Germ(x, y);
        mask = new Mask(x,y);

        var CovidImg = "src/Images/covid4.png";
        var ii = new ImageIcon(CovidImg);


        setImage(ii.getImage());
    }

    public void act(int direction) {

        this.x += direction;

    }

    public Germ getGerm() {
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

            var germImg = "src/Images/germ.png";
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

    public Mask getMask() {
        return mask;
    }

    public class Mask extends Sprite{
        private boolean destroyed;
        public Mask(int x, int y) { initMask(x,y);}
        private void initMask(int x, int y){
            setDestroyed2(true);
            this.x=x;
            this.y=y;

            var maskImg = "src/Images/mask.png";
            var ii = new ImageIcon(maskImg);
            setImage(ii.getImage());
        }
        public void setDestroyed2(boolean destroyed){
            this.destroyed = destroyed;
        }

        public boolean isDestroyed2(){
            return destroyed;
        }
    }

}
