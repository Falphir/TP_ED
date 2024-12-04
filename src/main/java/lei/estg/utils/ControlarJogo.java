package lei.estg.utils;

import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import lei.estg.dataStructures.Network;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.models.Divisao;
import lei.estg.models.Inimigo;
import lei.estg.models.Player;

public class ControlarJogo {
    
    public void movimentarPlayer(Player player, Network<Divisao> edificio) {
        Divisao divisaoAtual = player.getDivisao();
        
        UnorderedArrayList<Divisao> divisoesAdjacentes = edificio.getAdjVertex(divisaoAtual);
        if (divisoesAdjacentes.isEmpty()) {
            System.out.println("Erro: divisão sem ligações.");
            return; 
        }

        System.out.println("Você está na divisão: " + divisaoAtual.getNome());
        System.out.println("Divisões disponíveis para movimentação:");
        Iterator<Divisao> iterator = divisoesAdjacentes.iterator();
        int index = 1;
        while (iterator.hasNext()) {
            Divisao divisao = iterator.next();
            System.out.println(index + " - " + divisao.getNome());
            index++;
        }
        
        Scanner scanner = new Scanner(System.in);
        int escolha = -1;
        while (escolha < 1 || escolha > divisoesAdjacentes.size()) {
            System.out.print("Escolha uma divisão (1-" + divisoesAdjacentes.size() + "): ");
            if (scanner.hasNextInt()) {
                escolha = scanner.nextInt();
            } else {
                scanner.next();
            }
        }
    
        iterator = divisoesAdjacentes.iterator();
        Divisao novaDivisao = null;
        index = 1; // Reinicia o contador
        while (iterator.hasNext()) {
            Divisao divisao = iterator.next();
            if (index == escolha) {
                novaDivisao = divisao;
                break;
            }
            index++;
        }
    
        // Atualiza a posição do player
        player.moverPlayer(novaDivisao);
    
        System.out.println("Player " + player.getNome() + " movido de " +
                divisaoAtual.getNome() + " para " + novaDivisao.getNome());
    }
    
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
