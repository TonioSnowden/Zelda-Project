package java.ch.epfl.cs107.play.game.areagame.actor;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;


/**
 * Represents Interactor object (i.e. it can interact with some Interactable)
 * @see Interactable
 * This interface makes sense only in the "Area Context" with Actor contained into Area Cell
 */
public interface Interactor {

    /**
     * Get this Interactor's current occupying cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    List<DiscreteCoordinates> getCurrentCells();


    /**
     * Get this Interactor's current field of view cells coordinates
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    List<DiscreteCoordinates> getFieldOfViewCells();


    /**@return (boolean): true if this require cell interaction */
    boolean wantsCellInteraction();
    /**@return (boolean): true if this require view interaction */
    boolean wantsViewInteraction();

    /**
     * Do this Interactor interact with the given Interactable
     * The interaction is implemented on the interactor side !
     * @param other (Interactable). Not null
     */
    void interactWith(Interactable other);

}
