package java.ch.epfl.cs107.play.game.ARPG.handler;

import ch.epfl.cs107.play.game.areagame.actor.Interactor;

public interface FlyableEntity extends Interactor{
	
	public default boolean canFly() {
		return true;
	}


}
