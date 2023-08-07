package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public abstract class Monster extends MovableAreaEntity implements Interactor{
	private static final int ANIMATION_DURATION = 4;
	private float hp;
	private float maxHp;
	private Animation death;
	private ArrayList<String> vulnerabilityArray;
	private boolean immunity;
	private float immunityTime;

	//Appelle du constructeur de la super Classe et initialise les points de vie du monstres ainsi que l'animation de la mort du monstre
	public Monster(Area area, Orientation orientation, DiscreteCoordinates coordinates,int maxHp) {	
		super(area, orientation, coordinates);
		this.maxHp=maxHp;
		this.hp=maxHp;
		RPGSprite[] deathArray = new RPGSprite[7];
		for (int i = 0; i < deathArray.length; ++i) {
			deathArray[i]=new RPGSprite("zelda/vanish", 2, 2, this, new RegionOfInterest(i*32, 0, 32, 32),new Vector(-0.5f,0f));
		}
		death = new Animation(ANIMATION_DURATION/2,deathArray, false);
		vulnerabilityArray=new ArrayList<String>();
		immunity=false;
	}
	
	public void update(float deltaTime) {		
		 super.update(deltaTime);
		 immunityUpdate(deltaTime);
	}
	
	//Permet d'ajouter une vulnerabilite aux monstres
	public void setVulnerability(String vulnerability) {
		vulnerabilityArray.add(vulnerability);
	}
	
	//retourne la liste de vulnerabilite du monstre
	public ArrayList<String> getVulnerability(){
		return vulnerabilityArray;
	}
	
	//renvoie un boolean qui dit si le monstre est immunise
	public boolean getImmunity() {
		return immunity;
	}
	
	//enleve des points de vie aux monstres en fonction des degats qu'il subit
	public void removeHp(float damage) {
		if(!immunity) {
		hp-=damage;
		immunity=true;
		immunityTime=0;
		}
	}
	
	//gere le temps d'immunite du monstre 
	public void immunityUpdate(float deltaTime) {
		immunityTime+=deltaTime;
		if(immunityTime>=2) {
			immunity=false;
		}
	}
	
	//renvoie un boolean qui dit si le monstre est mort(point de vie à zero)
	public boolean death() {
		if(hp<=0) {
			return true;
		} else {
			return false;
		}	
	}
	
	//les monstres doivent laisser des objets quand ils sont morts, objets qui different selon les monstres
	public abstract void letObject();
	
	//renvoie les points de vie max d'un monstre
	public float getMaxHp() {
		return maxHp;
	}
		
	//renvoie l'animation de la mort d'un monstre
    public Animation getDeathAnimation() {
    	return death;
    }

	@Override
	public abstract void draw(Canvas canvas);

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	
	//Oriente le monstre de maniere aleatoire
	public void changeOrientation() {
		int max=4;
		int possibility = RandomGenerator.getInstance().nextInt(max);
		orientate(Orientation.fromInt(possibility));
		changeAnimation();
	}
	
	//Le monstre doit changer d'animation en fonction de son orientation
	public abstract void changeAnimation();

	@Override
	public abstract boolean takeCellSpace();
	
    public abstract boolean wantsViewInteraction();

    public abstract boolean wantsCellInteraction();

	@Override
	public boolean isCellInteractable() {
		return !(death() && immunity);
	}

	@Override
	public boolean isViewInteractable() {
		return !(death() && immunity);
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}
	
	public abstract void interactWith(Interactable other);


}
