import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;
import static java.util.Arrays.sort;

public class Server extends Thread {
    private int port;
    private int playerLimit;
    private ArrayList<ClientThread> clientThreads;
    private boolean in_match;
    private Game game;
    private int drawCardCounter; // används för att begränsa så att användaren inte kan plocka upp nmer än 3 kort
    private String HighscoreValues;

    public Server(int _port, int player_limit)
    {
        port = _port;
        game = new Game();
        clientThreads = new ArrayList<ClientThread>();
        playerLimit = player_limit; // hårdkodat så länge
        in_match = false;
        drawCardCounter = 0;
    }

    public Game getGame()
    {
        return game;
    }

    public void dealCards()
    {
        Deck tmp = new Deck();
        tmp.fillDeck();
        Deck.shuffle(tmp);
        game.setDiscardDeck(tmp);

        // dela ut 7 kort var
        int cpp = 0;
        while(cpp < 7)
        {
            for(int i = 0; i < game.getPlayers().size(); i++)
            {
                if(game.getDiscardDeck().getSize() <= 0)
                    break;
                
                Card top_card = game.getDiscardDeck().drawCard();
                game.discardDeckRemove(top_card);
                game.playerAddCard(i, top_card);
                Deck.shuffle(game.getDiscardDeck());
            }
            cpp++;
        }
        Card top_card = game.getDiscardDeck().drawCard();
        game.deckAddCard(top_card);
        game.discardDeckRemove(top_card);

        // I spelarnas game vill vi sätta player_id till unika värden så
        // att spelarna kan hålla koll på vilka kort som är sina
        updateClientsGame(game);
    }

    public void handleCard(TransmitData data)
    {
        int currentPlayer = game.getCurrentTurn();
        
        if(game.getDiscardDeck().getSize() == 0) {
        	reShuffle();
        }
        
        // om vi får in ett svart kort vill vi vänta på att 
        // en färg väljs
        if(!data.getChooseColor() && !data.getDrawCard() && data.getCard().getColor() == Col.black)
        {
            Card tmp = data.getCard();
            game.playerRemoveCard(currentPlayer, tmp);
            game.deckAddCard(tmp);
            return;
        }
        // hanterar färgval om kortet är svart
        if(data.getChooseColor() && !data.getDrawCard() && game.getDeck().drawCard().getColor() == Col.black)
        {
            Col chosenColor = data.getChosenColor();
            Card tmp = data.getCard();
            game.getDeck().drawCard().setColor(chosenColor);
            game.setCurrentTurn(game.nextTurn());
            updateClientsGame(game);
            return;
        }
        // om användaren endast vill dra ett kort från discard deck
        if(data.getDrawCard())
        {
            drawCardCounter++;
            Card tmp = game.getDiscardDeck().drawCard();
            game.playerAddCard(currentPlayer, tmp);
            game.discardDeckRemove(tmp);

            // användaren har dragit så många kort hen kan gå vidare
            if(drawCardCounter >= 3)
            {
                game.setCurrentTurn(game.nextTurn());
                drawCardCounter = 0;
            }
            // om det kort som dras går att lägga så vill vi återställa räknaren
            if(Card.isStackable(tmp, game.getDeck().drawCard()))
            {
                drawCardCounter = 0;
            }

            updateClientsGame(game);
            return;
        }
        // kolla om vi kan lägga kortet
        Card card = data.getCard();
        if(!game.getDeck().isEmpty())
        {
            if(!Card.isStackable(card, game.getDeck().drawCard()))
            {
                send("can't place that card", clientThreads.get(currentPlayer));
                return;
            }
        }

        // om det 
        if(card.getValue() == Value.plus2){
        	penaltyDraw(2);
            //System.out.println(game.nextTurn() + " drew 2 cards");
            game.setCurrentTurn(game.nextTurn());
        }
        if(card.getValue() == Value.plus4){
        	penaltyDraw(4);
            //System.out.println(game.nextTurn() + " drew 4 cards");
            game.setCurrentTurn(game.nextTurn());
        }

        if(card.getValue() == Value.stop)
            game.setCurrentTurn(game.nextTurn());
        if(card.getValue() == Value.reverse)
            game.setReverse(!game.getReverse());


        if(data.getChooseColor())
        {
            //System.out.println("Color chosen : " + data.getChosenColor().toString());
            card.setColor(data.getChosenColor());
        }
        game.deckAddCard(card);
        game.playerRemoveCard(currentPlayer, card);
        // om vi får in ett svart kort vill vi vänta på att
        // en färg väljs

//        WeHaveAWinner();
        if(!WeHaveAWinner()) {
            game.setCurrentTurn(game.nextTurn());
            updateClientsGame(game);
        }else{
            game.setCurrentTurn(-2);
            updateClientsGame(game);
        }
    }
    
