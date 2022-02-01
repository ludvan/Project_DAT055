import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    private int port;
    private int playerLimit;
    private Deck deck;
    private Set<Player> players = new HashSet<>();
    private Set<ClientThread> clientThreads = new HashSet<>();
    private boolean in_match;

    public Server(int _port)
    {
        port = _port;
        playerLimit = 2; // hårdkodat så länge
        deck = new Deck();
        deck.fillDeck();
        Deck.shuffle(deck);
        in_match = false;
    }

    public void dealCards()
    {
        while(deck.getSize() > 0)
        {
            for(Player player : players)
            {
                if(deck.getSize() <= 0)
                    break;
                
                Card tmp = deck.drawCard();
                deck.removeCard(tmp);
                broadcast("player : " + player.getName() + " add card " + tmp.toString());
            }
        }
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
                while (players.size() < playerLimit) {
                    Socket socket = serverSocket.accept();
                    ClientThread newUser = new ClientThread(socket, this);
                    clientThreads.add(newUser);
                    System.out.println("New user joined the lobby");
                    newUser.start();
                }             
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }

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
 
    /**
     * Stores username of the newly connected client.
     */
    void addUser(Player user) {
        players.add(user);
        System.out.println("(" + players.size() + "/"+ playerLimit + ") users connected");
        if(players.size() == playerLimit)
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
        boolean removed = players.remove(user);
        if (removed) {
            clientThreads.remove(aUser);
            System.out.println("The user " + user.getName() + " quitted");
            System.out.println("(" + players.size() + "/"+ playerLimit + ") users connected");
        }
    }
 
    Set<Player> getUserNames() {
        return this.players;
    }
 
    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {
        return !this.players.isEmpty();
    }
}
