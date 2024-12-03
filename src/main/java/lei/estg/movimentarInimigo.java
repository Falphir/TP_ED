package lei.estg;

import lei.estg.dataStructures.Network;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.models.Divisao;
import lei.estg.models.Inimigo;
import lei.estg.utils.ControlarJogo;

public class movimentarInimigo {
    public static void main(String[] args) {
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
        edificio.addEdge(sala1, sala2, 30);  // Peso 5 entre Sala1 e Sala2
        edificio.addEdge(sala2, sala3, 10); // Peso 10 entre Sala2 e Sala3
        edificio.addEdge(sala3, sala4, 0);  // Peso 7 entre Sala3 e Sala4
        System.out.println("network: " + edificio.toString());

        // 4. Criar o inimigo na Sala1
        Inimigo inimigo = new Inimigo("Guarda Diogo", 20, sala1);
        Inimigo inimigo2 = new Inimigo("Guarda Tiago", 10, sala2); // Poder do inimigo é 20

        // 5. Verificar os pesos antes do movimento
        System.out.println("Pesos antes da movimentação:");
        imprimirPesos(edificio);

        // 6. Criar controlador de jogo e movimentar o inimigo
        ControlarJogo controlador = new ControlarJogo();
        controlador.movimentarInimigo(inimigo, edificio);
        controlador.movimentarInimigo(inimigo2, edificio);

        // 7. Verificar os pesos após o movimento
        System.out.println("Pesos após a movimentação:");
        imprimirPesos(edificio);
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
