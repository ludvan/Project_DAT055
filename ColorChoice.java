import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ColorChoice extends JPanel implements ActionListener {
	
	private ChatClient Choiceclient;
	
	private JButton redB;
	private JButton blueB;
	private JButton greenB;
	private JButton yellowB;
	
	private Col ChosenColor;
	
	private boolean visible;
	
	ColorChoice(ChatClient currentClient){
		setLayout(new FlowLayout());
		
		Choiceclient = currentClient;
		
		redB = new JButton("red");
		blueB = new JButton("blue");
		greenB = new JButton("green");
		yellowB = new JButton("yellow");
		redB.addActionListener(this);
		blueB.addActionListener(this);
		greenB.addActionListener(this);
		yellowB.addActionListener(this);
		
		add(redB);
		add(blueB);
		add(greenB);
		add(yellowB);
		visible = false;
	}
	
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "red":
			ChosenColor = Col.red;
		case "blue":
			ChosenColor = Col.blue;
		case "green":
			ChosenColor = Col.green;
		case "yellow":
			ChosenColor = Col.yellow;
		default:
			System.out.println("didn't get a valid color");
		}
		//inte snyggt
		TransmitData data = new TransmitData(new Card(Value.zero,Col.blue), -100, ChosenColor, false, true);
		Choiceclient.sendToServer(data);
		
		toggle();
		
	}
	
	
	public void toggle() {
		visible = !visible;
		
		setVisible(visible);
	}
	

}
