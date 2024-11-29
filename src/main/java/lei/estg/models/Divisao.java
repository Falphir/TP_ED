package lei.estg.models;

import lei.estg.dataStructures.UnorderedArrayList;

public class Divisao {
    private String nome;
    private UnorderedArrayList<Inimigo> inimigos;

    public Divisao(String nome, UnorderedArrayList<Inimigo> inimigos) {
        this.nome = nome;
        this.inimigos = inimigos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UnorderedArrayList<Inimigo> getInimigos() {
        return inimigos;
    }

    public void setInimigos(UnorderedArrayList<Inimigo> inimigos) {
        this.inimigos = inimigos;
    }
}
