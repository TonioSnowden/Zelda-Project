package java.ch.epfl.cs107.play.game.ARPG.handler;

import ch.epfl.cs107.play.game.ARPG.ARPGBehavior.ARPGCell;
import ch.epfl.cs107.play.game.ARPG.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.ARPG.actor.Arrow;
import ch.epfl.cs107.play.game.ARPG.actor.Bomb;
import ch.epfl.cs107.play.game.ARPG.actor.CastleDoor;
import ch.epfl.cs107.play.game.ARPG.actor.CastleKey;
import ch.epfl.cs107.play.game.ARPG.actor.Coin;
import ch.epfl.cs107.play.game.ARPG.actor.FireSpell;
import ch.epfl.cs107.play.game.ARPG.actor.Grass;
import ch.epfl.cs107.play.game.ARPG.actor.Heart;
import ch.epfl.cs107.play.game.ARPG.actor.King;
import ch.epfl.cs107.play.game.ARPG.actor.MagicWaterProjectile;
import ch.epfl.cs107.play.game.ARPG.actor.Monster;
import ch.epfl.cs107.play.game.ARPG.actor.Orbe;
import ch.epfl.cs107.play.game.ARPG.actor.PNJ;
import ch.epfl.cs107.play.game.ARPG.actor.Staff;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor{
	
	default void interactWith(ARPGCell arpgcell) {
	}
    default void interactWith(ARPGPlayer arpgplayer) {	
	}
    default void interactWith(Grass grass) {
    }
    default void interactWith(Monster monster) {
    }
    default void interactWith(Bomb bomb) {
    }
    default void interactWith(Heart heart) {
    }
    default void interactWith(Coin coin) {
    }
    default void interactWith(CastleDoor castleDoor) {
    }
    default void interactWith(FireSpell firespell) {
    }
    default void interactWith(Arrow arrow) {
    }
    default void interactWith(MagicWaterProjectile magicWaterProjectile) {
    }
    default void interactWith(CastleKey castleKey) {
    }
    default void interactWith(Orbe orbe) {
    }
    default void interactWith(Staff staff) {
    }
    default void interactWith(King king) {
    }
    default void interactWith(PNJ pnj) {
    }
}
