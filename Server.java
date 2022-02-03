import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
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

    public void dealCards()
    {
        Deck tmp = new Deck();
        tmp.fillDeck();
        Deck.shuffle(tmp);
        game.setDeck(tmp);

        while(game.getDeck().getSize() > 0)
        {
            for(Player player : game.getPlayers())
            {
                if(game.getDeck().getSize() <= 0)
                    break;
                
                Card topCard = game.getDeck().drawCard();
                game.getDeck().removeCard(topCard);
                player.getDeck().addCard(topCard);
            }
        }

        broadcastGame(game);
    }

    public void execute()
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
            while(in_match)
            {
                // spel logik
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 8989;
 
        Server server = new Server(port);
        server.execute();
    }
 
    void broadcast(String message, ClientThread excludeUser) {
        for (ClientThread aUser : clientThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    void broadcast(String message) {
        for (ClientThread aUser : clientThreads) {
            aUser.sendMessage(message);
        }
    }

    void broadcastGame(Game game) {
        for (ClientThread aUser : clientThreads) {
            aUser.sendGame(game);
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
