package lei.estg;

import lei.estg.dataStructures.Network;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.Divisao;
import lei.estg.models.Inimigo;
import lei.estg.models.Item;
import lei.estg.models.Player;
import lei.estg.models.enums.EItemTipo;
import lei.estg.utils.ControlarJogo;

public class movimentarInimigoEPlayer {
    public static void main(String[] args) throws EmptyStackException {
        // 1. Criar divisões do edifício
        Divisao sala1 = new Divisao("Sala1");
        Divisao sala2 = new Divisao("Sala2");
        Divisao sala3 = new Divisao("Sala3");
        Divisao sala4 = new Divisao("Sala4");

        // 2. Criar a rede (network) do edifício
        Network<Divisao> edificio = new Network<>(true);
        edificio.addVertex(sala1);
        edificio.addVertex(sala2);
        edificio.addVertex(sala3);
        edificio.addVertex(sala4);

        // 3. Adicionar conexões (arestas) com pesos iniciais
        edificio.addEdge(sala1, sala2, 30);  // Peso 30 entre Sala1 e Sala2
        edificio.addEdge(sala2, sala3, 10); // Peso 10 entre Sala2 e Sala3
        edificio.addEdge(sala3, sala4, 0);  // Peso 0 entre Sala3 e Sala4
        System.out.println("network: " + edificio.toString());

        // 4. Criar inimigos nas divisões iniciais
        Inimigo inimigo1 = new Inimigo("Guarda Diogo", 20, sala1);
        Inimigo inimigo2 = new Inimigo("Guarda Tiago", 10, sala2);

        // 5. Criar o player e posicioná-lo na Sala1
        Player player = new Player("Tó Cruz", 15, 125, 3); // Nome, poder, vida máxima e limite da mochila
        player.setDivisao(sala1);

        // 6. Criar alguns itens (kits e coletes)
        Item kit1 = new Item("Kit de Vida Pequeno", sala3, 20, EItemTipo.KIT);
        Item kit2 = new Item("Kit de Vida Grande", sala4, 50, EItemTipo.KIT);
        Item colete1 = new Item("Colete Leve", sala2, 30, EItemTipo.COLETE);

        // 7. Verificar os pesos antes da movimentação
        System.out.println("\nPesos antes da movimentação:");
        imprimirPesos(edificio);

        // 8. Criar controlador de jogo
        ControlarJogo controlador = new ControlarJogo();

        // 9. Movimentar os inimigos
        controlador.movimentarInimigo(inimigo1, edificio);
        controlador.movimentarInimigo(inimigo2, edificio);

        // 10. Verificar os pesos após a movimentação dos inimigos
        System.out.println("\nPesos após a movimentação dos inimigos:");
        imprimirPesos(edificio);

        // 11. Movimentar o player
        System.out.println("\nMovimentação do player:");
        controlador.movimentarPlayer(player, edificio);

        // 12. Interagir com itens na nova divisão
        if (player.getDivisao().equals(sala3)) {
            player.apanharItem(kit1);
        } else if (player.getDivisao().equals(sala4)) {
            player.apanharItem(kit2);
        } else if (player.getDivisao().equals(sala2)) {
            player.apanharItem(colete1);
        }

        // 13. Verificar a nova posição do player
        System.out.println("\nPlayer " + player.getNome() + " está agora na divisão: " + player.getDivisao().getNome());

        // 14. Testar uso do kit (se disponível)
        try {
            System.out.println("\nPlayer usa um kit:");
            player.usarKit();
        } catch (Exception e) {
            System.out.println("Erro ao usar o kit: " + e.getMessage());
        }
    }

    // Método para imprimir os pesos das arestas
    private static void imprimirPesos(Network<Divisao> edificio) {
        for (int i = 0; i < edificio.size(); i++) {
            Divisao divisao1 = edificio.getVertex(i);
            UnorderedArrayList<Divisao> adjacentes = edificio.getAdjVertex(divisao1);

            for (Divisao divisao2 : adjacentes) {
                double peso = edificio.getWeight(divisao1, divisao2);
                System.out.println("Peso entre " + divisao1.getNome() + " e " + divisao2.getNome() + ": " + peso);
            }
        }
    }
}
