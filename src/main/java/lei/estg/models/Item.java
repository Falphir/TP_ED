package lei.estg.models;

public abstract class Item {
    private String nome;
    private Divisao localizacao;
    private int pontos;

    public Item(String nome, Divisao localizacao, int pontos) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.pontos = pontos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
}
