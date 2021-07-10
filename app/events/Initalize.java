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
		
		//initial set up player 1 & 2 initial health and mana
		BasicCommands.setPlayer1Health(out, gameState.getPlayer1());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer2Health(out, gameState.getPlayer2());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer1Mana(out, gameState.getPlayer1());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer2Mana(out, gameState.getPlayer2());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		
		displayTiles(out,gameState.getBoard()); //draw the board
		gameState.drawHand(out);//draw the hand only for player 1
		gameState.getBoard().addPlayer1Avatar(1,2,gameState); //Construct player1
		displayPlayerAvatar(out,gameState,gameState.getBoard().getPlayer1Avatar()); //display player 1
		gameState.getBoard().addPlayer2Avatar(7,2,gameState); //Construct player2
		displayPlayerAvatar(out,gameState,gameState.getBoard().getPlayer2Avatar());//display player2

		//==========================Below units is just for testing purpose===================================================//
		//delete later
		//Player 1 Unit
//		Unit fire_spitter = BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, 99, Unit.class);
//		fire_spitter.setup(BasicObjectBuilders.loadCard(StaticConfFiles.c_fire_spitter, 99, Card.class));
//		fire_spitter.initSetHealth(2);
//		Tile tile = gameState.getBoard().getTile(2, 0);
//		fire_spitter.setHealth(2, out);
//		fire_spitter.initSetAttack(2);
//		gameState.getBoard().addTileAndAvatarToPlayerArray(tile, fire_spitter, gameState);
//		BasicCommands.drawUnit(out, fire_spitter, tile);
//		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//		BasicCommands.setUnitAttack(out, fire_spitter, 2);
//		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//		BasicCommands.setUnitHealth(out, fire_spitter , fire_spitter.getHealth());
//		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//
//		//Player2 Unit
//		Unit fire_spitter1 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, -99, Unit.class);
//		fire_spitter1.setup(BasicObjectBuilders.loadCard(StaticConfFiles.c_fire_spitter, -99, Card.class));
//		fire_spitter1.initSetHealth(2);
//		Tile tile1 = gameState.getBoard().getTile(2, 2);
//		fire_spitter1.setHealth(2, out);
//		fire_spitter1.initSetAttack(2);
//		fire_spitter1.setPositionByTile(tile1);
//		tile1.setUnit(fire_spitter1);
//		gameState.getBoard().getUnitOccupiedTiles().add(tile1);	
//		gameState.getBoard().getPlayer2Units().add(fire_spitter1);
//		gameState.getBoard().getPlayer2UnitTiles().add(tile1);
//	
//		BasicCommands.drawUnit(out, fire_spitter1, tile1);
//		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//		BasicCommands.setUnitAttack(out, fire_spitter1, 2);
//		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
//		BasicCommands.setUnitHealth(out, fire_spitter1 , fire_spitter1.getHealth());
//		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}}
		//================================Testing purpose end===================================================
	
	}
	//helper methods	
	
	//Method to draw all Tiles
	//max x, y is stored in board class
	public void displayTiles(ActorRef out, Board board) {
		for(int i=0; i<board.getX(); i++){ 
			for(int j=0; j<board.getY();j++){
				BasicCommands.drawTile(out, board.getTile(i, j), 0);
				try{Thread.sleep(drawTileSleepTime);}catch(InterruptedException e){e.printStackTrace();}
			}	
		}
	}
	
	/////////////////////seeems likely need refactor////////////////////////////
	//---Combine two methods into one-----//
	/*public void displayPlayer1Avatar(ActorRef out, GameState gameState) {
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
	}*/
		
	public void displayPlayerAvatar(ActorRef out, GameState gameState, Avatar avatar) {
		int x  = avatar.getPosition().getTilex();
		int y = avatar.getPosition().getTiley();
		int health = avatar.getHealth();
		int attack = avatar.getAttack();
		Tile avatarTile = gameState.getBoard().getTile(x, y);
		BasicCommands.drawUnit(out,avatar,avatarTile);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, avatar, attack);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, avatar, health);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}
				
}
		



