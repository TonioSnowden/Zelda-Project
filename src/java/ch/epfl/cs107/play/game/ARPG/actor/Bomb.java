package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Bomb extends AreaEntity implements Interactor{
	private static final int ANIMATION_DURATION = 10;
	private float retardateur;
	private ARPGBombHandler bombHandler;
	private Animation bombAnimation;
	private Animation explosionAnimation;
	private Animation thisAnimation;
	private TextGraphics retardateurText;
	private boolean explosed;

	//Creation des animations et de la bombe
	public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		retardateur = 5;
		retardateurText = new TextGraphics(Integer.toString((int)retardateur), 0.4f, Color.BLACK);
		retardateurText.setParent(this);
		retardateurText.setAnchor(new Vector(-0.3f, 0.1f));
		bombHandler = new ARPGBombHandler();

		RPGSprite[] bombArray = new RPGSprite[2];
		for (int i = 0; i < bombArray.length; ++i) {
			bombArray[i]=new RPGSprite("zelda/bomb", 1, 1, this, new RegionOfInterest(i*16, 0, 16, 16));
		}
		RPGSprite[] explosionArray = new RPGSprite[7];
		for (int i = 0; i < explosionArray.length; ++i) {
			explosionArray[i]=new RPGSprite("zelda/explosion", 1, 1, this, new RegionOfInterest(i*32, 0, 32, 32));
		}
		bombAnimation = new Animation(ANIMATION_DURATION/2,bombArray, true);
		explosionAnimation = new Animation(ANIMATION_DURATION/2,explosionArray,false);
		thisAnimation=bombAnimation;
		explosed=false;
	}
	
	//gere le retardateur de la bombe et la fait exploser si son conteur est a 0
	public void update(float deltaTime) {
		if(retardateur>=0) {
			retardateur-=deltaTime;
		}
    	retardateurText.setText(Integer.toString((int)retardateur));
	
		if(retardateur<=0) {
			thisAnimation=explosionAnimation;
			if(thisAnimation.isCompleted()) {
				getOwnerArea().unregisterActor(this);
			}
			explosed=true;
		}
		thisAnimation.update(deltaTime);
		super.update(deltaTime);

	}
	
	//fait exploser la bombe
	public void explosion() {
		retardateur=0;
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return !explosed;
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
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		if(!thisAnimation.isCompleted()) {
		thisAnimation.draw(canvas);
		retardateurText.draw(canvas);
		}
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return getCurrentMainCellCoordinates().getNeighbours();
	}

	@Override
	public boolean wantsCellInteraction() {
		return explosed;
	}

	@Override
	public boolean wantsViewInteraction() {
		return explosed;
	}
	public void interactWith(Interactable other) {
		other.acceptInteraction(bombHandler);	
	}
	private class ARPGBombHandler implements ARPGInteractionVisitor {
		@Override
		public void interactWith(Grass grass) {
			if(retardateur<=0) {
				grass.BeSliced();
			}
		}
		@Override
		public void interactWith(Monster monster) {
			for(int i=0;i<(monster.getVulnerability()).size();i++) {
				if(("par dommage physique").equals((monster.getVulnerability()).get(i))) {
					monster.removeHp(10.f);
				}
			}
		}
	}
}
