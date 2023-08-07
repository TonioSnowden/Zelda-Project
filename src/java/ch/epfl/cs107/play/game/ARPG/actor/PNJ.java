package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class PNJ extends MovableAreaEntity{
	private static final int ANIMATION_DURATION = 20;
	private Animation[] animation;
	private Animation currentAnimation;
	private final int SIMULATION_STEPS_MAX=20;
	private int simulationSteps;
	private String message;
	private Dialog dialog;
	private boolean interacted;
	private float interactedTime;

	public PNJ(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		Sprite[][] sprites=RPGSprite.extractSprites("zelda/character", 4, 1, 2, this, 16, 32,new Vector(0f,0f), new Orientation[] {Orientation.UP, Orientation.LEFT, 
				Orientation.DOWN, Orientation.RIGHT});
		animation=RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites,true);
		message="Sauvez nous!  Le darkLord detruit nos Terres";
		dialog= new Dialog(message, "zelda/dialog", getOwnerArea());
		changeAnimation();
		simulationSteps=0;
		interacted=false;
		interactedTime=0;
	}
	
	public void update(float deltaTime) {
		if(interacted) {
			interactedTime+=deltaTime;
			if(interactedTime>=5){
				interacted=false;
				interactedTime=0;
			}
		} else {
		//Initialise des moments d'inaction
		if(simulationSteps<SIMULATION_STEPS_MAX) {
			simulationSteps+=1;
		} else {
			//Adapte l'animation du logMonster et opere a un changement de direction aleatoire
			changeAnimation();
			int randomInt = RandomGenerator.getInstance().nextInt(10);
			if(randomInt<2 || !isDisplacementOccurs()) {
				changeOrientation();
				move(ANIMATION_DURATION);
			}	
			randomInactionMoment();
		}
		super.update(deltaTime);
		if(isDisplacementOccurs()) {
			currentAnimation.update(deltaTime);
		}else {
			currentAnimation.reset();
		}
	}
	}
	public void changeOrientation() {
		int max=4;
		int possibility = RandomGenerator.getInstance().nextInt(max);
		orientate(Orientation.fromInt(possibility));
		changeAnimation();
	}
	
	private void randomInactionMoment() {
		int probabilite = 100;
		int randomInt = RandomGenerator.getInstance().nextInt(probabilite);
		if(randomInt==0) {
			simulationSteps=0;
		}
	}
	
	public void changeAnimation() {
		switch(getOrientation()) {
		case DOWN:					
			currentAnimation= animation[2];
			break;
		case UP: 
			currentAnimation= animation[0];
			break;
		case LEFT:
			currentAnimation= animation[1];	
			break;
		case RIGHT:
			currentAnimation= animation[3];		
			break;
		}
	}
	

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		// TODO Auto-generated method stub
		return Collections.singletonList (getCurrentMainCellCoordinates());

	}
	public List<DiscreteCoordinates> getFieldOfViewCells(){
		return getCurrentMainCellCoordinates().getNeighbours();
	}

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
	    currentAnimation.draw(canvas);	
	    if(interacted) {
			dialog.draw(canvas);
		}
	}
	public void interacted() {
		interacted=true;
	}

}
