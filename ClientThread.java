import java.io.*;
import java.net.*;
import java.util.*;
 
/**
 * This thread handles connection for each connected client, so the server
 * can handle multiple clients at the same time.
 *
 * @author www.codejava.net
 */
public class ClientThread extends Thread {
    private Socket socket;
    private Server server;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
 
    public ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }
 
    public void run() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            printUsers();

            String username = (String)ois.readObject();
            Player newUser = new Player(username);
            server.addUser(newUser);
 
            String serverMessage = "New user connected: " + username;
            server.broadcast(serverMessage, this);
            Object clientMessage;

            do {
                if(ois.available() != 0)
                {
                    clientMessage = ois.readObject();
                    if(clientMessage instanceof Game)
                    {
                        System.out.println("game was recieved from client");
                    }
                    else if(clientMessage instanceof String)
                    {
                        System.out.println("string was recieved from client");
                        server.broadcast((String)clientMessage, this);
                    }
                }
                //serverMessage = "[" + userName + "]: " + clientMessage;
 
            } while (true);
            /*
            server.removeUser(newUser, this);
            socket.close();
 
            serverMessage = username + " has quitted.";
            server.broadcast(serverMessage, this);
            */
 
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error recieving username in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }
 
    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        try {
            if (server.hasUsers()) {
                oos.writeObject("Connected users: " + server.getPlayers());
            } else {
                oos.writeObject("No other users connected");
            }
        } catch (IOException e) {
            System.out.println("Error printing users : " + e.getMessage());
        }
    }
 
    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        //writer.println(message);
    }

    void sendGame(Game game) {
        try {
            oos.writeObject(game);
        } catch (IOException ex) {
            System.out.println("Error in UserThread, can't send game : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
