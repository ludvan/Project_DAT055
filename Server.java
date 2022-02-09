import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends Thread {
    private int port;
    private int playerLimit;
    private ArrayList<ClientThread> clientThreads;
    private boolean in_match;
    private Game game;

    public Server(int _port)
    {
        port = _port;
        game = new Game();
        clientThreads = new ArrayList<ClientThread>();
        playerLimit = 2; // hårdkodat så länge
        in_match = false;
    }

    public Game getGame()
    {
        return game;
    }

    public void dealCards()
    {
        Deck tmp = new Deck();
        tmp.fillDeck();
        Deck.shuffle(tmp);
        game.setDeck(tmp);

        while(game.getDeck().getSize() > 0)
        {
            for(int i = 0; i < game.getPlayers().size(); i++)
            {
                if(game.getDeck().getSize() <= 0)
                    break;
                
                Card topCard = game.getDeck().drawCard();
                game.deckRemove(topCard);
                game.playerAddCard(i, topCard);
            }
        }

        // I spelarnas game vill vi sätta player_id till unika värden så
        // att spelarna kan hålla koll på vilka kort som är sina
        for(int i = 0; i < game.getPlayers().size(); i++)
        {   
            Game player_game = game;
            player_game.setPlayerId(i);
            send(player_game, clientThreads.get(i));
        }
    }

    public void handleCard(Card card)
    {
        if(!game.getDeck().isEmpty())
        {
            if(!Card.isStackable(card, game.getDeck().drawCard()))
            {
                send("can't place that card", clientThreads.get(game.getCurrentTurn()));
                return;
            }
        }
        
        if(card.getValue() == Value.reverse)
            game.setReverse(!game.getReverse());

        addCardToDeck(card);
        removeCardFromPlayer(game.getCurrentTurn(), card);
        nextTurn();
        //server.broadcast("\n" + tmp.getPlayers().get(tmp.getCurrentTurn()).getName() + " placed card : " + ((Card)recieved).toString() + "\n");
        updateClientsGame();
    }

    public void run()
    {
        try (ServerSocket serverSocket = new ServerSocket(port)) 
        {
            // lobby
            System.out.println("Chat Server is listening on port " + port);
            System.out.println("Waiting for players to connect...");
            
            if(!in_match)
            {
                while (game.getPlayers().size() < playerLimit) {
                    Socket socket = serverSocket.accept();
                    ClientThread newUser = new ClientThread(socket, this);
                    clientThreads.add(newUser);
                    System.out.println("New user joined the lobby");
                    newUser.start();
                }             
            }
            /*
            while(in_match)
            {
                // spel logik
            }
            */
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //int port = 8989;
 
        //Server server = new Server(port);
        //server.execute();
    }

    public void send(Object object, ClientThread user)
    {
        user.sendObject(object);
    }

    public void broadcast(Object object) {
        for (ClientThread aUser : clientThreads) {
            aUser.sendObject(object);
        }
    }

    public void broadcast(Object object, ClientThread excludeUser) {
        for (ClientThread aUser : clientThreads) {
            if (aUser != excludeUser) {
                aUser.sendObject(object);
            }
        }
    }

    public void addCardToPlayer(int player, Card card)
    {
        this.game.playerAddCard(player, card);
    }

    public void removeCardFromPlayer(int player, Card card)
    {
        this.game.playerRemoveCard(player, card);
    }

    public void addCardToDeck(Card card)
    {
        this.game.deckAddCard(card);
    }

    public void removeCardFromDeck(Card card)
    {
        this.game.deckRemove(card);
    }

    public void nextTurn()
    {
        game.setCurrentTurn(game.nextTurn());
    } 

    public void updateClientsGame()
    {
        Game player_game = new Game(game);
        for(int i = 0; i < this.game.getPlayers().size(); i++)
        {   
            player_game.setPlayerId(i);
            send(player_game, clientThreads.get(i));
        }
    }
 
    /**
     * Stores username of the newly connected client.
     */
    void addUser(Player user) {
        game.addPlayer(user);
        System.out.println("(" + game.getPlayers().size() + "/"+ playerLimit + ") users connected");
        if(game.getPlayers().size() == playerLimit)
        {
            in_match = true;
            System.out.println("Match full, dealing cards...");
            dealCards();
        }
    }
 
    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    void removeUser(Player user, ClientThread aUser) {
        game.removePlayer(user);
        clientThreads.remove(aUser);
        System.out.println("The user " + user.getName() + " quitted");
        System.out.println("(" + game.getPlayers().size() + "/"+ playerLimit + ") users connected");
    }
 
    ArrayList<Player> getPlayers() {
        return this.game.getPlayers();
    }
 
    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {
        return !this.game.getPlayers().isEmpty();
    }

    public boolean inMatch()
    {
        return in_match;
    }
}
