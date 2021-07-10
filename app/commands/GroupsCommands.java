package commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import akka.actor.ActorRef;
import events.EventProcessor;
import events.TileClicked;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class GroupsCommands {
	static int sleepTime = EventProcessor.sleepTime;	
	static int middleSleepTime = EventProcessor.middleSleepTime;	
	static int shortSleepTime = EventProcessor.shortSleepTime;

//	//6 steps to move a unit: unit (1)swap unit's associated tiles (2)change player1/2UnitTiles & unitOccupiedTiles
//	//(3) unhightlight (4) move animation; (5) actual moving (6) set UnitClicked to null
//	public static void moveUnit(ActorRef out, GameState gameState, Unit unit, Tile targetTile) {
//		//gameState.switchUnitMoving();
//		int player;
//		if (gameState.getCurrentPlayer().equals(gameState.getPlayer1())){player=1;}
//		else {player=2;}
//		//(1) get unit's tile b4 moving; swap unit's associated tile to new tile
//		Tile previousTile = gameState.getTileClicked();
//		previousTile.setUnit(null);
//		targetTile.setUnit(unit);
//		//(2) change player1/2UnitTiles & unitOccupiedTiles
//		gameState.getBoard().removeUnit(previousTile);
//		gameState.getBoard().addUnit(targetTile, player);
//		//(3) un-hightlight tiles
//		gameState.getBoard().unhighlightWhiteTiles(out);
//		gameState.getBoard().unhighlightRedTiles(out);
//		//(4) move animation
//		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.move);
//		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//		//(5) moveUnitToTile
//		BasicCommands.moveUnitToTile(out, unit, targetTile);
//		unit.setPositionByTile(targetTile); 
//		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//		//(6) set seleted unit to null & pos to -1
//		gameState.unSelectCard();
//		//(7) set move to false
//		unit.setMoved(true);
//	}
	
	//Below is the method to highlight the tiles in white for move
		//public static void highlightMoveTile (ActorRef out,GameState gameState ,Tile tileClicked) {
		
			//int positionX = tileClicked.getTilex();
			//int positionY = tileClicked.getTiley();
			//ArrayList<Tile> occupiedTiles = gameState.getBoard().getUnitOccupiedTiles();
			//ArrayList<Tile> player1UnitTiles = gameState.getBoard().getPlayer1UnitTiles();
			//ArrayList<Tile> player2UnitTiles = gameState.getBoard().getPlayer2UnitTiles();
			
			//Flying ability
//			if(gameState.getBoard().getTile(positionX, positionY).getUnit().getName().equals("Windshrike")){
//				for(int i=0; i<gameState.getBoard().getX(); i++) {
//					for(int j=0; j<gameState.getBoard().getY();j++) {
//						Tile tile = gameState.getBoard().getTile(i, j);
//						if(occupiedTiles.contains(tile)) continue;
//						if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
//							BasicCommands.drawTile(out, tile, 1); //only draw for player1
//						}
//						gameState.getBoard().addHighlightWhiteTiles(tile);
//						try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
//					}
//				} return;
//			}
			
			//Highlighted the valid move tiles surrounding the unit first.
			//int x = positionX-1;
			//int y = positionY-1;

			//for(int i=x;i<= positionX+1;i++) {
				//for(int j=y;j<=positionY+1;j++) {
					//Tile tile = gameState.getBoard().getTile(i, j) ;
						//if the tile does not have any unit and is not null. It will be in white tile
						//if(!(tile==null)&& (checkTile(tile, occupiedTiles)==false)) {
							//adding the tile to the white tile list
							//gameState.getBoard().addHighlightWhiteTiles(tile);
						//}
			//}
		//}
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
				//if((checkTile(tileXPlus1Y, player2UnitTiles)==false)&&(checkTile(tileXPlus2Y, occupiedTiles)==false)&&		
					//(!(tileXPlus1Y==null))&& (!(tileXPlus2Y==null))){
						//gameState.getBoard().addHighlightWhiteTiles(tileXPlus2Y);
				//}
				//if((checkTile(tileXMinus1Y, player2UnitTiles)==false)&&(checkTile(tileXMinus2Y, occupiedTiles)==false)&&
					//(!(tileXMinus1Y==null))&& (!(tileXMinus2Y==null))){
					//gameState.getBoard().addHighlightWhiteTiles(tileXMinus2Y);
				//}
				//if((checkTile(tileXYMinus1, player2UnitTiles)==false)&&(checkTile(tileXYMinus2, occupiedTiles)==false)&&
					//(!(tileXYMinus1==null))&& (!(tileXYMinus2==null))){
					//BasicCommands.drawTile(out,tileXYMinus2, 1);
					//gameState.getBoard().addHighlightWhiteTiles(tileXYMinus2);
				//}	
				//if((checkTile(tileXYPlus1, player2UnitTiles)==false)&&(checkTile(tileXYPlus2, occupiedTiles)==false)&&
					//(!(tileXYPlus1==null))&& (!(tileXYPlus2==null))){
					//gameState.getBoard().addHighlightWhiteTiles(tileXYPlus2);
				//}
			//}else{//If player2.if the tile does not contain player 1 unit..add the tile to the valid move tile array.
				
				//if((checkTile(tileXPlus1Y, player1UnitTiles)==false)&&(checkTile(tileXPlus2Y, occupiedTiles)==false)&&		
						//(!(tileXPlus1Y==null))&& (!(tileXPlus2Y==null))){
							//gameState.getBoard().addHighlightWhiteTiles(tileXPlus2Y);
					//}
					//if((checkTile(tileXMinus1Y, player1UnitTiles)==false)&&(checkTile(tileXMinus2Y, occupiedTiles)==false)&&
							//(!(tileXMinus1Y==null))&& (!(tileXMinus2Y==null))){
						//gameState.getBoard().addHighlightWhiteTiles(tileXMinus2Y);
					//}
					//if((checkTile(tileXYMinus1, player1UnitTiles)==false)&&(checkTile(tileXYMinus2, occupiedTiles)==false)&&
						//(!(tileXYMinus1==null))&& (!(tileXYMinus2==null))){
						//gameState.getBoard().addHighlightWhiteTiles(tileXYMinus2);
					//}	
					//if((checkTile(tileXYPlus1, player1UnitTiles)==false)&&(checkTile(tileXYPlus2, occupiedTiles)==false)&&
						//(!(tileXYPlus1==null))&& (!(tileXYPlus2==null))){
						//gameState.getBoard().addHighlightWhiteTiles(tileXYPlus2);
					//}
			
				//}
			//if(gameState.getCurrentPlayer().equals(gameState.getPlayer1())) {
				//displayWhiteTile (out,gameState.getBoard().getHighlightedWhiteTiles());
			//}
//}
	//Below is the method to highlight the adjacent tiles in red for attack
	//public static void highlightAttackTile (ActorRef out,GameState gameState ,Tile tileClicked) {
			
			//int positionX = tileClicked.getTilex();
			//int positionY = tileClicked.getTiley();
			//Highlighted the tiles surrounding the unit first.
			//int x = positionX-1;
			//int y = positionY-1;
			//Get the opponent unit list
			//ArrayList<Tile> player1UnitTiles = gameState.getBoard().getPlayer1UnitTiles();
			//ArrayList<Tile> player2UnitTiles = gameState.getBoard().getPlayer2UnitTiles();
			//Checking whether the surrounding tile contains opponent unit. if yes, it will be red highlighted
			//if(gameState.getUnitClicked().isAttacked()==false) {
				//if(gameState.getCurrentPlayer().equals(gameState.getPlayer1())) {
					//for(int i=x;i<= positionX+1;i++) {
						//for(int j=y;j<=positionY+1;j++) {
							//Tile tile = gameState.getBoard().getTile(i, j) ;
								//if(player2UnitTiles.contains(tile)&&(!(tile==null))){
									//gameState.getBoard().addHighlightRedTiles(tile);
								//}
						//}
					//}
					//displayRedTile (out,gameState.getBoard().getHighlightedRedTiles());
				//}				
				//}else {
					//for player2..only add tile..no display
					//for(int i=x;i<= positionX+1;i++) {
						//for(int j=y;j<=positionY+1;j++) {
							//Tile tile = gameState.getBoard().getTile(i, j) ;
								//if((player1UnitTiles.contains(tile)&&(!(tile==null)))){
									//gameState.getBoard().addHighlightRedTiles(tile);
								//}
						//}
					//}			
				//}
			//}			
	
	//public static void highlightMoveAndAttackTile (ActorRef out,GameState gameState ,Tile tile) {
		
		/*First, add the possible valid move Tile.
		 * Then run through the hashset and call the highlightattacktile for each element
		 * to add the possible valid attack tiles.
		 */
		//highlightMoveTile(out,gameState,tile);
		//for(Tile i: gameState.getBoard().getHighlightedWhiteTiles()) {	
			//highlightAttackTile(out,gameState,i);
		//}
	
		//displayWhiteTile (out,gameState.getBoard().getHighlightedWhiteTiles());
		//displayRedTile (out,gameState.getBoard().getHighlightedRedTiles());
	//}
	
	//attack, without counterattack
	//public static void attackUnit (ActorRef out, GameState gameState,Unit unit, Tile target) {

			//Unit attackTarget = target.getUnit();
			//int targetNewHealth = attackTarget.getHealth() - unit.getAttack();
			//BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.attack);
			//try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			//gameState.setUnitHealth(out, attackTarget, targetNewHealth);
			//if(attackTarget instanceof Avatar) {
				//Avatar avatar = (Avatar)attackTarget;
				//avatar.setHealth(targetNewHealth, out);
				//if(gameState.getCurrentPlayer().equals(gameState.getPlayer1())){
				//BasicCommands.setPlayer2Health(out, gameState.getPlayer2());	
				//}else {
				//BasicCommands.setPlayer1Health(out, gameState.getPlayer1());
				//}
			//}
			//unit.setAttacked(true);
	//}
	
	//attack, with counterattack
//	public static void attackUnitWithCounter (ActorRef out, GameState gameState,Unit unit, Tile target) {
		//attackUnit(out,gameState,unit,target);
		//if(target.getUnit().getHealth()>=0) {counterAttack(out,gameState,unit,target);}
		//unit.setAttacked(true);
//}
	
//	public static void counterAttack (ActorRef out, GameState gameState,Unit unit, Tile target){
//		int attackerNewHealth = unit.getHealth() - target.getUnit().getAttack();
//		BasicCommands.playUnitAnimation(out, target.getUnit(), UnitAnimationType.attack);
//		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
//		gameState.setUnitHealth(out, unit, attackerNewHealth);
//		if(unit instanceof Avatar) {
//			Avatar avatar = (Avatar) unit;
//			avatar.setHealth(attackerNewHealth, out);
//			if(gameState.getCurrentPlayer().equals(gameState.getPlayer1())){
//			BasicCommands.setPlayer1Health(out, gameState.getPlayer1());	
//			}else {
//			BasicCommands.setPlayer2Health(out, gameState.getPlayer2());
//			}
//		}
//	}
		

	
	public static void rangeAttackHighLight(ActorRef out,GameState gameState) {
		
		if(gameState.getCurrentPlayer().equals(gameState.getPlayer1())) {	
			for(Tile i : gameState.getBoard().getPlayer2UnitTiles()) {
				gameState.getBoard().addHighlightRedTiles(i);
			}
			//displayRedTile (out,gameState.getBoard().getHighlightedRedTiles());
		}else {
			for(Tile i : gameState.getBoard().getPlayer1UnitTiles()) {
				gameState.getBoard().addHighlightRedTiles(i);
		}
	}
	}
}
	
	//public static void displayWhiteTile (ActorRef out,HashSet<Tile> whiteTiles) {
		
		//for(Tile i : whiteTiles) {
			//BasicCommands.drawTile(out, i, 1);
			//try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		//}
	//}
	//public static void displayRedTile (ActorRef out,HashSet<Tile> redTiles) {
			
			//for(Tile i : redTiles) {
				//BasicCommands.drawTile(out, i, 2);
				//try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
			//}
		
	//}
