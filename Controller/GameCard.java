package Controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Model.Card;

public class GameCard extends JButton {
    private Card card;

    private float opacity;

    public Card getCard() {
        return card;
    }

    public void setCard(Card _card) {
        card = _card;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        if (opacity < 1)
            setOpaque(false);
        else
            setOpaque(true);
    }

    public GameCard() {
        ImageIcon icon = new ImageIcon("GUI/Textures/backface.png");
        Image image = icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(64, 100, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);
        setIcon(icon);
        sharedConst();
    }

    public GameCard(String text) {
        ImageIcon icon = new ImageIcon("GUI/Textures/backface.png");
        Image image = icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(64, 100, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);
        setText(text);
        setIcon(icon);
        sharedConst();
    }

    public GameCard(Card _card) {
        card = _card;
        ImageIcon icon = new ImageIcon("GUI/Textures/" + _card.getColor() + "" + _card.getValue() + ".png");
        Image image = icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(64, 100, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);
        setIcon(icon);
        sharedConst();
    }

    public void sharedConst() {
        setOpacity(1);
        setBorderPainted(false);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent me) {
                Point current = getLocation();
                setLocation(current.x, current.y - 10);
            }

            public void mouseExited(MouseEvent me) {
                Point current = getLocation();
                setLocation(current.x, current.y + 10);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity));
        super.paint(g2);
        g2.dispose();
    }
}