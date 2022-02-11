import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;


public class GameBoard extends JFrame {

	private Game game;
	private ChatClient client;
	private int width;
	private int height;

	public GameBoard()
	{
		new JFrame();
		setLayout(null);
		width = 1200;
		height = 600;
		setSize(width, height);
		setBackground(Color.white);
		setVisible(true);
	}

	public void update()
	{
		if(game == null)
			return;

		getContentPane().removeAll();

		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		GridLayout center_layout = new GridLayout(1, 2);
		JPanel center_panel = new JPanel();
		center_panel.setLayout(center_layout);
		// deck card
		if(!game.getDeck().isEmpty())
		{
			GameCard gameDeck = new GameCard(game.getDeck().drawCard());
			center_panel.add(gameDeck);
		}
		// discard deck card
		if(!game.getDiscardDeck().isEmpty())
		{
			GameCard discardDeck = new GameCard();
			discardDeck.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					if(client.isClientTurn())
					{
						TransmitData data = new TransmitData(null, -100, Col.black, true, false);
						client.sendToServer(data);
					}
				}
			});
			if(!client.hasStackableCard())
				center_panel.add(discardDeck);
		}
		add(center_panel, layout.CENTER);
		// player hand
		GridLayout handLayout = new GridLayout(2, 7);
		JPanel hand = new JPanel();
		hand.setLayout(handLayout);
		for(int i = 0; i < game.getPlayerDeck(game.getPlayerId()).getSize(); i++)
		{
			Card card = game.getPlayerDeck(game.getPlayerId()).getCard(i);
			GameCard card_button = new GameCard(card);
			card_button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("button pressed!");
					if(client.isClientTurn())
					{
						System.out.println("Card selected : " + card_button.getCard().toString());
						TransmitData data = new TransmitData(card_button.getCard(), -100, Col.black, false, false);
						client.sendToServer(data);
					}
				}
			});
			hand.add(card_button);
		}
		add(hand, layout.SOUTH);

		GridLayout opLayout = new GridLayout(1, game.getPlayers().size()-1);
		JPanel opPanel = new JPanel();
		for(int i = 0; i < game.getPlayers().size(); i++)
		{
			if(i!=game.getPlayerId())
				opPanel.add(new JButton(game.getPlayers().get(i).getName() + " " + game.getPlayerDeck(i).getSize()));
		}
		add(opPanel, layout.NORTH);
		revalidate();
		repaint();
	}

	public void setClient(ChatClient _client)
	{
		client = _client;
	}

	public ChatClient getClient()
	{
		return client;
	}

	public void setGame(Game _game)
	{
		game = _game;
	}

	public Game getGame()
	{
		return game;
	}
	public static void main(String[] args){
        GameBoard board = new GameBoard();
        
    }
	
}
