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
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } 
        catch (IOException ex) {
            System.out.println("Error in ClientThread constructor: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
        try {
            // om vi inte är i en match, lägg till nya spelare
            if(!server.inMatch())
            {
                // skapa ny användare och lägg till i servern
                String username = (String)ois.readObject();
                Player newUser = new Player(username);
                server.addUser(newUser);
                /*
                String serverMessage = "New user connected: " + username;
                server.broadcast(serverMessage);  
                */      
            }
            while(true)
            {
                if(server.inMatch())
                {
                    try {
                        System.out.println("server reading inputstream...");
                        Object recieved = ois.readObject();

                        if(recieved instanceof Card)
                        {
                            Game tmp = server.getGame();
                            System.out.println("server recieved card");
                            server.broadcast(tmp.getPlayers().get(tmp.getCurrentTurn()).getName() + " placed card : " + ((Card)recieved).toString() + "\n");
                        }
                        System.out.println("server recieved something");
                    }
                    catch (ClassNotFoundException e) {
                        System.out.println("Error recieving card in UserThread: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
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
    void sendObject(Object message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Error in userThread, can't send object : " + e.getMessage());
        }
    }
}
