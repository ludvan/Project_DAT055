package View;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import Model.Player;

/**
 * @author ?
 * @version 22-02-21
 */
public class LobbyView extends JPanel {

	/**
	 * Creates a visual representation of a lobby with the chosen color as background
	 * @param color The color of the lobby
	 */
    public LobbyView(Color color) {

        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        GridLayout lobbyLayout = new GridLayout(2, 5);
        setLayout(lobbyLayout);
        setBackground(color);
        setVisible(true);
    }

    /**
     * Updates the visual representation of a lobby based on the size of the lobby and the players already in the server
     * @param players The list of players currently in the lobby
     * @param limit The max size of the lobby
     */
    public void update(ArrayList<Player> players, int limit) {

        removeAll();
        // Add player
        for (int i = 0; i < players.size(); i++) {
            GamePlayer lobbyDisplay = new GamePlayer(true);
            lobbyDisplay.setText(players.get(i).getName());

            add(lobbyDisplay);
        }

        // Add blank positions
        for (int i = 0; i < limit - players.size(); i++) {
            GamePlayer lobbyDisplay = new GamePlayer();
            lobbyDisplay.setText("Open slot");

            add(lobbyDisplay);
        }
        revalidate();
        repaint();
    }

}