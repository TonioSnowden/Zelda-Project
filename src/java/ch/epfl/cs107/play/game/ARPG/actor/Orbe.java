package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Orbe extends AreaEntity{

		private Sprite[] orbe = new Sprite[6];
		private Animation animation;
		private final static int ANIMATION_DURATION = 3;

		public Orbe(Area area, Orientation orientation, DiscreteCoordinates position) {
			super(area, orientation, position);
			creerOrbe();

		}
		public void creerOrbe() {
			
			for(int i = 0 ; i < orbe.length ; ++i) {
				orbe[i] = new RPGSprite("zelda/orbe", 1f, 1f, this , new RegionOfInterest(32*i, 32*i, 32, 32), new Vector(0f, 0.4f));
			}
			animation = new Animation(ANIMATION_DURATION, orbe, true );
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
		@Override
		public List<DiscreteCoordinates> getCurrentCells() {
			return Collections.singletonList(getCurrentMainCellCoordinates());
		}
		@Override
		public boolean takeCellSpace() {
			return false;
		}
		@Override
		public boolean isCellInteractable() {
			return false;
		}
		@Override
		public boolean isViewInteractable() {
			return true;
		}
	}
