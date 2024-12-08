package lei.estg.models;

import lei.estg.models.Interfaces.EntidadeADT;

public class Inimigo<T extends EntidadeADT<T>> implements EntidadeADT<T> {
    private String nome;
    private int poder;

    public Inimigo(String nome, int poder) {
        this.nome = nome;
        this.poder = poder;
    }

    @Override
    public void receberDano(int dano) {
        this.poder -= dano;
        if (this.poder < 0) this.poder = 0;
        System.out.println(nome + " recebeu " + dano + " de dano. Poder restante: " + poder);
    }


    @Override
    public void atacar(T alvo) {
        alvo.receberDano(poder);
    }

    @Override
    public void mover(Divisao destino) {
        destino.getInimigos().addToRear(this);
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


    @Override
    public String toString() {
        return "Inimigo{" +
                "nome='" + nome + '\'' +
                ", poder=" + poder +
                '}';
    }
}
