import java.io.Serializable;

/*
 * Klass f�r data som ska skickas fr�n klient till server
*/
public class TransmitData implements Serializable {
	private Card card;
	private int playerId;
	private Col chosenColor;
	private boolean drawCard; // indikerar att användaren vill dra ett kort från discard deck

	public TransmitData(Card c, int id, Col chosenColor, boolean drawCard) {
		this.card = c;
		this.playerId = id;
		this.chosenColor = chosenColor;
		this.drawCard = drawCard;
	}
	
	public void setCard(Card c) {
		this.card = c;
	}
	public Card getCard()
	{
		return card;
	}
	
	public void setPlayerId(int id) {
		this.playerId = id;
	}
	public int getPlayerId()
	{
		return playerId;
	}
	
	public void setChosenColor(Col chosenColor) {
		this.chosenColor = chosenColor;
	}
	public Col getChosenColor()
	{
		return chosenColor;
	}

	public void setDrawCard(boolean b)
	{
		drawCard = b;
	}
	public boolean getDrawCard()
	{
		return drawCard;
	}
}
