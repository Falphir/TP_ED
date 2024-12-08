package lei.estg.utils;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.UnorderedListADT;
import lei.estg.models.Divisao;
import lei.estg.models.Edificio;
import lei.estg.models.Inimigo;
import lei.estg.models.Interfaces.JogoADT;
import lei.estg.models.Player;

import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class ControladorJogo implements JogoADT {
    @Override
    public void iniciarJogo() {

    }

    @Override
    public void terminarJogo() {

    }

    @Override
    public void selecionarEntrada(Player player, Edificio<Divisao> edificio) {
        Iterator<Divisao> divisoes = edificio.getVertex();
        UnorderedListADT<Divisao> entradas = new UnorderedArrayList<>();
        int index = 1;
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            if (divisao.isEntradaSaida()) {
                System.out.println(index + " - " + divisao.getNome());
                entradas.addToRear(divisao);
                index++;
            }
        }

        int escolha = -1;
        Scanner scanner = new Scanner(System.in);
        // Garantir que a escolha é válida
        while (escolha < 1 || escolha > entradas.size()) {
            System.out.print("Escolha uma divisão para entrar (1-" + entradas.size() + "): ");
            if (scanner.hasNextInt()) {
                escolha = scanner.nextInt();
            } else {
                scanner.next();  // Descarta a entrada inválida
            }
        }

        Divisao divisaoEscolhida = null;
        int contador = 1;
        for (Divisao divisao : entradas) {
            if (contador == escolha) {
                divisaoEscolhida = divisao;
                break;
            }
            contador++;
        };

        if (divisaoEscolhida != null) {
            player.mover(divisaoEscolhida);
            System.out.println("Player entrou na divisão: " + divisaoEscolhida.getNome());
    }
    }

    @Override
    public void moverPlayer(Player player, Edificio<Divisao> edificio) {

        Divisao divisaoAtual = encontrarPlayer(player, edificio);
        UnorderedListADT<Divisao> divisoesAdjacentes = new UnorderedArrayList<>();
        Iterator<Divisao> iterator = edificio.getAdjacentes(divisaoAtual);
        int index = 1;
        while (iterator.hasNext()) {
            Divisao divisao = iterator.next();
            System.out.println(index + " - " + divisao.getNome());
            divisoesAdjacentes.addToRear(divisao);
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

        divisaoAtual.setPlayer(null);
        player.mover(novaDivisao);


        System.out.println("Player " + player.getNome() + " movido de " +
                divisaoAtual.getNome() + " para " + novaDivisao.getNome());
    }

    private Divisao encontrarPlayer(Player player, Edificio<Divisao> edificio) {
        Iterator<Divisao> divisoes = edificio.getVertex();
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            if (divisao.getPlayer() != null && divisao.getPlayer().equals(player)) {
                return divisao;
            }
        }
        return null;
    }

    @Override
    public void moverInimigo(Inimigo inimigo, Edificio<Divisao> edificio) throws EmptyStackException {
        Random random = new Random();
        Divisao divisaoAtual = encontrarInimigo(inimigo, edificio);
        int movimentos = 0;

        while (movimentos < 2) {

            Iterator<Divisao> iter = edificio.getAdjacentes(divisaoAtual);
            UnorderedListADT<Divisao> divisoesAdjacentes = new UnorderedArrayList<>();
            while (iter.hasNext()) {
                Divisao divisao = iter.next();
                divisoesAdjacentes.addToRear(divisao);
            }

            int index = random.nextInt(divisoesAdjacentes.size());
            Divisao novaDivisao = null;
            int count = 0;

            Iterator<Divisao> iter2 = divisoesAdjacentes.iterator();
            while (iter2.hasNext()) {
                Divisao divisao = iter2.next();
                if (count == index) {
                    novaDivisao = divisao;
                    break;
                }
                count++;
            }

            resetarPeso(divisaoAtual, edificio, inimigo);
            atualizarPeso(novaDivisao, edificio, inimigo);

            divisaoAtual.getInimigos().remove(inimigo);
            inimigo.mover(novaDivisao);
            System.out.println("Inimigo " + inimigo.getNome() + " movido de " +
                    divisaoAtual.getNome() + " para " + novaDivisao.getNome());

            divisaoAtual = novaDivisao;

            movimentos++;
        }
    }

    private Divisao encontrarInimigo(Inimigo inimigo, Edificio<Divisao> edificio) {
        Iterator<Divisao> divisoes = edificio.getVertex();
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            if (divisao.getInimigos() != null) {
                for(Inimigo i : divisao.getInimigos()) {
                    if (i.equals(inimigo)) {
                        return divisao;
                    }
                }
            }
        }
        return null;
    }

    private void resetarPeso(Divisao divisao, Edificio<Divisao> edificio, Inimigo inimigo) {
        Iterator<Divisao> iter = edificio.getAdjacentes(divisao);

        while (iter.hasNext()) {
            Divisao adjacente = iter.next();

            int pesoAtual = (int) edificio.getWeight(divisao, adjacente);

            int novoPeso = pesoAtual - inimigo.getPoder();

            edificio.updateEdge(edificio.getIndex(divisao), edificio.getIndex(adjacente), novoPeso);
        }
    }

    private void atualizarPeso(Divisao divisao, Edificio<Divisao> edificio, Inimigo inimigo) {
        Iterator<Divisao> iter = edificio.getAdjacentes(divisao);

        while (iter.hasNext()) {
            Divisao adjacente = iter.next();

            int pesoAtual = (int) edificio.getWeight(divisao, adjacente);

            int novoPeso = pesoAtual + inimigo.getPoder();

            edificio.updateEdge(edificio.getIndex(divisao), edificio.getIndex(adjacente), novoPeso);
        }
    }

    @Override
    public boolean verificarFimJogo() {
        return false;
    }

    @Override
    public void verificarVitoria() {

    }
}
