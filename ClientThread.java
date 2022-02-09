import java.io.*;
import java.net.*;
 
public class ClientThread extends Thread {
    private Socket socket;
    private Server server;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
 
    public ClientThread(Socket _socket, Server _server) {
        this.socket = _socket;
        this.server = _server;
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
                try {
                    Object recieved = ois.readObject();
                    if(recieved instanceof Card)
                    {
                        server.handleCard((Card)recieved);
                    }
                }
                catch(ClassNotFoundException e)
                {
                    System.out.println("Error recieving card : " + e.getMessage());
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
