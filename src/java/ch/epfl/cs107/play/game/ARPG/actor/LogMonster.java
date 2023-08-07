package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;



public class LogMonster extends Monster{

	//Type enumere donnant les differents etats du monstre
	public enum State{
		IDLE(),
		ATTACKING(),
		FALLINGASLEEP(),
		SLEEPING(),
		WAKINGUP();
	}

	private final int ANIMATION_DURATION = 20;
	private final int RANGE=8;
	private final float DAMAGE=0.3f;
	private final float SIMULATION_STEPS_MAX=2;
	private float simulationSteps;
	private final float MIN_SLEEPING_DURATION=1;
	private final float MAX_SLEEPING_DURATION=5;
	private float sleepingDuration;
	private State state;
	private Animation[] animationIdle;
	private Animation animationSleeping;
	private Animation animationFallingAsleep;
	private Animation animationWakingUp;
	private Animation currentAnimation;
	private ARPGLogMonsterHandler arpgLogMonsterHandler;

	//Initialise le monstre en appelant le constructeur de la superClasse et en initialisant ses differentes animation, vulnerabilite et son etat
	public LogMonster(Area area, Orientation orientation, DiscreteCoordinates coordinates, int maxHp) {
		super(area, orientation, coordinates, maxHp);
		simulationSteps=0;
		super.setVulnerability("par dommage physique");
		super.setVulnerability("par le feu");
		arpgLogMonsterHandler =new ARPGLogMonsterHandler();

		Sprite[][] idleArray= RPGSprite.extractSprites("zelda/logMonster", 4, 2, 2, this, 32, 32,new Vector(-0.5f,0.25f), new Orientation[] {Orientation.DOWN, Orientation.RIGHT, 
				Orientation.UP, Orientation.LEFT});
		animationIdle = RPGSprite.createAnimations(ANIMATION_DURATION/2, idleArray,true);

		RPGSprite[] sleepingArray = new RPGSprite[4];
		for (int i = 0; i < sleepingArray.length; ++i) {
			sleepingArray[i]=new RPGSprite("zelda/logMonster.sleeping",2, 2, this, new RegionOfInterest(0, i*32, 32, 32),new Vector(-0.5f,0.25f));
		}
		animationSleeping = new Animation(ANIMATION_DURATION/2, sleepingArray,true);

		RPGSprite[] fallingAsleepArray = new RPGSprite[3];
		for (int i = 0; i < fallingAsleepArray.length; ++i) {
			fallingAsleepArray[i]=new RPGSprite("zelda/logMonster.wakingUp", 2, 2, this, new RegionOfInterest(0, 64-i*32, 32, 32),new Vector(-0.5f,0.25f));
		}
		animationFallingAsleep = new Animation(ANIMATION_DURATION/4, fallingAsleepArray,false);

		RPGSprite[] WakingUpArray = new RPGSprite[3];
		for (int i = 0; i < WakingUpArray.length; ++i) {
			WakingUpArray[i]=new RPGSprite("zelda/logMonster.wakingUp", 2, 2, this, new RegionOfInterest(0, i*32, 32, 32),new Vector(-0.5f,0.25f));
		}
		animationWakingUp = new Animation(ANIMATION_DURATION/2, WakingUpArray,false);

		changeAnimation();
		state=State.IDLE;
		sleepingDuration=10;
	}

