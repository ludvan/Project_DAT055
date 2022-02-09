import java.net.*;
import java.io.*;
import java.util.*;

import javax.lang.model.util.ElementScanner6;
 
public class ChatClient {
    private String hostname; // serverns ip 
    private int port;
    private String userName;
    private Game game; // lokal kopia av spel state
    private boolean in_match;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket socket;
    private ArrayList<Object> sendBuffer; // buffra det vi vill skicka
 
    public ChatClient(String hostname, int port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.userName = username;
        sendBuffer = new ArrayList<Object>();
        in_match = false;
    }
    
    public void sendToServer(Object object)
    {
        sendBuffer.add(object);
    }

    public void execute() {
        try {
            // skapa en socket och skicka användarnamn till servern som lägger till användaren i en lista
            // och startar en tråd för användaren
            socket = new Socket(hostname, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            outputStream.writeObject(userName);
            //outputStream.flush();

            // loop som läser från och skriver till servern
            try {
                Object recieved;
                while(true) {
                    recieved = inputStream.readObject();
                    // detta borde hanteras av en extern class kanske??
                    // todo skicka generaliserat medelande som egen class. detta skall innehålla ändringar mm
                    if(recieved instanceof Game)
                    {
                        setGame((Game)recieved);
                    }
                    if(recieved instanceof String)
                    {
                        System.out.println(recieved);
                    }
                    // detta sker när clienten är i en match
                    if(in_match)
                    {
                        if(isClientTurn())
                        {
                            System.out.println("your turn! \n select card \n");
                            // Det är vår tur att spela temporär lösning på att skicka ett kort
                            String console_in = System.console().readLine();
                            int selected_card = Integer.valueOf(console_in);
                            Card selected = game.getPlayerDeck(game.getPlayerId()).getCard(selected_card);
                            // lägg till kortet i vår send buffer
                            sendToServer(selected);
                        }
                    }

                    // om vi har något att skicka, skicka det (detta sker om vi vill skicka kort mm)
                    // OBS för debug har buffern endast ett object åt gången
                    if(sendBuffer.size() != 0)
                    {
                        try{
                            outputStream.writeObject(sendBuffer.get(0));
                            System.out.print("client sent " + sendBuffer.get(0).toString() + " in ChatClient");
                            sendBuffer.clear();//sendBuffer.remove(0);
                        }
                        catch (IOException ex) {
                            System.out.println("I/O Error: " + ex.getMessage());
                        }
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
            inputStream.close();
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
        in_match = true;
        // visa våra kort för skojs skull
        int id = this.game.getPlayerId();
        System.out.println("ID : " + id);
        System.out.println("your cards : \n" + this.game.getPlayers().get(id).getDeck());
    }

    public boolean isClientTurn()
    {
        return this.game.getPlayerId() == game.getCurrentTurn();
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