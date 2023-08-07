package java.ch.epfl.cs107.play.game.ARPG.actor;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Coin extends CollectableAreaEntity{
	private Sprite[] pieces = new Sprite[4];
	final int valeur = 10;
	private Animation animation;
	private final static int ANIMATION_DURATION = 5;
	
	public Coin(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		creerPieces();
		
	}
	public void creerPieces() {
		for(int i = 0 ; i < pieces.length ; ++i) {
			pieces[i] = new RPGSprite("zelda/coin", 1f, 1f, this , 
					new RegionOfInterest(16*i, 0, 16, 16));
		}
		animation = new Animation(ANIMATION_DURATION/2, pieces, true );
	}
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		animation.update(deltaTime);
	}
	@Override
	 public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor) v).interactWith(this);
	}
	@Override
	public void draw(Canvas canvas) {
		animation.draw(canvas);
	}

}
