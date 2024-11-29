package lei.estg.models;

import lei.estg.models.enums.EAlvoTipo;

public class Alvo {
    private Divisao divisao;
    private EAlvoTipo tipo;

    public Alvo(Divisao divisao, EAlvoTipo tipo) {
        this.divisao = divisao;
        this.tipo = tipo;
    }

    public Divisao getDivisao() {
        return divisao;
    }

    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
    }

    public EAlvoTipo getTipo() {
        return tipo;
    }

    public void setTipo(EAlvoTipo tipo) {
        this.tipo = tipo;
    }
}