//}


////method that do both front-end display & back-end unit health
//public static void setUnitHealth(ActorRef out, Unit targetUnit, int health) {
//	targetUnit.setHealth(health, out);
//	BasicCommands.setUnitHealth(out, targetUnit, health);
//	try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//}

////draw the hand in display
//public static void drawHand(ActorRef out, GameState gameState) {
//	int pos=0;	
//	ArrayList<Card> currHand = gameState.getPlayer1().getMyhand().getMyhand();
//	for (Card i:currHand) {
//		BasicCommands.drawCard(out, i, pos++, 0);
//		try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//	}
//}

////delete a card both in front-end (display) and back-end (Hand)
////few things it does: (1)unselect cards: gameState.cardPos=-1 gameState.CardSelected=null
////(2) delete card from player's Hand (back-end)
////(3) delect card from front-end display and shift the remaining cards to the left
//public static void deleteCard(ActorRef out, GameState gameState, int n) {gameState.deleteCard(n);
//	if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {gameState.drawHand(out);;//change the displays:redraw previous card
//		BasicCommands.deleteCard(out, gameState.getCurrentPlayer().getMyhand().getMyhand().size());//change the displays: remove last card (will not be redraw)
//		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//	}
//} 

//play spell cards
	//////////////without animation///////////////////
