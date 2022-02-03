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
            // annars prata med servern
            else
            {
                Object clientMessage;
                do {
                    if(ois.available() != 0)
                    {
                        clientMessage = ois.readObject();
                        if(clientMessage instanceof Game)
                        {
                            System.out.println("game was recieved from server");
                        }
                        else if(clientMessage instanceof String)
                        {
                            System.out.println("string was recieved from server");
                            System.out.println(clientMessage);
                            //server.broadcast((String)clientMessage, this);
                        }
                    }
    
                } while (true);
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
            oos.flush();
        } catch (IOException ex) {
            System.out.println("Error in UserThread, can't send game : " + ex.getMessage());
        }
    }
}
