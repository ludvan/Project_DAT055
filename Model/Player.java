package Model;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private Deck deck;

    public Player(String name) {
        this.name = name;
        deck = new Deck();
    }

    public Player(String name, Deck deck) {
        this.name = name;
        this.deck = deck;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getDeck() {
        return deck;
    }

    public String toString() {
        return "name : " + name + " deck : " + deck.toString();

    }
}
