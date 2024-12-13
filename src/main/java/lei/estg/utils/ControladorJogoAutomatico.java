package lei.estg.utils;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.UnorderedListADT;
import lei.estg.models.*;
import lei.estg.models.Interfaces.JogoADT;

import java.util.Iterator;
import java.util.Random;

public class ControladorJogoAutomatico implements JogoADT {
    private boolean isJogoAtivo = true;

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
     * This method iterates through all divisions in the building to find the entry/exit points,
     * calculates the total enemy power in each division, and selects the division with the least total enemy power.
     * If such a division is found, the player is moved to it and a confrontation with the enemies in that division is initiated if any are present.
     *
     * @param player the player who is selecting the entry division
     * @param edificio the building containing the divisions
     * @return the selected entry division, or null if no suitable division is found
     */
    @Override
    public Divisao selecionarEntrada(Player player, Edificio<Divisao> edificio) {
        Iterator<Divisao> divisoes = edificio.getVertex();
        int minDano = Integer.MAX_VALUE;
        Divisao divisaoEscolhida = null;
        int index = 1;
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            if (divisao.isEntradaSaida()) {
                int danoTotal = 0;
                if (!divisao.getInimigos().isEmpty()) {
                    for (Inimigo inimigo : divisao.getInimigos()) {
                        danoTotal += inimigo.getPoder();
                    }
                }

                if (danoTotal < minDano) {
                    minDano = danoTotal;
                    divisaoEscolhida = divisao;
                }
                index++;
            }
        }

