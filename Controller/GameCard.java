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

    public void setCard(Card card) {
        this.card = card;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        if (opacity < 1)
            setOpaque(false);
        else
            setOpaque(true);
    }

    // Sets the texture of the card
    private void SetCardFace(Card card, boolean flipped) {
        ImageIcon icon;
        if (flipped || card == null)
            icon = new ImageIcon("GUI/Textures/backface.png");
        else
            icon = new ImageIcon("GUI/Textures/" + card.getColor() + "" + card.getValue() + ".png");

        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(64, 100, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);
        setIcon(icon);
    }

    public GameCard() {
        SetCardFace(null, true);
        sharedConst();
    }

    public GameCard(Card card) {
        this.card = card;
        SetCardFace(this.card, true);
        sharedConst();
    }

    // This has to be done for all cards, regardless of which card it is.
    private void sharedConst() {
        setOpacity(1);
        setBorderPainted(false);

        // Moves the card when the cursor is hovering over it
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

    // Modified to allow for opacity
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity));
        super.paint(g2);
        g2.dispose();
    }
}