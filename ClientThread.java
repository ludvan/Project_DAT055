import java.io.*;
import java.net.*;
import java.util.*;
 
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

            // om vi inte är i en match, lägg till nya spelare
            if(!server.inMatch())
            {
                String username = (String)ois.readObject();
                Player newUser = new Player(username);
                server.addUser(newUser);
    
                String serverMessage = "New user connected: " + username;
                server.broadcast(serverMessage, this);
                
            }

            //socket.close();
            
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error recieving username in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }

 
    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Error in userThread, can't send message : " + e.getMessage());
        }
    }

    // send game state to client
    void sendGame(Game game) {
        try {
            oos.writeObject(game);
            System.out.println("Game state sent from server");
            oos.flush();
        } catch (IOException ex) {
            System.out.println("Error in UserThread, can't send game : " + ex.getMessage());
        }
    }
}
