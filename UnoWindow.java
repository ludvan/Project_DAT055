import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class UnoWindow extends JFrame implements ActionListener{
	private JPanel myHand;
	private JPanel op1Hand;
	private JPanel op2Hand;
	private JPanel op3Hand;
	private JPanel centerArea;
	
	private JButton test;
	private JButton test1;
	private JButton test2;
	private JButton test3;
	private JButton pile;
	
	
	
	public UnoWindow(){
        setTitle("Uno");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920,1080);

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
        
        centerArea.setBackground(Color.white);
        centerArea.add(pile,BorderLayout.CENTER);
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
		
	}
	
	private void removeCard() {
		
	}
	
	//bara kopplad till knappen "test"
	public void actionPerformed(ActionEvent e) {
		Toolkit.getDefaultToolkit().beep();
	}
	
	
	public static void main(String[] args){
        UnoWindow u = new UnoWindow();
        
    }
	
}
