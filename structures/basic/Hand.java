package structures.basic;

import java.util.ArrayList;

/**
 * hand of cards for the player
 * max hand size =6
 * 
 * @author daniel
 *
 */

public class Hand {
	ArrayList<Card> myhand;
	public Hand() {
		this.myhand=new ArrayList<>(6);
	}
	
	public void addCard(Card card) {
		if (myhand.size()<=6) myhand.add(card);
		//exception handling need
	}
	public Card getCard(int index) {return myhand.get(index);}
	public void removeCard(int index) {myhand.remove(index);}
}
