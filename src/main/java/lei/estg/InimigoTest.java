package lei.estg;

import lei.estg.dataStructures.ArrayStack;
import lei.estg.models.*;

public class InimigoTest {
    public static void main(String[] args) {
        // Criando uma divisão fictícia (assumindo que a classe Divisao existe)
        Divisao divisao = new Divisao("Floresta Sombria");
        
        // Criando um inimigo
        Inimigo inimigo = new Inimigo("Goblin", 40, divisao);

        
        // Criando o jogador com 30 pontos de vida e poder 20
        Player player = new Player("Herói", 20, 100, 5);

        // Mostrando informações iniciais
        System.out.println("Início do combate:");
        System.out.println(player.getNome() + " tem " + player.getVida() + " pontos de vida.");
        System.out.println(inimigo.getNome() + " tem " + inimigo.getPoder() + " de poder.");
        System.out.println();

        // O inimigo ataca o jogador
        inimigo.atacar(player); // O inimigo causa dano ao jogador com base na diferença de poder

        // Mostrando o estado após o ataque
        System.out.println("\nApós o ataque do inimigo:");
        System.out.println(player.getNome() + " tem " + player.getVida() + " pontos de vida.");
        System.out.println(inimigo.getNome() + " tem " + inimigo.getPoder() + " de poder.");
        System.out.println();

        // Agora o jogador ataca o inimigo
        player.atacar(inimigo); // O jogador causa dano ao inimigo

        // Mostrando o estado após o ataque do jogador
        System.out.println("\nApós o ataque do jogador:");
        System.out.println(player.getNome() + " tem " + player.getVida() + " pontos de vida.");
        System.out.println(inimigo.getNome() + " tem " + inimigo.getPoder() + " de poder.");
    }
}
