import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.JOptionPane;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.*;
import Controller.*;
import View.*;
import View.ServerOutputView;

public class JUnitTests {
	
	private Card compSillyCard;
	private Card sillyCard;
	private Player tim;
	private Server testServ;
	
	private Process serverProcess;
	
	private ServerSocket serverSocket;
	
	
	ChatClient timClient;
	ChatClient bobClient;
	
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
		
		
		//ArrayList<ClientThread> threadList = new ArrayList<ClientThread>();
		
		//ClientThread thread = new ClientThread(new Socket(), testServ);
		
		//threadList.add(thread);
		
		//testServ.setClientThreads(threadList);
		
		/*
		try{
			serverSocket = new ServerSocket(8989);
			System.out.println("here1");
			Socket socket = new Socket("localhost",8989);
			//Socket socket = serverSocket.accept();
			System.out.println("here2");
	        ClientThread newUser = new ClientThread(socket, testServ);
	        System.out.println("here3");
	        threadList.add(newUser);
	        System.out.println("here4");
	        testServ.setClientThreads(threadList);
		}
		
        catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
		*/
	}
	
	/*
	@AfterEach
	void close() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/*
	@BeforeEach
	void setUpTest() {
		
		testServ = new Server(8990, 2);
		testServ.run();
		
		timClient = new ChatClient("localhost", 8990, "tim");

		timClient.execute();
		
		bobClient = new ChatClient("localhost", 8990, "bob");

		bobClient.execute();
    }
*/
	
	@Test
	//tests deck functions
	void testDeck() {
		assertTrue(testServ.getGame().getDeck().inDeck(compSillyCard));
		assertTrue(tim.getDeck().isEmpty());
		
		
		Card draw = testServ.getGame().getDiscardDeck().drawCard();
		tim.getDeck().addCard(draw);
		testServ.getGame().getDiscardDeck().removeCard(draw);
		//check if tim drew card
		assertTrue(tim.getDeck().drawCard() == draw);
		//check if drawpile is empty
		assertTrue(testServ.getGame().getDiscardDeck().isEmpty());
		//tries to draw from empty deck
		assertThrows(IllegalArgumentException.class, () -> testServ.getGame().getDiscardDeck().removeCard(draw));
		
		testServ.getGame().getDeck().revertBlack();
		System.out.println(testServ.getGame().getDeck());
		assertFalse(testServ.getGame().getDeck().inDeck(compSillyCard));
		
		

		//assertFalse(testServ.getGame().getDiscardDeck().inDeck(compSillyCard));
	}
	
	/*
	@Test
	void testServer() {
		
		assertTrue(true);
	}
	*/
}
