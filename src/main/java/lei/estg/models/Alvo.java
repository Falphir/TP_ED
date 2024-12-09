package lei.estg.models;

import lei.estg.models.enums.EMissaoTipo;

public class Alvo {
    private String tipo;
    private boolean acaoRealizada = false;

    public Alvo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isAcaoRealizada() {
        return acaoRealizada;
    }

    public void setAcaoRealizada(boolean acaoRealizada) {
        this.acaoRealizada = acaoRealizada;
    }
}
