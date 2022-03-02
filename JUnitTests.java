import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JOptionPane;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.*;

public class JUnitTests {
	
	private Card compSillyCard;
	private Card sillyCard;
	private Player tim;
	private Server testServ;
	
	@BeforeEach
	void setUp() {
		tim = new Player("Tim");
		testServ = new Server(0,2);
		testServ.getGame().addPlayer(tim);
		
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
		sillyCard = new Card(Value.plus4, Col.black);
		compSillyCard = sillyCard;
		sillyCard.setColor(Col.green);
		testServ.getGame().getDeck().addCard(sillyCard);
		
		
		Deck.shuffle(testServ.getGame().getDeck());
		
		testServ.getGame().getDiscardDeck().addCard(new Card(Value.one,Col.green));
	}
	
	@Test
	void testThing() {
		assertTrue(testServ.getGame().getDeck().inDeck(sillyCard));
		TransmitData data = new TransmitData(null, -100, Col.black, true, false, false);
		testServ.handleCard(data);
		testServ.handleCard(data);

		//assertFalse(testServ.getGame().getDiscardDeck().inDeck(compSillyCard));
	}
	
}
