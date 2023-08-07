package java.ch.epfl.cs107.play.game.ARPG.actor;

import ch.epfl.cs107.play.game.ARPG.Item.InventoryItem;
import ch.epfl.cs107.play.game.ARPG.actor.Inventory;

public class ARPGInventory extends Inventory {

	private int money;
	private int valInventory;
	private int fortune = money + valInventory;
	
	public ARPGInventory(int maxWeight,int money, int fortune) {
		super(maxWeight);
		this.money = money;
		this.fortune = fortune;
	}
	public void addMoney(int prime) {
		 money += prime;
	}
	public int getmoney() {
		return this.money;
	}
	public int getFortune() {
		return fortune; 
	}
	//permet d'ajouter un item a l'inventaire
	@Override
	public boolean addItem(InventoryItem item, int quantite) {
		if(super.addItem(item, quantite)) {
			fortune += item.getPrix() * quantite;
			return true;
		}else {
			return false;
		}

	}
	//permet de supprimer l'item utilise de l'inventaire
	@Override 
	public boolean deleteItem(InventoryItem item, int quantite) {
		if(super.deleteItem(item, quantite)) {
			fortune -= item.getPrix() * quantite;
			return true;
		}else {
			return false;
		}
	}
	@Override
	public boolean haveThisItem(ARPGItem item) {
		return super.haveThisItem(item);
		}
	

}
