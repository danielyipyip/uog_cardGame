package structures.basic;

import java.util.ArrayList;
import java.util.HashMap;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Board {//This Class is used to store all the variables related to units and tiles

	private HashMap<String, Tile> tileMap; // to store the tiles in HashMap. xy is the key, value stored is Tile.
	private int x; // x = number of square of a row
	private int y; // y = number of square of a column
	private ArrayList<Tile> player1UnitTiles; //to store the Tiles player1 units occupy
	private ArrayList<Tile> player2UnitTiles; //to store the Tiles player2 units occupy
	private ArrayList<Unit> player1Units;//to store player1 units
	private ArrayList<Unit> player2Units;// to store player2 units
	private ArrayList<Tile> unitOccupiedTiles;// to store the Tiles occupied by all Units on board.
	private ArrayList<Tile> highlightedRedTiles;// to store the tiles for valid attack and spell tiles.
	private ArrayList<Tile> highlightedWhiteTiles;//to store the tiles for valid move and summon tiles.

	public Board() 

	{ //constructor		
		tileMap = new HashMap<String,Tile>();
		player1UnitTiles = new ArrayList<Tile>();
		player2UnitTiles = new ArrayList<Tile>();
		player1Units = new ArrayList<Unit>();
		player2Units = new ArrayList<Unit>();
		unitOccupiedTiles = new ArrayList<Tile>();
		highlightedRedTiles = new ArrayList<Tile>();
		highlightedWhiteTiles = new ArrayList<Tile>();
		x = 9; //
		y = 5;
		addTiles(x,y);  //Create a board with tiles x:9* y:5;
		addPlayer1Avatar(1,2); //Construct player1
		addPlayer2Avatar(7,2); //Construct player2
	}

	public void addTiles (int x, int y) { //Store the tiles in HashMap, key is the string of xy..e.g. x:2 y:2 key = 22
		for(int i=0; i<x; i++) {
			for(int j =0; j<y;j++) 
			{
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
	
	/*Below is to construct player1 avatar. The avatar class extends unit class and display it on the board.
	 * Update the position of the player1 avatar.
	 * Set the Avatar to a tile; Add the tile to unitOccupiedTiles and player1UnitTiles.
	 * Add the unit to player1Units 
	 */
	
	public void addPlayer1Avatar (int x, int y) {
		Avatar player1Avatar = (Avatar) BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Avatar.class);
		player1Avatar.setAttack(2);
		Tile tile = getTile(x,y);
		player1Avatar.setPositionByTile(tile);
		tile.setUnit(player1Avatar);
		player1UnitTiles.add(tile);
		unitOccupiedTiles.add(tile);
		player1Units.add(player1Avatar);

	}
	
	/*Below is to construct player1 avatar. The avatar class extends unit class and display it on the board.
	 * Update the position of the player1 avatar.
	 * Set the Avatar to a tile; Add the tile to unitOccupiedTiles and player1UnitTiles.
	 * Add the unit to player1Units 
	 */
	
	public void addPlayer2Avatar (int x, int y) {
		Avatar player2Avatar = (Avatar) BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 0, Avatar.class);
		player2Avatar.setAttack(2);
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
	
	
	//Below is the method to highlight the tiles in white for move
	public void highlightMoveTile (ActorRef out,GameState gameState ,Unit unit) {
		int positionX = unit.getPosition().getTilex();
		int positionY = unit.getPosition().getTiley();
		//Highlighted the tiles surrounding the avatar first.
		int x = positionX-1;
		int y = positionY-1;
		ArrayList<Tile> occupiedTiles = gameState.getBoard().getUnitOccupiedTiles();
		for(int i=x;i<= unit.getPosition().getTilex()+1;i++) {
			for(int j=y;j<=unit.getPosition().getTiley()+1;j++) {
				Tile tile = gameState.getBoard().getTile(i, j) ;
				if(!(occupiedTiles.contains(tile))) {
				//if the tile does not have any unit...then draw Tile.
				BasicCommands.drawTile(out, tile, 1);
				try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
				highlightedWhiteTiles.add(tile);
				}
			}
		}
		//drawing the x+2 and x-2 square
		for(int i=x-1;i<=unit.getPosition().getTilex()+2;i++) {
			Tile tile = gameState.getBoard().getTile(i, unit.getPosition().getTiley()) ;
			if(!(occupiedTiles.contains(tile))) {
				BasicCommands.drawTile(out, tile, 1);
				try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
				highlightedWhiteTiles.add(tile);
				}
			}
		//drawing the y+2 and y-2 square
		for(int i=y-1;i<= unit.getPosition().getTiley()+2;i++) {
			Tile tile = gameState.getBoard().getTile(unit.getPosition().getTilex(), i) ;
			if(!(occupiedTiles.contains(tile))) {
				//if the tile does not have any unit
				BasicCommands.drawTile(out, tile, 1);
				try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
				highlightedWhiteTiles.add(tile);
				}
			}
		}
	
	//Below cant return a Avatar unit...since parent class cant be cast into child class....
	public Unit getPlayer1Avatar() { //to get the avatar of player1
		Unit player1Avatar = player1Units.get(0);
		return player1Avatar;}	
	
	public Unit getPlayer2Avatar() {// to get the avatar of player2
		Unit player2Avatar = player2Units.get(0);
		return player2Avatar;}	

	
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
	

}		






