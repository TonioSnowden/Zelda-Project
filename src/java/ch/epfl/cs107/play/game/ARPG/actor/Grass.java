package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Grass extends AreaEntity{
	private static final int ANIMATION_DURATION = 20;
	private static final double PROBABILITY_TO_DROP_ITEM = 0.4f;
	private static final double PROBABILITY_TO_DROP_HEART = 0.4f;
	private Animation grassAnimation;
	private Animation grassSliced;
	private Animation thisAnimation;
	boolean alive;
	private Coin pieces;
	private Heart heart;
	
	public Grass(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area,orientation,position);
		RPGSprite[] grass = {new RPGSprite("zelda/grass",1.f,1.f,this,new RegionOfInterest(0, 0, 16, 16))};
		grassAnimation =new Animation(ANIMATION_DURATION/2,grass, false);
		RPGSprite[] grassArray= new RPGSprite[4];
		for(int i=0;i<grassArray.length;i++) {
		  grassArray[i] = new RPGSprite("zelda/grass.sliced",2.f,2.f,this,new RegionOfInterest(i*32,0,32,32),new Vector(-0.4f,0f));
		}
		grassSliced=new Animation(ANIMATION_DURATION/3,grassArray, false);
		alive=true;
		thisAnimation=grassAnimation;
		pieces =  new Coin(area, orientation, position);
		heart = new Heart(area, orientation, position);
	}
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	//cette methode permet de couper l'herbe
	public void BeSliced() {
		thisAnimation=grassSliced;
		alive=false;
		creationrandomItem();
	}
	@Override
	public void update(float deltaTime) {
		if(alive == false) {
			thisAnimation.update(deltaTime);
			if(thisAnimation.isCompleted()) {
	        	getOwnerArea().unregisterActor(this);
	        }
		}
	}

	@Override
	public boolean takeCellSpace() {
		return alive;
		}

	@Override
	public boolean isCellInteractable() {
		return alive;
	}

	@Override
	public boolean isViewInteractable() {
		return alive;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		if(!thisAnimation.isCompleted()) {
		thisAnimation.draw(canvas);
		}
	}
	// cette methode permet de creer une piece ou une vie aleatoirement derriere une touffe d'herbe
	public void creationrandomItem() {
		double i = RandomGenerator.getInstance().nextDouble();
		double j = RandomGenerator.getInstance().nextDouble();
		if(i <= PROBABILITY_TO_DROP_ITEM) {
			if(j<= PROBABILITY_TO_DROP_HEART) {
				getOwnerArea().registerActor(heart);
			}else {
				getOwnerArea().registerActor(pieces);
			}

		}
	}
	
}
