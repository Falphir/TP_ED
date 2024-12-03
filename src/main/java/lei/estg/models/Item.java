package lei.estg.models;

import lei.estg.models.enums.EItemTipo;

public class Item {
    private String nome;
    private Divisao localizacao;
    private int pontos;
    private EItemTipo tipo;

    public Item(String nome, Divisao localizacao, int pontos, EItemTipo tipo) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.pontos = pontos;
        this.tipo = tipo;
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

    public EItemTipo getTipo() {
        return tipo;
    }

    public void setTipo(EItemTipo tipo) {
        this.tipo = tipo;
    }
}
