import static org.junit.Assert.assertTrue;

import org.junit.Test;

import commands.CheckMessageIsNotNullOnTell;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Board;
import structures.basic.Tile;
public class BoardTest {
	
	public Board board1;
	@Test
	public void CheckBoardPos()
	{	
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		board1 = new Board();
		Tile tiletest = board1.getTile(3, 4);
		assertTrue(tiletest.getTilex()==3);
		assertTrue(tiletest.getTiley()==4);
	}
	@Test
	public void CheckAvatarInBoard()
	{	
		GameState gamestate = new GameState();
		Board board2 = gamestate.getBoard();
		board2.addPlayer1Avatar(2,3,gamestate);
		
		Avatar ava = board2.getPlayer1Avatar();
		assertTrue(ava.getAttack()==2);
		
	}
	@Test
	public void CheckBoard()
	{	
		board1 = new Board();
		assertTrue(board1.getX()==9);
		assertTrue(board1.getY()==5);
		
	}

}