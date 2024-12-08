package lei.estg;

import lei.estg.models.Divisao;
import lei.estg.models.Edificio;

import java.util.Iterator;

public class Adjacentes {
    public static void main(String[] args) {
        Divisao divisao1 = new Divisao("Sala 1");
        Divisao divisao2 = new Divisao("Sala 2");
        Divisao divisao3 = new Divisao("Sala 3");

// Criação de Edifício e conexões
        Edificio<Divisao> edificio = new Edificio<>(true, 3);
        edificio.addVertex(divisao1);
        edificio.addVertex(divisao2);
        edificio.addVertex(divisao3);

// Adiciona conexões
        edificio.addEdge(divisao1, divisao2, 10);
        edificio.addEdge(divisao2, divisao3, 10);

// Testa adjacências
        System.out.println("Adjacentes à Sala 1:");
        Iterator<Divisao> adj1 = edificio.getAdjacentes(divisao1);
        while (adj1.hasNext()) {
            System.out.println(adj1.next().getNome());
        }

        System.out.println("Adjacentes à Sala 2:");
        Iterator<Divisao> adj2 = edificio.getAdjacentes(divisao2);
        while (adj2.hasNext()) {
            System.out.println(adj2.next().getNome());
        }

    }
}
