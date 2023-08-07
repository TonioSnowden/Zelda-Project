package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.ARPG.handler.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class FlameSkull extends Monster implements FlyableEntity{
	private final int ANIMATION_DURATION = 10;
	private Sprite[][] sprites;
	private final float MIN_LIFE_TIME=20;
	private final float MAX_LIFE_TIME=30;
	private float lifeTime;
	private Animation[] animation;
	private Animation currentAnimation;
	private ARPGFlameSkullHandler flameSkullHandler;
	private final float DAMAGE;

	//Initialise le monstre en appelant le constructeur de la superClasse et en initialisant ses differentes animations et vulnerabilites ainsi que son temps de vie
	public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates coordinates, int maxHp) {
		super(area, orientation, coordinates, maxHp);
		lifeTimeCreator();
		sprites=RPGSprite.extractSprites("zelda/flameSkull", 3, 2, 2, this, 32, 32,new Vector(-0.5f,0f), new Orientation[] {Orientation.UP, Orientation.LEFT, 
				Orientation.DOWN, Orientation.RIGHT});
		animation=RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites,true);
		flameSkullHandler=new ARPGFlameSkullHandler();
		setVulnerability("par dommage physique");
		setVulnerability("par la magie");
		DAMAGE=1;
	}

	//gere le temps de vie du flameskull et le fait changer de trajectoire de maniere aleatoire
	public void update(float deltaTime) {
		lifeTime-=deltaTime;
		if(isWeak()) {
			currentAnimation=getDeathAnimation();
		} else {
			int randomInt = RandomGenerator.getInstance().nextInt(10);
			if(randomInt<2 || !isDisplacementOccurs()) {
				changeOrientation();
				move(ANIMATION_DURATION);
			}
			super.update(deltaTime);
		}
		currentAnimation.update(deltaTime);
	}

	//Gere l'animation en fonction de son orientatiopn
	public void changeAnimation() {
		switch(getOrientation()) {
		case DOWN:					
			currentAnimation= animation[2];
			break;
		case UP: 
			currentAnimation= animation[0];
			break;
		case LEFT:
			currentAnimation= animation[3];	
			break;
		case RIGHT:
			currentAnimation= animation[1];		
			break;
		}
	}

	//Represente l'animation du personnage et le desenregistre si il est mort
	public void draw(Canvas canvas) {
		currentAnimation.draw(canvas);
		if(isWeak()) {		
			if(currentAnimation.isCompleted()) {
				letObject();
				getOwnerArea().unregisterActor(this);
			}
		}
	}
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList (getCurrentMainCellCoordinates());
	}
	@Override
	public boolean wantsCellInteraction() {
		return !isWeak();
	}
	@Override
	public boolean wantsViewInteraction() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(flameSkullHandler);	
	}

	//Initialise son temps de vie
	public void lifeTimeCreator() {
		int interval=(int)(MAX_LIFE_TIME-MIN_LIFE_TIME);
		int randomInt = RandomGenerator.getInstance().nextInt(interval);
		lifeTime=randomInt+MIN_LIFE_TIME;
	}

	public boolean isWeak() {
		return ((lifeTime<0) || death());
	}

	@Override
	public boolean takeCellSpace() {
		return false;
	}

	//Depose une piece a la mort du flameskull
	@Override
	public void letObject() {
		if (death()) {
			Coin coin=new Coin(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
			getOwnerArea().registerActor(coin);
		}

	}

	//Classe gerant les differents interaction du monstres avec les autres acteurs
	private class ARPGFlameSkullHandler implements ARPGInteractionVisitor{

		@Override
		public void interactWith(Monster monster) {
			for(int i=0;i<(monster.getVulnerability()).size();i++) {
				if(("par le feu").equals((monster.getVulnerability()).get(i))) {
					monster.removeHp(DAMAGE);
				}
			}
		}
		@Override
		public void interactWith(ARPGPlayer player) {
			player.removeHp(DAMAGE);
		}
		@Override
		public void interactWith(Grass grass) {
			grass.BeSliced();
		}
		@Override
		public void interactWith(Bomb bomb) {
			bomb.explosion();
		}
	}
}



