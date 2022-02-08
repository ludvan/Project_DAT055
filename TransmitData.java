
public class TransmitData {
	private Card card;
	private int playerId;
	
	public TransmitData(Card c, int id) {
		this.card = c;
		this.playerId = id;
	}
	
	public void setCard(Card c) {
		this.card = c;
	}
	
	public void setPlayerId(int id) {
		this.playerId = id;
	}
}
