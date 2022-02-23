import java.io.Serializable;

/*
 * Klass f�r data som ska skickas fr�n klient till server
*/
public class TransmitData implements Serializable {
	private Card card;
	private int playerId;
	private Col chosenColor;
	private boolean drawCard; // indikerar att användaren vill dra ett kort från discard deck
	private boolean chooseColor;
	private String winner;
	private int[] pointArr;
	private String highscores;

	public TransmitData(Card c, int id, Col chosenColor, boolean drawCard, boolean chooseColor) {
		this.card = c;
		this.playerId = id;
		this.chosenColor = chosenColor;
		this.drawCard = drawCard;
		this.chooseColor = chooseColor;
	}
	public TransmitData(String str1, int[] arr, String str2){
		this.winner = str1;
		this.pointArr = arr;
		this.highscores = str2;
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

	public void setChooseColor(boolean c)
	{
		chooseColor = c;
	}
	public boolean getChooseColor()
	{
		return chooseColor;
	}

	public void setDrawCard(boolean b)
	{
		drawCard = b;
	}
	public boolean getDrawCard()
	{
		return drawCard;
	}

	public void setWinner(String str){winner = str;}
	public String getWinner(){return winner; }

	public void setPointArr(int[] arr){pointArr= arr.clone(); }
	public int[] getPointArr(){ return pointArr; }

	public void setHighscore(String str){highscores = str;}
	public String getHighscores(){return highscores; }
}
