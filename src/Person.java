

public class Person {

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
        String[] data = line.split(": ");
        String name = data[0];
        int score = Integer.parseInt(data[1]);
        return new Person(name, score);
    }




}