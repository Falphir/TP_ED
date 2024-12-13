package lei.estg.models.Interfaces;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.Item;

/**
 * The interface Player adt.
 *
 * @param <T> the type parameter
 */
public interface PlayerADT<T> extends EntidadeADT<T> {

    /**
     * Handles the action of picking up an item by the player.
     * If the item is a kit, it's stored in the player's backpack.
     * If the item is a vest, it's equipped immediately.
     *
     * @param item The item to be picked up. Can be either a kit or a vest.
     */
    public void apanharItem(Item item);

    /**
     * Uses a health kit from the player's inventory to restore health.
     * This method attempts to use a health kit from the player's backpack.
     * If successful, it will apply the health kit's effects to the player.
     *
     * @throws EmptyStackException if the player's backpack is empty or there are no health kits available
     */
    public void usarKit() throws EmptyStackException;

}
