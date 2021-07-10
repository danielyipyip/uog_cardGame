package structures.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.EventProcessor;
import structures.GameState;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Board {//This Class is used to store all the variables related to units and tiles

	int sleepTime = EventProcessor.sleepTime;
	int middleSleepTime = EventProcessor.middleSleepTime;
	
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
	private HashSet<Tile> highlightedRedTiles = new HashSet<>();// to store the tiles for valid attack and spell tiles.
	private HashSet<Tile> highlightedWhiteTiles = new HashSet<>();//to store the tiles for valid move and summon tiles.

	//constructor
	public Board() { 
		tileMap = new HashMap<String,Tile>();
		addTiles(this.x,this.y);  //Create a board with tiles x:9* y:5;
		//construct player's avatar when board is created
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

	public Tile getTile(int x, int y) { // use the x and y index to get the tile with xy key
		Tile tile = tileMap.get(Integer.toString(x)+Integer.toString(y));
		return tile;
	}
	
	/*Below is to construct player1 & 2 avatar (extends Unit) and display it on the board.
	 * Update the position of the player1 avatar.
	 * Set the Avatar to a tile; Add the tile to unitOccupiedTiles and player1UnitTiles.
	 * Add the unit to player1Units 
	 */
	
	public void addPlayer1Avatar (int x, int y, GameState gameState) {
		Avatar player1Avatar = (Avatar)BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 1, Avatar.class);
		Tile tile = this.getTile(x,y);
		player1Avatar.setPositionByTile(tile);
		tile.setUnit(player1Avatar);
		player1UnitTiles.add(tile);
		unitOccupiedTiles.add(tile);
		player1Units.add(player1Avatar);
		player1Avatar.setPlayer(gameState.getPlayer1());
		player1Avatar.setName("player1Avatar");
	}
	
	public void addPlayer2Avatar (int x, int y,GameState gameState) {
		Avatar player2Avatar = (Avatar)BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, -1, Avatar.class);
		Tile tile = getTile(x,y);
		player2Avatar.setPositionByTile(tile); 	
		tile.setUnit(player2Avatar);
		player2UnitTiles.add(tile);
		unitOccupiedTiles.add(tile);
		player2Units.add(player2Avatar);
		player2Avatar.setPlayer(gameState.getPlayer2());
		player2Avatar.setName("player2Avatar");
	}

	//Method to unhighlight tiles in Red Color
	public void unhighlightRedTiles(ActorRef out) {
		for(Tile tile: highlightedRedTiles) {
			BasicCommands.drawTile(out, tile, 0);
			try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
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
	 */
	public void addTileAndAvatarToPlayerArray (Tile tile,Unit unit,GameState gameState) {
			
		unit.setPositionByTile(tile);
		tile.setUnit(unit);
		unitOccupiedTiles.add(tile);	
			if(gameState.getCurrentPlayer().equals(gameState.getPlayer1())) {
				player1UnitTiles.add(tile);
				player1Units.add(unit);
			}
			 else {player2Units.add(unit);
			 	  player2UnitTiles.add(tile);}
	}
		
	//things to remove when a unit died: (1)PlayerNUnit (2)playerNUnitTile 
	//(3)unitOccupiedTiles (4) unit from the title
	public void removeUnit(Unit unit) {
		removeUnit(unit2Tile(unit)); //just use the other implementation
	}
	
	//the remove method will only remove if that object is present; 
	//player 1 & 2 units have different id, so it wont affect the others
	//for tile side since 1 tile 1 unit, so it wont matter too
	public void removeUnit(Tile tile) {
		Unit unit = tile.getUnit();
		player1Units.remove(unit); //(1)
		player2Units.remove(unit); //(1)
		player1UnitTiles.remove(tile);//(2)
		player2UnitTiles.remove(tile);//(2)
		unitOccupiedTiles.remove(tile); //(3)
		tile.setUnit(null); //(4)
	}
	
	//method to add a unit to a tile: things to do similar to above remove unit 
	//public void addUnit(Unit unit, int player) {
		//addUnit(unit, player);
	//}
	
	public void addUnit(Tile tile, int player) {
		Unit unit = tile.getUnit();
		if (player==1) {
			player1Units.add(unit);//(1)
			player1UnitTiles.add(tile);//(2)
		}else {
			player2Units.add(unit);//(1)
			player2UnitTiles.add(tile);//(2)
		}
		//both players
		unitOccupiedTiles.add(tile); //(3)
		tile.setUnit(unit);
		unit.setPositionByTile(tile);
	}
	
	//return the tile unit is on
	public Tile unit2Tile(Unit unit) {
		int x=unit.getPosition().getTilex();
		int y=unit.getPosition().getTiley();
		Tile tile = this.getTile(x, y);
		return tile;
	}
	
	public void flyAbility (ActorRef out,GameState gameState ,Tile tileClicked) {
		

		int positionX = tileClicked.getTilex();
		int positionY = tileClicked.getTiley();

		//Flying ability
		if(getTile(positionX, positionY).getUnit().getName().equals("Windshrike")){
			for(int i=0; i<getX(); i++) {
				for(int j=0; j<getY();j++) {
					Tile tile = gameState.getBoard().getTile(i, j);
					if(unitOccupiedTiles.contains(tile)) continue;
					if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
						BasicCommands.drawTile(out, tile, 1); //only draw for player1
					}
					gameState.getBoard().addHighlightWhiteTiles(tile);
					try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
				}			
			} return;
		}
		
	}
	
	//Below is the method to highlight the tiles in white for move, adjacent
	public void highlightAdjacentTile (Tile tileClicked,ActorRef out) {
			
				int positionX = tileClicked.getTilex();
				int positionY = tileClicked.getTiley();

				//Flying ability
				//if(getTile(positionX, positionY).getUnit().getName().equals("Windshrike")){
					//for(int i=0; i<getX(); i++) {
						//for(int j=0; j<getY();j++) {
							//Tile tile = gameState.getBoard().getTile(i, j);
							//if(unitOccupiedTiles.contains(tile)) continue;
							//if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
								//BasicCommands.drawTile(out, tile, 1); //only draw for player1
							//}
							//gameState.getBoard().addHighlightWhiteTiles(tile);
							//try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
						//}			
					//} return;
				//}
				//Normal Unit move, without ability
				//Highlighted the valid move tiles surrounding the unit first.
				int x = positionX-1;
				int y = positionY-1;
				//BasicCommands.addPlayer1Notification(out,""+positionX + positionY, 2);
				for(int i=x;i<= positionX+1;i++) {
					for(int j= y;j<=positionY+1;j++) {
						Tile tile = getTile(i, j) ;
							//if the tile does not have any unit and is not null. It will be in white tile
							if((!(tile==null)) && (unitOccupiedTiles.contains(tile)==false)) {
								//adding the tile to the white tile list
								addHighlightWhiteTiles(tile);
					
							}
				}
			}
			/* drawing the x+2,y and x-2,y and x,y+2 and x,y-2 tile
			 * It will only be highlighted when it is null and if x+1,y and x-1,y is not occupied by the opponent unit.
			 * If x+1,y and x-1,y contains opponent units..x+2,y and x-2,y will not be highlighted.
			 * Only player 1 will display the tiles.
			 */
				//Tile tileXPlus1Y = gameState.getBoard().getTile(positionX+1, positionY);
				//Tile tileXPlus2Y = gameState.getBoard().getTile(positionX+2, positionY);
				//Tile tileXYPlus1 = gameState.getBoard().getTile(positionX, positionY+1);
				//Tile tileXYPlus2 = gameState.getBoard().getTile(positionX, positionY+2);
				//Tile tileXMinus1Y = gameState.getBoard().getTile(positionX-1, positionY);
				//Tile tileXMinus2Y = gameState.getBoard().getTile(positionX-2, positionY);
				//Tile tileXYMinus1 = gameState.getBoard().getTile(positionX, positionY-1);
				//Tile tileXYMinus2 = gameState.getBoard().getTile(positionX, positionY-2);
		
				//if(gameState.getCurrentPlayer().equals(gameState.getPlayer1())) {
					//if((checkTile(tileXPlus1Y, player2UnitTiles)==false)&&(checkTile(tileXPlus2Y, unitOccupiedTiles)==false)&&		
						//(!(tileXPlus1Y==null))&& (!(tileXPlus2Y==null))){
						//	gameState.getBoard().addHighlightWhiteTiles(tileXPlus2Y);
					//}
					//if((checkTile(tileXMinus1Y, player2UnitTiles)==false)&&(checkTile(tileXMinus2Y, unitOccupiedTiles)==false)&&
						//(!(tileXMinus1Y==null))&& (!(tileXMinus2Y==null))){
						//gameState.getBoard().addHighlightWhiteTiles(tileXMinus2Y);
					//}
					//if((checkTile(tileXYMinus1, player2UnitTiles)==false)&&(checkTile(tileXYMinus2, unitOccupiedTiles)==false)&&
						//(!(tileXYMinus1==null))&& (!(tileXYMinus2==null))){
						///BasicCommands.drawTile(out,tileXYMinus2, 1);
						//gameState.getBoard().addHighlightWhiteTiles(tileXYMinus2);
					//}	
					//if((checkTile(tileXYPlus1, player2UnitTiles)==false)&&(checkTile(tileXYPlus2, unitOccupiedTiles)==false)&&
						//(!(tileXYPlus1==null))&& (!(tileXYPlus2==null))){
						//gameState.getBoard().addHighlightWhiteTiles(tileXYPlus2);
					//}
				//}else{//If player2.if the tile does not contain player 1 unit..add the tile to the valid move tile array.
					
					//if((checkTile(tileXPlus1Y, player1UnitTiles)==false)&&(checkTile(tileXPlus2Y, unitOccupiedTiles)==false)&&		
							//(!(tileXPlus1Y==null))&& (!(tileXPlus2Y==null))){
								//gameState.getBoard().addHighlightWhiteTiles(tileXPlus2Y);
						//}
						//if((checkTile(tileXMinus1Y, player1UnitTiles)==false)&&(checkTile(tileXMinus2Y, unitOccupiedTiles)==false)&&
								//(!(tileXMinus1Y==null))&& (!(tileXMinus2Y==null))){
							//gameState.getBoard().addHighlightWhiteTiles(tileXMinus2Y);
						//}
						//if((checkTile(tileXYMinus1, player1UnitTiles)==false)&&(checkTile(tileXYMinus2, unitOccupiedTiles)==false)&&
							//(!(tileXYMinus1==null))&& (!(tileXYMinus2==null))){
							//gameState.getBoard().addHighlightWhiteTiles(tileXYMinus2);
						//}	
						//if((checkTile(tileXYPlus1, player1UnitTiles)==false)&&(checkTile(tileXYPlus2, unitOccupiedTiles)==false)&&
							//(!(tileXYPlus1==null))&& (!(tileXYPlus2==null))){
							//gameState.getBoard().addHighlightWhiteTiles(tileXYPlus2);
						//}
				
					//}
				//if(gameState.getCurrentPlayer().equals(gameState.getPlayer1())) {
					//displayWhiteTile (out,gameState.getBoard().getHighlightedWhiteTiles());
				//}
	}
	
	public void highlightDiagonal (ActorRef out,GameState gameState ,Tile tileClicked) {
		
		/* drawing the x+2,y and x-2,y and x,y+2 and x,y-2 tile
		 * It will only be highlighted when it is null and if x+1,y and x-1,y is not occupied by the opponent unit.
		 * If x+1,y and x-1,y contains opponent units..x+2,y and x-2,y will not be highlighted.
		 * Only player 1 will display the tiles.
		 */
		int positionX = tileClicked.getTilex();
		int positionY = tileClicked.getTiley();
		ArrayList<Tile> opponentUnitTiles = player2UnitTiles;
		
		Tile tileXPlus1Y = getTile(positionX+1, positionY);
		Tile tileXPlus2Y = getTile(positionX+2, positionY);
		Tile tileXYPlus1 = getTile(positionX, positionY+1);
		Tile tileXYPlus2 = getTile(positionX, positionY+2);
		Tile tileXMinus1Y = getTile(positionX-1, positionY);
		Tile tileXMinus2Y = getTile(positionX-2, positionY);
		Tile tileXYMinus1 = getTile(positionX, positionY-1);
		Tile tileXYMinus2 = getTile(positionX, positionY-2);
		

		//if(gameState.getCurrentPlayer().equals(gameState.getPlayer1())){opponentUnitTiles = player2UnitTiles;}
		if(gameState.getCurrentPlayer().equals(gameState.getPlayer2())){opponentUnitTiles = player1UnitTiles;}

		if((opponentUnitTiles.contains(tileXPlus1Y)==false)&&(unitOccupiedTiles.contains(tileXPlus2Y)==false)
			&& (!(tileXPlus1Y==null))&& (!(tileXPlus2Y==null))){
			addHighlightWhiteTiles(tileXPlus2Y);}
		if((opponentUnitTiles.contains(tileXMinus1Y)==false)&&(unitOccupiedTiles.contains(tileXMinus2Y)==false)
			&& (!(tileXMinus1Y==null))&& (!(tileXMinus2Y==null))){
			addHighlightWhiteTiles(tileXMinus2Y);}
		if((opponentUnitTiles.contains(tileXYMinus1)==false)&&(unitOccupiedTiles.contains(tileXYMinus2)==false)
				&& (!(tileXYMinus1==null))&& (!(tileXYMinus2==null))){
			addHighlightWhiteTiles(tileXYMinus2);}
		if((opponentUnitTiles.contains(tileXYPlus1)==false)&&(unitOccupiedTiles.contains(tileXYPlus2)==false)
				&& (!(tileXYPlus1==null))&& (!(tileXYPlus2==null))){
			addHighlightWhiteTiles(tileXYPlus2);}}
	
	public void highlightMoveTile (ActorRef out,GameState gameState ,Tile tileClicked) {
		highlightAdjacentTile(tileClicked,out);
		highlightDiagonal(out,gameState,tileClicked);}
	
	
	//Below is the method to highlight the adjacent tiles in red for attack
	public void highlightAttackTile (GameState gameState ,Tile tileClicked) {
		
		int positionX = tileClicked.getTilex();
		int positionY = tileClicked.getTiley();
		//Highlighted the tiles surrounding the unit first.
		int x = positionX-1;
		int y = positionY-1;
		
		ArrayList<Tile> opponentUnitTiles = null;
		if(gameState.getCurrentPlayer().equals(gameState.getPlayer1())){opponentUnitTiles = player2UnitTiles;}
		if(gameState.getCurrentPlayer().equals(gameState.getPlayer2())){opponentUnitTiles = player1UnitTiles;}
				
		//Checking whether the surrounding tile contains opponent unit. if yes, it will be red highlighted
				if(gameState.getUnitClicked().isAttacked()==false) {
						for(int i=x;i<= positionX+1;i++) {
							for(int j=y;j<=positionY+1;j++) {
								Tile tile = gameState.getBoard().getTile(i, j) ;
									if(opponentUnitTiles.contains(tile)&&(!(tile==null))){
										gameState.getBoard().addHighlightRedTiles(tile);
									}
							}
						}
				}			
	}
	
	public void highlightMoveAndAttackTile (ActorRef out, GameState gameState ,Tile tile) {
		
		/*First, add the possible valid move Tile.
		 * Then run through the hashset and call the highlightattacktile for each element
		 * to add the possible valid attack tiles.
		 */
		highlightMoveTile(out, gameState,tile);
		for(Tile i: getHighlightedWhiteTiles()) {	
			highlightAttackTile(gameState,i);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public void addHighlightWhiteTiles(Tile tile) {highlightedWhiteTiles.add(tile);}
	public void addHighlightRedTiles(Tile tile) {highlightedRedTiles.add(tile);}
		
	//Getter and setter method for Board Class;
	public ArrayList<Tile> getPlayer1UnitTiles() {return player1UnitTiles;}
	public ArrayList<Tile> getPlayer2UnitTiles() {return player2UnitTiles;}
	public int getX() {return x;}
	public int getY() {return y;}
	public HashMap<String, Tile> getTileMap() {return tileMap;}
	public ArrayList<Unit> getPlayer1Units() {return player1Units;}
	public ArrayList<Unit> getPlayer2Units() {return player2Units;}
	public ArrayList<Tile> getUnitOccupiedTiles() {return unitOccupiedTiles;}
	public HashSet<Tile> getHighlightedRedTiles() {return highlightedRedTiles;}
	public HashSet<Tile> getHighlightedWhiteTiles() {return highlightedWhiteTiles;}
	public Avatar getPlayer1Avatar() { return (Avatar)player1Units.get(0); }
	public Avatar getPlayer2Avatar() { return (Avatar)player2Units.get(0); }	
//	public Avatar getAvatar(Player player) {
//		if () {}
//		else{}
//	}
}		

/*public Avatar getPlayer1Avatar() {
for(Unit i: player1Units) {
	if(i.getId()==1) {
		if(i instanceof Avatar) {
			Avatar avatar = (Avatar) i;
			return avatar;
		}
	}	
}return null;
return (Avatar)player1Units.get(0);
}*/

/*public Avatar getPlayer2Avatar() {
for(Unit i: player2Units) {
	if(i.getId()==-1) {
		if(i instanceof Avatar) {
			Avatar avatar = (Avatar) i;
			return avatar;
		}
	}	
}return null;
}*/		



