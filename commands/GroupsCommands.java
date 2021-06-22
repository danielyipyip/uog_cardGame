package commands;

import java.util.ArrayList;

import akka.actor.ActorRef;
import events.EventProcessor;
import events.TileClicked;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

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
		//(6) set seleted unit to null & pos to -1
		gameState.unSelectCard();
		//(7) set move to false
		unit.setMoved(true);
	} 

	//method that do both front-end display & back-end unit health
	public static void setUnitHealth(ActorRef out, Unit targetUnit, int health) {
		targetUnit.setHealth(health);
		BasicCommands.setUnitHealth(out, targetUnit, health);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}

	//method that do both front-end display & back-end unit attack
	public static void setUnitAttack(ActorRef out, Unit targetUnit, int attack) {
		targetUnit.setHealth(attack);
		BasicCommands.setUnitAttack(out, targetUnit, attack);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}

	//draw the hand in display
	public static void drawHand(ActorRef out, GameState gameState) {
		int pos=0;	
		ArrayList<Card> currHand = gameState.getPlayer1().getMyhand().getMyhand();
		for (Card i:currHand) {
			BasicCommands.drawCard(out, i, pos++, 0);
			try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

	//delete a card both in front-end (display) and back-end (Hand)
	//few things it does: (1)unselect cards: gameState.cardPos=-1 gameState.CardSelected=null
	//(2) delete card from player's Hand (back-end)
	//(3) delect card from front-end display and shift the remaining cards to the left
	public static void deleteCard(ActorRef out, GameState gameState, int n) {gameState.deleteCard(n);
		if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {drawHand(out, gameState);//change the displays:redraw previous card
			BasicCommands.deleteCard(out, gameState.getCurrentPlayer().getMyhand().getMyhand().size());//change the displays: remove last card (will not be redraw)
			try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		}
	} 

	//play unit cards
	//For player1 only now
	public static void playUnitCard(ActorRef out, GameState gameState, String cardName, Tile currentTileClicked) {
		
		Unit unit = null;
		switch (cardName) {
			//Player1 Unit Cards
			//Important: the "id" parameter needs to be modified
			case "Comodo Charger": 
				unit = BasicObjectBuilders.loadUnit(StaticConfFiles.u_comodo_charger, 3, Unit.class);
				unit.setAttack(1);
				unit.setHealth(3);
				break;
			case "Hailstone Golem":
				unit = BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golem, 3, Unit.class);
				unit.setAttack(4);
				unit.setHealth(6);
				break;
			case "Pureblade Enforcer":
				unit = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pureblade_enforcer, 3, Unit.class);
				unit.setAttack(1);
				unit.setHealth(4);
				break;
			case "Azure Herald":
				unit = BasicObjectBuilders.loadUnit(StaticConfFiles.u_azure_herald, 3, Unit.class);
				unit.setAttack(1);
				unit.setHealth(4);
				break;
			case "Silverguard Knight":
				unit = BasicObjectBuilders.loadUnit(StaticConfFiles.u_silverguard_knight, 3, Unit.class);
				unit.setAttack(1);
				unit.setHealth(5);
				break;
			case "Azurite Lion":
				unit = BasicObjectBuilders.loadUnit(StaticConfFiles.u_azurite_lion, 3, Unit.class);
				unit.setAttack(2);
				unit.setHealth(3);
				break;
			case "Fire Spitter":
				unit = BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, 3, Unit.class);
				unit.setAttack(3);
				unit.setHealth(2);
				break;
			case "Ironcliff Guardian":
				unit = BasicObjectBuilders.loadUnit(StaticConfFiles.u_ironcliff_guardian, 3, Unit.class);
				unit.setAttack(3);
				unit.setHealth(10);
				break;
		}
		
		//Unit cannot move or attack after being summon in the turn
		unit.setAttacked(true);
		unit.setMoved(true);
		
		//Add the unit to the relevant array
		gameState.getBoard().addTileAndAvatarToPlayerArray(currentTileClicked, gameState.getBoard().getPlayer1UnitTiles(), unit);
		
		//Draw out Unit and it's stats on the browser
		BasicCommands.drawUnit(out, unit, currentTileClicked);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit, unit.getAttack());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, unit , unit.getHealth());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		
		//Unhighlight Tiles and Delete Cards
		gameState.getBoard().unhighlightWhiteTiles(out);
		deleteCard(out, gameState, gameState.getcardPos());
	}
	
	
	//play spell cards
	//////////////without animation///////////////////
	public static void playSpellCard(ActorRef out, GameState gameState, String cardName, Tile currentTileClicked) {
		//Unit that the spell act on
		Unit targetUnit = currentTileClicked.getUnit();
		//play the card: separated by which card is played
		if(cardName.equals("Truestrike")) {//deal 2 damage to a unit
			GroupsCommands.setUnitHealth(out, targetUnit, targetUnit.getHealth()-2);
		}
		if(cardName.equals("Sundrop Elixir")) { //heal a unit by 5
			int newHealth = targetUnit.getHealth()+5;
			if (newHealth>targetUnit.getMaxHealth()) { //if >max, set to max
				GroupsCommands.setUnitHealth(out, targetUnit, targetUnit.getMaxHealth());
			}else {GroupsCommands.setUnitHealth(out, targetUnit, newHealth);} //if NOT > max, set to health+5
		}
		if(cardName.equals("Staff of Y'Kir'")) { //avatar attack +=2
			GroupsCommands.setUnitAttack(out, targetUnit, targetUnit.getAttack()+2);
		}
		if(cardName.equals("Entropic Decay")) {  //unit health -> 0
			GroupsCommands.setUnitHealth(out, targetUnit,0); //need other story: trigger death
		}
		//after card played, 
		//un-hightlight
		gameState.getBoard().unhighlightRedTiles(out);
		//animation??

		//remove the card from hand (& update display)
		deleteCard(out, gameState, gameState.getcardPos());
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
							if(!(gameState.getBoard().getPlayer1Avatar().getPosition().getTilex()==positionX)&&
									!(gameState.getBoard().getPlayer1Avatar().getPosition().getTilex()==positionY)) {
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



