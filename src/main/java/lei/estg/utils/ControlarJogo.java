package lei.estg.utils;

import java.util.Iterator;
import java.util.Random;

import lei.estg.dataStructures.Network;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.models.Divisao;
import lei.estg.models.Inimigo;

public class ControlarJogo {
    
    public void movimentarInimigo(Inimigo inimigo, Network<Divisao> edificio) {
        Random random = new Random();
        Divisao divisaoAtual = inimigo.getDivisao();
        int movimentos = 0;

        while (movimentos < 2) {
            UnorderedArrayList<Divisao> divisoesAdjacentes = edificio.getAdjVertex(divisaoAtual);
            if (divisoesAdjacentes.isEmpty()) {
                System.out.println("Erro: divisão sem ligações.");
                break;
            }

            Iterator<Divisao> iter = divisoesAdjacentes.iterator();
            int index = random.nextInt(divisoesAdjacentes.size());
            Divisao novaDivisao = null;
            int count = 0;

            while (iter.hasNext()) {
                Divisao divisao = iter.next();
                if (count == index) {
                    novaDivisao = divisao;
                    break;
                }
                count++;
            }

            resetarPeso(divisaoAtual, edificio, inimigo);
            atualizarPeso(novaDivisao, edificio, inimigo);

            inimigo.moverInimigo(novaDivisao);
            divisaoAtual = novaDivisao;
            movimentos++;
        }
    }
    
    private void resetarPeso(Divisao divisao, Network<Divisao> edificio, Inimigo inimigo) {
        UnorderedArrayList<Divisao> divisaoAdjacente = edificio.getAdjVertex(divisao);
        Iterator<Divisao> iter = divisaoAdjacente.iterator();

        while (iter.hasNext()) {
            Divisao adjacente = iter.next();

            double pesoAtual = edificio.getWeight(divisao, adjacente);
    
            double novoPeso = pesoAtual - inimigo.getPoder();

            edificio.updateEdgeWeight(divisao, adjacente, novoPeso);
        }
    }

    private void atualizarPeso(Divisao divisao, Network<Divisao> edificio, Inimigo inimigo) {
        UnorderedArrayList<Divisao> divisaoAdjacente = edificio.getAdjVertex(divisao);
        Iterator<Divisao> iter = divisaoAdjacente.iterator();
    
        while (iter.hasNext()) {
            Divisao adjacente = iter.next();
    
            double pesoAtual = edificio.getWeight(divisao, adjacente);
    
            double novoPeso = pesoAtual + inimigo.getPoder();
    
            edificio.updateEdgeWeight(divisao, adjacente, novoPeso);
        }
    }
}
