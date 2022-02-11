import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ColorChoice extends JPanel implements ActionListener {
	
	
	private JButton redB;
	private JButton blueB;
	private JButton greenB;
	private JButton yellowB;
	
	private boolean visible;
	
	ColorChoice(){
		setLayout(new FlowLayout());
		
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
		//logik för att byta färg insert
		
		
		toggle();
		
	}
	
	
	public void toggle() {
		visible = !visible;
		
		setVisible(visible);
	}
	

}
