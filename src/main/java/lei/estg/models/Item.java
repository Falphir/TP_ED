package lei.estg.models;

public abstract class Item {
    private Divisao localizacao;
    private int pontos;
    private String tipo;

    public Item(Divisao localizacao, int pontos, String tipo) {
        this.localizacao = localizacao;
        this.pontos = pontos;
        this.tipo = tipo;
    }

    public Divisao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Divisao localizacao) {
        this.localizacao = localizacao;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
