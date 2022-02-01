import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    private int port;
    private Set<String> clientNames = new HashSet<>();
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
 
        int port = Integer.parseInt(args[0]);
 
        Server server = new Server(port);
        server.execute();
    }
 
    /**
     * Delivers a message from one user to others (broadcasting)
     */
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
    void addUserName(String userName) {
        clientNames.add(userName);
    }
 
    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    void removeUser(String userName, ClientThread aUser) {
        boolean removed = clientNames.remove(userName);
        if (removed) {
            clientThreads.remove(aUser);
            System.out.println("The user " + userName + " quitted");
        }
    }
 
    Set<String> getUserNames() {
        return this.clientNames;
    }
 
    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {
        return !this.clientNames.isEmpty();
    }
}
