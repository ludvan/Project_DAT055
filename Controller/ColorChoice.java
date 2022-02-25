package Controller;
import javax.swing.*;

import Model.Card;
import Model.ChatClient;
import Model.Col;
import Model.TransmitData;

public class ColorChoice{
	private ChatClient choiceClient;
	private Card currentCard;
	private Col chosenColor;
	//Ordning i str�ng viktig
	private String[] options = {"red","blue","yellow","green"};

	public ColorChoice(ChatClient currentClient, Card card){
		choiceClient = currentClient;
		currentCard = card;
		ChooseColor();
	}
	

	public void ChooseColor(){
		int answer = JOptionPane.showOptionDialog(null, "Choose a color", "",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		
		//answer = -1 om man st�nger f�nstret ist�llet f�r att v�lja n�got
		if(answer == -1) {
			ChooseColor();
		}else {
			switch(answer) {
			case 0:
				chosenColor = Col.red;
				break;
			case 1:
				chosenColor = Col.blue;
				break;
			case 2:
				chosenColor = Col.yellow;
				break;
			case 3:
				chosenColor = Col.green;
				break;
			default:
				throw new IllegalArgumentException("Unknown input");
				
			}
			
			// skickar det valda kortet till servern
			TransmitData data = new TransmitData(currentCard, -100, chosenColor, false, true);
			choiceClient.sendToServer(data);
		}
		
	}

}