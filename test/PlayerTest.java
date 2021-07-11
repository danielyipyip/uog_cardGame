import structures.basic.Player;
import structures.basic.Card;
import structures.basic.Deck;
import structures.basic.Hand;
import events.Initalize;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import commands.CheckMessageIsNotNullOnTell;

public class PlayerTest {
	
	public Player Player1 ;
	public Deck player1deck;
	public Hand myhand ;
	@Test
	public void checkPlayer()
	{	
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		Player1 = new Player (20, 2); 		
		
		player1deck = new Deck(1);
		myhand = new Hand();
		Player1.setMydeck(player1deck);
		Player1.setMyhand(myhand);
				
	}
    @Test
	public void checkPlayerStatus()
	{
		assertTrue(Player1.getHealth()==20);
		assertTrue(Player1.getMana()==2);
	}
    @Test
	public void checkPlayerHand()
	{
		assertTrue(Player1.getMyhand().equals(myhand));
		
	}
    @Test
	public void checkPlayerDeck()
	{
		assertTrue(Player1.getMydeck().equals(player1deck));
		
	}
	
}