import java.io.Serializable;

public class Player implements Serializable
{
    private String name;
    private Deck deck;

    public Player(String _name)
    {
        name = _name;
        deck = new Deck();
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
 //       return " name : " + name + " deck : " + deck.toString();
        return "name : " + name + " deck : " + deck.toString();

    }
}