//	public static void playSpellCard(ActorRef out, GameState gameState, String cardName, Tile currentTileClicked) {
//		//Unit that the spell act on
//		Unit targetUnit = currentTileClicked.getUnit();
//		//play the card: separated by which card is played
//		if(cardName.equals("Truestrike")) {//deal 2 damage to a unit
//			GroupsCommands.setUnitHealth(out, targetUnit, targetUnit.getHealth()-2);
//			BasicCommands.playEffectAnimation(out , BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation) , currentTileClicked);
//			try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//		}
//		if(cardName.equals("Sundrop Elixir")) { //heal a unit by 5
//			int newHealth = targetUnit.getHealth()+5;
//			if (newHealth>targetUnit.getMaxHealth()) { //if >max, set to max
//				GroupsCommands.setUnitHealth(out, targetUnit, targetUnit.getMaxHealth());
//			}else {GroupsCommands.setUnitHealth(out, targetUnit, newHealth);} //if NOT > max, set to health+5
//			BasicCommands.playEffectAnimation(out , BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff) , currentTileClicked);
//			try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//		}
//		if(cardName.equals("Staff of Y'Kir'")) { //avatar attack +=2
//			GroupsCommands.setUnitAttack(out, targetUnit, targetUnit.getAttack()+2);
//			BasicCommands.playEffectAnimation(out , BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation) , currentTileClicked);
//			try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//			spellThief(out, gameState); //Only when player2 plays a spell card, check if opponent(player1) has a Pureblade Enforcer
//		}
//		if(cardName.equals("Entropic Decay")) {  //unit health -> 0
//			GroupsCommands.setUnitHealth(out, targetUnit,0); //need other story: trigger death
//			BasicCommands.playEffectAnimation(out , BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom) , currentTileClicked);
//			try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//			spellThief(out, gameState); //Only when player2 plays a spell card, check if opponent(player1) has a Pureblade Enforcer
//		}
//		//after card played, 
//		//un-hightlight
//		gameState.getBoard().unhighlightRedTiles(out);
//		//remove the card from hand (& update display)
//		gameState.deleteCard(out);
//	}
//
//	//Only works on player1 (when player2 play a spell, buff player1 unit)
//	//Better to modify it to check "does enemy has PureBlade Enforcer" instead of player1 only
//	//(Can it exceed maximum health??)
//	public static void spellThief(ActorRef out, GameState gameState) {
//		ArrayList<Unit> player1Units = gameState.getBoard().getPlayer1Units(); 
//		
//		for(Unit unit: player1Units) {
//			String name = unit.getName();
//			if(name == null) {continue;} //For now, some units do not have name, so use this line to prevent error first
//			if(name.equals("Pureblade Enforcer")) {
//				unit.setAttack(unit.getAttack() + 1);
//				BasicCommands.setUnitAttack(out, unit, unit.getAttack());
//				try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//				unit.setHealth(unit.getHealth() + 1, out);
//				BasicCommands.setUnitHealth(out, unit, unit.getHealth());
//				try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//			} 
//		}
//	}

