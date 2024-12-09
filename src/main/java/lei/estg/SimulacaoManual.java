package lei.estg;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.*;
import lei.estg.utils.ControladorJogo;
import lei.estg.utils.JogoConfig;
import lei.estg.utils.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

public class SimulacaoManual {
    protected static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        // Inicialização

        Missao missao;
        Player player;
        ControladorJogo jogo = new ControladorJogo();
        try {
            Path caminhoArquivo = Paths.get(Main.class.getClassLoader().getResource("missoes/missao.json").toURI());
            missao = JsonUtils.carregarMissao(String.valueOf(caminhoArquivo));
            Path caminhoConfig = Paths.get(Main.class.getClassLoader().getResource("config/simuladorConfig.json").toURI());
            JogoConfig config = new JogoConfig();
            player = config.carregarPlayerConfig(String.valueOf(caminhoConfig));

            System.out.println("Bem-vindo ao jogo!");

            Divisao divisaoInicial = jogo.selecionarEntrada(player, missao.getEdificio());

            Edificio<Divisao> edificio = missao.getEdificio();

            boolean jogoAtivo = true;

            int turno = 1;

            while (jogoAtivo) {
                System.out.println("Turno do jogador " + turno);
                turnoPlayer(player, edificio, jogo);
                System.out.println("Turno do inimigo " + turno);
                Iterator it = missao.getEdificio().getVertex();
                UnorderedArrayList<Inimigo> inimigosParaMover = new UnorderedArrayList<>(); // Lista temporária para armazenar inimigos a mover
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
                //jogoAtivo = jogo.verificarFimJogo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (EmptyStackException e) {
            throw new RuntimeException(e);
        }
    }

    private static void turnoPlayer(Player player, Edificio<Divisao> edificio, ControladorJogo jogo) throws EmptyStackException {

        Divisao divisao = jogo.encontrarPlayer(player, edificio);

        System.out.println(player.getNome() + " encontra-se na divisão " + divisao.getNome());
        System.out.println("Escolha uma ação:");
        System.out.println("1 - Mover-se para outra divisão");
        System.out.println("2 - Atacar inimigo");
        System.out.println("3 - Usar kit");
        System.out.println("4 - Verificar status");
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
                player.toString();
                break;
            default:
                System.out.println("Opção inválida");
        }
    }
}
