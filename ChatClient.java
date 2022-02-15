import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient {
    private GameBoard board;
    private String hostname; // serverns ip 
    private int port;
    private String userName;
    private Game game; // lokal kopia av spel state
    private boolean in_match;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket socket;
    private ArrayList<Object> sendBuffer; // buffra det vi vill skicka
    private boolean hasSelectCard = false;
 
    public ChatClient(String hostname, int port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.userName = username;
        sendBuffer = new ArrayList<Object>();
        in_match = false;
        board = new GameBoard();
        board.setGame(game);
        board.setClient(this);
    }
    
    public void sendToServer(Object object)
    {
        System.out.println("sending " + object.toString() + " to server...");
        sendBuffer.add(object);
        hasSelectCard = true;
    }

    public void execute() {
        try {
            // skapa en socket och skicka anv√§ndarnamn till servern som l√§gger till anv√§ndaren i en lista
            // och startar en tr√•d f√∂r anv√§ndaren
            socket = new Socket(hostname, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            outputStream.writeObject(userName);

            // loop som l√§ser fr√•n och skriver till servern
            try {
                Object recieved;
                while(true) {
                    recieved = inputStream.readObject();
                    //instanceof ArrayList<Player> var ej tillÂtet
                    //OBS mÂste skrivas om ifall vi skickar andra ArrayList (tror/hoppas vi inte behˆver)
                    if(recieved instanceof ArrayList) {
                    	@SuppressWarnings("unchecked") //fˆrsˆkt hitta b‰ttre lˆsning, kˆr sÂ h‰r sÂ l‰nge
						ArrayList<Player> playerList = (ArrayList<Player>) recieved;
                    	board.lobbyUpdate(playerList);
                    }
                    // detta borde hanteras av en extern class kanske??
                    // todo skicka generaliserat medelande som egen class. detta skall inneh√•lla √§ndringar mm
                    if(recieved instanceof Game)
                    {
                        if(!in_match)
                        {
                            setGame((Game)recieved);
                        }
                        else
                        {
                            updateGame((Game)recieved);
                        }
                    }
                    if(recieved instanceof String)
                    {
                        System.out.println(recieved);
                    }
                    // detta sker n√§r clienten √§r i en match
                    
                    if(in_match)
                    {
                        if(isClientTurn())
                        {
                            hasSelectCard = false;
                            while(!hasSelectCard)
                            {
                                // v√§nta medan anv√§ndaren v√§ljer kort
                                try {
                                    Thread.sleep(250);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        {
                            System.out.println("wait for your turn \n");
                        }
                    }
                    
                    // om vi har n√•got att skicka, skicka det (detta sker om vi vill skicka kort mm)
                    // OBS f√∂r debug har buffern endast ett object √•t g√•ngen
                    if(sendBuffer.size() != 0)
                    {
                        try{
                            outputStream.writeObject(sendBuffer.get(0));
                            outputStream.reset();
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

    void setGame(Game new_game)
    {
        this.game = new Game();
        game = game.copy(new_game);
        in_match = true;
        board.setGame(game);
        board.update();
    }

    void updateGame(Game new_game)
    {
        System.out.println(new_game.toString());
        game = game.copy(new_game);
        board.setGame(game);
        board.update();
    }

    public boolean isClientTurn()
    {
        return this.game.getPlayerId() == game.getCurrentTurn();
    }

    public boolean hasStackableCard()
    {
        boolean tmp = false;
        for(int i = 0; i < game.getPlayerDeck(game.getPlayerId()).getSize(); i++)
        {
            if(Card.isStackable(game.getPlayerDeck(game.getPlayerId()).getCard(i), game.getDeck().drawCard()))
            {
                tmp = true;
            }
        }
        return tmp;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }
 
    String getUserName() {
        return userName;
    }
 
    public static void main(String[] args) { 
        String hostname;
        String username;
        int port;
        if(args.length < 3)
        {
            System.out.println("Enter server ip write 'localhost' for running on same device");
            hostname = System.console().readLine();
            port = 8989;
            System.out.println("Enter a username : ");
            username = System.console().readLine();
        }
        else
        {
            hostname = args[0];
            port = Integer.parseInt(args[1]);
            username = args[2];
        }
        ChatClient client = new ChatClient(hostname, port, username);

        client.execute();
    }
}