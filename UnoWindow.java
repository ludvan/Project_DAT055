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

	int width=1020;
	int height= 540;

	
	
	public UnoWindow(){
		deck = new Deck();
		
		deck.fillDeck();
		Deck.shuffle(deck);
		
		
        setTitle("Uno");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width, height);

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
		myHand.setPreferredSize(new Dimension(100, 100));
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

		Value value = addedCard.getValue();
		Col color = addedCard.getColor();

		String buttontext=null;

		switch (value){
			case zero : buttontext = "0";
						break;
			case one : buttontext = "1";
						break;
			case two : buttontext = "2";
						break;
			case three : buttontext = "3";
						break;
			case four : buttontext = "4";
						break;
			case five : buttontext = "5";
						break;
			case six : buttontext = "6";
						break;
			case seven : buttontext = "7";
						break;
			case eight : buttontext = "8";
						break;
			case nine : buttontext = "9";
						break;
			case stop : buttontext = "stop";
						break;
			case plus2 : buttontext = "+2";
						break;
			case reverse : buttontext = "<-->";
						break;
			case plus4 : buttontext = "+4";
						break;
			case pickColor : buttontext = "pick color";
						break;
		}

		JButton addedButton = new JButton(buttontext);

		//ändrar inte buttonstorlek än,
		addedButton.setBounds(20, 10, 70, 120);


		switch(color){
			case red :
				addedButton.setBackground(Color.red);
				break;
			case green :
				addedButton.setBackground(Color.green);
				break;
			case yellow :
				addedButton.setBackground(Color.yellow);
				break;
			case blue :
				addedButton.setBackground(Color.blue);
				addedButton.setForeground(Color.white);
				break;
			case black :
				addedButton.setBackground(Color.black);
				addedButton.setForeground(Color.white);
				break;
			default:
				System.out.println("didn't get a valid color");
		}

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

	public static void main(String[] args){
        UnoWindow u = new UnoWindow();
        
    }
	
}
