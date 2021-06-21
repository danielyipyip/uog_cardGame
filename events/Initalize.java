package events;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.GroupsCommands;
import demo.CommandDemo;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Board;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import structures.basic.Card;
import structures.basic.Hand;
import structures.basic.Position;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Initalize implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		//CommandDemo.executeDemo(out); // this executes the command demo, comment out this when implementing your solution
		GroupsCommands.setUpPlayerHealthMana(out, gameState);
		displayTiles(out,gameState.getBoard());
		drawHand(out, gameState); //only for player 1
		displayPlayer1Avatar(out,gameState);
		displayPlayer2Avatar(out,gameState);
		
		
		//delete later
		//Player 1 Unit
		Unit fire_spitter = BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, 2, Unit.class);
		Tile tile = gameState.getBoard().getTile(3, 3);
		gameState.getBoard().addTileAndAvatarToPlayerArray(tile, gameState.getBoard().getPlayer1UnitTiles(), fire_spitter);
		BasicCommands.drawUnit(out, fire_spitter, tile);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, fire_spitter, 2);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, fire_spitter , fire_spitter.getHealth());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}

		//Player2 Unit
		Unit fire_spitter1 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, -2, Unit.class);
		Tile tile1 = gameState.getBoard().getTile(4, 2);
		fire_spitter1.setHealth(10);
		gameState.getBoard().addTileAndAvatarToPlayerArray(tile1, gameState.getBoard().getPlayer2UnitTiles(), fire_spitter1);
		BasicCommands.drawUnit(out, fire_spitter1, tile1);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, fire_spitter1, 2);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, fire_spitter1 , fire_spitter1.getHealth());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}

	//helper methods	
	public void displayTiles(ActorRef out, Board board) {//Method to display Tiles
		
		int x = board.getX();
		int y = board.getY();
		
		for(int i=0; i<x; i++) {
			
			for(int j=0; j<y;j++) 
				{
				Tile tile = board.getTile(i, j);
				BasicCommands.drawTile(out, tile, 0);
				try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
				}			
		}
	}
	
	//drawHand means drawing out the hand
	public void drawHand(ActorRef out, GameState gameState) {
		int pos=0;
		ArrayList<Card> currHand = gameState.getPlayer1().getMyhand().getMyhand();
		for (Card i:currHand) {
			BasicCommands.drawCard(out, i, pos++, 0);
			try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	
	public void displayPlayer1Avatar(ActorRef out, GameState gameState) {
		int player1XPosition = gameState.getBoard().getPlayer1Avatar().getPosition().getTilex();
		int player1YPosition = gameState.getBoard().getPlayer1Avatar().getPosition().getTiley();
		Unit player1Avatar = gameState.getBoard().getPlayer1Avatar();
		int health = gameState.getPlayer1().getHealth();
		int attack = player1Avatar.getAttack();
		Tile playerTile =  gameState.getBoard().getTile(player1XPosition, player1YPosition);
		BasicCommands.drawUnit(out,player1Avatar,playerTile);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, player1Avatar, attack);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, player1Avatar, health);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
		public void displayPlayer2Avatar(ActorRef out, GameState gameState) {
			int player2XPosition = gameState.getBoard().getPlayer2Avatar().getPosition().getTilex();
			int player2YPosition = gameState.getBoard().getPlayer2Avatar().getPosition().getTiley();
			Unit player2Avatar = gameState.getBoard().getPlayer2Avatar();
			int health = gameState.getPlayer2().getHealth();
			int attack = player2Avatar.getAttack();
			Tile playerTile =  gameState.getBoard().getTile(player2XPosition, player2YPosition);
			BasicCommands.drawUnit(out,player2Avatar,playerTile);
			try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.setUnitAttack(out, player2Avatar, attack);
			try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.setUnitHealth(out, player2Avatar, health);
			try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		}
}


