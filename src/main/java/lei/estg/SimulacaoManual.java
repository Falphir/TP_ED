package lei.estg;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.*;
import lei.estg.utils.ControladorJogoManual;
import lei.estg.utils.JogoConfig;
import lei.estg.utils.JsonUtils;
import lei.estg.utils.Mapa;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

public class SimulacaoManual {
    protected static Scanner scanner = new Scanner(System.in);

    /**
     * Starts the game simulation.
     * This method initializes the mission, player, and game controller, and then starts the game loop.
     * The game loop alternates between the player's turn and the enemies' turn until the game is no longer active.
     * During each turn, the game displays the current state and allows the player to take actions.
     * Enemies are moved after the player's turn.
     */
    public static void jogar() {
        Missao missao;
        Player player;
        ControladorJogoManual jogo;
        Mapa mapa = new Mapa();

        try {
            Path caminhoArquivo = Paths
                    .get(Objects.requireNonNull(Main.class.getClassLoader().getResource("missoes/pata_coelho.json")).toURI());
            missao = JsonUtils.carregarMissao(String.valueOf(caminhoArquivo));
            Path caminhoConfig = Paths
                    .get(Objects.requireNonNull(Main.class.getClassLoader().getResource("config/simuladorConfig.json")).toURI());
            JogoConfig config = new JogoConfig();
            player = config.carregarPlayerConfig(String.valueOf(caminhoConfig));

            jogo = new ControladorJogoManual(missao, mapa);

            System.out.println("Bem-vindo ao jogo!");

            System.out.println("\033[1m\033[35m==================  Missão  ==================\033[0m");
            System.out.println("\033[36mCódigo: " + missao.getCodMissao() + "\033[0m");
            System.out.println("\033[36mVersão: " + missao.getVersao() + "\033[0m");
            System.out.println("\033[36mDificuldade: " + missao.getDificuldade() + "\033[0m");
            System.out.println("\033[1m\033[35m==============================================\033[0m");

            Edificio<Divisao> edificio = missao.getEdificio();

            mapa.mostrarMapa(edificio);

            jogo.mostrarInimigos(edificio);
            jogo.mostrarItens(edificio);
            jogo.mostrarAlvo(player, edificio);
            jogo.selecionarEntrada(player, edificio);

            while (jogo.isJogoAtivo()) {
                System.out.println("\033[32m========== Turno do jogador ==========\033[0m");
                jogo.mostrarInimigos(edificio);
                jogo.mostrarItens(edificio);
                jogo.mostrarAlvo(player, edificio);
                turnoPlayer(player, edificio, jogo);
                System.out.println("\033[31m==========  Turno do inimigo ==========\033[0m");
                Iterator it = missao.getEdificio().getVertex();
                UnorderedArrayList<Inimigo> inimigosParaMover = new UnorderedArrayList<>();
                while (it.hasNext()) {
                    Divisao divisao = (Divisao) it.next();
                    if (divisao.getInimigos() != null) {
                        for (Inimigo inimigo : divisao.getInimigos()) {
                            inimigosParaMover.addToRear(inimigo);
                        }
                    }
                }

                for (Inimigo inimigo : inimigosParaMover) {
                    jogo.moverInimigo(inimigo, missao.getEdificio());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (EmptyStackException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes the player's turn in the game.
     * This method handles the player's actions during their turn, including displaying the player's status,
     * showing available actions, and processing the selected action.
     *
     * @param player the player object
     * @param edificio the building object containing the divisions
     * @param jogo the game controller object
     * @throws EmptyStackException if an error occurs during the player's turn
     */
    private static void turnoPlayer(Player player, Edificio<Divisao> edificio, ControladorJogoManual jogo)
            throws EmptyStackException {

        Divisao divisao = jogo.encontrarPlayer(player, edificio);

        System.out.println("\033[1m\033[32m============  Status " + player.getNome() + " ============\033[0m");
        System.out.println("\033[34mVida: " + player.getVida() + "\033[0m");
        System.out.println("\033[34mPoder: " + player.getPoder() + "\033[0m");
        System.out.println("\033[34mColete: " + player.getVidaColete() + "\033[0m");
        System.out.println("\033[34mKits: " + player.getMochila().size() + "\033[0m");
        System.out.println("\033[34mDivisao: " + divisao.getNome() + "\033[0m");
        System.out.println("\033[1m\033[32m=========================================\033[0m");

        jogo.mostrarTodosCaminhosMaisProximos(player, edificio);

        System.out.println("============  Escolha uma ação  ============");
        System.out.println("1 - Mover-se para outra divisão");
        System.out.println("2 - Atacar inimigo");
        System.out.println("3 - Usar kit");
        System.out.println("4 - Interagir com o Alvo");
        System.out.println("5 - Sair do edificio");
        System.out.println("============================================");
        System.out.println("Opção: ");

        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                jogo.moverPlayer(player, edificio);
                break;
            case 2:
                if (divisao.getInimigos() != null) {
                    for (Inimigo inimigo : divisao.getInimigos()) {
                        player.atacar(inimigo);
                    }
                } else {
                    System.out.println("Não existem inimigos nesta divisão.");
                }
                break;
            case 3:
                player.usarKit();
                break;
            case 4:
                if (divisao.getAlvo() != null) {
                    jogo.interagirComAlvo(player, divisao.getAlvo(), divisao);
                } else {
                    System.out.println("Não existe um alvo nesta divisão.");
                }
                break;
            case 5:
                if (divisao.isEntradaSaida()) {
                    jogo.terminarJogo(player, edificio);
                } else {
                    System.out.println("Você não pode sair do edificio.");
                }
                break;
            default:
                System.out.println("Opção inválida");
        }
    }
}
