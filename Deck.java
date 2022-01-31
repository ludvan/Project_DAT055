import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;

    public Deck()
    {
        cards = new ArrayList<Card>();
    }

    public Deck(Card[] _cards)
    {
        cards = new ArrayList<Card>();
        for(int i = 0; i < _cards.length; i++)
        {
            addCard(_cards[i]);
        }
    }

    public void fillDeck()
    {
        // lägg till alla kort förutom de svarta
        for(int c = 0; c < 4; c++)
        {
            Col color = Col.values()[c];
            for(int v = 0; v <= 11; v++)
            {
                Value value = Value.values()[v];
                Card tmp = new Card(value, color);
                addCard(tmp);
            }
        }

        addCard(new Card(Value.plus4, Col.black));
        addCard(new Card(Value.plus4, Col.black));
        addCard(new Card(Value.pickColor, Col.black));
        addCard(new Card(Value.pickColor, Col.black));
    }

    public void addCard(Card card)
    {
        if(!inDeck(card) || card.getColor() == Col.black)
            cards.add(card);
        else
            throw new IllegalArgumentException("Can't add card, card is already in deck!");
    }

    public void removeCard(Card card)
    {
        if(!isEmpty())
        {
            for(int i = 0; i < cards.size(); i++)
            {
                Card tmp = cards.get(i);
                if(Card.equals(card, tmp))
                    cards.remove(i);
            }
        }
        else
            throw new IllegalArgumentException("Can't remove card, deck is empty!");
    }

    public Card drawCard()
    {
        return cards.get(cards.size()-1);
    }

    public int getSize()
    {
        return cards.size();
    }

    public boolean isEmpty()
    {
        return cards.size() == 0;
    }

    public boolean inDeck(Card card)
    {
        for(int i = 0; i < cards.size(); i++)
        {
            if(Card.equals(card, cards.get(i)))
                return true;
        }
        return false;
    }

    public String toString()
    {
        String tmp = "";
        for(int i = 0; i < cards.size(); i++)
        {
            tmp += cards.get(i).toString() + "/n";
        }
        tmp += "Deck size : " + cards.size();
        return tmp;
    }
}
