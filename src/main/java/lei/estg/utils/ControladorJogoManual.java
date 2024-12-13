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
    private UnorderedArrayList<Divisao> trajeto;
    private Missao missao;
    private Mapa mapa;

    public ControladorJogoManual(Missao missao, Mapa mapa) {
        this.missao = missao;
        this.trajeto = new UnorderedArrayList<>();
    }

    /**
     * Terminates the game.
     * This method finds the player's current division, removes the player from it,
     * sets the game as inactive, checks the end game conditions, and exits the system.
     *
     * @param player the player whose game is being terminated
     * @param edificio the building where the game is taking place
     */
    @Override
    public void terminarJogo(Player player, Edificio<Divisao> edificio) {
        Divisao divisao = encontrarPlayer(player, edificio);
        if (divisao != null) {
            divisao.setPlayer(null);
        }
        isJogoAtivo = false;
        verificarFimJogo(player, divisao.getAlvo(), true);
        System.exit(0);
    }

    /**
     * Selects an entry division for the player.
     * This method iterates through all divisions in the building to find entry/exit points,
     * prompts the player to choose one, and moves the player to the selected division.
     *
     * @param player the player who is selecting the entry division
     * @param edificio the building where the game is taking place
     * @return the selected entry division
     */
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
            trajeto.addToRear(divisaoEscolhida);
            System.out.println("\033[3m\033[32m" + player.getNome() + " entrou na divisão: " + divisaoEscolhida.getNome() + "\033[0m");
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

    /**
     * Finds the division where the player is currently located.
     * This method iterates through all divisions in the building to find the one
     * that contains the specified player.
     *
     * @param player the player to be located
     * @param edificio the building where the search is conducted
     * @return the division containing the player, or null if the player is not found
     */
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

    /**
     * Moves the player to a new division.
     * This method displays possible divisions, prompts the player to choose one,
     * and moves the player to the selected division. It also handles interactions
     * with enemies and items in the new division.
     *
     * @param player the player to be moved
     * @param edificio the building where the game is taking place
     */
    @Override
    public void moverPlayer(Player player, Edificio<Divisao> edificio) {
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
        trajeto.addToRear(novaDivisao);

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

    /**
     * Moves the enemy to a new division.
     * This method randomly selects an adjacent division and moves the enemy to it.
     * It also handles interactions with the player if present in the new division.
     *
     * @param inimigo the enemy to be moved
     * @param edificio the building where the game is taking place
     * @throws EmptyStackException if an error occurs during the confrontation
     */
    @Override
    public void moverInimigo(Inimigo inimigo, Edificio<Divisao> edificio) throws EmptyStackException {
        Random random = new Random();
        Divisao divisaoAtual = encontrarInimigo(inimigo, edificio);
        int movimentos = 0;
        int maxMovimentos = random.nextInt(2);


        while (movimentos < maxMovimentos) {
            if (inimigo.getPoder() == 0) {
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
                return;
            }

            atualizarPesoDivisaoAnterior(divisaoAtual, edificio, inimigo);
            atualizarPesoNovaDivisao(novaDivisao, edificio, inimigo);

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

    /**
     * Handles the confrontation between the player and enemies in a division.
     * This method initiates the confrontation, allows the player to choose actions,
     * and processes the turns of both the player and the enemies until the confrontation ends.
     *
     * @param player the player involved in the confrontation
     * @param edificio the building where the confrontation is taking place
     * @param inimigos the list of enemies in the division
     * @param divisao the division where the confrontation is happening
     * @throws EmptyStackException if an error occurs during the confrontation
     */
    @Override
    public void confronto(Player player, Edificio<Divisao> edificio, UnorderedArrayList<Inimigo> inimigos, Divisao divisao) throws EmptyStackException {
        System.out.println("\033[31mExistem " + inimigos.size() + " inimigos nesta divisão. Confronto Iniciado\033[0m");

        while (!inimigos.isEmpty() && player.getVida() > 0) {
            System.out.println("=== Turno do Player ===");
            System.out.println("1 - Atacar todos os inimigos");
            System.out.println("2 - Usar um kit (perde o turno)");
            System.out.print("Escolha uma ação (1 ou 2): ");

            Scanner scanner = new Scanner(System.in);
            int escolha = scanner.nextInt();

            if (escolha == 1) {
                System.out.println("Escolheste atacar todos os inimigos!");

                Iterator<Inimigo> inimigosIterator = inimigos.iterator();
                while (inimigosIterator.hasNext()) {
                    Inimigo inimigo = inimigosIterator.next();
                    player.atacar(inimigo);
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
                    System.out.println(inimigo.getNome() + " atacou-te. Vida restante: " + player.getVida());

                    if (player.getVida() <= 0) {
                        terminarJogo(player, edificio);
                        return;
                    }
                } else {
                    System.out.println(inimigo.getNome() + " foi derrotado.");
                    inimigosRemover.addToRear(inimigo);
                }
            }

            for (Inimigo inimigo : inimigosRemover) {
                inimigos.remove(inimigo);
                resetarPeso(divisao, edificio, inimigo);
            }
        }

        System.out.println("\033[32mTodos os inimigos na divisão foram derrotados!\033[0m");
    }

    /**
     * Displays all enemies in the building.
     * This method iterates through all divisions in the building, checks for enemies,
     * and prints the details of each enemy found in the divisions.
     *
     * @param edificio the building where the game is taking place
     */
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

    /**
     * Displays all items in the building.
     * This method iterates through all divisions in the building, checks for items,
     * and prints the details of each item found in the divisions.
     *
     * @param edificio the building where the game is taking place
     */
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

    /**
     * Displays the target location in the building.
     * This method checks if the player has already interacted with the target.
     * If the player has interacted with the target, it prompts the player to go to the exit.
     * Otherwise, it displays the division where the target is located.
     *
     * @param player the player whose target location is being displayed
     * @param edificio the building where the game is taking place
     */
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

    /**
     * Displays the shortest paths to various targets in the building.
     * This method prints the shortest paths to the nearest kit, vest, target, and exit.
     *
     * @param player the player for whom the paths are being displayed
     * @param edificio the building where the game is taking place
     */
    @Override
    public void mostrarTodosCaminhosMaisProximos(Player player, Edificio<Divisao> edificio) {
        System.out.println("\033[33m============  Caminhos mais próximos  ============\033[0m");
        caminhoMaisCurtoKit(player, edificio);
        caminhoMaisCurtoColete(player, edificio);
        caminhoMaisCurtoAlvo(player, edificio);
        caminhoMaisCurtoSaida(player, edificio);
        System.out.println("\033[33m=================================================\033[0m");
    }

    /**
     * Finds the division where the specified enemy is currently located.
     * This method iterates through all divisions in the building to find the one
     * that contains the specified enemy.
     *
     * @param inimigo the enemy to be located
     * @param edificio the building where the search is conducted
     * @return the division containing the enemy, or null if the enemy is not found
     */
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

    /**
     * Resets the weight of the edges connected to the specified division.
     * This method iterates through all adjacent divisions, subtracts the enemy's power
     * from the current weight of the edge, and updates the edge with the new weight.
     *
     * @param divisao the division whose edges' weights are being reset
     * @param edificio the building containing the divisions
     * @param inimigo the enemy whose power is subtracted from the edge weights
     */
    private void resetarPeso(Divisao divisao, Edificio<Divisao> edificio, Inimigo inimigo) {
        Iterator<Divisao> iter = edificio.getAdjacentes(divisao);

        while (iter.hasNext()) {
            Divisao adjacente = iter.next();

            double pesoBase = 0.1;

            double novoPeso = pesoBase + inimigo.getPoder();

            edificio.updateEdge(edificio.getIndex(divisao), edificio.getIndex(adjacente), novoPeso);
        }
    }

    /**
     * Updates the weight of the edges connected to the specified division.
     * This method iterates through all adjacent divisions, subtracts the enemy's power
     * from the current weight of the edge, and updates the edge with the new weight.
     *
     * @param divisao the division whose edges' weights are being updated
     * @param edificio the building containing the divisions
     * @param inimigo the enemy whose power is subtracted from the edge weights
     */
    private void atualizarPesoDivisaoAnterior(Divisao divisao, Edificio<Divisao> edificio, Inimigo inimigo) {
        Iterator<Divisao> iter = edificio.getAdjacentes(divisao);

        while (iter.hasNext()) {
            Divisao adjacente = iter.next();

            double pesoAtual = edificio.getWeight(divisao, adjacente);

            double novoPeso = pesoAtual - inimigo.getPoder();

            edificio.updateEdge(edificio.getIndex(divisao), edificio.getIndex(adjacente), novoPeso);
        }
    }

    /**
     * Updates the weight of the edges connected to the specified division.
     * This method iterates through all adjacent divisions, adds the enemy's power
     * to the current weight of the edge, and updates the edge with the new weight.
     *
     * @param divisao the division whose edges' weights are being updated
     * @param edificio the building containing the divisions
     * @param inimigo the enemy whose power is added to the edge weights
     */
    private void atualizarPesoNovaDivisao(Divisao divisao, Edificio<Divisao> edificio, Inimigo inimigo) {
        Iterator<Divisao> iter = edificio.getAdjacentes(divisao);

        while (iter.hasNext()) {
            Divisao adjacente = iter.next();

            double pesoAtual = edificio.getWeight(divisao, adjacente);

            double novoPeso = pesoAtual + inimigo.getPoder();

            edificio.updateEdge(edificio.getIndex(divisao), edificio.getIndex(adjacente), novoPeso);
        }
    }

    /**
     * Finds the shortest path to the nearest kit in the building.
     * This method iterates through all divisions in the building to find the one
     * that contains a kit and calculates the shortest path to it from the player's current division.
     *
     * @param player the player for whom the path is being calculated
     * @param edificio the building where the search is conducted
     */
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

    /**
     * Finds the shortest path to the nearest vest in the building.
     * This method iterates through all divisions in the building to find the one
     * that contains a vest and calculates the shortest path to it from the player's current division.
     *
     * @param player the player for whom the path is being calculated
     * @param edificio the building where the search is conducted
     */
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

    /**
     * Finds the shortest path to the nearest target in the building.
     * This method iterates through all divisions in the building to find the one
     * that contains the target and calculates the shortest path to it from the player's current division.
     *
     * @param player the player for whom the path is being calculated
     * @param edificio the building where the search is conducted
     */
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

    /**
     * Finds the shortest path to the nearest exit in the building.
     * This method iterates through all divisions in the building to find the one
     * that is an exit and calculates the shortest path to it from the player's current division.
     *
     * @param player the player for whom the path is being calculated
     * @param edificio the building where the search is conducted
     */
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

    /**
     * Checks the end game conditions and updates the game state accordingly.
     * This method verifies if the player has died, if the player has interacted with the target and exited,
     * or if the player has exited without interacting with the target. It prints the appropriate messages,
     * generates a mission report, and returns a boolean indicating whether the game has ended.
     *
     * @param player the player whose game state is being checked
     * @param alvo the target that the player needs to interact with
     * @param playerSaiu a boolean indicating if the player has exited the building
     * @return true if the game has ended, false otherwise
     */
    @Override
    public boolean verificarFimJogo(Player player, Alvo alvo, boolean playerSaiu) {
        if (player.getVida() == 0) {
            System.out.println("\033[31m"+ player.getNome()+ "morreu!\033[0m");
            System.out.println("\033[31mDerrota! Não desistas, tenta novamente!\033[0m");
            RelatorioMissao relatorio = new RelatorioMissao(missao.getCodMissao(), missao.getVersao(), missao.getDificuldade(), player.getVida(), trajeto);
            relatorio.exportarParaJSON(relatorio);
            return true;
        } else if (player.isAlvoInteragido() && playerSaiu) {
            System.out.println("\033[32mVitória! Parabéns! Continua assim!\033[0m");
            RelatorioMissao relatorio = new RelatorioMissao(missao.getCodMissao(), missao.getVersao(), missao.getDificuldade(), player.getVida(), trajeto);
            relatorio.exportarParaJSON(relatorio);
            return true;
        } else if (playerSaiu && !player.isAlvoInteragido()) {
            System.out.println("\033[31mPlayer saiu do edifício! Mas nao interagiu com o alvo!\033[0m");
            System.out.println("\033[31mDerrota! Não desistas, tenta novamente!\033[0m");
            RelatorioMissao relatorio = new RelatorioMissao(missao.getCodMissao(), missao.getVersao(), missao.getDificuldade(), player.getVida(), trajeto);
            relatorio.exportarParaJSON(relatorio);
            return true;
        }
        return false;
    }

    /**
     * Interacts with the target in the specified division.
     * This method checks if the player has already interacted with the target.
     * If not, it sets the player's interaction status to true and removes the target from the division.
     * If the player has already interacted with the target, it prints a message indicating so.
     * If the target is not found in the division, it prints a message indicating that the target is not found.
     *
     * @param player the player interacting with the target
     * @param alvo the target to be interacted with
     * @param divisao the division where the interaction is taking place
     */
    @Override
    public void interagirComAlvo(Player player, Alvo alvo, Divisao divisao) {
        if (player.isAlvoInteragido()) {
            System.out.println("Player já interagiu com o alvo!");
            return;
        }
        if (alvo != null) {
            System.out.println("\033[3m\033[32mPlayer interagiu com o alvo!\033[0m");
            player.setAlvoInteragido(true);
            divisao.setAlvo(null);
        } else {
            System.out.println("Alvo não encontrado nesta divisão!");
        }
    }

    /**
     * Checks if the game is currently active.
     * This method returns a boolean indicating whether the game is active or not.
     *
     * @return true if the game is active, false otherwise
     */
    public boolean isJogoAtivo() {
        return isJogoAtivo;
    }

    /**
     * Finds the division where the target is currently located.
     * This method iterates through all divisions in the building to find the one
     * that contains the target.
     *
     * @param edificio the building where the search is conducted
     * @return the division containing the target, or null if the target is not found
     */
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

}
