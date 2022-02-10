

import java.awt.Image;

import javax.swing.*;

public class GameCard extends JButton
{
    private Card card;

    public Card getCard()
    {
        return card;
    }
    public void setCard(Card _card)
    {
        card = _card;
    }
    public GameCard()
    {
        ImageIcon icon = new ImageIcon("GUI/Textures/backface.png");
        Image image = icon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(64, 100,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
        icon = new ImageIcon(newimg); 
        setIcon(icon);
    }
    public GameCard(String text)
    {
        ImageIcon icon = new ImageIcon("GUI/Textures/backface.png");
        Image image = icon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(64, 100,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
        icon = new ImageIcon(newimg); 
        setText(text);
        setIcon(icon);
    }
    public GameCard(Card _card)
    {
        card = _card;
        ImageIcon icon = new ImageIcon("GUI/Textures/" + _card.getColor()+""+_card.getValue() + ".png");
        Image image = icon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(64, 100,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        icon = new ImageIcon(newimg); 
        setIcon(icon);
    }
}