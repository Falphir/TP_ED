package lei.estg.models;

import lei.estg.dataStructures.ArrayStack;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.enums.EItemTipo;

public class Player {
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

    public void receberDano(int dano) {
        if (vidaColete > 0) {
            // Primeiro subtrai o dano do colete
            if (dano <= vidaColete) {
                vidaColete -= dano;
                System.out.println(nome + " levou " + dano + " de dano, mas foi absorvido pelo colete. Vida do Colete: " + vidaColete);
            } else {
                // Dano excede a proteção do colete
                int danoRestante = dano - vidaColete;
                vidaColete = 0; // Colete é destruído
                this.vida -= danoRestante;
                System.out.println(nome + " levou " + dano + " de dano. Colete destruído! Vida Atual: " + vida);
            }
        } else {
            // Se não houver colete, aplica dano diretamente à vida
            this.vida -= dano;
            if (this.vida < 0) {
                this.vida = 0; // Garante que a vida não é negativa
            }
            System.out.println(nome + " levou " + dano + " de dano. Vida Atual: " + vida);
        }
    }
    

    public void atacar(Inimigo inimigo) {
        inimigo.receberDano(poder);
        if (inimigo.getPoder() == 0) {
            System.out.println(nome + " derrotou " + inimigo.getNome());
        } 
        System.out.println(nome + " atacou " + inimigo.getNome() + " causando " + poder + " de dano.");
        System.out.println("Poder atual de " + inimigo.getNome() + ": " + inimigo.getPoder());
    }

    public void moverPlayer(Divisao destino) {

    }

    public void apanharItem(Item item) {
        EItemTipo itemTipo = item.getTipo();
        if (itemTipo.equals(EItemTipo.KIT)) {
            guardarKitMochila(item);
        } else {
            usarColete(item);
        }
    }

    public void guardarKitMochila(Item kit) {
        if (mochila.size() < mochilaLimite) {
            mochila.push(kit);
            //kit.setLocalizacao(null);
            System.out.println("Kit guardado na mochila com sucesso.");
        } else {
            System.out.println("Mochila cheia. Não é possível guardar mais kits.");
        }
    }

    public void usarKit() throws EmptyStackException {
        if (mochila.isEmpty()) {
            System.out.println("Mochila vazia. Não é possível usar um kit.");
        } else {
            Item kit = mochila.pop();
            curar(kit);
        }
    }
    
    public void curar(Item kit) {
        this.vida += kit.getPontos();
        if (this.vida > this.vidaMaxima) {
            this.vida = this.vidaMaxima;
        }
        System.out.println(nome + " usou um kit e agora tem " + vida + " pontos de vida.");
    }

    protected void usarColete(Item colete) {
        if (isColeteEquipado()) {
            System.out.println("Já existe um colete equipado.");
            return;
        }
        this.vidaColete = colete.getPontos(); // Define a proteção do colete
        //colete.setLocalizacao(null);
        System.out.println(nome + " equipou um colete com " + vidaColete + " pontos de proteção.");
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
