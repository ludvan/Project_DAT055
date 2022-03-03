/**
 * Data is transmitted between server and client using this class.
 * @version 22-02-29
 * @author DEN ANDRA FÖRFATTAREN!!!!
 * @author Ebba Håkansson
 */

package Model;

import java.io.Serializable;

public class TransmitData implements Serializable {
	private Card card;
	private int playerId;
	private Col chosenColor;
	private boolean drawCard;
	private boolean chooseColor;
	private String winner;
	private int[] pointArr;
	private boolean has_pressed_uno;

	/**
	 * Creates packet of data to transmit
	 * @author ANNAN FÖRFATTARE!!!!
	 * @author Ebba Håkansson
	 * @param c				the card
	 * @param id			the players id
	 * @param chosenColor	the color that the player has chosen
	 * @param drawCard		if the player has drawn a new card
	 * @param chooseColor	if player has to choose color
	 * @param uno			if the player has pressed uno
	 */
	public TransmitData(Card c, int id, Col chosenColor, boolean drawCard, boolean chooseColor, boolean uno) {
		this.card = c;
		this.playerId = id;
		this.chosenColor = chosenColor;
		this.drawCard = drawCard;
		this.chooseColor = chooseColor;
		this.has_pressed_uno = uno;
	}

	public TransmitData(Col chosenColor, boolean chooseColor, Card ca) {
		this.chosenColor = chosenColor;
		this.drawCard = false;
		this.chooseColor = chooseColor;
		this.card = ca;
	}

	public TransmitData(boolean uno) {
		this.has_pressed_uno = uno;
	}

	public TransmitData(String str1, int[] arr) {
		this.winner = str1;
		this.pointArr = arr;
	}

	public void setCard(Card c) {
		this.card = c;
	}

	public Card getCard() {
		return card;
	}

	public void setPlayerId(int id) {
		this.playerId = id;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setChosenColor(Col chosenColor) {
		this.chosenColor = chosenColor;
	}

	public Col getChosenColor() {
		return chosenColor;
	}

	public void setChooseColor(boolean c) {
		chooseColor = c;
	}

	public boolean getChooseColor() {
		return chooseColor;
	}

	public void setDrawCard(boolean b) {
		drawCard = b;
	}

	public boolean getDrawCard() {
		return drawCard;
	}

	public void setWinner(String str) {
		winner = str;
	}

	public String getWinner() {
		return winner;
	}

	public void setPointArr(int[] arr) {
		pointArr = arr.clone();
	}

	public int[] getPointArr() {
		return pointArr;
	}

	public boolean getPressedUno() {
		return has_pressed_uno;
	}

	public void setPressedUno(boolean uno) {
		has_pressed_uno = uno;
	}
}
