package structures.basic;

public class SpellCard extends Card{
	
	public SpellCard(){super();};
	
	//nothing new?
	public SpellCard(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard) {
		super(id, cardname, manacost, miniCard, bigCard);
	}
}
