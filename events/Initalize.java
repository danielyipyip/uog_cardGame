package events;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CommandDemo;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Hand;

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
		drawHand(out, gameState); //only for player 1
		
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
	
	public void drawHand(ActorRef out, GameState gameState) {
		int pos=0;
		ArrayList<Card> currHand = gameState.getPlayer1().getMyhand().getMyhand();
		for (Card i:currHand) {
			BasicCommands.drawCard(out, i, pos++, 0);
			try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}


