package View;

import java.awt.*;
import javax.swing.*;

public class GamePlayer extends JButton {
    private boolean isActive;

    public void setActive(boolean active) {
        isActive = active;
        setPlayerIcon();
    }

    public boolean getActive() {
        return isActive;
    }

    public void setPlayerIcon() {
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
