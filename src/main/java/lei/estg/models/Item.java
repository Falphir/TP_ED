package lei.estg.models;

import lei.estg.models.enums.EItemTipo;

public class Item {
    private String nome;
    private int pontos;
    private EItemTipo tipo;

    public Item(String nome, int pontos, EItemTipo tipo) {
        this.nome = nome;
        this.pontos = pontos;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    @Override
    public String toString() {
        return "Item{" +
                "nome='" + nome + '\'' +
                ", pontos=" + pontos +
                ", tipo=" + tipo +
                '}';
    }
}
