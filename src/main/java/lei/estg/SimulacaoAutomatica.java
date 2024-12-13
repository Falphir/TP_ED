package lei.estg;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.*;
import lei.estg.utils.ControladorJogoAutomatico;
import lei.estg.utils.JogoConfig;
import lei.estg.utils.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;

public class SimulacaoAutomatica {
    /**
     * Starts the automatic game simulation.
     * This method initializes the mission, player, and game controller, and then starts the game loop.
     * The game loop alternates between the player's turn and the enemies' turn until the game is no longer active.
     * During each turn, the game displays the current state and processes the player's automatic actions.
     * Enemies are moved after the player's turn.
     */
    public static void jogar() {
        Missao missao;
        Player player;
        ControladorJogoAutomatico jogo = new ControladorJogoAutomatico();
        try {
            Path caminhoArquivo = Paths.get(Objects.requireNonNull(SimulacaoAutomatica.class.getClassLoader().getResource("missoes/pata_coelho.json")).toURI());
            missao = JsonUtils.carregarMissao(String.valueOf(caminhoArquivo));
            Path caminhoConfig = Paths.get(Objects.requireNonNull(SimulacaoAutomatica.class.getClassLoader().getResource("config/simuladorConfig.json")).toURI());
            JogoConfig config = new JogoConfig();
            player = config.carregarPlayerConfig(String.valueOf(caminhoConfig));

            System.out.println("Bem-vindo ao jogo!");

            System.out.println("\033[1m\033[35m==================  Missão  ==================\033[0m");
            System.out.println("\033[36mCódigo: " + missao.getCodMissao() + "\033[0m");
            System.out.println("\033[36mVersão: " + missao.getVersao() + "\033[0m");
            System.out.println("\033[36mDificuldade: " + missao.getDificuldade() + "\033[0m");
            System.out.println("\033[1m\033[35m==============================================\033[0m");

            Divisao divisaoInicial = jogo.selecionarEntrada(player, missao.getEdificio());

            Edificio<Divisao> edificio = missao.getEdificio();

            while (jogo.isJogoAtivo()) {
                System.out.println("\033[32m========== Turno do jogador ==========\033[0m");

                turnoPlayerAutomatico(player, edificio, jogo);

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
     * Executes the player's turn automatically in the game.
     * This method handles the player's actions during their turn, including displaying the player's status,
     * moving the player, attacking enemies, using kits, and interacting with targets.
     * The player's turn ends if they exit the building after interacting with the target.
     *
     * @param player the player object
     * @param edificio the building object containing the divisions
     * @param jogo the game controller object
     * @throws EmptyStackException if an error occurs during the player's turn
     */
    private static void turnoPlayerAutomatico(Player player, Edificio<Divisao> edificio, ControladorJogoAutomatico jogo) throws EmptyStackException {

        Divisao divisao = jogo.encontrarPlayer(player, edificio);

        System.out.println("\033[1m\033[32m============  Status " + player.getNome() + " ============\033[0m");
        System.out.println("\033[34mVida: " + player.getVida() + "\033[0m");
        System.out.println("\033[34mPoder: " + player.getPoder() + "\033[0m");
        System.out.println("\033[34mColete: " + player.getVidaColete() + "\033[0m");
        System.out.println("\033[34mKits: " + player.getMochila().size() + "\033[0m");
        System.out.println("\033[34mDivisao: " + divisao.getNome() + "\033[0m");
        System.out.println("\033[1m\033[32m=========================================\033[0m");

        jogo.moverPlayer(player, edificio);

        if (divisao.getInimigos() != null) {
            for (Inimigo inimigo : divisao.getInimigos()) {
                player.atacar(inimigo);
                System.out.println(inimigo.getNome() + " recebeu dano. Poder restante: " + inimigo.getPoder());
            }
        }

        if (player.getVida() < 50 && !player.getMochila().isEmpty()) {
            player.usarKit();
            System.out.println(player.getNome() + " usou um kit para recuperar vida.");
        }

        if (divisao.getAlvo() != null) {
            player.setAlvoInteragido(true);
        }

        if (divisao.isEntradaSaida() && player.isAlvoInteragido()) {
            jogo.terminarJogo(player, edificio);
            System.out.println(player.getNome() + " saiu do edifício.");
        }
    }
}
