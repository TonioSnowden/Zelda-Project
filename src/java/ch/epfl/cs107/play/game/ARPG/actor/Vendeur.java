package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Vendeur extends AreaEntity{
	private Sprite sprite;

	public Vendeur(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		sprite=new RPGSprite("zelda/character", 1, 2, this,new RegionOfInterest(0, 64, 16, 32));
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList (getCurrentMainCellCoordinates());
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
	}

	@Override
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
	}
	

}
