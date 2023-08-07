package java.ch.epfl.cs107.play.game.ARPG.area;

import ch.epfl.cs107.play.game.ARPG.ARPGBehavior;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.ARPG.ARPG;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

public abstract class ARPGArea extends Area {		
	private ARPGBehavior behavior;
	
	protected abstract void createArea(); 

	public float getCameraScaleFactor() {
		return ARPG.CAMERA_SCALE_FACTOR;
	}
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		
		if (super.begin(window, fileSystem)) {
			behavior = new ARPGBehavior(window, getTitle());
			setBehavior(behavior);
			createArea();
			return true;
		}
		return false;
	}
	@Override
	public String getTitle() {
		return null;
	}
}

