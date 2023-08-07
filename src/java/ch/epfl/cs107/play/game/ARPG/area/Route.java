package java.ch.epfl.cs107.play.game.ARPG.area;

import ch.epfl.cs107.play.game.ARPG.actor.FlameSkull;
import ch.epfl.cs107.play.game.ARPG.actor.Grass;
import ch.epfl.cs107.play.game.ARPG.actor.LogMonster;
import ch.epfl.cs107.play.game.ARPG.actor.Orbe;
import ch.epfl.cs107.play.game.ARPG.actor.WaterFall;
import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;

public class Route extends ARPGArea{
	public void createArea() { 
		registerActor(new Background(this)) ;
		registerActor(new Foreground(this)) ;
		Door door1 = new Door("zelda/Ferme", new DiscreteCoordinates(18,15), Logic.TRUE ,this, Orientation.UP,
				new DiscreteCoordinates(0,15), new DiscreteCoordinates(0,16));
		registerActor(door1);
		Door door2 = new Door("zelda/Village", new DiscreteCoordinates(29,18), Logic.TRUE ,this, Orientation.DOWN,
				new DiscreteCoordinates(9,0), new DiscreteCoordinates(10,0));
		registerActor(door2);
		Door door3 = new Door("zelda/RouteChateau", new DiscreteCoordinates(9,1), Logic.TRUE ,this, Orientation.UP,
				new DiscreteCoordinates(9,19), new DiscreteCoordinates(10,19));
		registerActor(door3);
		Door door4 = new Door("zelda/RouteTemple", new DiscreteCoordinates(1,4), Logic.TRUE ,this, Orientation.LEFT,
				new DiscreteCoordinates(19,10));
		registerActor(door4);
		Grass[][] grass = new Grass[3][6];
		for(int i = 0; i <=2; ++i) {
			for(int j = 0; j<=5;++j) {
				grass[i][j]= new Grass(this, Orientation.DOWN , new DiscreteCoordinates(i+5,j+6));
				registerActor(grass[i][j]);
			}
		}
		WaterFall waterfall = new WaterFall(this, Orientation.LEFT, new DiscreteCoordinates(getWidth() - 5 , 3));
		registerActor(waterfall);
	    LogMonster logMonster = new LogMonster(this,Orientation.DOWN,new DiscreteCoordinates(10,8),10);
		registerActor(logMonster);
		LogMonster logMonster1 = new LogMonster(this,Orientation.DOWN,new DiscreteCoordinates(13,6),10);
	    registerActor(logMonster1);
	    LogMonster logMonster2 = new LogMonster(this,Orientation.RIGHT,new DiscreteCoordinates(4, 10),10);
	    registerActor(logMonster2);
	    FlameSkull flameSkull1=new FlameSkull(this,Orientation.DOWN,new DiscreteCoordinates(4,7),5);
	    registerActor(flameSkull1);
		Actor orbe = new Orbe(this, Orientation.UP, new DiscreteCoordinates(19,8));
		registerActor(orbe);

	}
	@Override
	public String getTitle() {
		return "zelda/Route";
	}

	public void createFlameSkull() {
		Keyboard keyboard = this.getKeyboard();
		Button buttonS = keyboard.get(Keyboard.S);
		if(buttonS.isPressed()) {
	}
	}
}
