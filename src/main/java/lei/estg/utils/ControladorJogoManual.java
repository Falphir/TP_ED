package lei.estg.utils;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.UnorderedListADT;
import lei.estg.models.*;
import lei.estg.models.Interfaces.ModoManualADT;
import lei.estg.models.enums.EItemTipo;

import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class ControladorJogoManual implements ModoManualADT {
    private boolean isJogoAtivo = true;

    @Override
    public void iniciarJogo() {

    }

    @Override
    public void terminarJogo(Player player, Edificio<Divisao> edificio) {
        Divisao divisao = encontrarPlayer(player, edificio);
        if (divisao != null) {
            divisao.setPlayer(null);
        }
        isJogoAtivo = false;
        verificarFimJogo(player, divisao.getAlvo(), true);
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
        }

        if (divisaoEscolhida != null) {
            player.mover(divisaoEscolhida);
            System.out.println("\033[3m\033[32mPlayer entrou na divisão: " + divisaoEscolhida.getNome() + "\033[0m");
            if (!divisaoEscolhida.getInimigos().isEmpty()) {
                try {
                    confronto(player, edificio ,divisaoEscolhida.getInimigos(), divisaoEscolhida);
                } catch (EmptyStackException e) {
                    e.printStackTrace();
                }
            }
        }

        return divisaoEscolhida;
    }

    @Override
    public void moverPlayer(Player player, Edificio<Divisao> edificio) {

        if(player.getVida() == 0) {
            terminarJogo(player, edificio);
            return;
        }
        System.out.println("===========  Divisões possiveis  ===========");
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
        System.out.println("============================================");


        Scanner scanner = new Scanner(System.in);
        int escolha = -1;
        while (escolha < 1 || escolha > divisoesAdjacentes.size()) {
            System.out.print("Escolha uma divisão (\033[1m\033[33m1-" + divisoesAdjacentes.size() + "\033[0m): ");
            if (scanner.hasNextInt()) {
                escolha = scanner.nextInt();
            } else {
                scanner.next();
            }
        }

        iterator = divisoesAdjacentes.iterator();
        Divisao novaDivisao = null;
        index = 1;
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

        System.out.println("\033[3m\033[32m" + player.getNome() + " movido de " +
                divisaoAtual.getNome() + " para " + novaDivisao.getNome()+ "\033[0m");

        if (!novaDivisao.getInimigos().isEmpty()) {
            try {
                confronto(player, edificio ,novaDivisao.getInimigos(), novaDivisao);
            } catch (EmptyStackException e) {
                e.printStackTrace();
            }
        }
        if (novaDivisao.getItems() != null) {
            for (Item item : novaDivisao.getItems()) {
                player.apanharItem(item);
            }
        }

        if (novaDivisao.getAlvo() != null) {
            player.setAlvoInteragido(true);
        }
    }

    @Override
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
        int maxMovimentos = random.nextInt(2);


        while (movimentos < maxMovimentos) {
            if (inimigo.getPoder() == 0) {
                System.out.println("Inimigo sem poder, saindo do movimento.");
                return;
            }

            Iterator<Divisao> iter = edificio.getAdjacentes(divisaoAtual);
            UnorderedListADT<Divisao> divisoesAdjacentes = new UnorderedArrayList<>();

            while (iter.hasNext()) {
                Divisao divisao = iter.next();
                divisoesAdjacentes.addToRear(divisao);
            }

            if (divisoesAdjacentes.isEmpty()) {
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

            if (novaDivisao == null) {
                System.out.println("Erro: novaDivisao é nula!");
                return;
            }

            resetarPeso(divisaoAtual, edificio, inimigo);
            atualizarPeso(novaDivisao, edificio, inimigo);

            divisaoAtual.getInimigos().remove(inimigo);

            inimigo.mover(novaDivisao);

            if (novaDivisao.getPlayer() != null) {
                System.out.println("\033[3m\033[31mConfronto iniciado com o jogador na nova divisão. \033[0m");
                inimigo.atacar(novaDivisao.getPlayer());
                confronto(novaDivisao.getPlayer(), edificio ,novaDivisao.getInimigos(), novaDivisao);

                if (inimigo.getPoder() == 0) {
                    System.out.println("\033[33mInimigo derrotado durante o confronto. \033[0m");
                    resetarPeso(novaDivisao, edificio, inimigo);
                    return;
                }
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
                for (Inimigo i : divisao.getInimigos()) {
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
    public void confronto(Player player, Edificio<Divisao> edificio, UnorderedArrayList<Inimigo> inimigos, Divisao divisao) throws EmptyStackException {
        System.out.println("Existem " + inimigos.size() + " inimigos nesta divisão. Confronto Iniciado");

        while (!inimigos.isEmpty() && player.getVida() > 0) {
            System.out.println("=== Turno do Player ===");
            System.out.println("1 - Atacar todos os inimigos");
            System.out.println("2 - Usar um kit (perde o turno)");
            System.out.print("Escolha uma ação (1 ou 2): ");

            Scanner scanner = new Scanner(System.in);
            int escolha = scanner.nextInt();

            if (escolha == 1) {
                System.out.println("Você escolheu atacar todos os inimigos!");

                Iterator<Inimigo> inimigosIterator = inimigos.iterator();
                while (inimigosIterator.hasNext()) {
                    Inimigo inimigo = inimigosIterator.next();
                    player.atacar(inimigo);
                    System.out.println("Inimigo " + inimigo.getNome() + " recebeu dano. Poder restante: " + inimigo.getPoder());
                }

            } else if (escolha == 2) {
                if (player.getMochila().isEmpty()) {
                    System.out.println("Mochila vazia. Não é possível usar um kit.");
                    continue;
                } else if (player.getVida() == 100) {
                    System.out.println("Vida já está no máximo. Não é possível usar um kit.");
                    continue;
                } else {
                    player.usarKit();
                    System.out.println("Você usou um kit e recuperou vida!");
                }
            } else {
                System.out.println("Escolha inválida. Por favor, tente novamente.");
                continue;
            }

            System.out.println("=== Turno dos Inimigos ===");
            UnorderedArrayList<Inimigo> inimigosRemover = new UnorderedArrayList<>();
            for (Inimigo inimigo : inimigos) {
                if (inimigo.getPoder() > 0) {
                    inimigo.atacar(player);
                    System.out.println("Inimigo " + inimigo.getNome() + " atacou você. Vida restante: " + player.getVida());

                    if (player.getVida() <= 0) {
                        System.out.println("Player " + player.getNome() + " morreu!");
                        terminarJogo(player, edificio);
                        return;
                    }
                } else {
                    System.out.println("Inimigo " + inimigo.getNome() + " foi derrotado e será removido.");
                    inimigosRemover.addToRear(inimigo);
                }
            }

            for (Inimigo inimigo : inimigosRemover) {
                inimigos.remove(inimigo);
            }
        }

        System.out.println("Todos os inimigos na divisão foram derrotados!");
    }


    @Override
    public void mostrarInimigos(Edificio<Divisao> edificio) {
        Iterator<Divisao> divisoes = edificio.getVertex();
        System.out.println("\033[1m\033[31m============  Inimigos  ============\033[0m");
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            if (!divisao.getInimigos().isEmpty()) {
                System.out.println("\033[34m Divisão: " + divisao.getNome() + "\033[0m");
                for (Inimigo inimigo : divisao.getInimigos()) {
                    System.out.println("\t\033[34m" + inimigo.toString() + "\033[0m");
                }
            }
        }
        System.out.println("\033[1m\033[31m====================================\033[0m");
    }

    @Override
    public void mostrarItens(Edificio<Divisao> edificio) {
        Iterator<Divisao> divisoes = edificio.getVertex();
        System.out.println("\033[1m\033[33m=============  Itens  =============\033[0m");
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            if (!divisao.getItems().isEmpty()) {
                System.out.println("\033[34m Divisão: " + divisao.getNome() + "\033[0m");
                for (Item item : divisao.getItems()) {
                    System.out.println("\t\033[34m " + item.toString() + "\033[0m");
                }
            }
        }
        System.out.println("\033[1m\033[33m===================================\033[0m");
    }

    @Override
    public void mostrarAlvo(Player player, Edificio<Divisao> edificio) {
        System.out.println("\033[1m\033[35m=============  Alvo  =============\033[0m");
        if (encontrarAlvo(edificio) == null && player.isAlvoInteragido()) {
            System.out.println("\033[34m " + player.getNome() + " já interagiu com o Alvo!\033[0m");
            System.out.println("\033[34m Vá para a saída para terminar a missao! \033[0m");
        } else {
            System.out.println("\033[34m O alvo está na Divisão: " + encontrarAlvo(edificio).getNome() + "\033[0m");
        }
        System.out.println("\033[1m\033[35m==================================\033[0m");
    }

    private void caminhoMaisCurtoKit(Player player, Edificio<Divisao> edificio) {
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
            System.out.println("\033[34mKit mais próximo encontrado na divisão: \033[0m\033[1m\033[32m" + divisaoComKitMaisProximo.getNome() + "\033[0m");

            System.out.print("\033[34mCaminho até o kit: \033[0m");
            while (caminhoMaisCurto != null && caminhoMaisCurto.hasNext()) {
                Divisao divisao = caminhoMaisCurto.next();
                if (divisao.equals(divisaoComKitMaisProximo)) {
                    break;
                }
                System.out.print("\033[1m\033[32m"+divisao.getNome() + "\033[0m\033[1m\033[35m -> \033[0m");
            }
            System.out.print("\033[1m\033[32m" + divisaoComKitMaisProximo.getNome() + "\033[0m");
        } else {
            System.out.println("\033[3m\033[31mNenhum kit encontrado no edifício.\033[0m");
        }
    }

    private void caminhoMaisCurtoColete(Player player, Edificio<Divisao> edificio) {
        Divisao divisaoPlayer = encontrarPlayer(player, edificio);
        Divisao divisaoComColeteMaisProximo = null;
        double menorDistancia = Double.POSITIVE_INFINITY;
        Iterator<Divisao> caminhoMaisCurto = null;

        Iterator<Divisao> iter = edificio.getVertex();
        while (iter.hasNext()) {
            Divisao divisao = iter.next();

            for (Item item : divisao.getItems()) {
                if (item.getTipo() == EItemTipo.COLETE) {
                    Iterator<Divisao> caminhoAtual = edificio.findShortestPath(divisaoPlayer, divisao);
                    double distancia = edificio.shortestPathWeight(divisaoPlayer, divisao);

                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        divisaoComColeteMaisProximo = divisao;
                        caminhoMaisCurto = caminhoAtual;
                    }
                }
            }
        }

        if (divisaoComColeteMaisProximo != null) {
            System.out.println("\n\033[34mColete mais próximo encontrado na divisão: \033[0m\033[1m\033[32m" + divisaoComColeteMaisProximo.getNome() + "\033[0m");

            System.out.print("\033[34mCaminho até o Colete: \033[0m");
            while (caminhoMaisCurto != null && caminhoMaisCurto.hasNext()) {
                Divisao divisao = caminhoMaisCurto.next();
                if (divisao.equals(divisaoComColeteMaisProximo)) {
                    break;
                }
                System.out.print("\033[1m\033[32m" + divisao.getNome() + "\033[0m\033[1m\033[35m -> \033[0m");
            }
            System.out.print("\033[1m\033[32m" + divisaoComColeteMaisProximo.getNome() + "\033[0m");
        } else {
            System.out.println("\033[3m\033[31mNenhum colete encontrado no edifício.\033[0m");
        }
    }

    private void caminhoMaisCurtoAlvo(Player player, Edificio<Divisao> edificio) {
        Divisao divisaoPlayer = encontrarPlayer(player, edificio);
        Divisao divisaoAlvo = null;
        double menorDistancia = Double.POSITIVE_INFINITY;
        Iterator<Divisao> caminhoMaisSeguro = null;

        Iterator<Divisao> iter = edificio.getVertex();
        while (iter.hasNext()) {
            Divisao divisao = iter.next();

            if (divisao.getAlvo() != null) {
                Iterator<Divisao> caminhoAtual = edificio.findShortestPath(divisaoPlayer, divisao);
                double distancia = edificio.shortestPathWeight(divisaoPlayer, divisao);

                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    divisaoAlvo = divisao;
                    caminhoMaisSeguro = caminhoAtual;
                }
            }
        }

        if (divisaoAlvo != null) {
            System.out.println("\n\033[34mAlvo encontrado na divisão: \033[0m\033[1m\033[32m" + divisaoAlvo.getNome() + "\033[0m");

            System.out.print("\033[34mCaminho mais seguro até ao Alvo: \033[0m");
            while (caminhoMaisSeguro != null && caminhoMaisSeguro.hasNext()) {
                Divisao divisao = caminhoMaisSeguro.next();
                if (divisao.equals(divisaoAlvo)) {
                    break;
                }
                System.out.print("\033[1m\033[32m" + divisao.getNome() + "\033[0m\033[1m\033[35m -> \033[0m");
            }
            System.out.print("\033[1m\033[32m" + divisaoAlvo.getNome() + "\033[0m");
        } else {
            System.out.println("\033[3m\033[31mAlvo não encontrado.\033[0m");
        }
    }

    private void caminhoMaisCurtoSaida(Player player, Edificio<Divisao> edificio) {
        Divisao divisaoPlayer = encontrarPlayer(player, edificio);

        if (divisaoPlayer.isEntradaSaida()) {
            System.out.println("\n\033[35m\033[3m"+  player.getNome() + " já se encontra na saída! \033[0m");
            return;
        }

        Divisao saidaMaisProxima = null;
        double menorDistancia = Double.POSITIVE_INFINITY;
        Iterator<Divisao> caminhoMaisCurto = null;

        Iterator<Divisao> iter = edificio.getVertex();
        while (iter.hasNext()) {
            Divisao divisao = iter.next();
            if (divisao.isEntradaSaida()) {
                Iterator<Divisao> caminhoAtual = edificio.findShortestPath(divisaoPlayer, divisao);
                double distancia = edificio.shortestPathWeight(divisaoPlayer, divisao);

                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    saidaMaisProxima = divisao;
                    caminhoMaisCurto = caminhoAtual;
                }
            }
        }

        if (saidaMaisProxima != null) {
            System.out.println("\n\033[34mSaída mais segura na divisão: \033[0m\033[1m\033[32m" + saidaMaisProxima.getNome() + "\033[0m");

            System.out.print("\033[34mCaminho até à saída: \033[0m");
            while (caminhoMaisCurto != null && caminhoMaisCurto.hasNext() ) {
                Divisao divisao = caminhoMaisCurto.next();
                if (divisao.equals(saidaMaisProxima)) {
                    break;
                }
                System.out.print("\033[1m\033[32m" + divisao.getNome() + "\033[0m\033[1m\033[35m -> \033[0m");
            }
            System.out.print("\033[1m\033[32m" + saidaMaisProxima.getNome() + "\033[0m\n");
        } else {
            System.out.println("\033[3m\033[31mNao foi possivel encontrar uma saída\033[0m");
        }
    }

    @Override
    public void mostrarTodosCaminhosMaisProximos(Player player, Edificio<Divisao> edificio) {
        System.out.println("\033[33m============  Caminhos mais próximos  ============\033[0m");
        caminhoMaisCurtoKit(player, edificio);
        caminhoMaisCurtoColete(player, edificio);
        caminhoMaisCurtoAlvo(player, edificio);
        caminhoMaisCurtoSaida(player, edificio);
        System.out.println("\033[33m=================================================\033[0m");
    }

    @Override
    public boolean verificarFimJogo(Player player, Alvo alvo, boolean playerSaiu) {
        if (player.getVida() == 0) {
            System.out.println("Player morreu!");
            System.out.println("\033[31mDerrota! Não desistas, tenta novamente!\033[0m");
            return true;
        } else if (player.isAlvoInteragido() && playerSaiu) {
            System.out.println("\033[32mVitória! Parabéns! Continua assim!\033[0m");

            return true;
        } else if (playerSaiu && !player.isAlvoInteragido()) {
            System.out.println("Player saiu do edifício! Mas nao interagiu com o alvo!");
            System.out.println("\033[31mDerrota! Não desistas, tenta novamente!\033[0m");
            return true;
        }
        return false;
    }

    public void interagirComAlvo(Player player, Alvo alvo, Divisao divisao) {
        if (player.isAlvoInteragido()) {
            System.out.println("Player já interagiu com o alvo!");
            return;
        }
        if (alvo != null) {
            System.out.println("Player interagiu com o alvo!");
            player.setAlvoInteragido(true);
            divisao.setAlvo(null);
        } else {
            System.out.println("Alvo não encontrado nesta divisão!");
        }
    }

    public boolean isJogoAtivo() {
        return isJogoAtivo;
    }

    public void setJogoAtivo(boolean jogoAtivo) {
        isJogoAtivo = jogoAtivo;
    }

    private Divisao encontrarAlvo (Edificio<Divisao> edificio) {
        Iterator<Divisao> divisoes = edificio.getVertex();
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            if (divisao.getAlvo() != null) {
                return divisao;
            }
        }
        return  null;
    }

    public void mostrarMapa(Edificio<Divisao> edificio, Player player) {
        Iterator<Divisao> divisoes = edificio.getVertex();

        System.out.println("=== Mapa ===");

        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();

            System.out.println(divisao.getNome() + ":");

            Iterator<Divisao> adjacentes = edificio.getAdjacentes(divisao);

            while (adjacentes.hasNext()) {
                Divisao divisaoAdjacente = adjacentes.next();
                System.out.print("    - " + divisaoAdjacente.getNome());

                boolean hasInfo = false;

                if (divisaoAdjacente.getPlayer() != null) {
                    System.out.print(" (Player aqui)");
                    hasInfo = true;
                }

                if (!divisaoAdjacente.getInimigos().isEmpty()) {
                    System.out.print(" (" + divisaoAdjacente.getInimigos().size() + " inimigos)");
                    hasInfo = true;
                }

                if (divisaoAdjacente.getAlvo() != null) {
                    System.out.print(" (Alvo aqui)");
                    hasInfo = true;
                }

                if (!divisaoAdjacente.getItems().isEmpty()) {
                    System.out.print(" (" + divisaoAdjacente.getItems().size() + " Kit" + (divisaoAdjacente.getItems().size() > 1 ? "s" : "") + ")");
                    hasInfo = true;
                }

                if (!hasInfo) {
                    System.out.print(" (Vazia)");
                }

                System.out.println();
            }
        }
    }


}
