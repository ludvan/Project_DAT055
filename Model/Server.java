package Model;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends Thread {
    private int port;
    private int playerLimit;
    private ArrayList<ClientThread> clientThreads;
    private boolean in_match;
    private Game game;
    private int drawCardCounter; // Used to limit the amount of drawn cards
    private boolean unoPressed;

    public Server(int _port, int player_limit) {
        port = _port;
        game = new Game();
        clientThreads = new ArrayList<ClientThread>();
        playerLimit = player_limit;
        in_match = false;
        drawCardCounter = 0;
    }

    public Game getGame() {
        return game;
    }

    // This function deals cards to the players in the beginning of the match.
    public void dealCards() {
        Deck tmp = new Deck();
        tmp.fillDeck();
        Deck.shuffle(tmp);
        game.setDiscardDeck(tmp);

        // Deal 7 cards per player
        int cpp = 0;
        while (cpp < 7) {
            for (int i = 0; i < game.getPlayers().size(); i++) {
                if (game.getDiscardDeck().getSize() <= 0)
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
        updateClientsGame(game);
    }

    // Handles draw card
    private void handleDrawCard(TransmitData data) {
        int currentPlayer = game.getCurrentTurn();

        // Check if the user wants to draw a card
        drawCardCounter++;
        Card tmp;
        playerDraw(1, currentPlayer);

        tmp = game.getPlayerDeck(currentPlayer).drawCard();

        // The user has drawn 3 cards. Move on
        if (drawCardCounter >= 3) {
            game.setCurrentTurn(game.nextTurn());
            drawCardCounter = 0;
        }
        // If the card that was drawn was stackable, then we reset the counter
        if (Card.isStackable(tmp, game.getDeck().drawCard())) {
            drawCardCounter = 0;
        }

        updateClientsGame(game);
    }

    // Handles a card
    public void handleCard(TransmitData data) {
        int currentPlayer = game.getCurrentTurn();

        if (data.getPressedUno()) {
            unoPressed = true;
            updateClientsGame(game);
            return;
        }

        if (data.getDrawCard()) {
            handleDrawCard(data);
            return;
        }

        // Check if we can place a card
        Card card = data.getCard();
        if (!game.getDeck().isEmpty()) {
            if (!Card.isStackable(card, game.getDeck().drawCard())) {
                send("can't place that card", clientThreads.get(currentPlayer));
                return;
            }
        }

        // If we can place a card do the following
        if (card.getValue() == Value.plus2) {
            playerDraw(2, game.nextTurn());
            game.setCurrentTurn(game.nextTurn());
        }
        if (card.getValue() == Value.plus4) {
            playerDraw(4, game.nextTurn());
            game.setCurrentTurn(game.nextTurn());
        }

        if (card.getValue() == Value.stop)
            game.setCurrentTurn(game.nextTurn());
        if (card.getValue() == Value.reverse)
            game.setReverse(!game.getReverse());
        // if a color was chosen (only for black cards)
        if (data.getChooseColor()) {
            card.setColor(data.getChosenColor());
        }
        // draw 4 penalty cards if uno wasnt pressed when player only had one card left
        if (!unoPressed && game.getPlayerDeck(currentPlayer).getSize() == 2) {
            playerDraw(4, currentPlayer);
        }

        unoPressed = false;

        game.deckAddCard(card);
        game.playerRemoveCard(currentPlayer, card);

        if (!WeHaveAWinner()) {
            game.setCurrentTurn(game.nextTurn());
            updateClientsGame(game);
        } else {
            game.setCurrentTurn(-2);
            updateClientsGame(game);
        }
    }

    // player draws number amount of cards
    private void playerDraw(int number, int player) {
        Card tmp;

        for (int i = 0; i < number; i++) {
            if (game.getDiscardDeck().getSize() == 0) {
                reShuffle();
            }
            tmp = game.getDiscardDeck().drawCard();
            game.playerAddCard(player, tmp);
            game.discardDeckRemove(tmp);
            Deck.shuffle(game.getDiscardDeck());
        }

    }

    // Reshuffles deck
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
    }

    /**
     * deciding if round is won
     * decision is made by cecking the size of a players handdeck at the end of
     * their turn
     *
     * @return true if round is wo by current player else false
     * @author Christina Meisoll
     */
    public boolean WeHaveAWinner() {
        int currentPlayer = game.getCurrentTurn();
        String name = game.getPlayers().get(currentPlayer).getName();

        if (game.getPlayers().get(currentPlayer).getDeck().getSize() == 0) {
            int[] pointsArr = countpoints();
            System.out.println(name + " HAS WON!");
            TransmitData data = new TransmitData(name, pointsArr);
            broadcast(data);
            return true;
        }
        return false;
    }

    /**
     * counts points in every players hand deck
     *
     * @return allPoints - an integer array containing each players points in the
     *         same order as the players
     * @author Christina Meisoll
     */
    private int[] countpoints() {
        int size = getPlayers().size();
        int[] allPoints = new int[size];
        for (int i = 0; i < size; i++) {
            int decksize = getPlayers().get(i).getDeck().getSize();
            int roundpoints = 0;
            for (int j = 0; j < decksize; j++) {
                Value val = getPlayers().get(i).getDeck().getCard(j).getValue();
                roundpoints = roundpoints + valToScore(val);
            }
            allPoints[i] = roundpoints;
        }
        return allPoints;
    }

    // Converts a cards value to a score
    private int valToScore(Value v) {
        int index = v.ordinal();
        if (index >= 13) {
            return 50;
        } else if ((index >= 10) && (index < 13)) {
            return 20;
        } else {
            return index;
        }
    }

    public void run() {
        // The below line will be true if someone is waiting to be let into the server
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat Server is listening on port " + port);
            System.out.println("Waiting for players to connect...");

            // Lobby, wait for players to join
            if (!in_match) {
                while (game.getPlayers().size() < playerLimit) {
                    Socket socket = serverSocket.accept();
                    ClientThread newUser = new ClientThread(socket, this);
                    clientThreads.add(newUser);
                    System.out.println("New user joined the lobby");
                    broadcast((Integer) playerLimit);
                    newUser.start();
                }

            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2)
            return;
        int port = Integer.parseInt(args[0]);
        int limit = Integer.parseInt(args[1]);
        Server server = new Server(port, limit);
        server.run();
    }

    // Sends object to specific player
    public void send(Object object, ClientThread user) {
        user.sendObject(object);
    }

    // Sends object to all players
    public void broadcast(Object object) {
        for (ClientThread aUser : clientThreads) {
            aUser.sendObject(object);
        }
    }

    // Sends object to all players except one
    public void broadcast(Object object, ClientThread excludeUser) {
        for (ClientThread aUser : clientThreads) {
            if (aUser != excludeUser) {
                aUser.sendObject(object);
            }
        }
    }

    // Updates the clients game
    public void updateClientsGame(Game new_game) {
        Game player_game = new_game.copy(new_game);
        // the player_id variable of the game class can't be the same for every player
        for (int i = 0; i < this.game.getPlayers().size(); i++) {
            player_game.setPlayerId(i);
            send(player_game, clientThreads.get(i));
        }
    }

    // Stores username of the newly connected client.
    void addUser(Player user) {
        game.addPlayer(user);
        System.out.println("(" + game.getPlayers().size() + "/" + playerLimit + ") users connected");
        broadcast(game.getPlayers());

        if (game.getPlayers().size() == playerLimit) {
            in_match = true;
            System.out.println("Match full, dealing cards...");
            dealCards();
        }
    }

    // When a client is disconneted, removes the associated username and UserThread
    void removeUser(Player user, ClientThread aUser) {
        game.removePlayer(user);
        clientThreads.remove(aUser);
        System.out.println("The user " + user.getName() + " quitted");
        System.out.println("(" + game.getPlayers().size() + "/" + playerLimit + ") users connected");
    }

    ArrayList<Player> getPlayers() {
        return this.game.getPlayers();
    }

    // Returns true if there are other users connected
    boolean hasUsers() {
        return !this.game.getPlayers().isEmpty();
    }

    public boolean inMatch() {
        return in_match;
    }
}
