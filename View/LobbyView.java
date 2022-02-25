package View;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import Model.Player;

public class LobbyView extends JPanel {

    public LobbyView(Color color) {

        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        GridLayout lobbyLayout = new GridLayout(2, 5);
        // playerLimit = limit;
        setLayout(lobbyLayout);
        setBackground(color);
        setVisible(true);

    }

    public void update(ArrayList<Player> players, int limit) {

        removeAll();
        // L�gg till spelare
        for (int i = 0; i < players.size(); i++) {
            GamePlayer lobbyDisplay = new GamePlayer(true);
            lobbyDisplay.setText(players.get(i).getName());

            add(lobbyDisplay);
        }

        // L�gg till rutor utan spelare
        for (int i = 0; i < limit - players.size(); i++) {
            GamePlayer lobbyDisplay = new GamePlayer();
            lobbyDisplay.setText("Open slot");

            add(lobbyDisplay);
        }
        revalidate();
        repaint();
    }

}