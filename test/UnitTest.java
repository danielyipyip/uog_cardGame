import static org.junit.Assert.assertTrue;

import org.junit.Test;

import structures.GameState;
import structures.basic.Board;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.unit.Attack2;

public class UnitTest {
	@Test
	public void Attackcheck() 
	{
		GameState gamestate = new GameState();
		Board boardtest = gamestate.getBoard();
		Unit Attacker = new Unit();
		Attacker.setAttack(2, null);
		Attacker.setHealth(10, null);
		Unit target =new Unit();
		target.setAttack(2, null);
		target.setHealth(10, null);
		Tile tiletest = new Tile ();
		tiletest.setUnit(target);
		Attacker.attackUnit(null,gamestate,Attacker,tiletest);
		assertTrue(target.getHealth()==8);
		assertTrue(Attacker.getHealth()==8);
	}
	@Test
	public void Attack2Unit() 
	{	
		GameState gamestate = new GameState();
		Board boardtest = gamestate.getBoard();
		Attack2 Attacker = new Attack2();
		Attacker.setAttack(2, null);
		Attacker.setHealth(10, null);
		Unit target =new Unit();
		target.setAttack(2, null);
		target.setHealth(10, null);
		Tile tiletest = new Tile ();
		tiletest.setUnit(target);
		Attacker.attackUnit(null,gamestate,Attacker,tiletest);
		Attacker.attackUnit(null,gamestate,Attacker,tiletest);
		assertTrue(target.getHealth()==6);
	}
	
	
}