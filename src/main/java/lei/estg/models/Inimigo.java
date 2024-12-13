package lei.estg.models;

import lei.estg.models.Interfaces.EntidadeADT;

/**
 * The type Inimigo.
 *
 * @param <T> the type parameter
 */
public class Inimigo<T extends EntidadeADT<T>> implements EntidadeADT<T> {
    private String nome;
    private int poder;

    /**
     * Instantiates a new Inimigo.
     *
     * @param nome  the nome
     * @param poder the poder
     */
    public Inimigo(String nome, int poder) {
        this.nome = nome;
        this.poder = poder;
    }

    /**
     * Receives damage.
     *
     * @param dano the amount of damage to be received
     */
    @Override
    public void receberDano(int dano) {
        this.poder -= dano;
        if (this.poder < 0) this.poder = 0;
        System.out.println(nome + " recebeu " + dano + " de dano. Poder restante: " + poder);
    }

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
    @Override
    public void atacar(T alvo) {
        alvo.receberDano(poder);
    }

    /**
     * Moves the entity to a specified destination division.
     *
     * @param destino The destination division where the entity will be moved to.
     *                This parameter represents the new location for the entity.
     */
    @Override
    public void mover(Divisao destino) {
        destino.getInimigos().addToRear(this);
    }

    /**
     * Gets nome.
     *
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets nome.
     *
     * @param nome the nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Gets poder.
     *
     * @return the poder
     */
    public int getPoder() {
        return poder;
    }

    /**
     * Sets poder.
     *
     * @param poder the poder
     */
    public void setPoder(int poder) {
        this.poder = poder;
    }

    @Override
    public String toString() {
        return String.format("- Inimigo: %s | Poder: %d", nome, poder);
    }

}
