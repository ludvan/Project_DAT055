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
		// deck card
		if(!game.getDeck().isEmpty())
		{
			GameCard gameDeck = new GameCard(game.getDeck().drawCard());
			add(gameDeck, layout.CENTER);
		}
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
					if(client.isClientTurn())
					{
						client.sendToServer(card_button.getCard());
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
		//int players = game.getPlayers().size();
		/*
		for(int i = 0; i < players; i++)
		{
			Player player = game.getPlayers().get(i);
			double angle = 90.0 + 360.0 * ((double)(i-game.getPlayerId())/players);
			double rel_posx = ((Math.cos(Math.toRadians(angle)) + 1)/2);
			double rel_posy = ((Math.sin(Math.toRadians(angle)) + 1)/2);
			int posx = (int)(rel_posx*(width-300));
			int posy = (int)(rel_posy*(height-150));

			JPanel panel = new JPanel();
			GridLayout layout = new GridLayout(1, 4);
			panel.setLayout(layout);
			panel.setBounds(posx, posy, 164*4, 258/2);
			for(int c = 0; c < player.getDeck().getSize(); i++)
			{
				if(game.getPlayerId() == i)
				{
					panel.add(new GameCard(player.getDeck().getCard(c)));
				}
				else
				{
					panel.add(new GameCard());
				}
				add(panel);
			}
			panel.revalidate();
			panel.repaint();
			
		}
		if(!game.getDeck().isEmpty())
		{
			GameCard gameDeck = new GameCard(game.getDeck().drawCard());
			gameDeck.setLayout(null);
			gameDeck.setBounds(width / 2 - 42, height / 2 - 65, 84, 129);
			add(gameDeck);
		}
		revalidate();
		repaint();
		*/
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
