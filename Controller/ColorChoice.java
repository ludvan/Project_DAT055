package Controller;

import javax.swing.*;
import Model.Card;
import Model.ChatClient;
import Model.Col;
import Model.TransmitData;

public class ColorChoice {
	private ChatClient choiceClient;
	private Card currentCard;
	private Col chosenColor;
	private String[] options = { "red", "blue", "yellow", "green" };

	public ColorChoice(ChatClient currentClient, Card card) {
		choiceClient = currentClient;
		currentCard = card;
		ChooseColor();
	}

	// For black cards. sends color selected by user to the server.
	private void ChooseColor() {
		int answer = JOptionPane.showOptionDialog(null, "Choose a color", "", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		// -1 means the user has closed the window. This isn't allowed, create a new window
		if (answer == -1) {
			ChooseColor();
		} else {
			switch (answer) {
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
			}

			// Sends the color to the server
			TransmitData data = new TransmitData(chosenColor, true, currentCard);
			choiceClient.sendToServer(data);
		}
	}
}