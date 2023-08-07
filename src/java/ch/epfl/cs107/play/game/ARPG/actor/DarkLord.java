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
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class DarkLord extends Monster{

	public enum State{
		IDLE(),
		ATTACKING(),
		INVOKINGCREATURES(),
		INVOKINGASPELL(),
		TELEPORTING();

	}
	private final int ANIMATION_DURATION = 20;
	private State state;
	private Animation[] darkLordAnimation;
	private Animation[] spellAnimation;
	private Animation currentAnimation;
	private final int RANGE=3;
	private ARPGDarkLordHandler arpgDarkLordHandler;
	private final float MIN_SPELL_WAIT_DURATION=5;
	private final float MAX_SPELL_WAIT_DURATION=10;
	private float waitDuration;
	private float index;
	private final float simulationStepsMax;
	private float simulationSteps;
	private final int TELEPORTATION_RADIUS;
	private DiscreteCoordinates playerPosition;


	//Initialise le monstre en appelant le constructeur de la superClasse et en initialisant ses differentes animations, vulnerabilites et son etat
	public DarkLord(Area area, Orientation orientation, DiscreteCoordinates coordinates, int maxHp) {
		super(area, orientation, coordinates, maxHp);
		super.setVulnerability("par la magie");
		state=State.IDLE;
		TELEPORTATION_RADIUS=8;
		arpgDarkLordHandler=new ARPGDarkLordHandler();
		setWaitDuration();
		index=0;
		simulationStepsMax=2;
		simulationSteps=0;

		Sprite[][] darkLordSprite=RPGSprite.extractSprites("zelda/darkLord", 3, 2, 2, this, 32, 32,new Vector(-0.5f,0.25f),new Orientation[] {Orientation.UP, Orientation.LEFT, 
				Orientation.DOWN, Orientation.RIGHT});
		darkLordAnimation=RPGSprite.createAnimations(ANIMATION_DURATION/3,darkLordSprite,true);

		Sprite[][] spellSprite = RPGSprite.extractSprites("zelda/darkLord.spell", 3, 2, 2, this, 32, 32,new Vector(-0.5f,0.25f), new Orientation[] {Orientation.UP, Orientation.LEFT, 
				Orientation.DOWN, Orientation.RIGHT});
		spellAnimation=RPGSprite.createAnimations(ANIMATION_DURATION/2, spellSprite,false);

		changeAnimation();


	}
	public void update(float deltaTime) {
		//Si il est mort son animation devient celle de la mort d'un monstre
		if(death()) {
			currentAnimation=getDeathAnimation();
		} else {
			//En fonction de son temps d'attente modifie l'état du seigneur des tenebres
			if(index<waitDuration) {
				index+=deltaTime;
			} else {
				index=0;
				int randomInt = RandomGenerator.getInstance().nextInt(10);
				if(randomInt<4) {
					state=State.ATTACKING;
				} else {
					state=State.INVOKINGCREATURES;
				}
			}
			//Proba de creer une flamme devant lui 1 pour 50
			int randomInt1 = RandomGenerator.getInstance().nextInt(50);
			if(randomInt1==0) {
				changeOrientation();
				if(getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))){
					FireSpell firespell = new FireSpell(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates().jump(getOrientation().toVector()),setStrengthFireSpell());
					getOwnerArea().registerActor(firespell);
				}
			}
			switch(state) {
			case IDLE:
				//Creation de moment d'inaction aleatoire
				randomInactionMoment();
				if(simulationSteps<simulationStepsMax) {
					simulationSteps+=deltaTime;
				} else {
					changeAnimation();
					//modification de son orientation de maniere aleatoire
					int randomInt = RandomGenerator.getInstance().nextInt(10);
					if(randomInt<4 || !isDisplacementOccurs()) {
						changeOrientation();
						move(ANIMATION_DURATION);
					}	
				}
				break;
			case ATTACKING:
				//Creation d'une flamme face a lui
				if(getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))){
					FireSpell firespell = new FireSpell(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates().jump(getOrientation().toVector()),setStrengthFireSpell());
					getOwnerArea().registerActor(firespell);
					state=State.IDLE;
				}
				break;
			case INVOKINGCREATURES:
				//Invocation de creature face a lui
				if(getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))){
					changeAnimation();
					if(currentAnimation.isCompleted()) {
						FlameSkull flameskull = new FlameSkull(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates().jump(getOrientation().toVector()),6);
						getOwnerArea().registerActor(flameskull);
						currentAnimation.reset();
						state=State.IDLE;
					}
				}
				break;
			case INVOKINGASPELL:
				//preparation a l teleportation lorsque son animation est finie il se teleporte
				changeAnimation();
				currentAnimation.update(deltaTime);
				if(currentAnimation.isCompleted()) {
					currentAnimation.reset();
					state=State.TELEPORTING;
				}
				break;
			case TELEPORTING:
				//teleportation du seigneur des tenebres
				changeAnimation();
				if(!isDisplacementOccurs()) {
					int arrayIndex=RandomGenerator.getInstance().nextInt(setTeleportingRadius().size());
					if(getOwnerArea().canEnterAreaCells(this,Collections.singletonList(setTeleportingRadius().get(arrayIndex)))) {
						getOwnerArea().leaveAreaCells(this, getCurrentCells());
						this.setCurrentPosition(setTeleportingRadius().get(arrayIndex).toVector());
						getOwnerArea().enterAreaCells(this, getCurrentCells());
						state=State.IDLE;
					}
				}
				break;
			}
			super.update(deltaTime);
		}
		//Reinitialisation de l'animation si le deplacement est finie
		if(state==State.IDLE) {
			if(isDisplacementOccurs()) {
				currentAnimation.update(deltaTime);
			}else {
				currentAnimation.reset();
			}} else {
				currentAnimation.update(deltaTime);
			}
	}

	//Initialisation de la force des flammes
	private float setStrengthFireSpell() {
		int randomInt = RandomGenerator.getInstance().nextInt(5);
		return randomInt+1;
	}

	//Creation de son champ de teleportation par soustraction du champ du player a celui de la teleportation globale en fonction de la portee de teleportation
	private List<DiscreteCoordinates> setTeleportingRadius(){
		List<DiscreteCoordinates> array= new ArrayList<DiscreteCoordinates>();
		for(int x=-TELEPORTATION_RADIUS;x<=TELEPORTATION_RADIUS;x++) {
			for(int y=-TELEPORTATION_RADIUS;y<=TELEPORTATION_RADIUS;y++) {
				array.add(getCurrentMainCellCoordinates().jump(x,y));
			}
		}
		List<DiscreteCoordinates> playerArray = new ArrayList<DiscreteCoordinates>();
		for(int x=-RANGE;x<=RANGE;x++) {
			for(int y=-RANGE;y<=RANGE;y++) {
				playerArray.add(playerPosition.jump(x,y));
			}
		}
		for(int i=0;i<array.size();i++) {
			for(int j=0;j<playerArray.size();j++) {
				if((array.get(i).equals(playerArray.get(j)))){
					array.remove(i);
				}
			}
		}
		return array;

	}
	
    //Champ d'interaction
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		List<DiscreteCoordinates> array = new ArrayList<DiscreteCoordinates>();
		for(int x=-RANGE;x<=RANGE;x++) {
			for(int y=-RANGE;y<=RANGE;y++) {
				array.add(getCurrentMainCellCoordinates().jump(x,y));
			}
		}
		return array;
	}

	@Override
	public boolean wantsViewInteraction() {
		return !death();
	}

	@Override
	public boolean wantsCellInteraction() {
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(arpgDarkLordHandler);
	}

	//represente l'animation est desenregistre le personnage si il meurt
	@Override
	public void draw(Canvas canvas) {
		if(!currentAnimation.isCompleted()) {
			currentAnimation.draw(canvas);
		}
		if(death()) {
			if(currentAnimation.isCompleted()) {
				letObject();
				getOwnerArea().unregisterActor(this);
			}
		}
	}
	
	//crée des moment d'inaction de maniere aleatoire
	private void randomInactionMoment() {
		int probabilite = 100;
		int randomInt = RandomGenerator.getInstance().nextInt(probabilite);
		if(randomInt==0) {
			simulationSteps=0;
		}
	}

	//Gère l'animation en fonction de son état et de son orientation
	@Override
	public void changeAnimation() {
		if(state == State.IDLE || state==State.ATTACKING || state==State.TELEPORTING) {
			switch(getOrientation()) {
			case DOWN:					
				currentAnimation= darkLordAnimation[2];
				break;
			case UP: 
				currentAnimation= darkLordAnimation[0];
				break;
			case LEFT:
				currentAnimation= darkLordAnimation[3];	
				break;
			case RIGHT:
				currentAnimation= darkLordAnimation[1];		
				break;
			}
		} else {
			switch(getOrientation()) {
			case DOWN:					
				currentAnimation= spellAnimation[2];
				break;
			case UP: 
				currentAnimation= spellAnimation[1];
				break;
			case LEFT:
				currentAnimation= spellAnimation[3];	
				break;
			case RIGHT:
				currentAnimation= spellAnimation[0];		
				break;
			}
		}
	}
	
	//initialise le temps d'attente du monstre
	private void setWaitDuration() {
		int interval=(int)(MAX_SPELL_WAIT_DURATION-MIN_SPELL_WAIT_DURATION);
		int randomInt = RandomGenerator.getInstance().nextInt(interval);
		waitDuration=randomInt+MIN_SPELL_WAIT_DURATION;
	}
	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return !death();
	}
	
	//laisse à la mort du personnage la clé pour ouvrir le chateau
	@Override
	public void letObject() {
		if(death()) {
			CastleKey castlekey=new  CastleKey(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
			getOwnerArea().registerActor(castlekey);
		}
		
	}
	
	//Gère les interaction du seigneur des tenebres avec le player(si le player est dans son champ de visioin il passe dans un état de téleportation
	private class ARPGDarkLordHandler implements ARPGInteractionVisitor{
		public void interactWith(ARPGPlayer arpgPlayer) {
			if(state==State.IDLE) {
				playerPosition=arpgPlayer.getCurrentMainCellCoordinates();
				state=State.INVOKINGASPELL;
			}
		}
	}
}
