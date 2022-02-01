public class Test 
{
    public static void main(String[] args)
    {
        Deck deck = new Deck();
        System.out.println(deck.toString());
        deck.fillDeck();
        System.out.println(deck.toString());
        deck.removeCard(new Card(Value.eight, Col.yellow));
        System.out.println(deck.toString());
        deck.addCard(new Card(Value.eight, Col.yellow));
        Deck.shuffle(deck);
        System.out.println("every card that is stackable on " + deck.drawCard().toString() + " : \n");

        for(int i = 0; i < deck.getSize(); i++)
        {
            if(Card.isStackable(deck.drawCard(), deck.getCard(i)))
                System.out.println(deck.getCard(i));
        }

        System.out.println(Card.isStackable(new Card(Value.eight, Col.yellow), new Card(Value.five, Col.yellow)));
        System.out.println(Card.isStackable(new Card(Value.eight, Col.yellow), new Card(Value.five, Col.red)));
    }
}
