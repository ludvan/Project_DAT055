import javax.swing.JButton;

public class PlayerCard extends JButton {
	private Card cardInfo;
	
	
	PlayerCard(String text, Card info){
		setText(text);
		cardInfo = info;
	}
	
	
	public Card getCardInfo() {
		return cardInfo;
	}
	
	
}
