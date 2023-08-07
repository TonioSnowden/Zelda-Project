package java.ch.epfl.cs107.play.game.ARPG.area;

import ch.epfl.cs107.play.game.ARPG.actor.CastleDoor;
import ch.epfl.cs107.play.game.ARPG.actor.DarkLord;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;


public class RouteChateau extends ARPGArea {
		public void createArea() {
			registerActor(new Background(this)) ;
			registerActor(new Foreground(this)) ;
			Door door1 = new Door("zelda/Route", new DiscreteCoordinates(9,18), Logic.TRUE ,this, Orientation.DOWN,
					new DiscreteCoordinates(9,0), new DiscreteCoordinates(10,0));
			registerActor(door1);
			CastleDoor castledoor = new CastleDoor("zelda/Chateau", new DiscreteCoordinates(7,1), Logic.FALSE ,this, Orientation.UP,
					new DiscreteCoordinates(9,13), new DiscreteCoordinates(10,13));
			registerActor(castledoor);
			DarkLord darkLord = new DarkLord(this,Orientation.DOWN,new DiscreteCoordinates(9,7),5);
			registerActor(darkLord);
				}
		@Override
		public String getTitle() {
			return "zelda/RouteChateau";
		}

	}

