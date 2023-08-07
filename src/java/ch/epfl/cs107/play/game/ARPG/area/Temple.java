package java.ch.epfl.cs107.play.game.ARPG.area;

import ch.epfl.cs107.play.game.ARPG.actor.Staff;
import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Temple extends ARPGArea {
	
	public void createArea() {
		registerActor(new Background(this)) ;
		registerActor(new Foreground(this)) ;
		Door door1 = new Door("zelda/RouteTemple", new DiscreteCoordinates(5,5), Logic.TRUE ,this, Orientation.UP,
				new DiscreteCoordinates(4,0));
		registerActor(door1);
		Actor staff = new Staff(this, Orientation.UP, new DiscreteCoordinates(4,3));
		registerActor(staff);
	}
	@Override
	public String getTitle() {
		return "zelda/Temple";
	}

}
