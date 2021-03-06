import java.util.*;

public class Game implements java.io.Serializable{
    private Deck deck;
    private Deck discardDeck;
    private int player_id; // används för att hålla koll på spelarens kort
    private ArrayList<Player> players;
    private int turn;
    private boolean reverse;

    public Game copy(Game game)
    {
        Game tmp = new Game();
        tmp.setDeck(game.getDeck());
        tmp.setDiscardDeck(game.getDiscardDeck());
        tmp.setPlayerId(game.getPlayerId());
        tmp.setPlayers(game.getPlayers());
        tmp.setCurrentTurn(game.getCurrentTurn());
        tmp.setReverse(game.getReverse());
        return tmp;
    }

    public Game()
    {
        deck = new Deck();
        discardDeck = new Deck();
        players = new ArrayList<>();
        player_id = -1; // -1 for the serber version of game
        turn = 0;
        reverse = false;
    }

    public Game(Game clone)
    {
        deck = clone.getDeck();
        discardDeck = clone.getDiscardDeck();
        players = clone.getPlayers();
        player_id = clone.getPlayerId(); // -1 for the serber version of game
        turn = clone.getCurrentTurn();
        reverse = clone.getReverse();
    }

    public void setPlayerId(int id)
    {
        player_id = id;
    }

    public int getPlayerId()
    {
        return player_id;
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

    public void deckRemove(Card card)
    {
        deck.removeCard(card);
    }

    public void setDiscardDeck(Deck deck)
    {
        this.discardDeck = deck;
    }

    public Deck getDiscardDeck()
    {
        return this.discardDeck;
    }

    public void discardDeckAddCard(Card card)
    {
        discardDeck.addCard(card);
    }

    public void discardDeckRemove(Card card)
    {
        discardDeck.removeCard(card);
    }

    public int getCurrentTurn()
    {
        return turn;
    }

    public void setCurrentTurn(int _turn)
    {
        turn = _turn; 
        System.out.println("setCurrentTurn");
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
        if(t < 0)
            t = players.size()-1;
        if(t > players.size()-1)
            t = 0;
        return t;
    }

    public boolean getReverse()
    {
        return reverse;
    }

    public void setReverse(boolean val)
    {
        reverse = val;
    }

    public String toString()
    {
        String tmp = "";
        tmp += "dealer deck : \n" + deck.toString();
        tmp += "\n";
        tmp += "players : " + players.size();
        tmp += "\n";
        tmp += "turn : " + turn;
        tmp += "\n";
        tmp += "reverse? : " + reverse;
        return tmp;
    }
}
