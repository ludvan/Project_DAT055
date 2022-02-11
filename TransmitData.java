/*
 * Klass för data som ska skickas från klient till server
*/
public class TransmitData {
	private Card card;
	private int playerId;
	private Col chosenColor;
	
	public TransmitData(Card c, int id, Col chosenColor) {
		this.card = c;
		this.playerId = id;
		this.chosenColor = chosenColor;
	}
	
	public void setCard(Card c) {
		this.card = c;
	}
	
	public void setPlayerId(int id) {
		this.playerId = id;
	}
	
	public void setChosenColor(Col chosenColor) {
		this.chosenColor = chosenColor;
	}
}
