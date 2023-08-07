package java.ch.epfl.cs107.play.game.ARPG.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGPlayerStatusGUI implements Graphics{
	private static final float DEPTH = 1001;
	private final int pointdevie = 5;
	private float life;
	private ARPGInventory inventory;
	private ARPGItem currentItem ;
	private int x;
	private int y;
	private int money;
	private int firstDigit;
	private int secondDigit;
	private int thirdDigit;

	//donne la region d'interet qui permet de prendre le numero correspondant au montant d'argent
	public int getRegionX(int i) {
		switch(i) {
		case 0 : 
			x = 16;
			break;
		case 1 : 
			x = 0;
			break;
		case 2 : 
			x = 16;
			break;
		case 3 : 
			x = 32;
			break;
		case 4 :
			x = 48;
			break;
		case 5 :
			x = 0;
			break;
		case 6 : 
			x = 16;
			break;
		case 7 : 
			x = 32;
			break;
		case 8 : 
			x = 48;
			break;
		case 9 : 
			x = 0;
			break;
		}
		return x;
	}
	//donne la region d'interet qui permet de prendre le numero correspondant au montant d'argent
	public int getRegionY(int i) {
		switch(i) {
		case 0 : 
			y = 32;
			break;
		case 1 : 
			y = 0;
			break;
		case 2 : 
			y = 0;
			break;
		case 3 : 
			y = 0;
			break;
		case 4 :
			y = 0;
			break;
		case 5 :
			y = 16;
			break;
		case 6 : 
			y = 16;
			break;
		case 7 : 
			y = 16;
			break;
		case 8 : 
			y = 16;
			break;
		case 9 : 
			y = 32;
			break;
		}
		return y;
	}

	public ARPGPlayerStatusGUI(float life, ARPGItem currentItem , ARPGInventory currentInventory) {
		this.currentItem =currentItem;
		setLife(life);
		setItem(currentItem);
		setCurrentInventory(currentInventory);
	}
//permet de dessiner la pastille marron en haut gauche d l'ecran et l'item courant 
	private void gearGraphics(Canvas canvas , Vector anchor , float width , float height) {
		ImageGraphics gear = new ImageGraphics(ResourcePath.getSprite("zelda/gearDisplay"), 2f, 2f,
				new RegionOfInterest(0, 0, 32, 32), anchor.add(new Vector(0, height - 2f)), 1, DEPTH);
		gear.draw(canvas);
		if(currentItem != null && currentItem.equals(ARPGItem.Bomb)) {
			ImageGraphics item = new ImageGraphics(ResourcePath.getSprite(currentItem.getName()), 1f, 1f,
					new RegionOfInterest(0, 0, 16, 16), anchor.add(new Vector(0.5f, height - 1.5f)), 1, DEPTH);
			item.draw(canvas);
		}else
			if(currentItem != null) {
				ImageGraphics item = new ImageGraphics(ResourcePath.getSprite(currentItem.getName()), 1f, 1f,
						new RegionOfInterest(0, 0, 32, 32), anchor.add(new Vector(0.5f, height - 1.5f)), 1, DEPTH);
				item.draw(canvas);
			}else {
				gear.draw(canvas);
			}
	}
	//permet de dessiner la vie du personnage
	private void heartGraphics(Canvas canvas , Vector anchor , float width , float height) {
		float var = 2f;
		float life = getLife();
		for(int i = 0 ; i < pointdevie ; ++i) {
			if(life >= 1f) {
				ImageGraphics heart1 = new ImageGraphics(ResourcePath.getSprite("zelda/heartDisplay"), 2f, 2f,
						new RegionOfInterest(32, 0, 16, 16), anchor.add(new Vector(var, height - 2f)), 1, DEPTH);
				heart1.draw(canvas);
				var = var + 2f;
				life -= 1;
			}else
				if(life > 0f && life < 1f) {
					ImageGraphics heart2 = new ImageGraphics(ResourcePath.getSprite("zelda/heartDisplay"), 1.75f, 1.75f,
							new RegionOfInterest(16, 0, 16, 16), anchor.add(new Vector(var, height - 2f)), 1, DEPTH);
					heart2.draw(canvas);
					var = var + 2f;
					life = 0;
				}
				else {
					ImageGraphics heart3 = new ImageGraphics(ResourcePath.getSprite("zelda/heartDisplay"), 1.5f, 1.5f,
							new RegionOfInterest(0, 0, 16, 16), anchor.add(new Vector(var, height - 2f)), 1, DEPTH);
					heart3.draw(canvas);
					var = var + 2f;
				}
		}
	}
	// permet d'afficher le montant d'argent
	private void moneyGraphics(Canvas canvas , Vector anchor , float width , float height) {
		ImageGraphics coin = new ImageGraphics(ResourcePath.getSprite("zelda/coinsDisplay"), 6f, 3f,
				new RegionOfInterest(0, 0, 64, 32), anchor.add(new Vector(0, 0)), 1, DEPTH);
		coin.draw(canvas);
		
		money = inventory.getmoney();
		firstDigit = money / 100;
		secondDigit = (money - firstDigit*100)/10  ;
		thirdDigit = money - firstDigit*100 - secondDigit*10 ;
		ImageGraphics digit1 = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.5f, 1.5f, 
							new RegionOfInterest(getRegionX(firstDigit),getRegionY(firstDigit), 16, 16), 
							anchor.add(new Vector( 2.1f, 0.75f)),1, DEPTH);
		ImageGraphics digit2 = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.5f, 1.5f, 
				new RegionOfInterest(getRegionX(secondDigit),getRegionY(secondDigit), 16, 16), 
				anchor.add(new Vector( 3.1f, 0.75f)),1, DEPTH);
		ImageGraphics digit3 = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.5f, 1.5f, 
				new RegionOfInterest(getRegionX(thirdDigit),getRegionY(thirdDigit), 16, 16), 
				anchor.add(new Vector( 4.1f, 0.75f)),1, DEPTH);
		digit1.draw(canvas);
		digit2.draw(canvas);
		digit3.draw(canvas); 
	}

	public void draw(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2, height/2));
		gearGraphics(canvas,anchor, width , height);
		heartGraphics(canvas, anchor, width , height);
		moneyGraphics(canvas, anchor , width , height);
	}
	public void setLife(float life) {
		this.life = life;
	}
	public void setCurrentInventory(ARPGInventory currentInventory) {
		this.inventory = currentInventory; 
	}
	public void setItem(ARPGItem currentItem) {
		this.currentItem = currentItem;
	}
	public float getLife() {
		return life;
	}
	public ARPGInventory getCurrentInventory() {
		return inventory;
	}
	public ARPGItem getItem() {
		return currentItem;
	}

}


