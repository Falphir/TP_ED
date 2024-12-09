package lei.estg.utils;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.UnorderedListADT;
import lei.estg.models.*;
import lei.estg.models.Interfaces.JogoADT;
import lei.estg.models.enums.EItemTipo;

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
    public Divisao selecionarEntrada(Player player, Edificio<Divisao> edificio) {
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
        while (escolha < 1 || escolha > entradas.size()) {
            System.out.print("Escolha uma divisão para entrar (1-" + entradas.size() + "): ");
            if (scanner.hasNextInt()) {
                escolha = scanner.nextInt();
            } else {
                scanner.next();
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
            if(!divisaoEscolhida.getInimigos().isEmpty()) {
                try {
                    confronto(player, divisaoEscolhida.getInimigos(), divisaoEscolhida);
                } catch (EmptyStackException e) {
                    e.printStackTrace();
                }
            }
        }

        return divisaoEscolhida;
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

        if (!novaDivisao.getInimigos().isEmpty()) {
            try {
                confronto(player, novaDivisao.getInimigos(), novaDivisao);
            } catch (EmptyStackException e) {
                e.printStackTrace();
            }
        }
        if (novaDivisao.getItems() != null) {
            for(Item item : novaDivisao.getItems()) {
                player.apanharItem(item);
            }
        }

        System.out.println("Player " + player.getNome() + " movido de " +
                divisaoAtual.getNome() + " para " + novaDivisao.getNome());
    }

    public Divisao encontrarPlayer(Player player, Edificio<Divisao> edificio) {
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

            if (divisoesAdjacentes.size() == 0) {
                return;
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

            if (novaDivisao.getPlayer() != null) {
                inimigo.atacar(novaDivisao.getPlayer());
                confronto(novaDivisao.getPlayer(), novaDivisao.getInimigos(), novaDivisao);
                return;
            }

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
    public void confronto(Player player, UnorderedArrayList<Inimigo> inimigos, Divisao divisao) throws EmptyStackException {
        System.out.println("Existem " + inimigos.size() + " inimigos nesta divisão. Confronto Iniciado");
        Iterator<Inimigo> inimigosIterator = inimigos.iterator();  // Obtendo o iterador para percorrer a lista

        while (inimigosIterator.hasNext()) {
            Inimigo inimigo = inimigosIterator.next(); // Pega o próximo inimigo da lista
            System.out.println("Confronto com " + inimigo.getNome() + " iniciado!");

            while (player.getVida() > 0 && inimigo.getPoder() > 0) {
                player.atacar(inimigo);

                if (inimigo.getPoder() > 0) { // Se o inimigo ainda está vivo
                    inimigo.atacar(player);
                } else { // Inimigo derrotado
                    System.out.println("Inimigo " + inimigo.getNome() + " foi derrotado!");
                    inimigosIterator.remove(); // Remove o inimigo derrotado da lista
                    // Não é necessário incrementar o índice, pois a remoção já ajusta o iterador
                    break; // Sai do loop para confrontar o próximo inimigo
                }
            }

            if (player.getVida() <= 0) { // Verifica se o jogador foi derrotado
                System.out.println("Player " + player.getNome() + " morreu!");
                divisao.getPlayer().mover(null);
                return;
            }
        }

        System.out.println("Todos os inimigos na divisão foram derrotados!");
    }

    @Override
    public void mostrarInimigos(Edificio<Divisao> edificio) {
        Iterator<Divisao> divisoes = edificio.getVertex();
        System.out.println("=== Inimigos ===");
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            if (!divisao.getInimigos().isEmpty()) {
                System.out.println("Divisão: " + divisao.getNome());
                for (Inimigo inimigo : divisao.getInimigos()) {
                    System.out.println(inimigo.toString());
                }
            }
        }
    }

    @Override
    public void mostrarItens(Edificio<Divisao> edificio) {
        Iterator<Divisao> divisoes = edificio.getVertex();
        System.out.println("=== Itens ===");
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            if (!divisao.getItems().isEmpty()) {
                System.out.println("Divisão: " + divisao.getNome());
                for (Item item : divisao.getItems()) {
                    System.out.println(item.toString());
                }
            }
        }
    }

    @Override
    public void mostrarAlvo(Edificio<Divisao> edificio) {
        Iterator<Divisao> divisoes = edificio.getVertex();
        System.out.println("=== Alvo ===");
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            if (divisao.getAlvo() != null) {
                System.out.println("O alvo está na Divisão: " + divisao.getNome());
                return;
            }
        }
    }

    @Override
    public void caminhoMaisCurtoKit(Player player, Edificio<Divisao> edificio) {
        Divisao divisaoPlayer = encontrarPlayer(player, edificio);

        Divisao divisaoComKitMaisProximo = null;
        double menorDistancia = Double.POSITIVE_INFINITY;
        Iterator<Divisao> caminhoMaisCurto = null;

        Iterator<Divisao> iter = edificio.getVertex();
        while (iter.hasNext()) {
            Divisao divisao = iter.next();

            for (Item item : divisao.getItems()) {
                if (item.getTipo() == EItemTipo.KIT) {
                    Iterator<Divisao> caminhoAtual = edificio.findShortestPath(divisaoPlayer, divisao);
                    double distancia = edificio.shortestPathWeight(divisaoPlayer, divisao);

                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        divisaoComKitMaisProximo = divisao;
                        caminhoMaisCurto = caminhoAtual;
                    }
                }
            }
        }

        if (divisaoComKitMaisProximo != null) {
            System.out.println("Kit mais próximo encontrado na divisão: " + divisaoComKitMaisProximo.getNome());
            System.out.println("Distância: " + menorDistancia);

            System.out.print("Caminho até o kit: ");
            while (caminhoMaisCurto != null && caminhoMaisCurto.hasNext()) {
                Divisao divisao = caminhoMaisCurto.next();
                System.out.print(divisao.getNome() + " -> ");
            }
            System.out.println("Destino: " + divisaoComKitMaisProximo.getNome());
        } else {
            System.out.println("Nenhum kit encontrado no edifício.");
        }
    }

    @Override
    public void caminhoMaisCurtoColete(Player player, Edificio<Divisao> edificio) {

    }

    @Override
    public void caminhoMaisCurtoAlvo(Player player, Edificio<Divisao> edificio) {

    }

    @Override
    public void caminhoMaisCurtoSaida(Player player, Edificio<Divisao> edificio) {

    }

    @Override
    public boolean verificarFimJogo(Player player, Alvo alvo, boolean playerSaiu) {
        if (player.getVida() == 0) {
            System.out.println("Player morreu!");
            return true;
        } else if (alvo.isAcaoRealizada() && playerSaiu) {
            System.out.println("Fim do Jogo! Player saiu do edifício e interagiu com o alvo!");
            return true;
        } else if (playerSaiu && !alvo.isAcaoRealizada()) {
            System.out.println("Player saiu do edifício! Mas nao interagiu com o alvo!");
            return true;
        }
        return false;
    }

    @Override
    public void verificarVitoria() {

    }
}