	public void update(float deltaTime) {
		//Si le monstre est mort son animation devient celle d'un monstre mort
		if(death()) {
			currentAnimation=getDeathAnimation();
		} else {
			switch(state) {
			case IDLE:
				//Initialise des moments d'inaction
				randomInactionMoment();
				if(simulationSteps<SIMULATION_STEPS_MAX) {
					simulationSteps+=deltaTime;
				} else {
					//Adapte l'animation du logMonster et opere a un changement de direction aleatoire
					changeAnimation();
					int randomInt = RandomGenerator.getInstance().nextInt(10);
					if(randomInt<2 || !isDisplacementOccurs()) {
						changeOrientation();
						move(ANIMATION_DURATION);
					}	
				}
				break;
			case ATTACKING:
				//Le monstre fonce sur le player
				currentAnimation.setSpeedFactor(ANIMATION_DURATION/10);
				move(ANIMATION_DURATION/5);
				if(!isDisplacementOccurs()) {
					state=State.FALLINGASLEEP;
				}
				break;
			case FALLINGASLEEP:
				//Le monstre tombe dans le sommeil quand son animation est finie il dort et initialisation du temps de sommeil
				currentAnimation=animationFallingAsleep;
				setSleepingDuration();
				if(currentAnimation.isCompleted()) {
					currentAnimation.reset();
					state=State.SLEEPING;
				}
				break;
			case SLEEPING:
				//Gere le reveil du monstre quand son temps de sommeil est depasse
				currentAnimation=animationSleeping;
				if(sleepingDuration>0) {
					sleepingDuration-=deltaTime;
				} else {
					state=State.WAKINGUP;
				}
				break;
			case WAKINGUP:
				//Animation de reveil et retour a un etat normal
				currentAnimation=animationWakingUp;
				if(currentAnimation.isCompleted()) {
					currentAnimation.reset();
					simulationSteps=0;
					changeAnimation();
					state=State.IDLE;
				}
				break;
			}
			super.update(deltaTime);
		}
		//Gere l'animation si deplacement
		if(state==State.IDLE) {
			if(isDisplacementOccurs()) {
				currentAnimation.update(deltaTime);
			}else {
				currentAnimation.reset();
			}} else {
				currentAnimation.update(deltaTime);
			}
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		//Creation du champ de vision du monstre en fonction de sa portee
		List<DiscreteCoordinates> array = new ArrayList<DiscreteCoordinates>();
		array.add(getCurrentMainCellCoordinates().jump((getOrientation().toVector())));
		for(int i=0;i<RANGE;i++) {
			array.add(array.get(i).jump(getOrientation().toVector()));
		}
		if(state==State.ATTACKING) {
			return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
		} else {
			return array;
		}
	}
	
	//Creation d'un moment d'inaction avec une probabilite de 1 pour 100
	private void randomInactionMoment() {
		int probabilite = 100;
		int randomInt = RandomGenerator.getInstance().nextInt(probabilite);
		if(randomInt==0) {
			simulationSteps=0;
		}
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(arpgLogMonsterHandler);
	}
	@Override
	public boolean wantsViewInteraction() {
		// TODO Auto-generated method stub
		return !death();
	}
	@Override
	public boolean wantsCellInteraction() {
		// TODO Auto-generated method stub
		return false;
	}

	//Represente l'animation du logMonster
	public void draw(Canvas canvas) {
		currentAnimation.draw(canvas);
		if(death()) {		
			if(currentAnimation.isCompleted()) {
				letObject();
				getOwnerArea().unregisterActor(this);
			}
		}
	}

	//Adaptation de l'animation en fonction de son orientation
	@Override
	public void changeAnimation() {
		switch(getOrientation()) {
		case DOWN:					
			currentAnimation= animationIdle[2];
			break;
		case UP: 
			currentAnimation= animationIdle[1];
			break;
		case LEFT:
			currentAnimation= animationIdle[3];	
			break;
		case RIGHT:
			currentAnimation= animationIdle[0];		
			break;
		}
	}
	
	//Initialise le temps de sommeil du monstre
	public void setSleepingDuration() {
		int interval=(int)(MAX_SLEEPING_DURATION-MIN_SLEEPING_DURATION);
		int randomInt = RandomGenerator.getInstance().nextInt(interval);
		sleepingDuration=randomInt+MIN_SLEEPING_DURATION;
	}
	@Override
	public boolean takeCellSpace() {
		return !death();
	}
	
	//Depose un coeur sur le sol si le monstre est mort
	@Override
	public void letObject() {
		if(death()) {
			Heart heart=new Heart(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
			getOwnerArea().registerActor(heart);
		}
	}
	
	//Classe gerant les interactions du monstre
	private class ARPGLogMonsterHandler implements ARPGInteractionVisitor{
		@Override
		public void interactWith(ARPGPlayer arpgPlayer) {
			if(state==State.IDLE) {
				if(!isDisplacementOccurs()) {
					state=State.ATTACKING;
				}
			}if(state==State.ATTACKING) {
				arpgPlayer.removeHp(DAMAGE);
			}
		}
	}
}	
