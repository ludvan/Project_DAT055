import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends Thread {
    private int port;
    private int playerLimit;
    private ArrayList<ClientThread> clientThreads;
    private boolean in_match;
    private Game game;
    private String server_status; // håller koll på vad som skrivs ut från servern

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
        game.setDiscardDeck(tmp);

        // dela ut 7 kort var
        int cpp = 0;
        while(cpp < 7)
        {
            for(int i = 0; i < game.getPlayers().size(); i++)
            {
                if(game.getDiscardDeck().getSize() <= 0)
                    break;
                
                Card top_card = game.getDiscardDeck().drawCard();
                game.discardDeckRemove(top_card);
                game.playerAddCard(i, top_card);
                Deck.shuffle(game.getDiscardDeck());
            }
            cpp++;
        }
        Card top_card = game.getDiscardDeck().drawCard();
        game.deckAddCard(top_card);
        game.discardDeckRemove(top_card);

        // I spelarnas game vill vi sätta player_id till unika värden så
        // att spelarna kan hålla koll på vilka kort som är sina
        updateClientsGame(game);
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

        if(card.getValue() == Value.plus2){
            /* Add two cards to the players hand */
            Card tmp = game.getDiscardDeck().drawCard();
            game.playerAddCard(game.nextTurn(), tmp);
            game.discardDeckRemove(tmp);
            tmp = game.getDiscardDeck().drawCard();
            game.playerAddCard(game.nextTurn(), tmp);
            game.discardDeckRemove(tmp);
            System.out.println(game.nextTurn() + " drew 2 cards");
        }
        if(card.getValue() == Value.plus4){
            Card tmp = game.getDiscardDeck().drawCard();
            game.playerAddCard(game.nextTurn(), tmp);
            game.discardDeckRemove(tmp);
            tmp = game.getDiscardDeck().drawCard();
            game.playerAddCard(game.nextTurn(), tmp);
            game.discardDeckRemove(tmp);     
            tmp = game.getDiscardDeck().drawCard();
            game.playerAddCard(game.nextTurn(), tmp);
            game.discardDeckRemove(tmp);            
            tmp = game.getDiscardDeck().drawCard();
            game.playerAddCard(game.nextTurn(), tmp);
            game.discardDeckRemove(tmp);
            System.out.println(game.nextTurn() + " drew 4 cards");
        }

        if(card.getValue() == Value.stop)
            game.setCurrentTurn(game.nextTurn());
        if(card.getValue() == Value.reverse)
            game.setReverse(!game.getReverse());

        game.deckAddCard(card);
        game.playerRemoveCard(game.getCurrentTurn(), card);
        game.setCurrentTurn(game.nextTurn());
        updateClientsGame(game);
    }

    public void run()
    {
        try (ServerSocket serverSocket = new ServerSocket(port)) 
        {
            // lobby
            server_status += "\n Chat Server is listening on port " + port;
            server_status += "\n Waiting for players to connect... ";
            System.out.println("Chat Server is listening on port " + port);
            System.out.println("Waiting for players to connect...");
            
            if(!in_match)
            {
                while (game.getPlayers().size() < playerLimit) {
                    Socket socket = serverSocket.accept();
                    ClientThread newUser = new ClientThread(socket, this);
                    clientThreads.add(newUser);
                    server_status += "\n New user joined the lobby";
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
            server_status += "\n Error in the server: " + ex.getMessage();
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String getServerStatus()
    {
        return server_status;
    }
    public static void main(String[] args) {
        int port = 8989;
 
        Server server = new Server(port);
        server.run();
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

    public void updateClientsGame(Game new_game)
    {
        Game player_game = new_game.copy(new_game);
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
        server_status += "\n (" + game.getPlayers().size() + "/"+ playerLimit + ") users connected";
        System.out.println("(" + game.getPlayers().size() + "/"+ playerLimit + ") users connected");
        if(game.getPlayers().size() == playerLimit)
        {
            in_match = true;
            server_status += "\n Match full, dealing cards...";
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
