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
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Staff extends AreaEntity{
	private final int ANIMATION_DURATION = 10;
	private Animation animation;

	public Staff(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		RPGSprite[] sprite=new RPGSprite[8];
		for(int i = 0 ; i < sprite.length ; ++i) {
			sprite[i] = new RPGSprite("zelda/staff", 2f, 2f, this , new RegionOfInterest(32*i, 0, 32, 32), new Vector(-0.5f, 0f));
		}
		animation = new Animation(ANIMATION_DURATION, sprite, true);
		
	}
	public void update(float deltaTime) {
		animation.update(deltaTime);
		super.update(deltaTime);
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
		return true;
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
		animation.draw(canvas);		
	}

}
