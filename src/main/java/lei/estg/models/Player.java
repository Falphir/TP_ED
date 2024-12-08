package lei.estg.models;

import lei.estg.dataStructures.ArrayStack;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.Interfaces.EntidadeADT;
import lei.estg.models.Interfaces.PlayerADT;
import lei.estg.models.enums.EItemTipo;

public class Player<T extends EntidadeADT<T>> implements PlayerADT<T> {
    private String nome;
    private int vida;
    private int vidaMaxima;
    private int vidaColete;
    private int poder;
    private ArrayStack<Item> mochila;
    private int mochilaLimite;

    public Player(String nome, int poder, int vidaMaxima, int mochilaLimite) {
        this.nome = nome;
        this.vida = 100;
        this.vidaMaxima = vidaMaxima;
        this.vidaColete = 0;
        this.poder = 15;
        this.mochilaLimite = mochilaLimite;
        this.mochila = new ArrayStack<>(mochilaLimite);
    }

    @Override
    public void receberDano(int dano) {
        if (vidaColete > 0) {
            if (dano <= vidaColete) {
                vidaColete -= dano;
            } else {
                int danoRestante = dano - vidaColete;
                vidaColete = 0;
                this.vida -= danoRestante;
            }
        } else {
            this.vida -= dano;
            if (this.vida < 0) this.vida = 0;
        }
        System.out.println(nome + " recebeu " + dano + " de dano. Vida restante: " + vida + ". Escudo Restante: " + vidaColete);
    }

    @Override   
    public void atacar(T inimigo) {
        inimigo.receberDano(poder);
    }

    @Override
    public void mover(Divisao destino) {
        destino.setPlayer(this);
    }

    public void apanharItem(Item item) {
        if (item.getTipo().equals(EItemTipo.KIT)) {
            guardarKitMochila(item);
        } else {
            usarColete(item);
        }
    }

    private void guardarKitMochila(Item kit) {
        if (mochila.size() < mochilaLimite) {
            mochila.push(kit);
            System.out.println("Kit guardado na mochila com sucesso.");
        } else {
            System.out.println("Mochila cheia. Não é possível guardar mais kits.");
        }
    }

    @Override
    public void usarKit() throws EmptyStackException {
        if (mochila.isEmpty()) {
            System.out.println("Mochila vazia. Não é possível usar um kit.");
        } else {
            Item kit = mochila.pop();
            curar(kit);
        }
    }

    private void curar(Item kit) {
        this.vida += kit.getPontos();
        if (this.vida > this.vidaMaxima) {
            this.vida = this.vidaMaxima;
        }
        System.out.println(nome + " usou um kit e agora tem " + vida + " pontos de vida.");
    }

    private void usarColete(Item colete) {
        if (isColeteEquipado()) {
            System.out.println("Já existe um colete equipado.");
        } else {
            this.vidaColete = colete.getPontos();
            System.out.println(nome + " equipou um colete com " + vidaColete + " pontos de proteção.");
        }
    }

    private boolean isColeteEquipado() {
        return vidaColete > 0;
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

    public int getVidaColete() {
        return vidaColete;
    }

    public void setVidaColete(int vidaColete) {
        this.vidaColete = vidaColete;
    }

    public int getPoder() {
        return poder;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public ArrayStack<Item> getMochila() {
        return mochila;
    }

    public void setMochila(ArrayStack<Item> mochila) {
        this.mochila = mochila;
    }
}
