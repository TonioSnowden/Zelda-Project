package java.ch.epfl.cs107.play.game.ARPG.area;

import ch.epfl.cs107.play.game.ARPG.actor.King;
import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Chateau extends ARPGArea {
	
	public void createArea() {
		registerActor(new Background(this)) ;
		registerActor(new Foreground(this)) ;
		Door door1 = new Door("zelda/RouteChateau", new DiscreteCoordinates(9,12), Logic.TRUE ,this, Orientation.DOWN,
				new DiscreteCoordinates(7,0), new DiscreteCoordinates(8,0));
		registerActor(door1);
		Actor king = new King(this, Orientation.UP, new DiscreteCoordinates(7,12));
		registerActor(king);
			}
	@Override
	public String getTitle() {
		return "zelda/Chateau";
	}

}
