package structures.basic;

import java.util.ArrayList;
import java.util.HashMap;

import akka.actor.ActorRef;
import commands.BasicCommands;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Board {

	private HashMap<String, Tile> tileMap;
	private int x;
	private int y;
	private Avatar player1Avatar;
	private Avatar player2Avatar;
	private ArrayList<Tile> player1UnitTiles; //to store the tile player1 unit occupy
	private ArrayList<Tile> player2UnitTiles; //to store the tile player2 unit occupy


	public Board() 

	{ //constructor		
		tileMap = new HashMap<String,Tile>();
		player1UnitTiles = new ArrayList<Tile>();
		player2UnitTiles = new ArrayList<Tile>();
		x = 9;
		y = 5;
		addTiles(x,y);  //Create a board with tiles x:9* y:5;
		addPlayer1Avatar(1,2);
		addPlayer2Avatar(7,2);
	}

	public void addTiles (int x, int y) {

		//Store the tiles in HashMap, key is the string of xy..e.g. x:2 y:2 key = 22;

		for(int i=0; i<x; i++) {

			for(int j =0; j<y;j++) 
			{
				String index = Integer.toString(i)+Integer.toString(j);
				Tile tile = BasicObjectBuilders.loadTile(i, j);
				tileMap.put(index, tile);

			}			

		}
	}

	public Tile getTile(int x, int y){ // use the x and y index to get the tile
		Tile tile = tileMap.get(Integer.toString(x)+Integer.toString(y));
		return tile;
	}
	public void addPlayer1Avatar (int x, int y) {
		player1Avatar = (Avatar) BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Avatar.class);
		Tile tile = getTile(x,y);
		player1Avatar.setPositionByTile(tile);
		tile.setUnit(player1Avatar);
		player1UnitTiles.add(tile);

	}

	public void addPlayer2Avatar (int x, int y) {
		player2Avatar = (Avatar) BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 0, Avatar.class);
		Tile tile = getTile(x,y);
		player2Avatar.setPositionByTile(tile); 	
		tile.setUnit(player2Avatar);
		player2UnitTiles.add(tile);
	}

	public ArrayList<Tile> getPlayer1UnitTiles() {return player1UnitTiles;}
	public void setPlayer1UnitTiles(ArrayList<Tile> player1UnitTiles) {this.player1UnitTiles = player1UnitTiles;}
	public ArrayList<Tile> getPlayer2UnitTiles() {return player2UnitTiles;}
	public void setPlayer2UnitTiles(ArrayList<Tile> player2UnitTiles) {this.player2UnitTiles = player2UnitTiles;}
	public Avatar getPlayer2Avatar() {return player2Avatar;}	
	public Avatar getPlayer1Avatar() {return player1Avatar;}
	public int getX() {return x;}
	public int getY() {return y;}


}		






