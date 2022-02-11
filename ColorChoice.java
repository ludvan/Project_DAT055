import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ColorChoice{
	private ChatClient ChoiceClient;
	private Col ChosenColor;
	//Ordning i sträng viktig
	private String[] options = {"red","blue","yellow","green"};

	ColorChoice(ChatClient currentClient){
		ChoiceClient = currentClient;
	}
	

	public void ChooseColor(){
		int answer = JOptionPane.showOptionDialog(null, "Choose a color", "",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		switch(answer) {
		case 0:
			ChosenColor = Col.red;
			break;
		case 1:
			ChosenColor = Col.blue;
			break;
		case 2:
			ChosenColor = Col.yellow;
			break;
		case 3:
			ChosenColor = Col.green;
			break;
		default:
			throw new IllegalArgumentException("Unknown input");
			
		}
		
		//bör ses över
		TransmitData data = new TransmitData(new Card(Value.zero,Col.blue), -100, ChosenColor, false, true);
		ChoiceClient.sendToServer(data);
	}
	
	

}
