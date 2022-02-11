import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;

public class Player implements Serializable
{
    private String name;
    private Deck deck;
    private int score;

    public Player(String _name)
    {
        name = _name;
        deck = new Deck();
        score = 0;
    }

    public Player(String _name, Deck _deck)
    {
        name = _name;
        deck = _deck;
    }

    public void setName(String _name)
    {
        name = _name;
    }

    public String getName()
    {
        return name;
    }

    public void setDeck(Deck _deck)
    {
        deck = _deck;
    }

    public Deck getDeck()
    {
        return deck;
    }

    public String toString()
    {
        return " name : " + name + " deck : " + deck.toString();
    }

    public int getScore(){
        return score;
    }

    public void setScore(int n){
        score = n;
    }

    public void loadScore(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("saveFile.txt"));
            name = br.readLine();
            score = Integer.parseInt(br.readLine());
            br.close();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    public void saveScore(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("saveFile.txt"));
            bw.write(getName() + " : " + score);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
}
