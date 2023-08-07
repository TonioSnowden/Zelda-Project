package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class ARPGPlayer extends Player{

	public enum State{
		PLAYER(),
		SWORD(),
		BOW(),
		STAFF_WATER(),
		ARROW(),
		BOMB(),
		CASTLEKEY();
	}

	private final static int ANIMATION_DURATION = 20;
	private final int SWORD_DAMAGE = 2;
	Keyboard keyboard = getOwnerArea().getKeyboard();
	private ARPGInteractionVisitor playerHandler;
	private Animation currentAnimation;
	private Animation[] playerAnimations;
	private Animation[] bowAnimations;
	private Animation[] swordAnimations;
	private Animation[] staff_WaterAnimations;
	private Sprite[][] sprites;
	private float life = 5f;
	private ARPGInventory inventory;
	private ARPGItem myItem;
	private ARPGPlayerStatusGUI GUI;
	private final float maxLife = 5;
	private State state;
	

	//Initialisation de ARPGPlayer en lui ajoutant des items et creation des differents animations
	public ARPGPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
		super(owner, orientation, coordinates);
		sprites = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this, 16, 32, new Orientation[] {
				Orientation.DOWN, Orientation.RIGHT, 
				Orientation.UP, Orientation.LEFT });
		playerAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/10, sprites);

		Sprite[][] bowSprites = RPGSprite.extractSprites("zelda/player.bow", 4, 2, 2, this, 32, 32,new Vector(-0.5f,0f), new Orientation[] {
				Orientation.DOWN, Orientation.RIGHT, 
				Orientation.UP, Orientation.LEFT });
		bowAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/6, bowSprites,false);

		Sprite[][] swordSprites = RPGSprite.extractSprites("zelda/player.sword", 4, 2, 2, this, 32, 32,new Vector(-0.5f,0f), new Orientation[] {
				Orientation.DOWN, Orientation.RIGHT, 
				Orientation.UP, Orientation.LEFT });
		swordAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/6, swordSprites,false);

		Sprite[][] staff_WaterSprites = RPGSprite.extractSprites("zelda/player.staff_water", 4, 2, 2, this, 32, 32,new Vector(-0.5f,0f), new Orientation[] {
				Orientation.DOWN, Orientation.RIGHT, 
				Orientation.UP, Orientation.LEFT });
		staff_WaterAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/6,staff_WaterSprites,false);

		playerHandler = new ARPGPlayerHandler();
		resetMotion();	
		state=State.PLAYER;
		changeAnimation(playerAnimations);
		inventory = new ARPGInventory(999, 250,999);
		inventory.addItem(ARPGItem.Arrow, 5);
		inventory.addItem(ARPGItem.Bomb, 5);
		inventory.addItem(ARPGItem.Bow, 1);
		inventory.addItem(ARPGItem.Sword, 1);

		GUI = new ARPGPlayerStatusGUI(life, myItem, inventory);
		state=State.PLAYER;
	}

	@Override
	public void update(float deltaTime) {
		Button SPACE = keyboard.get(Keyboard.SPACE);
		if (SPACE.isPressed()) {
			setState();
			switch(state) {
			case SWORD:
				changeAnimation(swordAnimations);
				break;
			case BOW:
				changeAnimation(bowAnimations);
				break;
			case STAFF_WATER:
				changeAnimation(staff_WaterAnimations);
				break;
			case ARROW:
				changeAnimation(playerAnimations);
				break;
			case BOMB:
				changeAnimation(playerAnimations);
				break;
			case CASTLEKEY:
				changeAnimation(playerAnimations);
				break;
			case PLAYER:
				changeItem();
				break;
			}
			if(state!=State.PLAYER) {
				canUseItem();
			}

		}
		if(state==State.BOW || state==State.SWORD || state==State.STAFF_WATER) {
			currentAnimation.update(deltaTime);
			if(currentAnimation.isCompleted()) {
				currentAnimation.reset();
				state=State.PLAYER;
			}
		} else {
			state=State.PLAYER;
		}

		if(state==State.PLAYER) {
			changeAnimation(playerAnimations);
			move(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
			move(Orientation.UP, keyboard.get(Keyboard.UP));
			move(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
			move(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
			if(isDisplacementOccurs()) {
				currentAnimation.update(deltaTime);
			}else {
				currentAnimation.reset();
			}
		}
		super.update(deltaTime);

		GUI.setLife(life);
		GUI.setItem(myItem);
		GUI.setCurrentInventory(inventory);
		nextItem();
	}
	
	//adapte l etat en fonction de l objet utilise
	private void setState() {
		if(myItem!=null) {
			switch(myItem) {
			case Bow:
				state=State.BOW;
				break;
			case Arrow:
				state=State.ARROW;
				break;
			case Bomb:
				state=State.BOMB;
				break;
			case CastleKey:
				state=State.CASTLEKEY;
				break;
			case Sword:
				state=State.SWORD;
				break;
			case Staff:
				state=State.STAFF_WATER;
				break;
			default : 
			}
		}
		else {		
			state=State.PLAYER;
		}
	}
	//Permet de changer l animation de l arpgPlayer en fonction de l orientationet de l animation demandé
	private void changeAnimation(Animation[] animation) {
		if(animation==playerAnimations) {
			switch(getOrientation()) {
			case UP :
				currentAnimation = animation[0];
				break;
			case DOWN : 
				currentAnimation = animation[2];
				break;
			case RIGHT :
				currentAnimation = animation[1];
				break;
			case LEFT : 
				currentAnimation = animation[3];
				break;
			}
		} else {
			switch(getOrientation()) {
			case UP :
				currentAnimation = animation[1];
				break;
			case DOWN : 
				currentAnimation = animation[2];
				break;
			case RIGHT :
				currentAnimation = animation[0];
				break;
			case LEFT : 
				currentAnimation = animation[3];
				break;
			}
		}
	}
	
	//utilise un objet et si il ny a plus d objet dans l inventaire l arpgplayer change automatiquement d item
	private void canUseItem() {
		switch(state) {
		case BOMB:
			Actor bomb =  new Bomb(getOwnerArea(),getOrientation(), 
					getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
			if(getOwnerArea().canEnterAreaCells(this,Collections.singletonList(getCurrentMainCellCoordinates().
					jump(getOrientation().toVector())))) {
				getOwnerArea().registerActor(bomb);		
				if(inventory.haveThisItem(myItem) ) {
			inventory.deleteItem(myItem, 1);
			if(!inventory.haveThisItem(myItem)) {
				changeItem();
			}
		}else if(myItem ==  null) {
			changeItem();
		}
			}
			break;

		case BOW:
			Actor arrow=new Arrow(getOwnerArea(),getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()),5,4);
			if(getOwnerArea().canEnterAreaCells((Interactable) arrow,Collections.singletonList(getCurrentMainCellCoordinates().
					jump(getOrientation().toVector())))) {
				getOwnerArea().registerActor(arrow);
				if(inventory.haveThisItem(ARPGItem.Arrow) ) {
					inventory.deleteItem(ARPGItem.Arrow, 1);
					if(!inventory.haveThisItem(ARPGItem.Arrow)) {
						changeItem();
					}
				}else if(ARPGItem.Arrow ==  null) {
					changeItem();
				}
					}
			break;
		case STAFF_WATER:
			Actor staff_water=new MagicWaterProjectile(getOwnerArea(),getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()),5,4);
			if(getOwnerArea().canEnterAreaCells((Interactable) staff_water,Collections.singletonList(getCurrentMainCellCoordinates().
					jump(getOrientation().toVector())))) {
				getOwnerArea().registerActor(staff_water);
			}
			break;
		default:		
		}

	}

	//permet au bouton tab de changer d item
	private void nextItem() {
		Keyboard keyboard = getOwnerArea().getKeyboard();
		Button buttonTAB = keyboard.get(Keyboard.TAB);
		if(buttonTAB.isPressed()) {
			changeItem();
		}
	}

	//change ditem si ce dernier n existe plus
	private void changeItem() {
		ARPGItem[] listOfItems = ARPGItem.values();
		int a = 0; 
		if(myItem != null) {
			while(listOfItems[a] != myItem) {
				++a;
			}
		}
		int i = (a + 1) % listOfItems.length;
		while(((i != a) && (!inventory.haveThisItem(listOfItems[i])))==true) {
			if(i < listOfItems.length-1) {
				++i;
			} else {
				i = 0;
			}
		}			
		if((i == a) && (!inventory.haveThisItem(myItem)) ||  (myItem == null) && (i == a)) {
			myItem = null;
		} else {
			myItem = listOfItems[i];
		}
	}

	//permet le deplacement du player
	private void move(Orientation orientation, Button button){
		if(button.isDown()) {
			if(getOrientation() == orientation) move(ANIMATION_DURATION/5);
			else orientate(orientation);
		}
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}


	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));

	}
	@Override
	public void draw(Canvas canvas) {
		currentAnimation.draw(canvas);
		GUI.draw(canvas);
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		if(state==State.SWORD) {
			return true;
		} else {
			Keyboard keyboard = getOwnerArea().getKeyboard();
			Button buttonE = keyboard.get(Keyboard.E);
			if(buttonE.isPressed()) {
				return true;
			}else {
				return false;
			}
		}
	}	


	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(playerHandler);

	}
	public int getFortune() {
		return inventory.getFortune();

	}
	public int getmoney() {
		return inventory.getmoney();
	}
	public void removeHp(float damage) {
		if(life>0) {
			life-=damage;
		}
	}
	public void addLife(float gaindevie) {
		life += gaindevie;
	}

	//classe gerant les interactions avec les autres player 
	private class ARPGPlayerHandler implements ARPGInteractionVisitor {
		private final int prime = 50;
		private final float gaindevie = 1f;
		public void interactWith(Door door) {
			setIsPassingADoor(door);
		}
		@Override
		public void interactWith(CastleDoor castleDoor) {
			if(myItem == ARPGItem.CastleKey) {
				if(!castleDoor.isOpen()) {
					castleDoor.openDoor();
				} else {
					setIsPassingADoor(castleDoor);
					castleDoor.closeDoor();
				}

			}
		}
		@Override
		public void interactWith(Grass grass) {
			if(state==State.SWORD) {
				grass.BeSliced();
			}
		}
		@Override
		public void interactWith(Bomb bomb) {
			if(state==State.SWORD) {
				bomb.explosion();		
			}
		}
		@Override
		public void interactWith(Monster monster) {
			if(state==State.SWORD) {
				for(int i=0;i<(monster.getVulnerability()).size();i++) {
					if(("par dommage physique").equals((monster.getVulnerability()).get(i))) {
						monster.removeHp(SWORD_DAMAGE);
					}
				}
			}
		}
		@Override
		public void interactWith(Heart heart) {
			if(GUI.getLife() < maxLife) {
				heart.takeObject();
				addLife(gaindevie);
			}
		}
		@Override
		public void interactWith(Staff staff) {
			inventory.addItem(ARPGItem.Staff,1);
            getOwnerArea().unregisterActor(staff);
		}
		@Override
		public void interactWith(Coin coin) {
			if(getmoney() < getFortune()){
				coin.takeObject();
				inventory.addMoney(prime);
			}
		}
		@Override
		public void interactWith(CastleKey castleKey) {
			castleKey.takeObject();
			inventory.addItem(ARPGItem.CastleKey,1);
		}
		@Override
		public void interactWith(King king) {
			king.interacted();
		}
		@Override
		public void interactWith(PNJ pnj) {
			pnj.interacted();
		}
	}

}

