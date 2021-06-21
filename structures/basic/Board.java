package structures.basic;

import java.util.ArrayList;
import java.util.HashMap;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.EventProcessor;
import structures.GameState;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Board {//This Class is used to store all the variables related to units and tiles

	int sleepTime = EventProcessor.sleepTime;
	
	private HashMap<String, Tile> tileMap; // to store the tiles in HashMap. xy is the key, value stored is Tile.
	private int x=9; // x = number of square of a row
	private int y=5; // y = number of square of a column
	
	//3 types: Unit, unitTiles (tiles with unit), unitOccupied Tiles (unitTiles but both players)
	//so do 3 things when unit changes
	private ArrayList<Tile> player1UnitTiles = new ArrayList<>(); //to store the Tiles player1 units occupy
	private ArrayList<Tile> player2UnitTiles = new ArrayList<>(); //to store the Tiles player2 units occupy
	private ArrayList<Unit> player1Units = new ArrayList<>();//to store player1 units
	private ArrayList<Unit> player2Units = new ArrayList<>();// to store player2 units
	private ArrayList<Tile> unitOccupiedTiles = new ArrayList<>();// to store the Tiles occupied by all Units on board.
	
	//hightlight: act as valid tiles
	private ArrayList<Tile> highlightedRedTiles = new ArrayList<>();// to store the tiles for valid attack and spell tiles.
	private ArrayList<Tile> highlightedWhiteTiles = new ArrayList<>();//to store the tiles for valid move and summon tiles.

	//constructor
	public Board() { 
		tileMap = new HashMap<String,Tile>();
//		player1UnitTiles = new ArrayList<Tile>();
//		player2UnitTiles = new ArrayList<Tile>();
//		player1Units = new ArrayList<Unit>();
//		player2Units = new ArrayList<Unit>();
//		unitOccupiedTiles = new ArrayList<Tile>();
//		highlightedRedTiles = new ArrayList<Tile>();
//		highlightedWhiteTiles = new ArrayList<Tile>();
		addTiles(this.x,this.y);  //Create a board with tiles x:9* y:5;
		//construct player's avatar when board is created
		addPlayer1Avatar(1,2); //Construct player1
		addPlayer2Avatar(7,2); //Construct player2
	}

	public void addTiles (int x, int y) { //Store the tiles in HashMap, key is the string of xy..e.g. x:2 y:2 key = 22
		for(int i=0; i<x; i++) {
			for(int j =0; j<y;j++) {
				String index = Integer.toString(i)+Integer.toString(j);
				Tile tile = BasicObjectBuilders.loadTile(i, j);
				tileMap.put(index, tile);
			}			
		}
	}

	public Tile getTile(int x, int y){ // use the x and y index to get the tile with xy key
		Tile tile = tileMap.get(Integer.toString(x)+Integer.toString(y));
		return tile;
	}
	
	/*Below is to construct player1 & 2 avatar (extends Unit) and display it on the board.
	 * Update the position of the player1 avatar.
	 * Set the Avatar to a tile; Add the tile to unitOccupiedTiles and player1UnitTiles.
	 * Add the unit to player1Units 
	 */
	
	public void addPlayer1Avatar (int x, int y) {
		Avatar player1Avatar = (Avatar) BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 1, Avatar.class);
		player1Avatar.setAttack(2);
		player1Avatar.setHealth(20);
		Tile tile = this.getTile(x,y);
		player1Avatar.setPositionByTile(tile);
		tile.setUnit(player1Avatar);
		player1UnitTiles.add(tile);
		unitOccupiedTiles.add(tile);
		player1Units.add(player1Avatar);
	}
	
	public void addPlayer2Avatar (int x, int y) {
		Avatar player2Avatar = (Avatar) BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, -1, Avatar.class);
		player2Avatar.setAttack(2);
		player2Avatar.setHealth(20);
		Tile tile = getTile(x,y);
		player2Avatar.setPositionByTile(tile); 	
		tile.setUnit(player2Avatar);
		player2UnitTiles.add(tile);
		unitOccupiedTiles.add(tile);
		player2Units.add(player2Avatar);
	}

	//Method to unhighlight tiles in Red Color
	public void unhighlightRedTiles(ActorRef out) {
		for(Tile tile: highlightedRedTiles) {
			BasicCommands.drawTile(out, tile, 0);
			try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		}
		highlightedRedTiles.clear();
	}
	
	//Method to unhighlight tiles in white Color
	public void unhighlightWhiteTiles(ActorRef out) {
		for(Tile tile: highlightedWhiteTiles) {
			BasicCommands.drawTile(out, tile, 0);
			try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		}
		highlightedWhiteTiles.clear();
	}
	
	
	
	/*Below is to add the unit and tile to player unit and player tiles and occuiped tile array.
	 * If player 1 is calling the method.The playerTileArray will be player1TileArray.
	 * Tile will be the tile the unit is going to occupy.
	 * Then add the tile to the array and the occupied array.
	 * Check that the first tile of playerTileArray contains and add the unit to the corresponding player.
	 *  
	 */

	public void addTileAndAvatarToPlayerArray (Tile tile, ArrayList<Tile> playerTileArray,Unit unit) {
		
			 Unit playerAvatar = playerTileArray.get(0).getUnit();
			 unit.setPositionByTile(tile);
			 tile.setUnit(unit);
			 playerTileArray.add(tile);
			 unitOccupiedTiles.add(tile);
			 if(player1Units.contains(playerAvatar)) {
				 player1Units.add(unit);		 
			 }else {
				 player2Units.add(unit);
			 }

	}
	
	
	
	
	
	
	
	
	public void addHightlightWhiteTiles(Tile tile) {highlightedWhiteTiles.add(tile);}
	public void addHightlightRedTiles(Tile tile) {highlightedRedTiles.add(tile);}
	
	
	//Getter and setter method for Board Class;
	public ArrayList<Tile> getPlayer1UnitTiles() {return player1UnitTiles;}
	public ArrayList<Tile> getPlayer2UnitTiles() {return player2UnitTiles;}
	public int getX() {return x;}
	public int getY() {return y;}
	public HashMap<String, Tile> getTileMap() {return tileMap;}
	public ArrayList<Unit> getPlayer1Units() {return player1Units;}
	public ArrayList<Unit> getPlayer2Units() {return player2Units;}
	public ArrayList<Tile> getUnitOccupiedTiles() {return unitOccupiedTiles;}
	public ArrayList<Tile> getHighlightedRedTiles() {return highlightedRedTiles;}
	public ArrayList<Tile> getHighlightedWhiteTiles() {return highlightedWhiteTiles;}
	public Unit getPlayer1Avatar() {return player1Units.get(0);}	
	public Unit getPlayer2Avatar() {return player2Units.get(0);}	
	
}		






