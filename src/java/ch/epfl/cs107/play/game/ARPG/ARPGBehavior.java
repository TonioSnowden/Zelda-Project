package java.ch.epfl.cs107.play.game.ARPG;

import ch.epfl.cs107.play.game.ARPG.actor.Bridge;
import ch.epfl.cs107.play.game.ARPG.actor.WaterFall;
import ch.epfl.cs107.play.game.ARPG.handler.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public class ARPGBehavior extends AreaBehavior{
	
	public enum ARPGCellType {
		NULL(0, false,false),
		WALL(-16777216, false,false),
		IMPASSABLE(-8750470, false,true),
		INTERACT(-256, true,true),
		DOOR(-195580, true,true),
		WALKABLE(-1, true,true),;

		final int type;
		final boolean isWalkable;
		final boolean isFlyable;

		ARPGCellType(int type, boolean isWalkable, boolean isFlyable){
			this.type = type;
			this.isWalkable = isWalkable;
			this.isFlyable=isFlyable;
		}

		public static ARPGCellType toType(int type){
			for(ARPGCellType ict : ARPGCellType.values()){
				if(ict.type == type)
					return ict;
			}
			return NULL;
		}
	}

	public ARPGBehavior(Window window, String name){
		super(window, name);
		int height = getHeight();
		int width = getWidth();
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width ; x++) {
				ARPGCellType color = ARPGCellType.toType(getRGB(height-1-y, x));
				setCell(x,y, (Cell) new ARPGCell(x,y,color));
			}
		}
	}
	public class ARPGCell extends Cell {
		private final ARPGCellType type;

		public ARPGCell(int x, int y, ARPGCellType type){
			super(x, y);
			this.type = type;
		}
		protected boolean canLeave(Interactable entity) {
			return true;
		}

		protected boolean canEnter(Interactable entity) {
			if(entity instanceof WaterFall) {
				return !hasNonTraversableContent();
			}
			if(entity instanceof Bridge) {
				return !hasNonTraversableContent();
				}
			if(entity instanceof FlyableEntity) {
				return (type.isFlyable);
			} else {
			return (type.isWalkable && !hasNonTraversableContent());
			}
		}

		public boolean isCellInteractable() {
			return true;
		}

		public boolean isViewInteractable() {
			return false;
		}

		public void acceptInteraction(AreaInteractionVisitor v) {
		}

	}
}


