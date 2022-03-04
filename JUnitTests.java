import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.*;

/**
 * @author Ludvig Andersson
 * Tests most Model methods that do not require threads
 */

public class JUnitTests {
	
	private Card compSillyCard;
	private Card sillyCard;
	private Card norm4;
	private Player tim;
	private Player bob;
	private Player joe;
	private Server testServ;
	
	private GetDate date;
	
	private Card card1;
	private Card card2;
	
	@BeforeEach
	void setUp() {
		date = new GetDate();
		tim = new Player("Tim");
		bob = new Player("Bob");
		joe = new Player("Joe");
		testServ = new Server(0,2);
		testServ.getGame().addPlayer(tim);
		testServ.getGame().addPlayer(bob);
		testServ.getGame().addPlayer(joe);
		
		for(int c = 0; c < 4; c++)
        {
            Col color = Col.values()[c];
            for(int v = 1; v <= 2; v++)
            {
                Value value = Value.values()[v];
                Card tmp = new Card(value, color);
                testServ.getGame().getDeck().addCard(tmp);
            }
        }
		norm4 = new Card(Value.plus4, Col.black);
		sillyCard = new Card(Value.plus4, Col.black);
		sillyCard.setColor(Col.green);
		compSillyCard = new Card(Value.plus4, Col.black);
		compSillyCard.setColor(Col.green);
		testServ.getGame().getDeck().addCard(sillyCard);
		
		
		testServ.getGame().getDiscardDeck().addCard(new Card(Value.one,Col.green));	
	}
	
	
	
	@Test
	//tests deck functions
	void testDeck() {
		//tries to remove card that doesn't exist
		assertThrows(IllegalArgumentException.class, () -> testServ.getGame().getDiscardDeck().removeCard(sillyCard));
		
		//tim draws card
		Card draw = testServ.getGame().getDiscardDeck().drawCard();
		tim.getDeck().addCard(draw);
		testServ.getGame().getDiscardDeck().removeCard(draw);
		//check if tim drew card
		assertTrue(tim.getDeck().drawCard() == draw);
		//check if drawpile is empty
		assertTrue(testServ.getGame().getDiscardDeck().isEmpty());
		//tries to draw from empty deck
		assertThrows(IllegalArgumentException.class, () -> testServ.getGame().getDiscardDeck().removeCard(draw));
		//check top card of empty deck
		assertTrue(testServ.getGame().getDiscardDeck().drawCard() == null);
		//check if fillDeck works
		testServ.getGame().getDiscardDeck().fillDeck();
		assertTrue(testServ.getGame().getDiscardDeck().getSize() == 108);
		//checks that only 1 card is removed by removeCard
		testServ.getGame().getDiscardDeck().removeCard(draw);
		assertTrue(testServ.getGame().getDiscardDeck().getSize() == 107);
		assertTrue(inDeckTest(draw,testServ.getGame().getDiscardDeck()));
		//checks again should be no more copies
		testServ.getGame().getDiscardDeck().removeCard(draw);
		assertTrue(testServ.getGame().getDiscardDeck().getSize() == 106);
		assertFalse(inDeckTest(draw,testServ.getGame().getDiscardDeck()));
		//tries to get card out of index
		assertThrows(IllegalArgumentException.class, () -> testServ.getGame().getDiscardDeck().getCard(200));
		//checks if revertBlack works
		testServ.getGame().getDeck().revertBlack();
		assertFalse(inDeckTest(compSillyCard, testServ.getGame().getDeck()));
		assertTrue(inDeckTest(norm4, testServ.getGame().getDeck()));
		//tests inDeck
		Card notin = new Card(Value.six, Col.green);
		assertFalse(testServ.getGame().getDeck().inDeck(notin));
		//tests toString
		String testString = "val : one  col : red\n"
				+ "val : two  col : red\n"
				+ "val : one  col : green\n"
				+ "val : two  col : green\n"
				+ "val : one  col : yellow\n"
				+ "val : two  col : yellow\n"
				+ "val : one  col : blue\n"
				+ "val : two  col : blue\n"
				+ "val : plus4  col : black\n"
				+ "Deck size : 9";
		assertTrue(testServ.getGame().getDeck().toString().equals(testString));
	}
	
	@Test
	//tests GetDate
	//!!!WARNING!!! have to update every day
	void testGetDate() {
		String dategot = date.FetchDate();
		assertTrue(dategot.equals("4 mars 2022"));
	}
	
	boolean equalTest(Card card1, Card card2) {
		return card1.getValue() == card2.getValue() && card1.getColor() == card2.getColor();
	}
	
	boolean inDeckTest(Card card, Deck tDeck) {
	    for (int i = 0; i < tDeck.getSize(); i++) {
	        if (equalTest(card, tDeck.getCard(i))) {
	            return true;
	        }
	    }
	    return false;
	}
	
	@Test
	//tests Card methods not covered by deckTest
	void cardTest() {
		//tries to create wildcard that isn't black
		assertThrows(IllegalArgumentException.class, () -> new Card(Value.plus4, Col.green));
		//tries to create black card that isn't wildcard
		assertThrows(IllegalArgumentException.class, () -> new Card(Value.one, Col.black));
		//checks if same value can be played on same value
		for(int v = 1; v <= 12; v++) {
			Value value = Value.values()[v];
			card1 = new Card(value, Col.red);
			card2 = new Card(value, Col.green);
			assertTrue(Card.isStackable(card2, card1));
		}
		//checks if same color can be played on same color
		for (int c = 0; c < 4; c++) {
            Col color = Col.values()[c];
            card1 = new Card(Value.two, color);
			card2 = new Card(Value.two, color);
			assertTrue(Card.isStackable(card2, card1));
        }
		//checks playing color card on black card/playing black card on color card
		card1 = new Card(Value.plus4, Col.black);
		for (int c = 0; c < 4; c++) {
            Col color = Col.values()[c];
            for (int v = 1; v <= 12; v++) {
                Value value = Value.values()[v];
                card2 = new Card(value,color);
                assertTrue(Card.isStackable(card2, card1));
            }
        }
		
	}
	
	@Test
	//tests nextturn and reverse
	void gameTest() {
		//step forwards 1 turn at a time
		for(int i = 1; i<3;i++) {
			testServ.getGame().setCurrentTurn(testServ.getGame().nextTurn());
			assertTrue(testServ.getGame().getCurrentTurn() == i);
		}
		//should go back to 0
		testServ.getGame().setCurrentTurn(testServ.getGame().nextTurn());
		assertTrue(testServ.getGame().getCurrentTurn() == 0);
		
		//reverse, should be 2
		testServ.getGame().setReverse(true);
		testServ.getGame().setCurrentTurn(testServ.getGame().nextTurn());
		assertTrue(testServ.getGame().getCurrentTurn() == 2);
	}
	
	
	
}
