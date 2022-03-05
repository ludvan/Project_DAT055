package Model;

import javax.swing.*;
import View.GameBoard;
import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient {
    private GameBoard board;
    private String hostname;
    private int port;
    private String userName;
    private Game game;
    private boolean in_match;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket socket;
    private ArrayList<Object> sendBuffer;
    private boolean hasSelectCard = false;
    private String HighscoreValues;

    /**
     * Creates a new client with a specifiec username with a connection to hostname on port
     * @author Dag Brynildsen Tholander
     * @param hostname
     * @param port
     * @param username
     */
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

    /**
     * Adds object to sendbuffer
     * @param object
     * @author Dag Brynildsen Tholander
     */
    public void sendToServer(Object object) {
        sendBuffer.add(object);
        hasSelectCard = true;
    }

    /**
     * Main loop of the client program. Handles connection, then reads and writes data to server.
     * @author Dag Brynildsen Tholander
     */
    public void execute() {
        try {
            // Create socket, and send username to server
            socket = new Socket(hostname, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(userName);

            // Loop that reads and writes to the server
            try {
                Object recieved;
                while (true) {
                    recieved = inputStream.readObject();

                    // If-statements that handle all types of data sent from server. Could be solved
                    // better by for example using the TransmitData class
                    if (recieved instanceof Integer) {
                        board.setPlayerLimit((Integer) recieved);
                    }
                    if (recieved instanceof ArrayList) {
                        @SuppressWarnings("unchecked") // Not good. Could be sent via an extension of Transmitdata
                                                       // instead
                        ArrayList<Player> playerList = (ArrayList<Player>) recieved;
                        board.lobbyUpdate(playerList);
                    }
                    if (recieved instanceof Game) {
                        if (!in_match) {
                            setGame((Game) recieved);
                        } else {
                            updateGame((Game) recieved);
                        }
                    }
                    // TransmitData is used to check wether or not someone has won
                    if (recieved instanceof TransmitData) {
                        someoneWon(((TransmitData) recieved).getWinner(), ((TransmitData) recieved).getPointArr());
                    }

                    if (in_match) {
                        if (isClientTurn()) {
                            hasSelectCard = false;
                            while (!hasSelectCard) {
                                // Wait while the user whose turn it is selects a card
                                try {
                                    Thread.sleep(250);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    // If the send buffer isnt empty, then send the object
                    if (sendBuffer.size() != 0) {
                        try {
                            outputStream.writeObject(sendBuffer.get(0));
                            outputStream.reset();
                            sendBuffer.clear();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (UnknownHostException ex) {
            // If the ip-adress is invalid or something else has gone wrong
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // @author Dag Brynildsen Tholander
    // Called once when the match has begun to sync all players games
    private void setGame(Game new_game) {
        this.game = new Game();
        game = game.copy(new_game);
        in_match = true;
        board.setGame(game);
        board.update();
    }

    // @author Dag Brynildsen Tholander
    // Used to update every players game once something is changed
    private void updateGame(Game new_game) {
        game = game.copy(new_game);
        board.setGame(game);
        board.update();
    }

    /**
     * @author Dag Brynildsen Tholander
     * @return returns true if its our turn
     */
    public boolean isClientTurn() {
        return this.game.getPlayerId() == game.getCurrentTurn();
    }

    /**
     * @author Dag Brynildsen Tholander
     * @return true if this client has a card stackable against the dealers card
     */
    public boolean hasStackableCard() {
        boolean tmp = false;
        // loop through every card on this client and check if its stackable on the
        // dealers deck
        for (int i = 0; i < game.getPlayerDeck(game.getPlayerId()).getSize(); i++) {
            if (Card.isStackable(game.getPlayerDeck(game.getPlayerId()).getCard(i), game.getDeck().drawCard())) {
                tmp = true;
            }
        }
        // else return false
        return tmp;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    /**
     * Showing a JOptionpane informing the player on the points every player had in this round
     * Showin a JOptionpane informing the player on the highscore table
     * closing the gamewindow, sending the player back to the menu
     *
     * @param str1 - the winners name
     * @param arr - array containings every participatings players nickname
     *              and how many points they had on their hand when the match ended
     * @author Christina Meisoll
     */
    public void someoneWon(String str1, int[] arr) {
        String[] allNames = new String[arr.length];

        for (int j = 0; j < game.getPlayers().size(); j++) {
            allNames[j] = (game.getPlayers().get(j).getName());
        }
        // Sortera arrayclone i stigandeordning
        int pointclone[] = arr.clone();
        Arrays.sort(pointclone);

        // skapar stringen till JOptionpane
        String LbString = "Leaderboard \n";
        // g� ignenom pointclone f�r att hitta
        for (int c = 0; c < pointclone.length; c++) {
            for (int a = 0; a < arr.length; a++) {
                if (pointclone[c] == arr[a]) {
                    LbString = LbString + allNames[a] + ": " + pointclone[c] + "\n ";
                }
            }
        }
        // LbString=LbString+"\n\n Do you want to play again?";
        String title;
        if (str1.equals(userName)) {
            title = "CONGRATULATIONS!!! YOU WON!!!";
        } else {
            title = str1 + " has won!";
        }
        JOptionPane.showMessageDialog(null, LbString, title, JOptionPane.PLAIN_MESSAGE);

        if (HighscoreValues == null) {
            this.getHighscoreValues();
            updateHighscoreValues(str1);
            HighscoreValues = makeItMultiline(HighscoreValues);
            int answer = JOptionPane.showConfirmDialog(null, HighscoreValues, "local player statistics",
                    JOptionPane.PLAIN_MESSAGE);
            // if (answer==0){
            board.closeBoard(answer);

            // }
        }
    }

    /**
     * läser från highscore.txt och sparar i HighscoreValues
     *
     * @throws IOException - if reading from the file was unsuccessful
     * @author Christina Meisoll
     */
    public void getHighscoreValues() {
        try {
            FileReader readfile = new FileReader("highscore.txt");
            BufferedReader reader = new BufferedReader(readfile);
            String s = reader.readLine();
            reader.close();
            HighscoreValues = s;
        } catch (IOException e) {
            System.out.println("Cannot read from highscore file");
        }
    }

    /**
     * Updates the highscore.txt file.
     * If necessary the players name entry is created.
     * Saves uppdated highscores to the file.
     *
     * @throws IOException - if file doesnt exist or saving to it was unsuccessful
     * @author Christina Meisoll
     */
    private void updateHighscoreValues(String str) {
        if (HighscoreValues == null || HighscoreValues.isEmpty()) {
            HighscoreValues = str + ":1;";
        } else {
            String[] entries = HighscoreValues.split(";");
            String temp1 = "";
            boolean changed = false;
            System.out.println("changes is:" + changed);
            System.out.println("antal entries: " + entries.length);
            for (int i = 0; i < entries.length; i++) {
                System.out.println("entries[" + i + "] is: " + entries[i]);
            }
            for (int i = 0; i < entries.length; i++) {
                // dela upp vid : och j�mf�r namn
                if (str.equals(entries[i].split(":")[0])) {
                    // �ka p��ngen med 1
                    int newPoints = Integer.parseInt(entries[i].split(":")[1]);
                    newPoints = newPoints + 1;
                    System.out.println("newpoints is: " + newPoints);
                    String x = Integer.toString(newPoints);
                    System.out.println("x is: " + x);
                    temp1 = temp1 + str + ":" + x + ";";
                    System.out.println("temp1 is: " + temp1);
                    changed = true;
                    System.out.println("changes is: " + changed);
                } else {
                    temp1 = temp1 + entries[i] + ";";
                }
            }
            if (changed == true) {
                System.out.println("changes was set true");
                String[] order = temp1.split(";");
                int size = order.length;
                sortStringArray(order, size);
                System.out.println("order arrayen sorterat: ");
                for (int l = 0; l < order.length; l++) {
                    System.out.println(order[l]);
                }
                HighscoreValues = new String();
                for (int k = 0; k < entries.length; k++) {
                    HighscoreValues = HighscoreValues + order[k] + ";";
                    System.out.println("highscorevalues after if changed true: " + HighscoreValues);
                }
            } else if (changed == false) {
                System.out.println("changed was set false");
                HighscoreValues = HighscoreValues + str + ":1;";
                System.out.println("highscorevalues after if changed false: " + HighscoreValues);
            }
        }

        // kolla om filen finns, ggf. skapa den
        File Higscorefile = new File("highscore.txt");
        if (!Higscorefile.exists()) {
            try {
                Higscorefile.createNewFile();
            } catch (IOException e) {
                System.out.println("Cant create file");
            }
        }
        // write to file
        FileWriter writeFile = null;
        BufferedWriter writer = null;
        try {
            writeFile = new FileWriter(Higscorefile);
            writer = new BufferedWriter(writeFile);
            writer.write(this.HighscoreValues);
            writer.close();
        } catch (IOException e) {
            System.out.println("Can't write to file");
        }
        System.out.println("after new calculation: " + HighscoreValues);

    }

    /**
     * sorts an array of string so that the amount of games won is decreasing
     *
     * @param stringArr - Array of strings where every string has the form of "username:amount of games won"
     * @param size - stringArrs length
     * @author Christina Meisoll
     */
    private void sortStringArray(String[] stringArr, int size) {
        String temp = null;

        for (int i = 0; i < size; i++) {
            for (int j = 1; j < size; j++) {
                System.out.println(Integer.parseInt(stringArr[j - 1].split(":")[1]) + "<"
                        + Integer.parseInt(stringArr[j].split(":")[1]));
                if (Integer.parseInt(stringArr[j - 1].split(":")[1]) < Integer.parseInt(stringArr[j].split(":")[1])) {
                    temp = stringArr[j - 1];
                    stringArr[j - 1] = stringArr[j];
                    stringArr[j] = temp;
                }
            }
        }
    }

    /**
     * makes the string str multiline by dividing it i substrings at every ";" and replacing these by "\n"
     *
     * @param str - a string to divide containing
     * @return the string with linebreaks
     * @author Christina Meisoll
     */
    private String makeItMultiline(String str) {
        String[] part = str.split(";");
        String temp = "";
        for (int i = 0; i < part.length; i++) {
            temp = temp + part[i] + " matches won\n";
        }
        return temp;
    }

    public static void main(String[] args) {
        String hostname;
        String username;
        int port;
        if (args.length < 3) {
            System.out.println("Enter server ip write 'localhost' for running on same device");
            hostname = System.console().readLine();
            port = 8989;
            System.out.println("Enter a username : ");
            username = System.console().readLine();
        } else {
            hostname = args[0];
            port = Integer.parseInt(args[1]);
            username = args[2];
        }
        ChatClient client = new ChatClient(hostname, port, username);

        client.execute();
    }
}