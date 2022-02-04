import java.net.*;
import java.io.*;
import java.util.*;

import javax.lang.model.util.ElementScanner6;
 
public class ChatClient {
    private String hostname;
    private int port;
    private String userName;
    private Game game; // lokal kopia av spel state
    private ObjectOutputStream outputStream;
    private Socket socket;
 
    public ChatClient(String hostname, int port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.userName = username;
    }
    
    public void sendToServer(Object object)
    {
        try{
            outputStream.writeObject(object);
            outputStream.flush();
        }
        catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public void execute() {
        try {
            // skapa en socket och skicka användarnamn till servern som lägger till användaren i en lista
            // och startar en tråd för användaren
            socket = new Socket(hostname, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            sendToServer(userName);
            // loop som läser från servern
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object recieved;
                while(true) {
                    recieved = ois.readObject();
                    // detta borde hanteras av en extern class kanske??
                    // todo skicka generaliserat medelande som egen class. detta skall innehålla ändringar mm
                    if(recieved instanceof Game)
                    {
                        setGame((Game)recieved);
                    }
                    else if(recieved instanceof String)
                    {
                        System.out.println(recieved);
                    }
                }
            }
            catch (IOException ex) {
                System.out.println("I/O Error: " + ex.getMessage());
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            outputStream.close();
            socket.close();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        } 
    }
 
    void setGame(Game game)
    {
        this.game = new Game();
        this.game.setPlayers(game.getPlayers());
        this.game.setDeck(game.getDeck());
        this.game.setPlayerId(game.getPlayerId());
        // visa våra kort för skojs skull
        int id = this.game.getPlayerId();
        System.out.println("ID : " + id);
        System.out.println("your cards : " + this.game.getPlayers().get(id).getDeck());
    }

    void setUserName(String userName) {
        this.userName = userName;
    }
 
    String getUserName() {
        return userName;
    }
 
    public static void main(String[] args) { 
        if(args.length < 1)
        {
            System.out.println("Enter username");
            return;
        }
        String hostname = "localhost";
        int port = 8989;
        String username = args[0];
 
        ChatClient client = new ChatClient(hostname, port, username);
        client.execute();
    }
}