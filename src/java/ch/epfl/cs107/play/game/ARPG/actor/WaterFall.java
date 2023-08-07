package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class WaterFall extends AreaEntity{
	private Sprite[] cascade = new Sprite[3];
	private Animation animation;
	private final static int ANIMATION_DURATION = 3;
	
	public WaterFall(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		creerCascade();
		
	}
	public void creerCascade() {
		for(int i = 0 ; i < cascade.length ; ++i) {
			cascade[i] = new RPGSprite("zelda/waterfall", 4f, 4f, this , 
					new RegionOfInterest(64*i, 0, 64, 64));
		}
		animation = new Animation(ANIMATION_DURATION, cascade, true );
	}
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		animation.update(deltaTime);
	}
	@Override
	 public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}
	@Override
	public void draw(Canvas canvas) {
		animation.draw(canvas);
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
		return false;
	}
	@Override
	public boolean isViewInteractable() {
		return false;
	}
}
