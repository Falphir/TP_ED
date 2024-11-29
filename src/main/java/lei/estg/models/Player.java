package lei.estg.models;

import lei.estg.dataStructures.ArrayStack;

public class Player {
    private String nome;
    private int vida;
    private int poder;
    private ArrayStack<Kit> mochila;

    public Player(String nome, int poder, ArrayStack<Kit> mochila) {
        this.nome = nome;
        this.vida = 100;
        this.poder = poder;
        this.mochila = mochila;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getPoder() {
        return poder;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public ArrayStack<Kit> getMochila() {
        return mochila;
    }

    public void setMochila(ArrayStack<Kit> mochila) {
        this.mochila = mochila;
    }
}
