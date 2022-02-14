import java.awt.*;
import javax.swing.*;

public class GamePlayer extends JButton
{
    public GamePlayer(String text)
    {
        ImageIcon icon = new ImageIcon("GUI/Misc/profile.png");
        Image image = icon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(64, 100,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
        icon = new ImageIcon(newimg); 
        setIcon(icon);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setText(text);
        setBorderPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);
        setForeground(Color.white);
    }

    public GamePlayer()
    {
        ImageIcon icon = new ImageIcon("GUI/Misc/profile.png");
        Image image = icon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
        icon = new ImageIcon(newimg); 
        setIcon(icon);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setBorderPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);
        setForeground(Color.white);
    }
}