        if (divisaoEscolhida != null) {
            player.mover(divisaoEscolhida);
            System.out.println("\033[3m\033[32m" + player.getNome() + " entrou na divisão: " + divisaoEscolhida.getNome() + "\033[0m");
            if (!divisaoEscolhida.getInimigos().isEmpty()) {
                try {
                    confronto(player, edificio, divisaoEscolhida.getInimigos(), divisaoEscolhida);
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
     * Moves the player within the building.
     * This method finds the player's current division and moves the player either towards the target or towards the exit,
     * depending on whether the player has interacted with the target.
     *
     * @param player the player to be moved
     * @param edificio the building containing the divisions
     */
    @Override
    public void moverPlayer(Player player, Edificio<Divisao> edificio) {
        Divisao divisaoAtual = encontrarPlayer(player, edificio);

        if (!player.isAlvoInteragido()) {
            moverPlayerAlvo(player, edificio, divisaoAtual);
        } else {
            moverPlayerSaida(player, edificio, divisaoAtual);
        }
    }

    /**
     * Moves the enemy within the building.
     * This method finds the enemy's current division and moves the enemy to a random adjacent division.
     * If the enemy encounters the player in the new division, a confrontation is initiated.
     *
     * @param inimigo the enemy to be moved
     * @param edificio the building containing the divisions
     * @throws EmptyStackException if an error occurs during the confrontation
     */
    @Override
    public void moverInimigo(Inimigo inimigo, Edificio<Divisao> edificio) throws EmptyStackException {
        Random random = new Random();
        Divisao divisaoAtual = encontrarInimigo(inimigo, edificio);
        int movimentos = 0;

        while (movimentos < 2) {
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

            System.out.println("\033[3m\033[31m" + inimigo.getNome() + " movido de " +
                    divisaoAtual.getNome() + " para " + novaDivisao.getNome()+ "\033[0m");

            if (novaDivisao.getPlayer() != null) {
                System.out.println("Confronto iniciado com o jogador na nova divisão.");

                inimigo.atacar(novaDivisao.getPlayer());
                if (novaDivisao.getPlayer().getVida() == 0) {
                    terminarJogo(novaDivisao.getPlayer(), edificio);
                    return;
                }
                confronto(novaDivisao.getPlayer(), edificio, novaDivisao.getInimigos(), novaDivisao);

                if (inimigo.getPoder() == 0) {
                    System.out.println("Inimigo derrotado durante o confronto.");
                    resetarPeso(novaDivisao, edificio, inimigo);
                    return;
                }
            }

            divisaoAtual = novaDivisao;
            movimentos++;
        }

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
     * Initiates a confrontation between the player and enemies in the specified division.
     * This method handles the turn-based combat between the player and the enemies.
     * If the player's health drops below 50, it automatically uses a health kit if available.
     * The confrontation continues until either the player or all enemies are defeated.
     *
     * @param player the player involved in the confrontation
     * @param edificio the building containing the divisions
     * @param inimigos the list of enemies in the division
     * @param divisao the division where the confrontation takes place
     * @throws EmptyStackException if an error occurs during the confrontation
     */
    @Override
    public void confronto(Player player, Edificio<Divisao> edificio, UnorderedArrayList<Inimigo> inimigos, Divisao divisao) throws EmptyStackException {
        System.out.println("\033[31mExistem " + inimigos.size() + " inimigos nesta divisão. Confronto Iniciado\033[0m");

        while (!inimigos.isEmpty() && player.getVida() > 0) {
            System.out.println("=== Turno do Player ===");

            if (player.getVida() < 50) {
                if (!player.getMochila().isEmpty()) {
                    System.out.println("Vida abaixo de 50. Usando kit automaticamente...");
                    player.usarKit();
                } else {
                    System.out.println("\033[33mMochila vazia nao possui kits.\033[0m");
                }
            }
            Iterator<Inimigo> inimigosIterator = inimigos.iterator();
            while (inimigosIterator.hasNext()) {
                Inimigo inimigo = inimigosIterator.next();
                player.atacar(inimigo);
            }

            System.out.println("=== Turno dos Inimigos ===");
            UnorderedArrayList<Inimigo> inimigosRemover = new UnorderedArrayList<>();
            for (Inimigo inimigo : inimigos) {
                if (inimigo.getPoder() > 0) {
                    inimigo.atacar(player);

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
                Divisao div = encontrarInimigo(inimigo, edificio);
                resetarPeso(div, edificio, inimigo);
                inimigos.remove(inimigo);
            }
        }
        System.out.println("\033[32mTodos os inimigos na divisão foram derrotados!\033[0m");
    }

    /**
     * Checks if the game has ended.
     * This method verifies the end game conditions based on the player's health, interaction with the target, and whether the player has exited the building.
     * It prints appropriate messages for victory or defeat based on the conditions met.
     *
     * @param player the player whose game status is being checked
     * @param alvo the target that the player needs to interact with
     * @param playerSaiu a boolean indicating if the player has exited the building
     * @return true if the game has ended, false otherwise
     */
    @Override
    public boolean verificarFimJogo(Player player, Alvo alvo, boolean playerSaiu) {
        if (player.getVida() == 0) {
            System.out.println("\033[31mPlayer morreu!\033[0m");
            System.out.println("\033[31mDerrota! Não desistas, tenta novamente!\033[0m");
            return true;
        } else if (player.isAlvoInteragido() && playerSaiu) {
            System.out.println("\033[32mVitória! Parabéns! Continua assim!\033[0m");
            return true;
        } else if (playerSaiu && !player.isAlvoInteragido()) {
            System.out.println("\033[31mPlayer saiu do edifício! Mas nao interagiu com o alvo!\033[0m");
            System.out.println("\033[31mDerrota! Não desistas, tenta novamente!\033[0m");
            return true;
        }
        return false;
    }

    /**
     * Finds the division containing the target.
     * This method iterates through all divisions in the building to find the one
     * that contains the target.
     *
     * @param edificio the building where the search is conducted
     * @return the division containing the target, or null if no target is found
     */
    private Divisao encontrarAlvo(Edificio<Divisao> edificio) {
        Iterator<Divisao> divisoes = edificio.getVertex();
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            if (divisao.getAlvo() != null) {
                return divisao;
            }
        }
        return null;
    }

    /**
     * Resets the weight of the edges connected to the specified division.
     * This method iterates through all adjacent divisions and updates the weight of the edges
     * by subtracting the enemy's power from the current weight.
     *
     * @param divisao the division whose edges' weights are to be reset
     * @param edificio the building containing the divisions
     * @param inimigo the enemy whose power is used to adjust the weights
     */
    private void resetarPeso(Divisao divisao, Edificio<Divisao> edificio, Inimigo inimigo) {
        Iterator<Divisao> iter = edificio.getAdjacentes(divisao);

        while (iter.hasNext()) {
            Divisao adjacente = iter.next();

            int pesoAtual = (int) edificio.getWeight(divisao, adjacente);

            int novoPeso = pesoAtual - inimigo.getPoder();

            edificio.updateEdge(edificio.getIndex(divisao), edificio.getIndex(adjacente), novoPeso);
        }
    }

    /**
     * Updates the weight of the edges connected to the specified division.
     * This method iterates through all adjacent divisions and updates the weight of the edges
     * by adding the enemy's power to the current weight.
     *
     * @param divisao the division whose edges' weights are to be updated
     * @param edificio the building containing the divisions
     * @param inimigo the enemy whose power is used to adjust the weights
     */
    private void atualizarPeso(Divisao divisao, Edificio<Divisao> edificio, Inimigo inimigo) {
        Iterator<Divisao> iter = edificio.getAdjacentes(divisao);

        while (iter.hasNext()) {
            Divisao adjacente = iter.next();

            int pesoAtual = (int) edificio.getWeight(divisao, adjacente);

            int novoPeso = pesoAtual + inimigo.getPoder();

            edificio.updateEdge(edificio.getIndex(divisao), edificio.getIndex(adjacente), novoPeso);
        }
    }

    /**
     * Checks if the game is currently active.
     * This method returns the status of the game, indicating whether it is active or not.
     *
     * @return true if the game is active, false otherwise
     */
    public boolean isJogoAtivo() {
        return isJogoAtivo;
    }

    /**
     * Moves the player towards the target within the building.
     * This method finds the target division and moves the player towards it.
     * If there are enemies in the next division, a confrontation is initiated.
     * The player picks up any items in the new division and interacts with the target if present.
     *
     * @param player the player to be moved
     * @param edificio the building containing the divisions
     * @param divisaoAtual the current division of the player
     */
    private void moverPlayerAlvo(Player player, Edificio<Divisao> edificio, Divisao divisaoAtual) {
        Divisao divisaoAlvo = encontrarAlvo(edificio);
        if (divisaoAlvo == null) {
            System.out.println("\033[31mNão há alvo disponível.\033[0m");
            return;
        }

        Iterator<Divisao> caminhoSeguro = edificio.findShortestPath(divisaoAtual, divisaoAlvo);

        if (caminhoSeguro == null) {
            System.out.println("\033[31mNão existe um caminho seguro até ao alvo.\033[0m");
            return;
        }
        Divisao proximaDivisao = caminhoSeguro.next();
        proximaDivisao = caminhoSeguro.next();

        if (!proximaDivisao.getInimigos().isEmpty()) {
            try {
                confronto(player, edificio, proximaDivisao.getInimigos(), proximaDivisao);
            } catch (EmptyStackException e) {
                e.printStackTrace();
            }
        }

        divisaoAtual.setPlayer(null);
        player.mover(proximaDivisao);

        System.out.println("\033[3m\033[32m" + player.getNome() + " movido de " +
                divisaoAtual.getNome() + " para " + proximaDivisao.getNome()+ "\033[0m");

        if (proximaDivisao.getItems() != null) {
            for (Item item : proximaDivisao.getItems()) {
                player.apanharItem(item);
            }
        }

        if (proximaDivisao.getAlvo() != null && !player.isAlvoInteragido()) {
            player.setAlvoInteragido(true);
            System.out.println("\033[34m" + player.getNome() + " já interagiu com o Alvo!\033[0m");
        }

        divisaoAtual = proximaDivisao;

    }

    /**
     * Moves the player towards the exit within the building.
     * This method finds the player's current division and moves the player towards the nearest exit.
     * If there are enemies in the next division, a confrontation is initiated.
     * The player picks up any items in the new division and exits the building if the exit is reached.
     *
     * @param player the player to be moved
     * @param edificio the building containing the divisions
     * @param divisaoAtual the current division of the player
     */
    private void moverPlayerSaida(Player player, Edificio<Divisao> edificio, Divisao divisaoAtual) {
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

        if (caminhoMaisCurto == null) {
            System.out.println("Não há caminho seguro");
        }

        while (caminhoMaisCurto.hasNext()) {
            Divisao proximaDivisao = caminhoMaisCurto.next();

            if (!proximaDivisao.getInimigos().isEmpty()) {
                try {
                    confronto(player, edificio, proximaDivisao.getInimigos(), proximaDivisao);
                } catch (EmptyStackException e) {
                    e.printStackTrace();
                }
            }

            divisaoAtual.setPlayer(null);
            player.mover(proximaDivisao);

            System.out.println("\033[3m\033[32m" + player.getNome() + " movido de " +
                    divisaoAtual.getNome() + " para " + proximaDivisao.getNome()+ "\033[0m");

            if (proximaDivisao.getItems() != null) {
                for (Item item : proximaDivisao.getItems()) {
                    player.apanharItem(item);
                }
            }

            if (proximaDivisao.isEntradaSaida()) {
                terminarJogo(player, edificio);
                break;
            }

            divisaoAtual = proximaDivisao;
        }
    }
}
