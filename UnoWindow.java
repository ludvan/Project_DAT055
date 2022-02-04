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
	
	
	//OBS b�r s�ttas i separat klass enligt MVC (motst�ndares h�nder styrs ej och b�r vara i en view)
	//h�lls h�r s� l�nge f�r testning
	private JButton test1;
	private JButton test2;
	private JButton test3;
	
	private JPanel op1Hand;
	private JPanel op2Hand;
	private JPanel op3Hand;
	
	private Deck deck;
	private Deck playedPile;

	int width=1020;
	int height= 540;


	public UnoWindow(){
		deck = new Deck();
		playedPile = new Deck();
		
		pile = new JButton("pile");
		
		
		
		deck.fillDeck();
		Deck.shuffle(deck);
		
		//l�gger f�rsta kortet
		pile.setText(ButtonText(deck.drawCard()));
		pile.setBackground(ButtonColor(deck.drawCard()));
		playedPile.addCard(deck.drawCard());
		deck.removeCard(deck.drawCard());
		Deck.shuffle(deck);
		
		
		
		
        setTitle("Uno");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width, height);

        myHand = new JPanel();
        
        op1Hand = new JPanel();
        op2Hand = new JPanel();
        op3Hand = new JPanel();
        
        centerArea = new JPanel();
        
        
        test1 = new JButton("test1");
        test2 = new JButton("test2");
        test3 = new JButton("test3");
        
        
        drawPile = new JButton ("draw");
        drawPile.addActionListener(this);
        addButton = new JButton("added");

        centerArea.setBackground(Color.white);
        centerArea.add(pile,BorderLayout.CENTER);
        centerArea.add(drawPile);
        add(centerArea,BorderLayout.CENTER);
        
        myHand.setBackground(Color.yellow);
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

	private String ButtonText(Card c){
		Value value = c.getValue();

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
		return buttontext;
	};

	private void addCard() {
		//System.out.println(deck.drawCard().toString());
		Card addedCard = deck.drawCard();

		PlayerCard addedButton = new PlayerCard(ButtonText(addedCard),addedCard);

		//�ndrar inte buttonstorlek �n,
		addedButton.setBounds(20, 10, 70, 120);


		addedButton.setBackground(ButtonColor(addedCard));
		if (ButtonColor(addedCard)==Color.blue || ButtonColor(addedCard)==Color.black){
			addedButton.setForeground(Color.white);
		}
		addedButton.addActionListener(this);

		addedButton.setActionCommand(addedCard.toString());
		myHand.add(addedButton);
		myHand.revalidate();
		myHand.repaint();
		deck.removeCard(addedCard);
		//shuffle l�ggs till, annars var n�sta kort alltid samma (t.ex. gul 1 tv� g�nger i rad innan det tog slut p� gul 1)
		Deck.shuffle(deck);
	}
	
	private void removeCard(PlayerCard clicked) {
		if(Card.isStackable(clicked.getCardInfo(),playedPile.drawCard())) {
			
		
			playedPile.addCard(clicked.getCardInfo());
			pile.setBackground(clicked.getBackground());
			if (pile.getBackground()==Color.blue || pile.getBackground()==Color.black){
				pile.setForeground(Color.white);
			}
			pile.setText((clicked.getText()));
	
			myHand.remove(clicked);
			myHand.revalidate();
			myHand.repaint();
			
		}else {
			System.out.println("not allowed");
		}
	}

	private Color ButtonColor(Card c){
		Col color = c.getColor();
		switch(color){
			case red :
				return Color.red;
			case green :
				return Color.green;
			case yellow :
				return Color.yellow;
			case blue :
				return Color.blue;
			case black :
				return Color.black;
			default:
				System.out.println("didn't get a valid color");
		}
		return Color.MAGENTA;
	}


	//bara kopplad till knappen "test", "draw" och nya knappar
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("draw")) {
			addCard();
		}
		//eftersom det g�ller f�r alla klickbara kort som ej �r draw (f�r tillf�llet)
		else {
			PlayerCard clicked = (PlayerCard) e.getSource();
			removeCard(clicked);
		}
	}

	public static void main(String[] args){
        UnoWindow u = new UnoWindow();
        
    }
	
}
