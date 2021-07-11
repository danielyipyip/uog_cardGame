package structures.basic;

import java.util.HashSet;
import java.util.Set;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.TileClicked;
import structures.GameState;

/**
 * AI player
 * planning to put AI move methods in here
 * draw 3 cards when deck is created
 * 
 * @author daniel
 *
 */

public class AIPlayer extends Player{

	public AIPlayer() {
		super();
	}

	public AIPlayer(int health, int mana) {
		super(health, mana);
		//deck is created with player
		mydeck = new Deck(2);
		//card shuffle when deck is created
		mydeck.shuffleCard(); 
		//draw 3 cards when deck is created
		for (int i=0; i<3; i++) {this.cardDraw();}
	}

	@Override
	//no need to draw on dislplay
	public void cardDraw(ActorRef out) {super.cardDraw();}

	@Override
	//no draw hand on dislplay
	public void drawHand(ActorRef out) {;}

	@Override
	//card only delete from Hand, not display
	public void removeCard(ActorRef out, int n) {myhand.removeCard(n);}

	//	@Override
	//	public void playCard(ActorRef out, GameState gameState, Card card, Tile currentTileClicked) {
	//		card.playCard(out, gameState, currentTileClicked);
	//	}

	/////////// AI decide what card to play //////////////
	@Override
	public void playCard(ActorRef out, GameState gameState, Card card) {
		Tile targetTile=null;
		if(card instanceof SpellCard) {
			Set<Tile> targetTileSet = gameState.getBoard().getHighlightedRedTiles();
			targetTile = pickSpellTarget(card, targetTileSet, gameState);
		}else if(card instanceof UnitCard) {
			Set<Tile> targetTileSet = gameState.getBoard().getHighlightedWhiteTiles();
			targetTile = pickUnitPlayTarget(card, targetTileSet, gameState);
		}
		if (targetTile==null) {
			BasicCommands.addPlayer1Notification(out, "Oops"+card.getCardname(), 2);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
			for (Tile i:gameState.getBoard().getHighlightedWhiteTiles()) {
				BasicCommands.addPlayer1Notification(out, "Oops", 2);
				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				BasicCommands.addPlayer1Notification(out, ""+i.getTilex() + i.getTiley(), 2);
				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		TileClicked.playCardOnTile(out, gameState,card, targetTile);
	}

	public Tile pickSpellTarget(Card card, Set<Tile> targetTile, GameState gameState) {
		if (card.getCardname().equals("Staff of Y'Kir'")) {
			for (Tile i: targetTile) {return i;}//always return player 2 avatar
		}else if (card.getCardname().equals("Entropic Decay")) {
			Tile tile = null;
			for (Tile i: targetTile) {
				if (tile==null) {tile=i;}
				else {//aim for higher health unit
					if (tile.getUnit().getHealth()<i.getUnit().getHealth() ) {tile=i;} 
				}
			}
			return tile;
		}
		return null;
	}

	public Tile pickUnitPlayTarget(Card card, Set<Tile> targetTile, GameState gameState) {
		Tile tile = null;
		Tile player1Tile = gameState.getBoard().getPlayer1UnitTiles().get(0);
		for (Tile i: targetTile) {
			if (i!=null) {
				if (tile==null) {tile=i;}
				else {//aim for tile closer to avatar
					if (tile.absdiff(player1Tile)>i.absdiff(player1Tile) ) {tile=i;} 
				}
			}
			return tile;
		}
		return null;
	}

	/////////// AI decide unit move //////////////
	//	@Override
	//	public void moveUnit(ActorRef out, Unit unit, Tile targetTile) {
	//		unit.moveUnit(out, targetTile);
	//	}



	@Override
	public void displayWhiteTile (ActorRef out,GameState gameState) {

	}
	@Override
	public void displayRedTile(ActorRef out,GameState gameState) {

	}
	@Override
	public void displayAllTiles (ActorRef out,GameState gameState) {

	}

	@Override
	public void lose(ActorRef out) {
		BasicCommands.addPlayer1Notification(out, "I Win", 2);
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
	}

}
