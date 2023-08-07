package java.ch.epfl.cs107.play.game.ARPG.area;

import ch.epfl.cs107.play.game.ARPG.actor.PNJ;
import ch.epfl.cs107.play.game.ARPG.actor.Vendeur;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Village extends ARPGArea{

	@Override
	public String getTitle() {
		return "zelda/Village";
	}

	protected void createArea() {
    
        registerActor(new Background(this)) ;
        registerActor(new Foreground(this)) ;
        Door door1 = new Door("zelda/Ferme",new DiscreteCoordinates(4,1),Logic.TRUE,this,Orientation.UP,new DiscreteCoordinates(4,19),new DiscreteCoordinates(5,19));
        registerActor(door1);
        Door door2 = new Door("zelda/Ferme",new DiscreteCoordinates(14,1),Logic.TRUE,this,Orientation.UP,new DiscreteCoordinates(13,19),new DiscreteCoordinates(14,19),new DiscreteCoordinates(15,19));
		registerActor(door2);
		Door door3 = new Door("zelda/Route",new DiscreteCoordinates(9,1),Logic.TRUE,this,Orientation.UP,new DiscreteCoordinates(29,19),new DiscreteCoordinates(30,19));
		registerActor(door3);
		
		PNJ pnj= new PNJ(this, Orientation.DOWN, new DiscreteCoordinates(4, 4));
		registerActor(pnj);
		PNJ pnj1= new PNJ(this, Orientation.DOWN, new DiscreteCoordinates(13, 4));
		registerActor(pnj1);
		PNJ pnj2= new PNJ(this, Orientation.DOWN, new DiscreteCoordinates(4, 14));
		registerActor(pnj2);
		PNJ pnj3= new PNJ(this, Orientation.DOWN, new DiscreteCoordinates(20, 4));
		registerActor(pnj3);
		PNJ pnj4= new PNJ(this, Orientation.DOWN, new DiscreteCoordinates(17, 8));
		registerActor(pnj4);
		PNJ pnj5= new PNJ(this, Orientation.DOWN, new DiscreteCoordinates(25, 4));
		registerActor(pnj5);
		PNJ pnj6= new PNJ(this, Orientation.DOWN, new DiscreteCoordinates(30, 19));
		registerActor(pnj6);
		Vendeur vendeur=new Vendeur(this, Orientation.DOWN, new DiscreteCoordinates(17, 11));
		registerActor(vendeur);

        }
	

}


