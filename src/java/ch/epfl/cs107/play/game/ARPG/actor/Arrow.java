package java.ch.epfl.cs107.play.game.ARPG.actor;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.ARPG.handler.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Arrow extends Projectile implements FlyableEntity{
	private RPGSprite[] sprite;
	private RPGSprite currentSprite;
	private final float DAMAGE=1f;
	ARPGArrowHandler arpgArrowHandler;
	private DiscreteCoordinates distMax;
	
    //Appel du constructeur de la superClasse et initialisation le sprite des fleches
	public Arrow(Area area, Orientation orientation, DiscreteCoordinates position, int range, float speed) {
		super(area, orientation, position, range, speed);
		sprite=new RPGSprite[4];
		distMax=getPositionMax();
		arpgArrowHandler=new ARPGArrowHandler();
		for(int i=0;i<sprite.length;i++) {
			sprite[i]=new RPGSprite("zelda/arrow",1, 1, this, new RegionOfInterest(i*32, 0, 32, 32),new Vector(0f,0.25f));
		}
		changeOrientation();
	}
	//Execute le mouvement de la fleche et la supprime lorsqu'elle a touche un objet ou qu'elle a atteint sa distance maximale
	public void update(float deltaTime) {
		move((int)getSpeed());
		if(positionMax(distMax) || !(isDisplacementOccurs())) {
			move((int)getSpeed());
			getOwnerArea().unregisterActor(this);
		}
		super.update(deltaTime);
	}

	//Adapte le sprite de la fleche en fonction de son orientation
	private void changeOrientation() {
		switch(getOrientation()) {
		case DOWN:					
			currentSprite= sprite[2];
			break;
		case UP: 
			currentSprite= sprite[0];
			break;
		case LEFT:
			currentSprite= sprite[3];	
			break;
		case RIGHT:
			currentSprite= sprite[1];		
			break;
		}
	}
	
	//desenregistre la fleche
	private void unregisterArrow() {
		getOwnerArea().unregisterActor(this);
	}
	@Override
	public void draw(Canvas canvas) {
		currentSprite.draw(canvas);
	}
	
	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(arpgArrowHandler);
	}	
	//Classe qui permet de gerer les interaction de la fleche avec les autres acteurs du jeu
	private class ARPGArrowHandler implements ARPGInteractionVisitor{
		@Override
		public void interactWith(Monster monster) {
			for(int i=0;i<(monster.getVulnerability()).size();i++) {
				if(("par dommage physique").equals((monster.getVulnerability()).get(i))) {
					monster.removeHp(DAMAGE);
				}
			}
			unregisterArrow();
		}
		@Override
		public void interactWith(Orbe orbe) {
			Bridge bridge = new Bridge(getOwnerArea(), Orientation.LEFT, new DiscreteCoordinates(15 , 9));
			getOwnerArea().registerActor(bridge);
			unregisterArrow();
		}
		@Override
		public void interactWith(Grass grass) {
			grass.BeSliced();
			unregisterArrow();
		}
		@Override
		public void interactWith(Bomb bomb) {
			bomb.explosion();
			unregisterArrow();
		}
		@Override
		public void interactWith(FireSpell fireSpell) {
			fireSpell.turnOff();
		}
	}
}

