package lei.estg.models;

import lei.estg.models.enums.EMissaoTipo;

public class Alvo {
    private Divisao divisao;
    private String tipo;
    private boolean acaoRealizada = false;

    public Alvo(Divisao divisao, String tipo) {
        this.divisao = divisao;
        this.tipo = tipo;
    }

    public Divisao getDivisao() {
        return divisao;
    }

    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
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
