package structures.basic;

import java.util.HashMap;

import akka.actor.ActorRef;
import commands.BasicCommands;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Board {
	
		
		private HashMap<String, Tile> tileMap;
		private int x;
		private int y;
		private Unit player1Avatar;
		private Unit player2Avatar;
		
		
		public Board() 
		
			{ //constructor
				
				tileMap = new HashMap<String,Tile>();
				x = 9;
				y = 5;
				addTiles(x,y);  //Create a board with tiles x:9* y:5;
				addPlayer1Avatar(1,2);
				addPlayer2Avatar(7,2);
			}

		public Unit getPlayer1Avatar() {
			return player1Avatar;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
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


		public Tile getTile(int x, int y) // use the x and y index to get the tile
			{ 
			Tile tile = tileMap.get(Integer.toString(x)+Integer.toString(y));
			return tile;
			}

		public void addPlayer1Avatar (int x, int y) {
			
			player1Avatar = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
			Tile tile = getTile(x,y);
			player1Avatar.setPositionByTile(tile); 
			
		}
		
		public void addPlayer2Avatar (int x, int y) {
			
			player2Avatar = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 0, Unit.class);
			Tile tile = getTile(x,y);
			player2Avatar.setPositionByTile(tile); 
			
		}


		public void highlightedTile (int x, int y, int mode, ActorRef out) {
			
			Tile targetTile = getTile(x,y) ;
			BasicCommands.drawTile(out, targetTile, mode);	
		}

		public Unit getPlayer2Avatar() {
			return player2Avatar;
		}
			

}		


	
	
	

