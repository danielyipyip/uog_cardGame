package structures.basic;

import java.util.ArrayList;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Deck {


	public Deck(int playerID) {
		this.cardID=1;
	}


	//create deck for player 1 & 2
	private void playerDeck(String[] playerCardConf) {
		Card currentCard;
		for(String current: playerCardConf) {
			currentCard = BasicObjectBuilders.loadCard(current, cardID++, UnitCard.class);
			mydeck.add(currentCard);
		}
		
	}
	
	//create deck for player 2
	private void player2Deck(){
		
	}
	
	//when card is drawn, remove from deck
	public void removeCard(){
		
	}
	
	//shuffle the deck
	public void shuffleCard() {
		
	}


	ArrayList<Card> mydeck; //the deck
	int cardID; //is unique to card, start from 1 to 20
	//String arrays to store cards of each deck
	String[] player1CardConf= {
			StaticConfFiles.c_truestrike, StaticConfFiles.c_sundrop_elixir, 
			StaticConfFiles.c_comodo_charger,StaticConfFiles.c_azure_herald, 
			StaticConfFiles.c_azurite_lion, StaticConfFiles.c_fire_spitter, 
			StaticConfFiles.c_hailstone_golem,StaticConfFiles.c_ironcliff_guardian, 
			StaticConfFiles.c_pureblade_enforcer, StaticConfFiles.c_silverguard_knight
			};
	String[] player2CardConf= {
			StaticConfFiles.c_staff_of_ykir, StaticConfFiles.c_entropic_decay, 
			StaticConfFiles.c_blaze_hound, StaticConfFiles.c_bloodshard_golem, 
			StaticConfFiles.c_planar_scout, StaticConfFiles.c_pyromancer, 
			StaticConfFiles.c_rock_pulveriser, StaticConfFiles.c_serpenti, 
			StaticConfFiles.c_windshrike, StaticConfFiles.c_hailstone_golem //10th card: take from deck 1
			};

}