    private void penaltyDraw(int number) {
    	Card tmp;
    	for(int i = 0; i<number; i++) {
    		if(game.getDiscardDeck().getSize() == 0) {
        		reShuffle();
        	}
    		tmp = game.getDiscardDeck().drawCard();
            game.playerAddCard(game.nextTurn(), tmp);
            game.discardDeckRemove(tmp); 
    	}
    	
    }
    
    private void reShuffle() {
    	Card topCard = game.getDeck().drawCard();
    	Deck tmpDeck = game.getDeck();
    	Deck newDeck = new Deck();
    	
    	newDeck.addCard(topCard);
    	tmpDeck.removeCard(topCard);
    	
    	tmpDeck.revertBlack();
    	Deck.shuffle(tmpDeck);
    	
    	game.setDeck(newDeck);
    	game.setDiscardDeck(tmpDeck);
    	
    	//test-syfte
    	JOptionPane.showMessageDialog(null, "reShuffle done");
    	JOptionPane.showMessageDialog(null, game.getDiscardDeck().toString());
    	JOptionPane.showMessageDialog(null, game.getDeck().toString());
    }


    /**
     * deciding if round is won
     *
     * @return true if round is wo by current player else false
     */
    public boolean WeHaveAWinner(){
        int currentPlayer = game.getCurrentTurn();
        String name=game.getPlayers().get(currentPlayer).getName();
        int[]pointsArr = countpoints();
        //kontrollprint för arrayen
        System.out.println("pointsArr: ");
        for(int m=0; m< pointsArr.length; m++) {
            System.out.println(pointsArr[m]);
        }
        for (int i=0; i<pointsArr.length; i++){
            int temp = pointsArr[i];
            if(temp == 0){
                System.out.println(name+" HAS WON!");
                if (HighscoreValues==null){
                    HighscoreValues=this.getHighscoreValues();
                }
                updateHighscoreValues(name);
                System.out.println("updated HighscoreValues: "+HighscoreValues);
                TransmitData data=new TransmitData(name, pointsArr, HighscoreValues);
                broadcast(data);

                return true;
            }
        }
        System.out.println(name+" has not won!");
        return false;
    }

    public int[] countpoints() {
        int size=getPlayers().size();
        int[] allPoints=new int[size];
        for(int i = 0; i < size; i++) {
            int decksize = getPlayers().get(i).getDeck().getSize();
            int roundpoints = 0;
            for (int j = 0; j < decksize; j++) {
                Value val = getPlayers().get(i).getDeck().getCard(j).getValue();
                roundpoints = roundpoints + valToInt(val);
            }
            allPoints[i] = roundpoints;
        }
        return allPoints;
    }

    public int valToInt(Value v){
        int index= v.ordinal();
        if (index >= 13){
            return 50;
        }else if ((index >= 10) && (index < 13)){
            return 20;
        }else {
            return index;
        }
    }

    public String getHighscoreValues() {
       try{
           FileReader readfile=new FileReader("highscore.txt");
           BufferedReader reader= new BufferedReader(readfile);
           String text="";
           String s;
           while ((s=reader.readLine())!= null) {
               text = text + s;
               System.out.println(text);
           }
           reader.close();
//           System.out.println("getHighscorevalues: "+text);
            return text;
       }catch(IOException e){
           return "Cannot read from highscore file";
       }
    }

