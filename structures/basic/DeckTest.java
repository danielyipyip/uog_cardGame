package structures.basic;

import static org.junit.Assert.*;

import org.junit.Test;

public class DeckTest {

	@Test
	//both deck should have 20 cards
	public void testDeckSize() {
		Deck player1deck = new Deck(1);
		Deck player2deck = new Deck(2);
		assertTrue(player1deck.getDeck().size()==20 && 
				player2deck.getDeck().size()==20);
	}
	
	@Test
	//assumed first card of player 1 is "Comodo Charger" but it could change 
	//according to implementation
	public void testDrawCard() {
		Deck player1deck = new Deck(1);
		Card firstCard = player1deck.drawCard();
		assertTrue(firstCard.getId()==0 && //all cards ID=0
				firstCard.getCardname()=="Comodo Charger" && 
				player1deck.getDeck().size()==19);
	}

	@Test
	public void testShuffleCard() {
		Deck player1deck = new Deck(1);
		player1deck.shuffleCard();
		Card firstCard = player1deck.drawCard();
		//might fail sometimes due to randomness
		assertTrue(firstCard.getCardname()!="Comodo Charger" && 
				player1deck.getDeck().size()==19);
	}

}
