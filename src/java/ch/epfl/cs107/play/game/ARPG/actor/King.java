package java.ch.epfl.cs107.play.game.ARPG.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.ARPG.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class King extends AreaEntity{
		private Sprite king;
		private String message;
		private Dialog dialog;
		private boolean interacted;
		private int interactedTime;
		
		public King(Area area, Orientation orientation, DiscreteCoordinates position) {
			super(area, orientation, position);
			message="Nous vous felicitons soldat vous avez sauvez le Royaume";
			dialog=new Dialog(message, "zelda/dialog", getOwnerArea());
			creerRoi();
			interacted=false;
			interactedTime=0;
		}
		public void update(float deltaTime) {
			if(interacted) {
				interactedTime+=deltaTime;
				if(interactedTime>=5){
					interacted=false;
					interactedTime=0;
				}
			}
			super.update(deltaTime);
		}
		
		public void creerRoi() {
				king = new Sprite("zelda/king", 1f, 2f, this , 
						new RegionOfInterest(0, 64, 32, 32));	
				king.setDepth(-20);
		}
		
		@Override
		 public void acceptInteraction(AreaInteractionVisitor v) {
			((ARPGInteractionVisitor) v).interactWith(this);
		}
		
		@Override
		public void draw(Canvas canvas) {
			king.draw(canvas);
			if(interacted) {
				dialog.draw(canvas);
			}
		}
		
		@Override
		public List<DiscreteCoordinates> getCurrentCells() {
			return Collections.singletonList(getCurrentMainCellCoordinates());
		}
		
		public void interacted() {
			interacted=true;
		}
		
		@Override
		public boolean takeCellSpace() {
			return true;
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
