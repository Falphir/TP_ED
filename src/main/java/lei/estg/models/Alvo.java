package lei.estg.models;

import lei.estg.models.enums.EMissaoTipo;

/**
 * The type Alvo.
 */
public class Alvo {
    private String tipo;

    /**
     * Instantiates a new Alvo.
     *
     * @param tipo the tipo
     */
    public Alvo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Gets tipo.
     *
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets tipo.
     *
     * @param tipo the tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
