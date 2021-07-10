
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import structures.basic.Card;
import structures.basic.Deck;


//test: story (2) Deck
//
public class Decktest {
		
	//test size
	@Test
	public void testDeckSize() {
		Deck player1deck = new Deck(1);
		Deck player2deck = new Deck(1);
		assertTrue(player1deck.size()==20 && player2deck.size()==20);
	}
	
//	//test 1st, last card of deck 1
//	@Test
//	public void testFirstLastCard() {
//		//1st card
//		Deck player1deck = new Deck(1);
//		Card firstCard = player1deck.drawCard();
//		assertTrue(firstCard.getCardname()=="Comodo Charger" && 
//				player1deck.getDeck().size()==19);
//		//middle 18 cards
//		for (int i=0; i<18; i++) {player1deck.drawCard();}
//		//last card
//		Card lastCard = player1deck.drawCard();
//		assertTrue(lastCard.getCardname()=="Sundrop Elixir" && 
//				player1deck.getDeck().size()==0);
//	}
	
	//test shuffled method
	@Test
	public void testShuffleCard() {
		Deck player1deck = new Deck(1);
		player1deck.shuffleCard();
		Card firstCard = player1deck.drawCard();
		Card AnotherfirstCard = player1deck.drawCard();
		//might fail sometimes due to randomness
		assertTrue(firstCard.getCardname()!="Comodo Charger" || 
				AnotherfirstCard.getCardname()!="Comodo Charger");
	}
	
	//test is card shuffled when game start
	
}
