package lei.estg.models.Interfaces;

import lei.estg.models.Divisao;

/**
 * The interface Entidade adt.
 *
 * @param <T> the type parameter
 */
public interface EntidadeADT<T> {

    /**
     * Receives damage.
     *
     * @param dano the amount of damage to be received
     */
    public void receberDano(int dano);

    /**
     *
     * This method is responsible for attacking a specified target.
     * The implementation of this method should define how the attack
     * is performed and the effects it has on the target.
     *
     * @param alvo the alvo
     *             The target that will be attacked. This parameter
     *             represents the entity that will receive the attack.
     */
    public void atacar(T alvo);

    /**
     * Moves the entity to a specified destination division.
     *
     * @param destino The destination division where the entity will be moved to.
     *                This parameter represents the new location for the entity.
     */
    public void mover(Divisao destino);


}
