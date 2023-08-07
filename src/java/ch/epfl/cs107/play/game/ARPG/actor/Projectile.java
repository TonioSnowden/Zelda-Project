package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public abstract class Projectile extends MovableAreaEntity implements Interactor{
	private int range;
	private float speed;
	
	//Initisalise les projectiles en leur donnant une vitesse et une portee
	public Projectile(Area area, Orientation orientation, DiscreteCoordinates position,int range, float speed) {
		super(area, orientation, position);
		this.range=range;
		this.speed=speed;
		
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	public float getSpeed() {
		return this.speed;
	}
	public int getRange() {
		return this.range;
	}
	//en fonction de l'orientation initiale renvoie la position maximale du projectile par saut d'un vecteur sur la position initiale
	protected DiscreteCoordinates getPositionMax() {
		switch(getOrientation()) {
		case UP:
			return getCurrentMainCellCoordinates().jump(0, range);
		case DOWN:
			return getCurrentMainCellCoordinates().jump(0, -range);
		case LEFT:
			return getCurrentMainCellCoordinates().jump(-range,0);
		case RIGHT:
			return getCurrentMainCellCoordinates().jump(range,0);
		default:
			return null;
		}
	}
	
	//Permet de savoir si le projectile a atteint sa distance maximale
		protected boolean positionMax(DiscreteCoordinates positionMax) {
			if((getCurrentMainCellCoordinates()).equals(positionMax)) {
				return true;
			} else {
				return false;
			}
		}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList (getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
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
	public abstract void draw(Canvas canvas);
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return true;
	}
	@Override
	public abstract void interactWith(Interactable other);
}
