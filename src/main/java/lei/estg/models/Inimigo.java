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

    public void receberDano(int dano) {
        this.poder -= dano;
        if (this.poder < 0) {
            this.poder = 0;
        }
        System.out.println(nome + " levou " + dano + " de dano. Poder Atual: " + poder);
        if (this.poder == 0) {
            System.out.println(nome + " foi derrotado!");
        }
    }

    public void atacar(Player player) {
        player.receberDano(poder);
        if (player.getVida() == 0) {
            System.out.println(nome + " atacou o player e morreu!");
        } else {
            System.out.println(nome + " atacou o player causando " + poder + " de dano.");
        }
    }

    public void moverInimigo(Divisao destino) {
        this.divisao = destino;
        System.out.println(nome + " movido para a divisão " + destino.getNome());
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

    @Override
    public String toString() {
        return "Inimigo{" +
                "nome='" + nome + '\'' +
                ", poder=" + poder +
                ", divisao=" + divisao.getNome() +
                '}';
    }
}
