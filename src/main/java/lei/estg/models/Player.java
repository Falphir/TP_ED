package lei.estg.models;

import lei.estg.dataStructures.ArrayStack;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.Interfaces.EntidadeADT;
import lei.estg.models.Interfaces.PlayerADT;
import lei.estg.models.enums.EItemTipo;

/**
 * The type Player.
 *
 * @param <T> the type parameter
 */
public class Player<T extends EntidadeADT<T>> implements PlayerADT<T> {
    private String nome;
    private int vida;
    private int vidaMaxima;
    private int vidaColete;
    private int poder;
    private ArrayStack<Item> mochila;
    private int mochilaLimite;
    private boolean alvoInteragido;

    /**
     * Instantiates a new Player.
     *
     * @param nome          the nome
     * @param poder         the poder
     * @param vidaMaxima    the vida maxima
     * @param mochilaLimite the mochila limite
     */
    public Player(String nome, int poder, int vidaMaxima, int mochilaLimite) {
        this.nome = nome;
        this.vida = vidaMaxima;
        this.vidaMaxima = vidaMaxima;
        this.vidaColete = 0;
        this.poder = 15;
        this.mochilaLimite = mochilaLimite;
        this.mochila = new ArrayStack<>(mochilaLimite);
        this.alvoInteragido = false;
    }

    /**
     * Receives damage.
     *
     * @param dano the amount of damage to be received
     */
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

    /**
     *
     * This method is responsible for attacking a specified target.
     * The implementation of this method should define how the attack
     * is performed and the effects it has on the target.
     *
     * @param alvo the alvo
     *             The target that will be attacked. This parameter
     *             represents the entity that will receive the attack.
     */
    @Override   
    public void atacar(T alvo) {
        alvo.receberDano(poder);
    }

    /**
     * Moves the entity to a specified destination division.
     *
     * @param destino The destination division where the entity will be moved to.
     *                This parameter represents the new location for the entity.
     */
    @Override
    public void mover(Divisao destino) {
        destino.setPlayer(this);
    }

    /**
     * Handles the action of picking up an item by the player.
     * If the item is a kit, it's stored in the player's backpack.
     * If the item is a vest, it's equipped immediately.
     *
     * @param item The item to be picked up. Can be either a kit or a vest.
     */
    public void apanharItem(Item item) {
        if (item.getTipo().equals(EItemTipo.KIT)) {
            guardarKitMochila(item);
        } else {
            usarColete(item);
        }
    }

    /**
     * Stores a health kit in the player's backpack.
     * If the backpack is full, the kit cannot be stored.
     *
     * @param kit The health kit to be stored in the backpack.
     */
    private void guardarKitMochila(Item kit) {
        if (mochila.size() < mochilaLimite) {
            mochila.push(kit);
            System.out.println("Kit guardado na mochila com sucesso.");
        } else {
            System.out.println("Mochila cheia. Não é possível guardar mais kits.");
        }
    }

    /**
     * Uses a health kit from the player's inventory to restore health.
     * This method attempts to use a health kit from the player's backpack.
     * If successful, it will apply the health kit's effects to the player.
     *
     * @throws EmptyStackException if the player's backpack is empty or there are no health kits available
     */
    @Override
    public void usarKit() throws EmptyStackException {
        if (vida == vidaMaxima) {
            System.out.println("Vida já está no máximo. Não é possível usar um kit.");
            return;
        }

        if (mochila.isEmpty()) {
            System.out.println("Mochila vazia. Não é possível usar um kit.");
        } else {
            Item kit = mochila.pop();
            curar(kit);
        }
    }

    /**
     * Heals the player using a health kit.
     * This method increases the player's health by the amount of points
     * provided by the health kit. If the resulting health exceeds the
     * maximum health, it is capped at the maximum health value.
     *
     * @param kit The health kit used to heal the player.
     */
    private void curar(Item kit) {
        this.vida += kit.getPontos();
        if (this.vida > this.vidaMaxima) {
            this.vida = this.vidaMaxima;
        }
        System.out.println(nome + " usou um kit e agora tem " + vida + " pontos de vida.");
    }

    /**
     * Equips a vest for the player.
     * If a vest is already equipped, it will not equip another one.
     * Otherwise, it sets the player's vest protection points.
     *
     * @param colete The vest to be equipped, providing protection points.
     */
    private void usarColete(Item colete) {
        if (isColeteEquipado()) {
            System.out.println("Já existe um colete equipado.");
        } else {
            this.vidaColete = colete.getPontos();
            System.out.println(nome + " equipou um colete com " + vidaColete + " pontos de proteção.");
        }
    }

    /**
     * Checks if a vest is equipped.
     * This method returns true if the player has a vest equipped,
     * indicated by a positive value of `vidaColete`.
     *
     * @return true if a vest is equipped, false otherwise.
     */
    private boolean isColeteEquipado() {
        return vidaColete > 0;
    }

    /**
     * Gets nome.
     *
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets nome.
     *
     * @param nome the nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Gets vida.
     *
     * @return the vida
     */
    public int getVida() {
        return vida;
    }

    /**
     * Sets vida.
     *
     * @param vida the vida
     */
    public void setVida(int vida) {
        this.vida = vida;
    }

    /**
     * Gets vida colete.
     *
     * @return the vida colete
     */
    public int getVidaColete() {
        return vidaColete;
    }

    /**
     * Sets vida colete.
     *
     * @param vidaColete the vida colete
     */
    public void setVidaColete(int vidaColete) {
        this.vidaColete = vidaColete;
    }

    /**
     * Gets poder.
     *
     * @return the poder
     */
    public int getPoder() {
        return poder;
    }

    /**
     * Sets poder.
     *
     * @param poder the poder
     */
    public void setPoder(int poder) {
        this.poder = poder;
    }

    /**
     * Gets mochila.
     *
     * @return the mochila
     */
    public ArrayStack<Item> getMochila() {
        return mochila;
    }

    /**
     * Sets mochila.
     *
     * @param mochila the mochila
     */
    public void setMochila(ArrayStack<Item> mochila) {
        this.mochila = mochila;
    }

    /**
     * Is alvo interagido boolean.
     *
     * @return the boolean
     */
    public boolean isAlvoInteragido() {
        return alvoInteragido;
    }

    /**
     * Sets alvo interagido.
     *
     * @param alvoInteragido the alvo interagido
     */
    public void setAlvoInteragido(boolean alvoInteragido) {
        this.alvoInteragido = alvoInteragido;
    }

    @Override
    public String toString() {
        return "=== Status "+ nome +" ===" +
                "\nvida: " + vida +
                "\nvidaColete: " + vidaColete +
                "\npoder: " + poder +
                "\nKits na Mochila: " + mochila.size();
    }
}
