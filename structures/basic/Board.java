package structures.basic;

import java.util.HashMap;

import akka.actor.ActorRef;
import commands.BasicCommands;
import utils.BasicObjectBuilders;

public class Board {
	
		
		public HashMap<String, Tile> tileMap;
		int x;
		int y;

		
		public Board() 
		
			{ //constructor
				
				tileMap = new HashMap<String,Tile>();
				x = 9;
				y = 5;
				addTiles(x,y);  //Create a board with tiles x:9* y:5;
			}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public void addTiles (int x, int y) {
			
		 //Store the tiles in HashMap, key is the string of xy..e.g. x:2 y:2 key = 22;
			
			for(int i=1; i<=x; i++) {
				
				for(int j =1; j<=y;j++) 
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



		public void highlightedTile (int x, int y, int mode, ActorRef out) {
			
			Tile targetTile = getTile(x,y) ;
			BasicCommands.drawTile(out, targetTile, mode);	
		}
			

}		


	
	
	

