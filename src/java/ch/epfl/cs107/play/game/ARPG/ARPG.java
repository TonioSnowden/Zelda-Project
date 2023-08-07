package java.ch.epfl.cs107.play.game.ARPG;

import ch.epfl.cs107.play.game.ARPG.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.ARPG.area.Route;
import ch.epfl.cs107.play.game.ARPG.area.RouteChateau;
import ch.epfl.cs107.play.game.ARPG.area.RouteTemple;
import ch.epfl.cs107.play.game.ARPG.area.Temple;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.ARPG.area.Chateau;
import ch.epfl.cs107.play.game.ARPG.area.Ferme;
import ch.epfl.cs107.play.game.ARPG.area.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends RPG {

	public static float CAMERA_SCALE_FACTOR = 20.f;
	public Player myplayer;

	public void createAreas() {
		addArea(new Ferme());
		addArea(new Village());
		addArea(new Route());
		addArea(new RouteChateau());
		addArea(new Chateau());
		addArea(new RouteTemple());
		addArea(new Temple());
	}

	public String getTitle() {
		return "GAME";
	}

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		if (super.begin(window, fileSystem)) {
			createAreas();
			DiscreteCoordinates coordinates = new DiscreteCoordinates(6, 10);
			String area = "zelda/Ferme";
			Area area1 = setCurrentArea(area, true);
			myplayer = new ARPGPlayer(area1, Orientation.DOWN, coordinates);
			initPlayer(myplayer);
			
			return true;
		} else
			return false;
	}
	
}
