package java.ch.epfl.cs107.play.game.ARPG.actor;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class CastleKey extends CollectableAreaEntity{
	private Sprite castleKey;
	
	public CastleKey(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		castleKey = new Sprite("zelda/key",1f, 1f, this, new RegionOfInterest(0, 0, 16, 16));
	}
	@Override
	 public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}
	@Override
	public void draw(Canvas canvas) {
		castleKey.draw(canvas);
	}
	

}
