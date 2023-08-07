package java.ch.epfl.cs107.play.game.ARPG.actor;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class MagicWaterProjectile extends Projectile{
	private final int ANIMATION_DURATION = 10;
	private Animation animation;
	private final float DAMAGE=1f;
	ARPGMagicWaterProjectileHandler arpgMagicWaterProjectileHandler;
	private DiscreteCoordinates distMax;
	
    //Appelle le constructeur de la superClasse et initialise l'animation du projectile magique
	public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position, int range,
			float speed) {
		super(area, orientation, position, range, speed);
		distMax=getPositionMax();
		arpgMagicWaterProjectileHandler=new ARPGMagicWaterProjectileHandler();
		RPGSprite[] sprite= new RPGSprite[4];
		for(int i=0;i<sprite.length;i++) {
		  sprite[i] = new RPGSprite("zelda/magicWaterProjectile",1.f,1.f,this,new RegionOfInterest(i*32,0,32,32));
		}
		animation=new Animation(ANIMATION_DURATION/2,sprite, true);
	}
	
	//Execute le mouvement du projectile magique et la supprime lorsqu'elle a touche un objet ou qu'elle a atteint sa distance maximale
	public void update(float deltaTime) {
		move((int)getSpeed());
		if(positionMax(distMax) || !(isDisplacementOccurs())) {
			move((int)getSpeed());
			getOwnerArea().unregisterActor(this);
		}
		animation.update(deltaTime);
		super.update(deltaTime);
		
	}

	@Override
	public void draw(Canvas canvas) {
		animation.draw(canvas);
	}
	
	//Permet de desenregistrer le projectile magique
	private void unregisterMagicWaterSpell() {
		getOwnerArea().unregisterActor(this);
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(arpgMagicWaterProjectileHandler);
	}
	
	//Classe qui permet de gerer les interactions du projectile magique avec les autres acteurs du jeu
	private class ARPGMagicWaterProjectileHandler implements ARPGInteractionVisitor{
		public void interactWith(Monster monster) {
			for(int i=0;i<(monster.getVulnerability()).size();i++) {
				if(("par la magie").equals((monster.getVulnerability()).get(i))) {
					monster.removeHp(DAMAGE);
				}
			}
			unregisterMagicWaterSpell();
		}
		public void interactWith(FireSpell fireSpell) {
			fireSpell.turnOff();
			unregisterMagicWaterSpell();
		}
	}
}
