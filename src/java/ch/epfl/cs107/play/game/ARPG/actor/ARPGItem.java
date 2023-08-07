package java.ch.epfl.cs107.play.game.ARPG.actor;

import ch.epfl.cs107.play.game.ARPG.Item.InventoryItem;

public enum ARPGItem implements InventoryItem{

	Arrow("zelda/arrow.icon", 1 , 1),
	Sword("zelda/sword.icon", 1 , 1),
	Bow("zelda/bow.icon", 1 , 1),
	Bomb("zelda/bomb", 1 , 1),
	Staff("zelda/staff_water.icon", 1 , 1),
	CastleKey("zelda/key", 1 , 1);


	final int prix;
	final float life;
	public final String name;

	ARPGItem(String name , int prix , float life) {
		this.name = name;
		this.prix = prix;
		this.life = life;

	}
	//donne le nom de l'item courant
	@Override
	public String getName() {
		return this.name;
	}
	//donne le poids l'item courant 
	@Override
	public float getLife() {
		return this.life;
	}
	//donne le prix de l'item courant
	@Override
	public int getPrix() {
		return this.prix;
	}

}
