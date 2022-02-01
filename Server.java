import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    private int port;
    private Set<Player> players = new HashSet<>();
    private Set<ClientThread> clientThreads = new HashSet<>();

    public Server(int _port)
    {
        port = _port;
    }

    public void execute()
    {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Chat Server is listening on port " + port);
 
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
 
                ClientThread newUser = new ClientThread(socket, this);
                clientThreads.add(newUser);
                newUser.start();
 
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
 
    /**
     * Stores username of the newly connected client.
     */
    void addUser(Player user) {
        players.add(user);
    }
 
    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    void removeUser(Player user, ClientThread aUser) {
        boolean removed = players.remove(user);
        if (removed) {
            clientThreads.remove(aUser);
            System.out.println("The user " + user.getName() + " quitted");
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
