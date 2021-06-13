package structures.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import Exceptions.WrongPlayerIDException;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * A basic representation of a player's deck
 * can draw/shuffle card
 * @author Dainel Yip
 *
 */

public class Deck {

	public Deck(int playerID) {
		this.cardID=1;
		mydeck = new Stack<Card>();
		//call helper method to create the deck
		if (playerID==1) {playerDeck(player1UnitCardConf, player1SpellCardConf);}
		else if (playerID==2) {playerDeck(player2UnitCardConf, player2SpellCardConf);}
		//exception handling, throw exception OR display text on screen?
		else {throw new WrongPlayerIDException("wrong player ID when creating deck");}
	}

	//create deck for player 1 & 2
	private void playerDeck(String[] UnitCardConf, String[] SpellCardConf) {
		Card currentCard;
		//add unit card
		for(String current: UnitCardConf) {
			currentCard = BasicObjectBuilders.loadCard(current, cardID++, UnitCard.class);
			mydeck.push(currentCard);
		}
		//add spell card
		for(String current2: SpellCardConf) {
			currentCard = BasicObjectBuilders.loadCard(current2, cardID++, SpellCard.class);
			mydeck.push(currentCard);
		}
	}

	//when card is draw, return it & remove top card
	public Card drawCard(){
		return mydeck.pop();
	}

	//shuffle the deck
	public void shuffleCard() {
		Collections.shuffle(mydeck);
	}
	
	public Stack<Card> getDeck(){return mydeck;}
	
	public boolean isEmpty() {return mydeck.empty();}


	private Stack<Card> mydeck; //the deck
	private int cardID; //is unique to card, start from 1 to 20
	//String arrays to store cards of each deck (separate unit & spell card
	//Player 1: 
	String[] player1UnitCardConf= {
			StaticConfFiles.c_comodo_charger, StaticConfFiles.c_hailstone_golem, 
			StaticConfFiles.c_pureblade_enforcer, StaticConfFiles.c_azure_herald, 
			StaticConfFiles.c_silverguard_knight, StaticConfFiles.c_azurite_lion, 
			StaticConfFiles.c_fire_spitter, StaticConfFiles.c_ironcliff_guardian
	};
	String[] player1SpellCardConf={StaticConfFiles.c_truestrike, StaticConfFiles.c_sundrop_elixir};
	//player 2: 
	String[] player2UnitCardConf= {
			StaticConfFiles.c_planar_scout, StaticConfFiles.c_rock_pulveriser, 
			StaticConfFiles.c_pyromancer, StaticConfFiles.c_bloodshard_golem, 
			StaticConfFiles.c_blaze_hound, StaticConfFiles.c_windshrike, 
			StaticConfFiles.c_hailstone_golem,  StaticConfFiles.c_serpenti, //hailstone_golem: take from deck 1
	};
	String[] player2SpellCardConf={StaticConfFiles.c_staff_of_ykir, StaticConfFiles.c_entropic_decay};

}
