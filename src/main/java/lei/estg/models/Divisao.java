package lei.estg.models;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.models.Interfaces.EntidadeADT;

/**
 * The type Divisao.
 */
public class Divisao {
    private String nome;
    private UnorderedArrayList<Inimigo> inimigos;
    private UnorderedArrayList<Item> items;
    private boolean isEntradaSaida;
    private Player player;
    private Alvo alvo;

    /**
     * Instantiates a new Divisao.
     *
     * @param nome the nome
     */
    public Divisao(String nome) {
        this.nome = nome;
        this.inimigos = new UnorderedArrayList<Inimigo>();
        this.items = new UnorderedArrayList<Item>();
        this.isEntradaSaida = false;
        this.alvo = null;
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
     * Gets inimigos.
     *
     * @return the inimigos
     */
    public UnorderedArrayList<Inimigo> getInimigos() {
        return inimigos;
    }

    /**
     * Sets inimigos.
     *
     * @param inimigos the inimigos
     */
    public void setInimigos(UnorderedArrayList<Inimigo> inimigos) {
        this.inimigos = inimigos;
    }

    /**
     * Gets items.
     *
     * @return the items
     */
    public UnorderedArrayList<Item> getItems() {
        return items;
    }

    /**
     * Sets items.
     *
     * @param items the items
     */
    public void setItems(UnorderedArrayList<Item> items) {
        this.items = items;
    }

    /**
     * Is entrada saida boolean.
     *
     * @return the boolean
     */
    public boolean isEntradaSaida() {
        return isEntradaSaida;
    }

    /**
     * Sets entrada saida.
     *
     * @param entradaSaida the entrada saida
     */
    public void setEntradaSaida(boolean entradaSaida) {
        isEntradaSaida = entradaSaida;
    }

    /**
     * Gets alvo.
     *
     * @return the alvo
     */
    public Alvo getAlvo() {
        return alvo;
    }

    /**
     * Sets alvo.
     *
     * @param alvo the alvo
     */
    public void setAlvo(Alvo alvo) {
        this.alvo = alvo;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets player.
     *
     * @param player the player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "Divisao: " + nome;
    }

}
