import java.io.Serializable;
import java.util.*;

public class Game implements Serializable{
    private Deck deck;
    private ArrayList<Player> players = new ArrayList<>();
    private int turn;
    private boolean reverse;

    public Game()
    {
        deck = new Deck();
        players = new ArrayList<>();
        turn = 0;
        reverse = false;
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public void addPlayer(Player player)
    {
        this.players.add(player);
    }

    public void removePlayer(Player player)
    {
        this.players.remove(player);
    }

    public void setPlayers(ArrayList<Player> players)
    {
        this.players = players;
    }

    public Deck getPlayerDeck(int player)
    {
        return players.get(player).getDeck();
    }

    public void playerAddCard(int player, Card card)
    {
        players.get(player).getDeck().addCard(card);
    }

    public void playerRemoveCard(int player, Card card)
    {
        players.get(player).getDeck().removeCard(card);
    }

    public void setDeck(Deck deck)
    {
        this.deck = deck;
    }

    public Deck getDeck()
    {
        return this.deck;
    }

    public void deckAddCard(Card card)
    {
        deck.addCard(card);
    }

    public int nextTurn()
    {
        int t = turn;
        if(!reverse)
        {
            t++;
        }
        else
        {
            t--;
        }
        return t%players.size();
    }
}
