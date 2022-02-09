import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;


public class GameBoard extends JFrame {

	private Game game;
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
		int players = game.getPlayers().size();
		ArrayList<JButton> panels = new ArrayList<JButton>();
		for(int i = 0; i < players; i++)
		{
			Player player = game.getPlayers().get(i);
			double angle = 90.0 + 360.0 * ((double)i/players);
			double rel_posx = ((Math.cos(Math.toRadians(angle)) + 1)/2);
			double rel_posy = ((Math.sin(Math.toRadians(angle)) + 1)/2);
			int posx = (int)(rel_posx*(width-300));
			int posy = (int)(rel_posy*(height-100));

			if(game.getCurrentTurn() == i)
			{
				JPanel panel = new JPanel();
				GridLayout layout = new GridLayout(1, 5);
				panel.setLayout(layout);
				panel.setLocation(posx, posy);
				panel.setSize(300, 100);
				panel.add(new JButton(" CARD "));
				panel.add(new JButton(" CARD "));
				panel.add(new JButton(" CARD "));
				panel.add(new JButton(" CARD "));
				add(panel);
				panel.revalidate();
				panel.repaint();
			}
			else
			{
				JButton panel = new JButton();
				panel.setText(player.getName() + " " + player.getDeck().getSize());
				panel.setLayout(null);
				panel.setSize(300, 100);
				panel.setLocation(posx, posy);
				panel.setBackground(Color.red);
				panels.add(panel);
				add(panel);
				panel.revalidate();
				panel.repaint();
			}
		}
		revalidate();
		repaint();
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
