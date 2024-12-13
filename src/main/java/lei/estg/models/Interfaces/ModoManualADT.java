package lei.estg.models.Interfaces;

import lei.estg.models.Alvo;
import lei.estg.models.Divisao;
import lei.estg.models.Edificio;
import lei.estg.models.Player;

public interface ModoManualADT extends JogoADT{

    /**
     * Displays all enemies in the building.
     * This method iterates through all divisions in the building, checks for enemies,
     * and prints the details of each enemy found in the divisions.
     *
     * @param edificio the building where the game is taking place
     */
    public void mostrarInimigos(Edificio<Divisao> edificio);

    /**
     * Displays all items in the building.
     * This method iterates through all divisions in the building, checks for items,
     * and prints the details of each item found in the divisions.
     *
     * @param edificio the building where the game is taking place
     */
    public void mostrarItens(Edificio<Divisao> edificio);

    /**
     * Displays the target location in the building.
     * This method checks if the player has already interacted with the target.
     * If the player has interacted with the target, it prompts the player to go to the exit.
     * Otherwise, it displays the division where the target is located.
     *
     * @param player the player whose target location is being displayed
     * @param edificio the building where the game is taking place
     */
    public void mostrarAlvo(Player player, Edificio<Divisao> edificio);

    /**
     * Displays the shortest paths to various targets in the building.
     * This method prints the shortest paths to the nearest kit, vest, target, and exit.
     *
     * @param player the player for whom the paths are being displayed
     * @param edificio the building where the game is taking place
     */
    public void mostrarTodosCaminhosMaisProximos(Player player, Edificio<Divisao> edificio);

    /**
     * Interacts with the target in the specified division.
     * This method checks if the player has already interacted with the target.
     * If not, it sets the player's interaction status to true and removes the target from the division.
     * If the player has already interacted with the target, it prints a message indicating so.
     * If the target is not found in the division, it prints a message indicating that the target is not found.
     *
     * @param player the player interacting with the target
     * @param alvo the target to be interacted with
     * @param divisao the division where the interaction is taking place
     */
    public void interagirComAlvo(Player player, Alvo alvo, Divisao divisao);
}
