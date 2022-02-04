import javax.swing.JButton;

public class playerCard extends JButton {
	private Card cardInfo;
	
	
	playerCard(String text, Card info){
		setText(text);
		cardInfo = info;
	}
	
	public Value getValue(){
		return cardInfo.getValue();
	}
	
	public Col getColor() {
		return cardInfo.getColor();
	}
	
	public Card getCard() {
		return cardInfo;
	}
	
	
}
