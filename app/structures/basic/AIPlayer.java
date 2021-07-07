package structures.basic;

import akka.actor.ActorRef;

/**
 * AI player
 * planning to put AI move methods in here
 * draw 3 cards when deck is created
 * 
 * @author daniel
 *
 */

public class AIPlayer extends Player{

	public AIPlayer() {
		super();
	}
	
	public AIPlayer(int health, int mana) {
		super(health, mana);
		//deck is created with player
		mydeck = new Deck(2);
		//card shuffle when deck is created
		mydeck.shuffleCard(); 
		//draw 3 cards when deck is created
		for (int i=0; i<3; i++) {this.cardDraw();}
	}
	
	@Override
	//no draw hand on dislplay
	public void drawHand(ActorRef out) {;}
	
	@Override
	//card only delete from Hand, not display
	public void removeCard(ActorRef out, int n) {myhand.removeCard(n);}
	
}
