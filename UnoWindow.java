import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class UnoWindow extends JFrame implements ActionListener{
	private JPanel myHand;
	private JPanel centerArea;
	
	private JButton test;
	private JButton pile;
	private JButton drawPile;
	private JButton addButton;
	
	
	//OBS bör sättas i separat klass enligt MVC (motståndares händer styrs ej och bör vara i en view)
	//hålls här så länge för testning
	private JButton test1;
	private JButton test2;
	private JButton test3;
	
	private JPanel op1Hand;
	private JPanel op2Hand;
	private JPanel op3Hand;
	
	private Deck deck;

	
	
	
	public UnoWindow(){
		deck = new Deck();
		
		deck.fillDeck();
		Deck.shuffle(deck);
		
		
        setTitle("Uno");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920,540);

        myHand = new JPanel();
        
        op1Hand = new JPanel();
        op2Hand = new JPanel();
        op3Hand = new JPanel();
        
        centerArea = new JPanel();
        
        
        test = new JButton("test");
        test.addActionListener(this);
        
        test1 = new JButton("test1");
        test2 = new JButton("test2");
        test3 = new JButton("test3");
        
        pile = new JButton("pile");
        drawPile = new JButton ("draw");
        drawPile.addActionListener(this);
        addButton = new JButton("added");

        centerArea.setBackground(Color.white);
        centerArea.add(pile,BorderLayout.CENTER);
        centerArea.add(drawPile);
        add(centerArea,BorderLayout.CENTER);
        
        myHand.setBackground(Color.yellow);
        myHand.add(test);
        add(myHand,BorderLayout.SOUTH);
        
        op1Hand.setBackground(Color.red);
        op1Hand.add(test1);
        add(op1Hand,BorderLayout.WEST);
        
        op2Hand.setBackground(Color.green);
        op2Hand.add(test2);
        add(op2Hand,BorderLayout.NORTH);
        
        op3Hand.setBackground(Color.blue);
        op3Hand.add(test3);
        add(op3Hand,BorderLayout.EAST);
        
        
        
        setVisible(true);
    }
	
	
	private void addCard() {
		System.out.println(deck.drawCard().toString());
		Card addedCard = deck.drawCard();
		JButton addedButton = new JButton(addedCard.toString());
		addedButton.addActionListener(this);
		
		addedButton.setActionCommand(addedCard.toString());
		
		myHand.add(addedButton);
		myHand.revalidate();
		myHand.repaint();
		deck.removeCard(addedCard);
		//shuffle läggs till, annars var nästa kort alltid samma (t.ex. gul 1 två gånger i rad innan det tog slut på gul 1)
		Deck.shuffle(deck);
	}
	
	private void removeCard(JButton clicked) {
		myHand.remove(clicked);
		myHand.revalidate();
		myHand.repaint();
	}
	
	//bara kopplad till knappen "test", "draw" och nya knappar
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("draw")) {
			addCard();
		}
		//eftersom det gäller för alla klickbara kort som ej är draw (för tillfället)
		else {
			JButton clicked = (JButton) e.getSource();
			removeCard(clicked);
		}
	}

	//inte kopplat till panelen än
	public void paintComponent(Graphics g){
	super.paintComponents(g);
	for (int CardAmount=0; CardAmount < deck.getSize(); CardAmount++){
		int x= 25*CardAmount;
		int y= 0;
		g.setColor(Color.black);
		g.drawRect(25+x, 10+y, 40,80);
		}
	};

	public static void main(String[] args){
        UnoWindow u = new UnoWindow();
        
    }
	
}
