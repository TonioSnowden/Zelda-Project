package java.ch.epfl.cs107.play.game.ARPG.actor;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door{
	private RPGSprite castleDoorOpened;
	private RPGSprite castleDoorClosed;

	public CastleDoor(String destination, DiscreteCoordinates othersideCoordinates, Logic signal, Area area,
			Orientation orientation, DiscreteCoordinates discreteCoordinates, DiscreteCoordinates... others) {
		super(destination, othersideCoordinates, signal, area, orientation , discreteCoordinates, others);
		castleDoorOpened = new RPGSprite("zelda/castleDoor.open", 2f, 2f, this);
		castleDoorClosed =  new RPGSprite("zelda/castleDoor.close", 2f, 2f, this);
		
	}
	
	public void openDoor() {
		setSignal(Logic.TRUE);
	}
	
	public void closeDoor() {
		setSignal(Logic.FALSE);
	}

	@Override
	public void draw(Canvas canvas) {
		if(isOpen()) {
			castleDoorOpened.draw(canvas);
		} else {
			castleDoorClosed.draw(canvas);
		}
	}

	@Override
	public boolean isViewInteractable() {
		return !isOpen();
	}
	  @Override
	    public void acceptInteraction(AreaInteractionVisitor v) {
	        ((ARPGInteractionVisitor)v).interactWith(this);
	    }
	
	
}
