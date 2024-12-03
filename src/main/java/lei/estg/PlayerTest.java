package lei.estg;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.Divisao;
import lei.estg.models.Inimigo;
import lei.estg.models.Item;
import lei.estg.models.Player;
import lei.estg.models.enums.EItemTipo;

public class PlayerTest {
    public static void main(String[] args) {
        // Criação de um jogador e um inimigo para testar interações
        Player player = new Player("Jogador 1", 20, 100, 5);
        Inimigo inimigo = new Inimigo("Inimigo 1", 50, new Divisao("Divisão A"));

        // Teste de dano recebido
        System.out.println("\n--- Teste de levar dano ---");
        inimigo.atacar(player); // O dano será absorvido pelo colete, se possível


        // Teste de dar dano ao inimigo
        System.out.println("\n--- Teste de dar dano ---");
        player.atacar(inimigo); // O inimigo deve ter seu poder reduzido

        // Teste de pegar um item do tipo KIT
        Item kit = new Item("Kit Básico", new Divisao("Divisão B"), 20, EItemTipo.KIT);
        System.out.println("\n--- Teste de apanhar item (KIT) ---");
        player.apanharItem(kit);
        player.apanharItem(kit);
        player.apanharItem(kit);
        player.apanharItem(kit);
        player.apanharItem(kit);
        player.apanharItem(kit); // O kit deve ser guardado na mochila

        // Teste de pegar um item do tipo COLETE
        Item colete = new Item("Colete Básico", new Divisao("Divisão C"), 30, EItemTipo.COLETE);
        System.out.println("\n--- Teste de apanhar item (COLETE) ---");
        player.apanharItem(colete); // O colete deve ser equipado
        Item colete2 = new Item("Colete Básico", new Divisao("Divisão C"), 30, EItemTipo.COLETE);
        System.out.println("\n--- Teste de apanhar item (COLETE) ---");
        player.apanharItem(colete2); // O colete deve ser equipado

        // Teste de curar o jogador com um kit
        System.out.println("\n--- Teste de cura com kit ---");
        try {
            player.usarKit();
        } catch (EmptyStackException e) {
            e.printStackTrace();
        } // O jogador deve ter sua vida restaurada
        System.out.println("Vida do jogador após cura: " + player.getVida());
        player.apanharItem(kit); 

        // Exibição final do estado do jogador
        System.out.println("\n--- Estado final do jogador ---");
        System.out.println("Vida do jogador: " + player.getVida());
        System.out.println("Vida do colete: " + player.getVidaColete());
    }
}
