package lei.estg.models;

public class Inimigo {
    private String nome;
    private int poder;
    private Divisao divisao;

    public Inimigo(String nome, int poder, Divisao divisao) {
        this.nome = nome;
        this.poder = poder;
        this.divisao = divisao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPoder() {
        return poder;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public Divisao getDivisao() {
        return divisao;
    }

    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
    }
}
