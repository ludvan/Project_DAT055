package Model;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.*;

public class Deck implements Serializable {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();
    }

    public Deck(Card[] _cards) {
        cards = new ArrayList<Card>();
        for (int i = 0; i < _cards.length; i++) {
            addCard(_cards[i]);
        }
    }

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

    public static void shuffle(Deck deck) {
        Collections.shuffle(deck.cards);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

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

    // Returns the card on top of the deck
    public Card drawCard() {
        if (isEmpty())
            return null;
        return cards.get(cards.size() - 1);
    }

    // Returns card by index
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

    // Check if a given card is in the deck
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

    public void revertBlack() {
        for (int i = 0; i < cards.size(); i++) {
            if ((cards.get(i).getValue() == Value.plus4 || cards.get(i).getValue() == Value.pickColor)
                    && cards.get(i).getColor() != Col.black) {
                cards.get(i).setColor(Col.black);
            }
        }
    }
}
