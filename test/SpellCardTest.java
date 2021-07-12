import static org.junit.Assert.assertTrue;

import org.junit.Test;

import structures.GameState;
import structures.basic.SpellCard;
import structures.basic.Tile;
import structures.basic.Unit;

public class SpellCardTest {

	
	
	@Test
	public void playCardTest ()
	{
		GameState gameState = new GameState();
		Tile tile = new Tile();
		Unit unit = new Unit();
		unit.setHealth(10, null);
		tile.setUnit(unit);
		
		
		SpellCard spellcard = new SpellCard();
		spellcard.setCardname("Truestrike");
		
		spellcard.playCard(null,gameState,tile);
		
		assertTrue(unit.getHealth()==8);
		
		// card "Truestrike" Test
		
		
	}
	
}