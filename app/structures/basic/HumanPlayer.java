package structures.basic;

import Exceptions.NotEnoughCardException;
import akka.actor.ActorRef;
import commands.BasicCommands;

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
	
	@Override
	public void cardDraw(ActorRef out) {
		super.cardDraw();
		this.drawHand(out);
	}
	
	@Override
	//draw the hand in display
	public void drawHand(ActorRef out) {
		int pos=0;	
		for (Card i:myhand.getMyhand()) {
			BasicCommands.drawCard(out, i, pos++, 0);
			try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	@Override
	//delete a card both in front-end (display) and back-end (Hand)
	//(2) delete card from player's Hand (back-end)
	//(3) delete card from front-end display and shift the remaining cards to the left
	public void removeCard(ActorRef out, int n) {
		myhand.removeCard(n);
		this.drawHand(out);//change the displays:redraw previous card
		BasicCommands.deleteCard(out, myhand.getMyhand().size());//change the displays: remove last card (will not be redraw)
		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}

	}
	
	@Override
	public void lose(ActorRef out) {
		BasicCommands.addPlayer1Notification(out, "I lost", 2);
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
}
