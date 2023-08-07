package java.ch.epfl.cs107.play.game.ARPG.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class RouteTemple extends ARPGArea {
	
	public void createArea() {
		registerActor(new Background(this)) ;
		registerActor(new Foreground(this)) ;
		Door door1 = new Door("zelda/Route", new DiscreteCoordinates(18,10), Logic.TRUE ,this, Orientation.DOWN,
				new DiscreteCoordinates(0,4), new DiscreteCoordinates(0,5));
		registerActor(door1);
		Door door2 = new Door("zelda/Temple", new DiscreteCoordinates(4,1), Logic.TRUE ,this, Orientation.UP,
				new DiscreteCoordinates(5,6));
		registerActor(door2);
	}
	@Override
	public String getTitle() {
		return "zelda/RouteTemple";
	}

}
