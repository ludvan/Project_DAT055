import java.net.*;
import java.io.*;
import java.util.*;
 
public class ChatClient {
    private String hostname;
    private int port;
    private String userName;
    private Set<Player> players;
 
    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        players = new HashSet<>();
    }
 
    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);
 
            System.out.println("Connected to the server");
 
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
 
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
 
    }
 
    void setUserName(String userName) {
        this.userName = userName;
    }
 
    String getUserName() {
        return this.userName;
    }
 
 
    public static void main(String[] args) { 
        String hostname = "localhost";
        int port = 8989;
 
        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
}