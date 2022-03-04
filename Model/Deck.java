/**
 * This class represents a deck of cards
 * @author Dag Brynildsen Tholander
 */

package Model;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.*;

public class Deck implements Serializable {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();
    }

    /**
     * Create a deck from an array of cards
     * @param _cards
     * @author Dag Brynildsen Tholander
     */
    public Deck(Card[] _cards) {
        cards = new ArrayList<Card>();
        for (int i = 0; i < _cards.length; i++) {
            addCard(_cards[i]);
        }
    }

    /**
     * Creates a standard UNO deck of 108 cards
     * @author Dag Brynildsen Tholander
     */
    public void fillDeck() {
        // creates a standard UNO deck of 108 cards
        // first create all basic cards
        for (int c = 0; c < 4; c++) {
            Col color = Col.values()[c];
            for (int v = 1; v <= 12; v++) {
                Value value = Value.values()[v];
                Card tmp = new Card(value, color);
                addCard(tmp);
                addCard(tmp);
            }
            addCard(new Card(Value.zero, color));
        }
        // then add all wildcards
        addCard(new Card(Value.plus4, Col.black));
        addCard(new Card(Value.plus4, Col.black));
        addCard(new Card(Value.plus4, Col.black));
        addCard(new Card(Value.plus4, Col.black));
        addCard(new Card(Value.pickColor, Col.black));
        addCard(new Card(Value.pickColor, Col.black));
        addCard(new Card(Value.pickColor, Col.black));
        addCard(new Card(Value.pickColor, Col.black));
    }

    /**
     * Suffles a deck
     * @param deck
     * @author Dag Brynildsen Tholander
     */
    public static void shuffle(Deck deck) {
        Collections.shuffle(deck.cards);
    }

    /**
     * Adds a new card to the deck
     * @param card
     * @author Dag Brynildsen Tholander
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Removes all cards that match card. 'card' object doesn't have to be in the deck, 
     * as long as there is a card with the same value and color
     * @param card
     * @author Dag Brynildsen Tholander
     */
    public void removeCard(Card card) {
        // Remove all cards that match 'card's value and color
        if (!isEmpty()) {
            for (int i = 0; i < cards.size(); i++) {
                Card tmp = cards.get(i);
                if (Card.equals(card, tmp)) {
                    cards.remove(i);
                    return;
                }
            }
            throw new IllegalArgumentException("Can't remove card, card is not in deck!");
        } else
            throw new IllegalArgumentException("Can't remove card, deck is empty!");
    }

    /**
     * @author Dag Brynildsen Tholander
     * @return the card at the top of the deck
     */
    public Card drawCard() {
        if (isEmpty())
            return null;
        return cards.get(cards.size() - 1);
    }

    /**
     * @author Dag Brynildsen Tholander
     * @param i
     * @return the card at index i
     */
    public Card getCard(int i) {
        if (i > cards.size())
            throw new IllegalArgumentException("Index of of range!");
        else
            return cards.get(i);
    }

    public int getSize() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.size() == 0;
    }

    /**
     * Check if an equal card is in the deck
     * @author Dag Brynildsen Tholander
     * @param card
     * @return true if there is such a card
     */
    public boolean inDeck(Card card) {
        for (int i = 0; i < cards.size(); i++) {
            if (Card.equals(card, cards.get(i)))
                return true;
        }
        return false;
    }

    public String toString() {
        String tmp = "";
        for (int i = 0; i < cards.size(); i++) {
            tmp += cards.get(i).toString() + "\n";
        }
        tmp += "Deck size : " + cards.size();
        return tmp;
    }
    /**
     * If deck contains a card with Value.plus4 or Value.pickColor that isn't black, make it black
     * @author Ludvig Andersson
     */
    public void revertBlack() {
        for (int i = 0; i < cards.size(); i++) {
            if ((cards.get(i).getValue() == Value.plus4 || cards.get(i).getValue() == Value.pickColor)
                    && cards.get(i).getColor() != Col.black) {
                cards.get(i).setColor(Col.black);
            }
        }
    }
}