////method that do both front-end display & back-end unit attack
//public static void setUnitAttack(ActorRef out, Unit targetUnit, int attack) {
//	targetUnit.setHealth(attack, out);
//	BasicCommands.setUnitAttack(out, targetUnit, attack);
//	try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//}

//play unit cards
//public static void playUnitCard(ActorRef out, GameState gameState, Card card, Tile currentTileClicked) {
//	int n;
//	String[] nameWord = card.getCardname().split(" ");
//	Unit unit = null;
//	if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {n=1;}
//	else {n=2;}
//	String unitConfigName = "conf/gameconfs/cards/"+n+"_c_s_"+nameWord[0]+"_"+nameWord[1]+".json";
//	unit = BasicObjectBuilders.loadUnit(unitConfigName, Unit.newid(n), Unit.class);
//	unit.setup(card, out); //do anything needed to init a unit
//
//	//Add the unit to the relevant array
//	gameState.getBoard().addTileAndAvatarToPlayerArray(currentTileClicked, unit, gameState);
//	
//	//Draw out Unit and it's stats on the browser
//	BasicCommands.drawUnit(out, unit, currentTileClicked);
//	try {Thread.sleep(middleSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//	BasicCommands.setUnitAttack(out, unit, unit.getAttack());
//	try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//	BasicCommands.setUnitHealth(out, unit , unit.getHealth());
//	try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//	
//	//Unit ability on summon
//	if(unit.getName().equals("Azure Herald")) {
//		azureHeraldPassive(out,gameState);
//	} 
//	if(unit.getName().equals("Blaze Hound")) {
//		blazeHoundPassive(out,gameState);
//	}
//	
//	/*
//	Can we do something like unit.skillOnSummon();
//	Then, Azure Herald and Blaze Hound are extended from unit.
//	And our code will call their own skill method?
//	
//	Or other ways, such that we do not need to hard code the abilities on summon like the above.
//	*/
//	
//	//Unhighlight Tiles and Delete Cards
//	gameState.getBoard().unhighlightWhiteTiles(out);
//	gameState.deleteCard(out);
//}

////works for player1 only for now
//public static void azureHeraldPassive(ActorRef out, GameState gameState) {
//	Unit player1Avatar = gameState.getBoard().getPlayer1Units().get(0);
//	int value = 0;
//	if(player1Avatar.getHealth() + 3 > player1Avatar.getMaxHealth()) {
//		value = player1Avatar.getMaxHealth();
//	} else {
//		value = player1Avatar.getHealth() + 3;
//	}
//	player1Avatar.setHealth(value, out);
//	gameState.getPlayer1().setHealth(value);
//	BasicCommands.setPlayer1Health(out, gameState.getPlayer1());
//	try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//	BasicCommands.setUnitHealth(out, player1Avatar, player1Avatar.getHealth());
//	try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//}
//
//public static void blazeHoundPassive(ActorRef out, GameState gameState) {
//	gameState.getPlayer1().cardDraw();
//	try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//	gameState.getPlayer2().cardDraw();
//	try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//}


