package commands;

import java.util.ArrayList;

import akka.actor.ActorRef;
import events.EventProcessor;
import events.TileClicked;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;

public class GroupsCommands {
	static int sleepTime = EventProcessor.sleepTime;

	public static void setUpPlayerHealthMana(ActorRef out, GameState gameState) {
		BasicCommands.setPlayer1Health(out, gameState.getPlayer1());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer2Health(out, gameState.getPlayer2());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer1Mana(out, gameState.getPlayer1());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer2Mana(out, gameState.getPlayer2());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}

	//6 steps to move a unit: unit (1)swap unit's associated tiles (2)change player1/2UnitTiles & unitOccupiedTiles
	//(3) unhightlight (4) move animation; (5) actual moving (6) set UnitClicked to null
	public static void moveUnit(ActorRef out, GameState gameState, Unit unit, Tile targetTile) {
		//(1) get unit's tile b4 moving; swap unit's associated tile to new tile
		Tile previousTile = gameState.getTileClicked();
		previousTile.setUnit(null);
		targetTile.setUnit(unit);
		//(2) change player1/2UnitTiles & unitOccupiedTiles
		if (gameState.getCurrentPlayer().equals(gameState.getPlayer1())) {
			gameState.getBoard().getPlayer1UnitTiles().remove(previousTile);
			gameState.getBoard().getPlayer1UnitTiles().add(targetTile);
		}else {
			gameState.getBoard().getPlayer2UnitTiles().remove(previousTile);
			gameState.getBoard().getPlayer2UnitTiles().add(targetTile);
		}
		gameState.getBoard().getUnitOccupiedTiles().remove(previousTile);
		gameState.getBoard().getUnitOccupiedTiles().add(targetTile);
		//(3) un-hightlight tiles
		gameState.getBoard().unhighlightWhiteTiles(out);
		gameState.getBoard().unhighlightRedTiles(out);
		//(4) move animation
		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.move);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		//(5) moveUnitToTile
		BasicCommands.moveUnitToTile(out, unit, targetTile);
		unit.setPositionByTile(targetTile); 
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		//(6) set seleted unit to null
		gameState.setUnitClicked(null);
		//(7) set move to false
		unit.setMoved(true);
	}

	//Below is the method to highlight the tiles in white for move
	public static void highlightMoveTile (ActorRef out,GameState gameState ,Unit unit) {
		int positionX = unit.getPosition().getTilex();
		int positionY = unit.getPosition().getTiley();
		//Highlighted the tiles surrounding the avatar first.
		int x = positionX-1;
		int y = positionY-1;
		ArrayList<Tile> occupiedTiles = gameState.getBoard().getUnitOccupiedTiles();
		for(int i=x;i<= positionX+1;i++) {
			for(int j=y;j<=positionY+1;j++) {
				Tile tile = gameState.getBoard().getTile(i, j) ;
				if(!(occupiedTiles.contains(tile))) {
					//if the tile does not have any unit...then draw Tile.
					BasicCommands.drawTile(out, tile, 1);
					try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
					gameState.getBoard().addHightlightWhiteTiles(tile);
				}
			}
		}
		//drawing the x+2 and x-2 square
		for(int i=x-1;i<=positionX+2;i++) {
			Tile tile = gameState.getBoard().getTile(i, positionY) ;
			if(!(occupiedTiles.contains(tile))) {
				BasicCommands.drawTile(out, tile, 1);
				try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
				gameState.getBoard().addHightlightWhiteTiles(tile);
			}
		}
		//drawing the y+2 and y-2 square
		for(int i=y-1;i<= positionY+2;i++) {
			Tile tile = gameState.getBoard().getTile(positionX, i) ;
			if(!(occupiedTiles.contains(tile))) {
				//if the tile does not have any unit
				BasicCommands.drawTile(out, tile, 1);
				try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
				gameState.getBoard().addHightlightWhiteTiles(tile);
			}
		}
	}


	//Below is the method to highlight the tiles in red for attack
	
	public static void highlightAttackTile (ActorRef out,GameState gameState ,Unit unit) {
		
		int positionX = unit.getPosition().getTilex();
		int positionY = unit.getPosition().getTiley();
		
		//Highlighted the tiles surrounding the avatar first.
		int x = positionX-1;
		int y = positionY-1;
		
		//Get the opponent unit list
		ArrayList<Tile> player2UnitTiles = gameState.getBoard().getPlayer2UnitTiles();
		
			for(int i=x;i<= positionX+1;i++) {
				for(int j=y;j<=positionY+1;j++) {
					Tile tile = gameState.getBoard().getTile(i, j) ;		
					if(!(gameState.getBoard().getPlayer1Avatar().equals(tile.getUnit()))){
						if((player2UnitTiles.contains(tile))){
							BasicCommands.drawTile(out, tile, 2);
							try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
							gameState.getBoard().addHightlightRedTiles(tile);}
					}
				}
			}
	}
	
	//adjacent attack, include counter attack condition.
	public static void attackUnit (ActorRef out, GameState gameState,Unit unit, Tile target) {

			Unit attackTarget = target.getUnit();
			int targetNewHealth = attackTarget.getHealth() - unit.getAttack();
			attackTarget.setHealth(targetNewHealth);
			if(attackTarget.equals(gameState.getBoard().getPlayer2Units().get(0))) {
				gameState.getPlayer2().setHealth(targetNewHealth);
				BasicCommands.setPlayer2Health(out, gameState.getPlayer2());
				
			}
			if(attackTarget.equals(gameState.getBoard().getPlayer1Units().get(0))) {
				gameState.getPlayer1().setHealth(targetNewHealth);
				BasicCommands.setPlayer1Health(out, gameState.getPlayer2());
			}
			
			BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.attack);
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.setUnitHealth(out, attackTarget , attackTarget.getHealth());
			try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
			if(targetNewHealth>=0) {counterAttack(out,gameState,unit,target);
			unit.setAttacked(true);}
	}
		
	
	
	
	
	

	public boolean checkTile (Tile tile, ArrayList<Tile> a) {//check whether the tile is in an arraylist.
		
		if(a.contains(tile)) {
		return true;	
		}return false;}
	
	
	public static void counterAttack (ActorRef out, GameState gameState,Unit unit, Tile target){
		
		int attackerNewHeath = unit.getHealth() - target.getUnit().getAttack();
		unit.setHealth(attackerNewHeath);
		if(unit.equals(gameState.getBoard().getPlayer1Units().get(0))) {
			gameState.getPlayer1().setHealth(attackerNewHeath);
			BasicCommands.setPlayer1Health(out, gameState.getPlayer1());
		}
		if(unit.equals(gameState.getBoard().getPlayer2Units().get(0))) {
			gameState.getPlayer2().setHealth(attackerNewHeath);
			BasicCommands.setPlayer2Health(out, gameState.getPlayer2());
		}
		BasicCommands.playUnitAnimation(out, target.getUnit(), UnitAnimationType.attack);
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, unit, unit.getHealth());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}

	}
		
		
		
		
	



}
	
	
	
