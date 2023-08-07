package java.ch.epfl.cs107.play.game.ARPG.actor;


import java.util.HashMap;
import java.util.Map;

import ch.epfl.cs107.play.game.ARPG.Item.InventoryItem;


public class Inventory {
	public float maxWeight ;
	private  Map<InventoryItem, Integer> items;
	public InventoryItem item;
	public float actualWeight;

	public Inventory(int maxWeight) {
		actualWeight = 0;
		items = new HashMap<InventoryItem, Integer>();
		this.maxWeight = maxWeight;


	}
	// permet d'ajouter un item a l'inventaire
	protected boolean addItem(InventoryItem item, int quantite) {
		boolean canAdd;
		if(actualWeight + item.getLife()*quantite <= maxWeight) {
			actualWeight+=item.getLife()*quantite;
			canAdd = true;
			if(items.containsKey(item)) {
				items.put(item, quantite+items.get(item));
			}else {
				items.put(item, quantite);
			}
		}else {
			canAdd = false;
		}
		return canAdd;


	}
	//permet de supprimer un item de l'inventaire qui est utilise
	protected boolean deleteItem(InventoryItem item , int quantite) {
		boolean canDelete;
		if(items.containsKey(item) && items.get(item) >= quantite) {
			items.put(item, items.get(item)-quantite);
			canDelete = true;
		}else {
			canDelete = false;
		}
		return canDelete;
	}
	// permet de savoir si l'inventaire contient l'item demande
	protected boolean haveThisItem(ARPGItem item) {
		return items.containsKey(item) && items.get(item) > 0;

	}
}