    public void updateHighscoreValues(String str){

        System.out.println("HighscoreValues before new calculation: "+HighscoreValues);
        //läsa in från HighscoreValues
        //dela upp vid \n
        String[] entries= HighscoreValues.split(";");
        String temp1="";
        boolean changed=false;
        System.out.println("antal entries: "+entries.length);
        for (int i = 0; i<entries.length; i++){
            System.out.println(entries[i]);
        }
        for (int i=0; i<entries.length; i++){
            //dela upp vid : och jämför namn
            if (str.equals(entries[i].split(":")[0])){
                //öka påängen med 1
                int newPoints=Integer.parseInt(entries[i].split(":")[1]);
                newPoints=newPoints+1;
                String x= Integer.toString(newPoints);
                temp1=temp1+str+":"+x+";";
                changed=true;
            }else{
                temp1=temp1+entries[i]+";";
            }
        }
        if (changed =true){
            String[] order=temp1.split(";");
            int size=order.length;
            sortStringArray(order, size);
            for (int l=0; l<order.length;l++){
                System.out.println(order[l]);
            }            HighscoreValues=new String();
            for (int k=0; k< entries.length; k++){
                HighscoreValues=HighscoreValues+order[k]+";";
            }
        }else
        if(changed==false){HighscoreValues=HighscoreValues+ str+":1;";}


        //kolla om filen finns, ggf. skapa den
        File Higscorefile=new File("highscore.txt");
        if (!Higscorefile.exists()){
            try{
                Higscorefile.createNewFile();
            }catch(IOException e){
                System.out.println("Cant create file");
            }
        }
        // write to file
        FileWriter writeFile=null;
        BufferedWriter writer= null;
        try{
            writeFile= new FileWriter(Higscorefile);
            writer=new BufferedWriter(writeFile);
            writer.write(this.HighscoreValues);
            writer.close();
        } catch (IOException e) {
            System.out.println("Cant write to file");
        }
        System.out.println("after new calculation: "+HighscoreValues);

    }
    public void sortStringArray(String[] stringArr, int size) {
    String temp=null;

    for (int i=0; i<size;i++) {
        for (int j=1; j<size;j++) {
            System.out.println(Integer.parseInt(stringArr[j-1].split(":")[1])+"<"+Integer.parseInt(stringArr[j].split(":")[1]));
        if (Integer.parseInt(stringArr[j-1].split(":")[1])<Integer.parseInt(stringArr[j].split(":")[1])){
            temp= stringArr[j-1];
            stringArr[j-1]=stringArr[j];
            stringArr[j]=temp;
        }
        }
    }}

    public void run()
    {
        try (ServerSocket serverSocket = new ServerSocket(port)) 
        {
            // lobby
            System.out.println("Chat Server is listening on port " + port);
            System.out.println("Waiting for players to connect...");
            
            if(!in_match)
            {
                while (game.getPlayers().size() < playerLimit) {
                    Socket socket = serverSocket.accept();
                    ClientThread newUser = new ClientThread(socket, this);
                    clientThreads.add(newUser);
                    System.out.println("New user joined the lobby");
                    newUser.start();
                    broadcast((Integer)playerLimit);
                }             
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if(args.length < 2)
            return;
        int port = Integer.parseInt(args[0]);
        int limit = Integer.parseInt(args[1]);
        Server server = new Server(port, limit);
        server.run();
    }

    public void send(Object object, ClientThread user)
    {
        user.sendObject(object);
    }

    public void broadcast(Object object) {
        for (ClientThread aUser : clientThreads) {
            aUser.sendObject(object);
        }
    }

    public void broadcast(Object object, ClientThread excludeUser) {
        for (ClientThread aUser : clientThreads) {
            if (aUser != excludeUser) {
                aUser.sendObject(object);
            }
        }
    }

    public void updateClientsGame(Game new_game)
    {
        Game player_game = new_game.copy(new_game);
        for(int i = 0; i < this.game.getPlayers().size(); i++)
        {   
            player_game.setPlayerId(i);
            send(player_game, clientThreads.get(i));
        }
    }
 
    /**
     * Stores username of the newly connected client.
     */
    void addUser(Player user) {
        game.addPlayer(user);
        System.out.println("(" + game.getPlayers().size() + "/"+ playerLimit + ") users connected");
        broadcast(game.getPlayers());
        
        if(game.getPlayers().size() == playerLimit)
        {
            in_match = true;
            System.out.println("Match full, dealing cards...");
            dealCards();
        }
    }
 
    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    void removeUser(Player user, ClientThread aUser) {
        game.removePlayer(user);
        clientThreads.remove(aUser);
        System.out.println("The user " + user.getName() + " quitted");
        System.out.println("(" + game.getPlayers().size() + "/"+ playerLimit + ") users connected");
    }
 
    ArrayList<Player> getPlayers() {
        return this.game.getPlayers();
    }
 
    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {
        return !this.game.getPlayers().isEmpty();
    }

    public boolean inMatch()
    {
        return in_match;
    }
}
