package lei.estg.models;

import lei.estg.models.enums.EMissaoTipo;

public class Alvo {
    private String tipo;

    public Alvo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
