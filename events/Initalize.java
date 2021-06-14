package events;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CommandDemo;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
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
		setUpPlayerHealthMana(out, gameState);
		displayTiles(out,gameState.getBoard());
		drawHand(out, gameState); //only for player 1
		displayPlayer1Avatar(out,gameState);
		displayPlayer2Avatar(out,gameState);
		
	}

	//helper methods
	public void setUpPlayerHealthMana(ActorRef out, GameState gameState) {
		BasicCommands.setPlayer1Health(out, gameState.getPlayer1());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer2Health(out, gameState.getPlayer2());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer1Mana(out, gameState.getPlayer1());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setPlayer2Mana(out, gameState.getPlayer2());
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
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
		
	
	
	


	public void drawHand(ActorRef out, GameState gameState) {
		int pos=0;
		ArrayList<Card> currHand = gameState.getPlayer1().getMyhand().getMyhand();
		for (Card i:currHand) {
			BasicCommands.drawCard(out, i, pos++, 0);
			try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	
	public void displayPlayer1Avatar(ActorRef out, GameState gameState) {
		int player1XPosition = gameState.getPlayer1Position().getTilex();
		int player1YPosition = gameState.getPlayer1Position().getTiley();
		Unit player1Avatar = gameState.getBoard().getPlayer1Avatar();
		int health = gameState.getPlayer1().getHealth();
		Tile playerTile =  gameState.getBoard().getTile(player1XPosition, player1YPosition);
		BasicCommands.drawUnit(out,player1Avatar,playerTile);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, player1Avatar, 2);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, player1Avatar, health);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	
	public void displayPlayer2Avatar(ActorRef out, GameState gameState) {
		int player2XPosition = gameState.getPlayer2Position().getTilex();
		int player2YPosition = gameState.getPlayer2Position().getTiley();
		Unit player2Avatar = gameState.getBoard().getPlayer2Avatar();
		int health = gameState.getPlayer2().getHealth();
		Tile playerTile =  gameState.getBoard().getTile(player2XPosition, player2YPosition);
		BasicCommands.drawUnit(out,player2Avatar,playerTile);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, player2Avatar, 2);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, player2Avatar, health);
		try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	
}



