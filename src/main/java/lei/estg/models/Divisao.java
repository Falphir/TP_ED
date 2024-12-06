package lei.estg.models;

import lei.estg.dataStructures.UnorderedArrayList;

public class Divisao {
    private String nome;
    private UnorderedArrayList<Inimigo> inimigos;
    private UnorderedArrayList<Item> items;
    private boolean isEntradaSaida;
    private Alvo alvo;

    public Divisao(String nome) {
        this.nome = nome;
        this.inimigos = new UnorderedArrayList<Inimigo>();
        this.items = new UnorderedArrayList<Item>();
        this.isEntradaSaida = false;
        this.alvo = null;
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

    public UnorderedArrayList<Item> getItems() {
        return items;
    }

    public void setItems(UnorderedArrayList<Item> items) {
        this.items = items;
    }

    public boolean isEntradaSaida() {
        return isEntradaSaida;
    }

    public void setEntradaSaida(boolean entradaSaida) {
        isEntradaSaida = entradaSaida;
    }

    public Alvo getAlvo() {
        return alvo;
    }

    public void setAlvo(Alvo alvo) {
        this.alvo = alvo;
    }

    @Override
    public String toString() {
        return "Divisao: " + nome;
    }
}
