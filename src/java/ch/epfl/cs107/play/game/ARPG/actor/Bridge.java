package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Bridge extends AreaEntity{
	private Sprite pont;
	final int valeur = 10;
	
	public Bridge(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		creerPont();
		
	}
	public void creerPont() {
			pont = new Sprite("zelda/bridge", 4f, 4f, this , 
					new RegionOfInterest(0, 0, 64, 64), new Vector(0f,-0.5f));	
			pont.setDepth(-20);
	}
	@Override
	 public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}
	@Override
	public void draw(Canvas canvas) {
		pont.draw(canvas);
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

