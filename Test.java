public class Test {
    public static void main(String[] args)
    {
        Deck deck = new Deck();
        System.out.println(deck.toString());
        deck.fillDeck();
        System.out.println(deck.toString());
        deck.removeCard(new Card(Value.eight, Col.yellow));
        System.out.println(deck.toString());
        deck.addCard(new Card(Value.eight, Col.yellow));
        deck.addCard(new Card(Value.eight, Col.yellow));
    }
}
