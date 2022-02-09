import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class UnoWindow extends JFrame implements ActionListener{
	private JPanel myHand;
	private JPanel centerArea;
	private JPanel middlePanel;
	private JPanel choosePanel;
	
	private JButton test;
	private JButton pile;
	private JButton drawPile;
	private JButton addButton;

	private JButton redB;
	private JButton blueB;
	private JButton greenB;
	private JButton yellowB;

	boolean chooseColor=false;


	
	
	//OBS bör sättas i separat klass enligt MVC (motståndares händer styrs ej och bör vara i en view)
	//hålls här så länge för testning
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
		
		//lägger första kortet
		pile.setText(ButtonText(deck.drawCard()));
		pile.setBackground(ButtonColor(deck.drawCard()));
		TextColorSettings(pile);
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
		centerArea.setLayout(new BorderLayout());
        
        test1 = new JButton("test1");
        test2 = new JButton("test2");
        test3 = new JButton("test3");
        
        
        drawPile = new JButton ("draw");
        drawPile.addActionListener(this);
        addButton = new JButton("added");

		middlePanel =new JPanel();
		middlePanel.setLayout(new FlowLayout());
		middlePanel.setBackground(Color.white);
		middlePanel.add(pile);
		middlePanel.add(drawPile);

		centerArea.add(middlePanel, BorderLayout.CENTER);
        centerArea.setBackground(Color.white);

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

		choosePanel=new JPanel();

		redB = new JButton("red");
		blueB = new JButton("blue");
		greenB = new JButton("green");
		yellowB = new JButton("yellow");
		redB.addActionListener(this);
		blueB.addActionListener(this);
		greenB.addActionListener(this);
		yellowB.addActionListener(this);


		choosePanel.setLayout(new FlowLayout());
		redB.setBackground(Color.red);
		blueB.setBackground(Color.blue);
		greenB.setBackground(Color.green);
		yellowB.setBackground(Color.yellow);
		choosePanel.add(redB);
		choosePanel.add(blueB);
		choosePanel.add(greenB);
		choosePanel.add(yellowB);


		centerArea.add(choosePanel,BorderLayout.SOUTH);
		choosePanel.setVisible(false);
        setVisible(true);
    }



	private void addCard() {
		//System.out.println(deck.drawCard().toString());
		Card addedCard = deck.drawCard();

		PlayerCard addedButton = new PlayerCard(ButtonText(addedCard),addedCard);

		//ändrar inte buttonstorlek än,
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
		//shuffle läggs till, annars var nästa kort alltid samma (t.ex. gul 1 två gånger i rad innan det tog slut på gul 1)
		Deck.shuffle(deck);
	}
	
	private void removeCard(PlayerCard clicked) {
		if (!chooseColor){
		if(Card.isStackable(clicked.getCardInfo(),playedPile.drawCard())) {

			playedPile.addCard(clicked.getCardInfo());
			if(clicked.getCardInfo().getColor().equals(Color.black)){
				choosePanel.setVisible(true);
	//			pile.getBackground(ChooseColor());
			}else{
			pile.setBackground(clicked.getBackground());
			}

 			TextColorSettings(pile);
			pile.setText((clicked.getText()));
	
			myHand.remove(clicked);
			myHand.revalidate();
			myHand.repaint();
			
		}else {
			System.out.println("not allowed");
		}
		}else{
	//		if(Card.ChooseIsStackable(clicked.getCardInfo(),playedPile.drawCard())) {
				if(ChooseIsStackable(clicked,pile)) {
				if(clicked.getCardInfo().getColor().equals(pile.getBackground())){
					choosePanel.setVisible(true);
					//			pile.getBackground(ChooseColor());
				}else{
					pile.setBackground(clicked.getBackground());
				}

				TextColorSettings(pile);
				pile.setText((clicked.getText()));

				myHand.remove(clicked);
				myHand.revalidate();
				myHand.repaint();
				chooseColor=false;
			}else {
				System.out.println("not allowed");
			}
		}
	}
	public static boolean ChooseIsStackable(PlayerCard c, JButton b) {
		boolean tmp = false;
		if(c.getCardInfo().getColor().equals(b.getBackground())){
		tmp = true;
	}
		return tmp;
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

	private void TextColorSettings(JButton b){
		if (b.getBackground()==Color.blue || b.getBackground()==Color.black){
				b.setForeground(Color.white);
		}else{
			b.setForeground(Color.black);
		}
	}

	//bara kopplad till knappen "test", "draw" och nya knappar
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("draw")) {
			addCard();
		}
		else if(e.getActionCommand().equals("red")) {
				pile.setBackground(Color.red);
				TextColorSettings(pile);
				chooseColor=true;
			choosePanel.setVisible(false);
		}
		else if(e.getActionCommand().equals("blue")) {
			   pile.setBackground(Color.blue);
			   TextColorSettings(pile);
			chooseColor=true;
			choosePanel.setVisible(false);
		}
		else if(e.getActionCommand().equals("green")) {
			pile.setBackground(Color.green);
			TextColorSettings(pile);
			chooseColor=true;
			choosePanel.setVisible(false);
		}
		else if(e.getActionCommand().equals("yellow")) {
			pile.setBackground(Color.yellow);
			TextColorSettings(pile);
			chooseColor=true;
			choosePanel.setVisible(false);
		}
		//eftersom det gäller för alla klickbara kort som ej är draw (för tillfället)
		else {
			PlayerCard clicked = (PlayerCard) e.getSource();
			System.out.println(clicked.getCardInfo().getColor());
			if(clicked.getCardInfo().getColor().equals(Col.black)){
				removeCard(clicked);
				chooseColor=true;
				choosePanel.setVisible(true);
			}
			removeCard(clicked);
		}
	}

	public static void main(String[] args){
        UnoWindow u = new UnoWindow();
        
    }
	
}
