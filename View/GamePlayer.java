/**
 * Class for displaying the profile pictures for 
 * all players in a game
 * @author Dag Brynildsen Tholander
 * @version 2022-03-25
 */

package View;

import java.awt.*;
import javax.swing.*;
public class GamePlayer extends JButton {
    private boolean isActive;

    /**
     * Toggles the backlight for the profile picture
     * @param active
     * @author Dag Brynildsen Tholander
     */
    public void setActive(boolean active) {
        isActive = active;
        setPlayerIcon();
    }

    public boolean getActive() {
        return isActive;
    }

    // Sets the texture of the profile pictures
    private void setPlayerIcon() {
        ImageIcon icon;

        if (isActive)
            icon = new ImageIcon("GUI/Misc/profile_active.png");
        else
            icon = new ImageIcon("GUI/Misc/profile.png");

        Image image = icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);
        setIcon(icon);
    }

    public GamePlayer(boolean active) {
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        isActive = active;
        setPlayerIcon();
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.white);
    }

    public GamePlayer() {
        isActive = false;
        setPlayerIcon();
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.white);
    }
}
