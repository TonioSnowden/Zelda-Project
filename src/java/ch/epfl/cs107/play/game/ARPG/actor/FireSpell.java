package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class FireSpell extends AreaEntity implements Interactor{
	private final int ANIMATION_DURATION = 10;
	private float strength;
	private final int MIN_LIFE_TIME=12;
	private final int MAX_LIFE_TIME=24;
	private float lifeTime;
	private Animation animation;
	private final float DAMAGE=0.5f;
	private ARPGFireSpellHandler arpgFireSpellHandler;
	private final float PROPAGATION_TIME_FIRE=1f;
	private float timeFire;

	//Appelle au constructeur de la superClasse et initialisation de son animation ainsi que de sa duree de vie 
	public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position,float strength) {
		super(area, orientation, position);
		this.strength=strength;
		setLifeTime();
		timeFire=0f;
		arpgFireSpellHandler=new ARPGFireSpellHandler();
		RPGSprite[] fireArray = new RPGSprite[7];
		for (int i = 0; i < fireArray.length; ++i) {
			fireArray[i]=new RPGSprite("zelda/fire", 1, 1, this, new RegionOfInterest(i*16, 0, 16, 16),new Vector(0f,0.25f));
			animation=new Animation(ANIMATION_DURATION/2, fireArray,true);
		}
	}
	
	//gere l'augmentation du temps de vie de la flamme ainsi que de l'initialisation d'une nouvelle flamme de force inferieur sur la prochaine cellule
	public void update(float deltaTime) {
		if(isWeak()) {
			getOwnerArea().unregisterActor(this);
		} else {
			lifeTime-=deltaTime;
			timeFire+=deltaTime;
			if(timeFire>=PROPAGATION_TIME_FIRE && strength>0) {
				if(getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))){
				FireSpell firespell=new FireSpell(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates().jump(getOrientation().toVector()),strength-1);
				getOwnerArea().registerActor(firespell);
				}
				strength =0;
			}
		}
		animation.update(deltaTime);
		super.update(deltaTime);
	}
	
	//renvoie un boolean si le temps de vie de la flamme est atteint
	private boolean isWeak() {
		if(lifeTime<0) {
			return true;
		}else {
			return false;
		}
	}
	
	//Initialise le temps de vie de la flamme
	public void setLifeTime() {
		int interval=(int)(MAX_LIFE_TIME-MIN_LIFE_TIME);
		int randomInt = RandomGenerator.getInstance().nextInt(interval);
		lifeTime=randomInt+MIN_LIFE_TIME;
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		return !isWeak();
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		if(isWeak()) {
			getOwnerArea().unregisterActor(this);
		}
		if(!animation.isCompleted()) {
			animation.draw(canvas);
		}
	}
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	@Override
	public boolean wantsCellInteraction() {
		return true;
	}
	@Override
	public boolean wantsViewInteraction() {
		return true;
	}
	public void turnOff() {
		getOwnerArea().unregisterActor(this);
	}
	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(arpgFireSpellHandler);	
	}
	
	//Classe gerant les interactions de la flamme avec les autres acteurs
	private class ARPGFireSpellHandler implements ARPGInteractionVisitor {
		public void interactWith(Grass grass) {
				grass.BeSliced();
			}
		public void interactWith(Monster monster) {
			for(int i=0;i<(monster.getVulnerability()).size();i++) {
				if(("par le feu").equals((monster.getVulnerability()).get(i))) {
					monster.removeHp(DAMAGE);
				}
			}
		}
		public void interactWith(ARPGPlayer arpgPlayer) {
			arpgPlayer.removeHp(DAMAGE);
		}
		public void interactWith(Bomb bomb) {
			bomb.explosion();
		}
	}

}
