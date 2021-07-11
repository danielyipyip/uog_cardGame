package structures;


import java.util.ArrayList;
import java.util.HashMap;

import Exceptions.AvatarException;
import Exceptions.DontPlayThisCardException;
import Exceptions.UnitDieException;

import java.util.*;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.CardClicked;
import events.EndTurnClicked;
import events.EventProcessor;
import structures.basic.*;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {
	//instance variables
	int turn;
	Player player1;
	Player player2;
	Board board;

	/*cardSelected = x when card x is clicked
	 *cardPos = n when card on hand position n is clicked
	 *if not selecting anyCard, n=-1 */
	Card cardSelected; 
	int cardPos; 
	Tile tileClicked; //to store the previous clicked tile;
	Unit unitClicked;
	Player currentPlayer;
	//Boolean unitMoving;

	int sleepTime = EventProcessor.sleepTime;
	int middleSleepTime = EventProcessor.middleSleepTime;
	int shortSleepTime = EventProcessor.shortSleepTime;
	int longSleepTime = EventProcessor.longSleepTime;

	//constructor
	public GameState() { //is an object hold by GameActor
		this.turn=1; //start of game, turn =1

		//create new players
		//starting mana =2 (turn 1)
		player1 = new HumanPlayer(20,2);
		player2 = new AIPlayer(20,2);
		board = new Board();
		tileClicked= null;
		//Default cardSelected is null and cardPos is -1
		cardSelected = null; 
		cardPos = -1;

		//start from player 1
		currentPlayer=player1;

		//unitMoving = false;
	}

	//helper method
	//just to group 2 steps in once
	public void unSelectCard() {this.setcardPos(-1); this.setCardSelected(null);} //unselect the card

	//Getter and Setter Method
	public int getTurn() {return turn;}
	public void setTurn(int turn) {this.turn = turn;}
	public Player getPlayer1() {return player1;}
	public Player getPlayer2() {return player2;}
	public Board getBoard() {return board;}
	public void setCardSelected(Card cardClicked) {this.cardSelected = cardClicked;}
	public Card getCardSelected() {return cardSelected;}
	public void setcardPos(int pos) {this.cardPos = pos;}

	public Tile getTileClicked() {return tileClicked;}
	public void setTileClicked(Tile tileClicked) {this.tileClicked = tileClicked;}

	/*public void switchUnitMoving() {
		this.unitMoving = !this.unitMoving;
	}
	public boolean getUnitMoving() {
		return this.unitMoving;
	}*/

	public int getcardPos() {return cardPos;}	
	public Unit getUnitClicked() {return unitClicked;}
	public void setUnitClicked(Unit unitClicked) {this.unitClicked = unitClicked;}
	public Player getCurrentPlayer() {return currentPlayer;}
	public void setCurrentPlayer(Player currentPlayer) {this.currentPlayer = currentPlayer;}

	////////////// cards related /////////////////
	//draw hand (of cards) on display
	public void drawHand(ActorRef out) {currentPlayer.drawHand(out);}

	//delete a card both in front-end (display) and back-end (Hand)
	//few things it does: (1)unselect cards: gameState.cardPos=-1 gameState.CardSelected=null
	//(2) delete card from player's Hand (back-end)
	//(3) delect card from front-end display and shift the remaining cards to the left
	public void deleteCard(ActorRef out) {
		this.currentPlayer.removeCard(out, cardPos); //(2,3)
		this.unSelectCard(); //(1)
	} 

	//play a card: go to player then card
	public void playCard(ActorRef out, GameState gameState, Card card, Tile currentTileClicked) {	
		currentPlayer.playCard(out, gameState, card, currentTileClicked);
		currentPlayer.setMana(currentPlayer.getMana()-card.getManacost());
		if (gameState.getCurrentPlayer()==gameState.getPlayer1()) {
			BasicCommands.setPlayer1Mana(out, gameState.getPlayer1());
		}else {
			BasicCommands.setPlayer2Mana(out, gameState.getPlayer2());
		}try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		//after card played, 
		//un-hightlight
		gameState.getBoard().unhighlightRedTiles(out);
		//Unhighlight Tiles and Delete Cards
		gameState.getBoard().unhighlightWhiteTiles(out);
		//remove the card from hand (& update display)
		gameState.deleteCard(out);
	}

	///////////////////unit related/////////////////

	//other than 4 things to remove when a unit died, 2 more things
	//(5)show dead animation (6)remove from front end display 
	public void setUnitHealth(ActorRef out, Unit unit, int newHealth) {
		try{unit.setHealth(newHealth, out);} //will 
		catch(UnitDieException e) {
			//add death trigger for "WindShrike"
			unit.deathTrigger(currentPlayer, out);
			this.getBoard().removeUnit(unit);
		}
		catch(AvatarException f) {
			Avatar avatar = (Avatar) unit; 
			silverGuardKnightPassive(out, this, avatar);
			if(avatar.getPlayer()==this.getPlayer1()) {
				BasicCommands.setPlayer1Health(out, this.getPlayer1());
				try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
			}else {
				BasicCommands.setPlayer2Health(out, this.getPlayer2());
				try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}

	public void setUnitAttack(ActorRef out, Unit targetUnit, int attack) {
		targetUnit.setAttack(attack, out);
	}

	/////////////////unit action related (unit move/attack) ///////////////////
	public void moveUnit(ActorRef out,  Unit unit, Tile targetTile) {
		int player;
		if (this.getCurrentPlayer().equals(this.getPlayer1())){player=1;}
		else {player=2;}
		//(1) get unit's tile b4 moving; swap unit's associated tile to new tile
		Tile previousTile = this.getTileClicked();
		previousTile.setUnit(null);
		targetTile.setUnit(unit);
		//(2) change player1/2UnitTiles & unitOccupiedTiles
		this.getBoard().removeUnit(previousTile);
		this.getBoard().addUnit(targetTile, player);
		//(3) un-hightlight tiles
		this.getBoard().unhighlightWhiteTiles(out);
		this.getBoard().unhighlightRedTiles(out);

		this.getCurrentPlayer().moveUnit(out, unit, targetTile);
		//(6) set seleted unit to null & pos to -1
		this.unSelectCard();
	}


	//////////////highlight related/////////////////
	public boolean ablePlayOrMove(Tile tile) {
		return this.getBoard().getHighlightedWhiteTiles().contains(tile);
	}




	//unhighlight card and set instance variables to default value
	public void unHighlightCard(ActorRef out) {
		BasicCommands.drawCard(out, cardSelected, cardPos, 0);
		unSelectCard();
	}
	public int currentPlayer() {
		if (this.getCurrentPlayer()==this.getPlayer1()) {return 1;}
		else {return 2;}
	}

	//play AI Turn
	public void playAITurn(ActorRef out) {
		//Move and Attack
		Set<Tile> targetTiles;
		Tile targetTile;
//		if (this.getBoard().getPlayer2UnitTiles()==null) {
//			BasicCommands.addPlayer1Notification(out, "Unit Tiles is null", 2);
//			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
//		}
//		if (this.getBoard().getPlayer2Units()==null) {
//			BasicCommands.addPlayer1Notification(out, "Units is null", 2);
//			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
//		}
//		for(Tile i:this.getBoard().getPlayer2UnitTiles()) {
//			if (i==null) {
//				BasicCommands.addPlayer1Notification(out, "sth is null", 2);
//				try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
//			}
//		}
		for(int j = 0; j<this.getBoard().getPlayer2UnitTiles().size(); j++) {
			Tile i = this.getBoard().getPlayer2UnitTiles().get(j);
			Unit currUnit = i.getUnit();
			this.setUnitClicked(currUnit);
			this.setTileClicked(this.getBoard().unit2Tile(currUnit));
			//if can attack then attack la
			currUnit.highlightAttackTile(this, i);
			if ((targetTiles=this.getBoard().getHighlightedRedTiles()).size()!=0) {
				//choose which one to atk
				targetTile = pickAttackTile(currUnit, targetTiles);
				currUnit.attackUnit(out, this, currUnit, targetTile);
			} //move (maybe atk afterward)
			else{
				currUnit.highlightMoveTile(this, i);
				if ((targetTiles=this.getBoard().getHighlightedWhiteTiles()).size()!=0) {
					//choose which one to atk
					targetTile = pickMoveTile(currUnit, targetTiles);
					this.moveUnit(out, currUnit, targetTile);
				}
				if ((targetTiles=this.getBoard().getHighlightedRedTiles()).size()!=0) {
					//choose which one to atk
					targetTile = pickAttackTile(currUnit, targetTiles);
					currUnit.attackUnit(out, this, currUnit, targetTile);
				} //move (maybe atk afterward)
			}
			board.unhighlightWhiteTiles(out);
			board.unhighlightRedTiles(out);
		}

		//Play cards		
		for(int j=0;j< this.getCurrentPlayer().getHand().size();j++) {
			//		(Card i: this.getCurrentPlayer().getHand()) {
			Card i=this.getCurrentPlayer().getHand().get(j);
			this.setcardPos(j); //set which card is selected so can delete

			//just add here so not play too fast
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			//if not enough mana, skip this card
			if(player2.getMana() < i.getManacost()) {continue;}

			//highlight available tiles
			CardClicked.cardHighlightTiles(out, this, i);
			try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}

			boolean haveValidTarget=true;
			//check is there valid target
			if(i instanceof SpellCard) {
				if (board.getHighlightedRedTiles().size()==0) {haveValidTarget=false; continue;} //no valid target -> next card
			}else if(i instanceof UnitCard) {
				if (board.getHighlightedWhiteTiles().size()==0) {haveValidTarget=false; continue;} //no valid target -> next card
			}
			//play according to logic
			try {
			if (haveValidTarget) {
				this.getCurrentPlayer().playCard(out, this, i);
				try {Thread.sleep(longSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
			}
			}catch (DontPlayThisCardException e) {continue;} //just skip this round
			board.unhighlightWhiteTiles(out);
			board.unhighlightRedTiles(out);
			try {Thread.sleep(shortSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		}

		//highlightMoveTile
		//highlightAttackTile

		//		for(Tile i)
		//		List<Unit> player2Units = this.getBoard().get


		//Pass the turn to player again
		EndTurnClicked endTurn = new EndTurnClicked();
		endTurn.processEvent(out, this, null);
	}


	public Tile pickAttackTile(Unit currUnit, Set<Tile> targetTiles) {
		Tile targetTile=null;
		for(Tile j:targetTiles) {
			targetTile=j;
			//prioritize avatar -> can kill -> random (last of list)
			if (j.getUnit() instanceof Avatar) {targetTile=j; return targetTile;}
			if (j.getUnit().getHealth()<=currUnit.getAttack()) {targetTile=j;return targetTile;}
		}
		return targetTile;
	}
	
	public Tile pickMoveTile(Unit currUnit, Set<Tile> targetTiles) {
	Tile targetTile=null;
	Tile player1Tile = this.getBoard().getPlayer1UnitTiles().get(0);
	for(Tile j:targetTiles) {
		if (targetTile==null) {targetTile=j;}
		//prioritize avatar -> can kill -> random (last of list)
		if (targetTile.absdiff(player1Tile)>j.absdiff(player1Tile) ) {targetTile=j;} 
	}
	return targetTile;
}
	
	//Silverguard knight +attack when avatar is damaged
	public void silverGuardKnightPassive(ActorRef out, GameState gameState, Avatar avatar) {
		ArrayList<Unit> playerUnitArray;
		ArrayList<Unit> player1Units = gameState.getBoard().getPlayer1Units(); 
		ArrayList<Unit> player2Units = gameState.getBoard().getPlayer2Units();
		if(avatar.getId() == 1) { playerUnitArray = player1Units; } else { playerUnitArray = player2Units; }
				
		for(Unit unit: playerUnitArray) {
			String name = unit.getName();
			if(name == null) {continue;} 
			if(name.equals("Silverguard Knight")) {
				unit.setAttack(unit.getAttack() + 2, out);
			} 
		}
	}

}




//other than 4 things to remove when a unit died, 2 more things
//(5)show dead animation (6)remove from front end display 
//public void setUnitHealth(ActorRef out, Unit unit, int newHealth) {
//	int pos;
//	//unit died
//	if (newHealth<=0) {
//		//only one of the indexOf will find
//		pos=board.getPlayer1Units().indexOf(unit); 
//		pos=board.getPlayer2Units().indexOf(unit);
//		//unit died
//		if (pos!=-1) {
//			board.removeUnit(unit); //(1-4)
//			//(5)
//			BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death); 
//			try {Thread.sleep(EventProcessor.sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//			//(6) just redraw empty tile?
//			BasicCommands.drawTile(out, board.unit2Tile(unit), 0);
//			try {Thread.sleep(EventProcessor.sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//		} //avatar died
//		else{
//			//add win/lose logic
//		}
//	}
//	//unit NOT died, just do normally
//	else {unit.setHealth(newHealth);} 
//}