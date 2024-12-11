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
        System.exit(0);
    }

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
            System.out.println("Player entrou na divisão: " + divisaoEscolhida.getNome());
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

    public void moverPlayer(Player player, Edificio<Divisao> edificio) {
        Divisao divisaoAtual = encontrarPlayer(player, edificio);

        if (!player.isAlvoInteragido()) {
            System.out.println("Player ainda não interagiu com o alvo.");
            moverPlayerAlvo(player, edificio, divisaoAtual);
        } else {
            System.out.println("Player já interagiu com o alvo!");
            moverPlayerSaida(player, edificio, divisaoAtual);
        }
    }


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
                //System.out.println("Sem divisões adjacentes, encerrando movimento.");
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
                System.out.println("Confronto iniciado com o jogador na nova divisão.");

                inimigo.atacar(novaDivisao.getPlayer());
                confronto(novaDivisao.getPlayer(), edificio, novaDivisao.getInimigos(), novaDivisao);

                if (inimigo.getPoder() == 0) {
                    System.out.println("Inimigo derrotado durante o confronto.");
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

    @Override
    public void confronto(Player player, Edificio<Divisao> edificio, UnorderedArrayList<Inimigo> inimigos, Divisao divisao) throws EmptyStackException {
        System.out.println("Existem " + inimigos.size() + " inimigos nesta divisão. Confronto Iniciado");

        while (!inimigos.isEmpty() && player.getVida() > 0) {
            System.out.println("=== Turno do Player ===");

            if (player.getVida() < 50) {
                if (!player.getMochila().isEmpty()) {
                    System.out.println("Vida abaixo de 50. Usando kit automaticamente...");
                    player.usarKit();
                } else {
                    System.out.println("Mochila vazia nao possui kits.");
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
                        System.out.println(player.getNome() + " morreu!");
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
    public boolean verificarFimJogo(Player player, Alvo alvo, boolean playerSaiu) {
        if (player.getVida() == 0) {
            System.out.println("Fim do Jogo! " + player.getNome() + " morreu!");
            return true;
        } else if (player.isAlvoInteragido() && playerSaiu) {
            System.out.println("Fim do Jogo! " + player.getNome() + " saiu do edifício e interagiu com o alvo!");
            return true;
        } else if (playerSaiu && !player.isAlvoInteragido()) {
            System.out.println("Player saiu do edifício! Mas nao interagiu com o alvo!");
            return true;
        }
        return false;
    }

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

    public boolean isJogoAtivo() {
        return isJogoAtivo;
    }

    public void setJogoAtivo(boolean jogoAtivo) {
        isJogoAtivo = jogoAtivo;
    }

    private void moverPlayerAlvo(Player player, Edificio<Divisao> edificio, Divisao divisaoAtual) {
        Divisao divisaoAlvo = encontrarAlvo(edificio);
        if (divisaoAlvo == null) {
            System.out.println("Não há alvo disponível.");
            return;
        }

        Iterator<Divisao> caminhoSeguro = edificio.findShortestPath(divisaoAtual, divisaoAlvo);

        if (caminhoSeguro == null) {
            System.out.println("Não há caminho seguro para o alvo.");
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

        System.out.println("Player " + player.getNome() + " movido para " + proximaDivisao.getNome());

        if (proximaDivisao.getItems() != null) {
            for (Item item : proximaDivisao.getItems()) {
                player.apanharItem(item);
            }
        }

        if (proximaDivisao.getAlvo() != null && !player.isAlvoInteragido()) {
            player.setAlvoInteragido(true);
            System.out.println(player.getNome() + " interagiu com o alvo!");
        }

        divisaoAtual = proximaDivisao;

    }

    private void moverPlayerSaida(Player player, Edificio<Divisao> edificio, Divisao divisaoAtual) {
        Divisao divisaoPlayer = encontrarPlayer(player, edificio);

        if (divisaoPlayer.isEntradaSaida()) {
            System.out.println("\n" + player.getNome() + " já se encontra na saída!");
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

            System.out.println("Player " + player.getNome() + " movido para " + proximaDivisao.getNome());

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
