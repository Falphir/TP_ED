package lei.estg.models.Interfaces;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.*;

/**
 * The interface Jogo adt.
 */
public interface JogoADT {
    /**
     * Terminates the game.
     * This method finds the player's current division, removes the player from it,
     * sets the game as inactive, checks the end game conditions, and exits the system.
     *
     * @param player the player whose game is being terminated
     * @param edificio the building where the game is taking place
     */
    public void terminarJogo(Player player, Edificio<Divisao> edificio);

    /**
     * Selects an entry division for the player.
     * This method iterates through all divisions in the building to find entry/exit points,
     * prompts the player to choose one, and moves the player to the selected division.
     *
     * @param player the player who is selecting the entry division
     * @param edificio the building where the game is taking place
     * @return the selected entry division
     */
    public Divisao selecionarEntrada(Player player, Edificio<Divisao> edificio);

    /**
     * Finds the division where the player is currently located.
     * This method iterates through all divisions in the building to find the one
     * that contains the specified player.
     *
     * @param player the player to be located
     * @param edificio the building where the search is conducted
     * @return the division containing the player, or null if the player is not found
     */
    public Divisao encontrarPlayer(Player player, Edificio<Divisao> edificio);

    /**
     * Moves the player to a new division.
     * This method displays possible divisions, prompts the player to choose one,
     * and moves the player to the selected division. It also handles interactions
     * with enemies and items in the new division.
     *
     * @param player the player to be moved
     * @param edificio the building where the game is taking place
     */
    public void moverPlayer(Player player, Edificio<Divisao> edificio);

    /**
     * Moves the enemy to a new division.
     * This method randomly selects an adjacent division and moves the enemy to it.
     * It also handles interactions with the player if present in the new division.
     *
     * @param inimigo the enemy to be moved
     * @param edificio the building where the game is taking place
     * @throws EmptyStackException if an error occurs during the confrontation
     */
    public void moverInimigo(Inimigo inimigo, Edificio<Divisao> edificio) throws EmptyStackException;

    /**
     * Handles the confrontation between the player and enemies in a division.
     * This method initiates the confrontation, allows the player to choose actions,
     * and processes the turns of both the player and the enemies until the confrontation ends.
     *
     * @param player the player involved in the confrontation
     * @param edificio the building where the confrontation is taking place
     * @param inimigos the list of enemies in the division
     * @param divisao the division where the confrontation is happening
     * @throws EmptyStackException if an error occurs during the confrontation
     */
    public void confronto(Player player, Edificio<Divisao> edificio ,UnorderedArrayList<Inimigo> inimigos, Divisao divisao) throws EmptyStackException;

    /**
     * Checks the end game conditions and updates the game state accordingly.
     * This method verifies if the player has died, if the player has interacted with the target and exited,
     * or if the player has exited without interacting with the target. It prints the appropriate messages,
     * generates a mission report, and returns a boolean indicating whether the game has ended.
     *
     * @param player the player whose game state is being checked
     * @param alvo the target that the player needs to interact with
     * @param playerSaiu a boolean indicating if the player has exited the building
     * @return true if the game has ended, false otherwise
     */
    public boolean verificarFimJogo(Player player, Alvo alvo, boolean playerSaiu);


}
