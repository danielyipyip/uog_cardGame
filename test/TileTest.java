import static org.junit.Assert.assertTrue;

import org.junit.Test;

import commands.CheckMessageIsNotNullOnTell;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.StaticConfFiles;
public class TileTest {
	@Test
	public void CheckUnitInTile()
	{	
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		Tile tile = new Tile();
		Unit unit = new Unit();
		unit.setName("windShrike");
		tile.setUnit(unit);		
		assertTrue(tile.getUnit().getName().equals("windShrike"));
		
	}
	@Test
	public void LoadConfTest()
	{

		
		Tile tile1 = Tile.constructTile(StaticConfFiles.tileConf);
		
		assertTrue(tile1.getWidth()==115);
	}
	

}