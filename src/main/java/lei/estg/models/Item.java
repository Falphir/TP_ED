package lei.estg.models;

import lei.estg.models.enums.EItemTipo;

/**
 * The type Item.
 */
public class Item {
    private String nome;
    private int pontos;
    private EItemTipo tipo;

    /**
     * Instantiates a new Item.
     *
     * @param nome   the nome
     * @param pontos the pontos
     * @param tipo   the tipo
     */
    public Item(String nome, int pontos, EItemTipo tipo) {
        this.nome = nome;
        this.pontos = pontos;
        this.tipo = tipo;
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
     * Gets pontos.
     *
     * @return the pontos
     */
    public int getPontos() {
        return pontos;
    }

    /**
     * Sets pontos.
     *
     * @param pontos the pontos
     */
    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    /**
     * Gets tipo.
     *
     * @return the tipo
     */
    public EItemTipo getTipo() {
        return tipo;
    }

    /**
     * Sets tipo.
     *
     * @param tipo the tipo
     */
    public void setTipo(EItemTipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return String.format("- Item: %s | Pontos: %d | Tipo: %s", nome, pontos, tipo);
    }
}
