import java.net.*;
import java.io.*;
import java.util.*;

import javax.lang.model.util.ElementScanner6;
 
public class ChatClient {
    private String hostname;
    private int port;
    private String userName;
    private Game game;
 
    public ChatClient(String hostname, int port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.userName = username;
    }
 
    public void execute() {
        try {
            // skapa en socket och skicka användarnamn till servern som lägger till användaren i en lista
            // och startar en tråd för användaren
            Socket socket = new Socket(hostname, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(userName);
            System.out.println(userName + " connected to the server"); 
            oos.flush();
            // loop som läser från servern
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object recieved;
                System.out.println("read client messages ClientThread");
                while(true) {
                    recieved = ois.readObject();
                    // todo skicka generaliserat medelande som egen class. detta skall innehålla ändringar mm
                    if(recieved instanceof Game)
                    {

                        System.out.println("game was recieved from server");
                        setGame((Game)recieved);
                    }
                    else if(recieved instanceof String)
                    {
                        System.out.println("string was recieved from server");
                        System.out.println(recieved);
                        //server.broadcast((String)clientMessage, this);
                    }
                    // något är fel
                    System.out.println("bad! recieved : " + recieved.toString());
                }
            }
            catch (IOException ex) {
                System.out.println("I/O Error: " + ex.getMessage());
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            oos.close();
            socket.close();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        } 
    }
 

    void setGame(Game game)
    {
        game.setPlayers(game.getPlayers());
        game.setDeck(game.getDeck());
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