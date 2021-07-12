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
		



