package View;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import GetDate;
import Controller.ColorChoice;
import Controller.GameCard;
import Model.Card;
import Model.ChatClient;
import Model.Col;
import Model.Game;
import Model.Player;
import Model.TransmitData;


public class GameBoard extends JFrame {

	private Game game;
	private ChatClient client;
	private int width;
	private int height;
	private Color backgroundColor = new Color(80, 0,0);
	private Color handColor = new Color(150, 0,0);
	private int playerLimit;
	private LobbyView lobby;

	public GameBoard()
	{
		setLayout(new BorderLayout());
		width = 1200;
		height = 600;
		setSize(width, height);
		setBackground(Color.white);
		lobby = new LobbyView(backgroundColor);
		GetDate date = new GetDate();
		setTitle("UNO " + date.FetchDate());
		setVisible(true);
	}

	public void update()
	{
		if(game == null)
			return;

		getContentPane().removeAll();

		BorderLayout layout = new BorderLayout();
		
		GridLayout center_layout = new GridLayout(1, 2);
		JPanel center_panel = new JPanel();
		center_panel.setBackground(backgroundColor);
		center_panel.setLayout(center_layout);
		// deck card
		if(!game.getDeck().isEmpty())
		{
			GameCard gameDeck = new GameCard(game.getDeck().drawCard());
			gameDeck.setBackground(backgroundColor);
			center_panel.add(gameDeck);
		}
		// discard deck card
		if(!game.getDiscardDeck().isEmpty())
		{
			GameCard discardDeck = new GameCard();
			discardDeck.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					if(client.isClientTurn() && !client.hasStackableCard())
					{
						TransmitData data = new TransmitData(null, -100, Col.black, true, false);
						client.sendToServer(data);
					}
				}
			});
			if(client.hasStackableCard() || !client.isClientTurn())
				discardDeck.setOpacity(0.5f);
			else
				discardDeck.setOpacity(1f);

			discardDeck.setBackground(backgroundColor);
			center_panel.add(discardDeck);
		}
		add(center_panel, layout.CENTER);
		// player hand
		GridLayout handLayout = new GridLayout(1, 10);
		JPanel hand = new JPanel();
		hand.setBackground(handColor);
		hand.setLayout(handLayout);
		for(int i = 0; i < game.getPlayerDeck(game.getPlayerId()).getSize(); i++)
		{
			Card card = game.getPlayerDeck(game.getPlayerId()).getCard(i);
			GameCard card_button = new GameCard(card);
			card_button.setBackground(handColor);
			card_button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					// System.out.println("button pressed!");
					if(client.isClientTurn())
					{
						if(card_button.getCard().getColor() != Col.black)
						{
							// System.out.println("Card selected : " + card_button.getCard().toString());
							TransmitData data = new TransmitData(card_button.getCard(), -100, Col.black, false, false);
							client.sendToServer(data);
						}
						else
						{
							// för svarta kort måste en färg bestämmas
							ColorChoice colorMenu = new ColorChoice(client, card_button.getCard());
						}
					}
				}
			});
			if(!client.isClientTurn())
				card_button.setOpacity(0.5f);
			hand.add(card_button);
		}
		add(hand, layout.SOUTH);

		GridLayout opLayout = new GridLayout(1, game.getPlayers().size());
		JPanel opPanel = new JPanel();
		opPanel.setBackground(handColor);
		for(int i = 0; i < game.getPlayers().size(); i++)
		{
			GamePlayer player_display = new GamePlayer();
			if(i!=game.getPlayerId())
				player_display.setText(game.getPlayers().get(i).getName() + " " + game.getPlayerDeck(i).getSize());
			else
				player_display.setText(game.getPlayers().get(i).getName() + "(You) " + game.getPlayerDeck(i).getSize());

			player_display.setActive(i==game.getCurrentTurn());
			opPanel.add(player_display);
		}
		add(opPanel, layout.NORTH);
		revalidate();
		repaint();
	}
	
	public void lobbyUpdate(ArrayList<Player> players) {
		getContentPane().removeAll();
		
		
		lobby.update(players,playerLimit);
		add(lobby);
		
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
	
	public void setPlayerLimit(Integer players) {
		playerLimit = players;
	}
	public static void main(String[] args){
        GameBoard board = new GameBoard();       
    }
	
}
