package structures.basic;

/**
 * Human player
 * deck is created when player is created
 * card is shuffled when deck is first created
 * draw 3 cards when deck is created
 * 
 * @author daniel
 *
 */

public class HumanPlayer extends Player{

	public HumanPlayer() {
		super();
	}
	
	public HumanPlayer(int health, int mana) {
		super(health, mana);
		//deck is created with player
		mydeck = new Deck(1);
		
		//card shuffle when deck is created
		mydeck.shuffleCard(); 
		//draw 3 cards when deck is created
		for (int i=0; i<3; i++) {this.cardDraw();}
	}
	
}
