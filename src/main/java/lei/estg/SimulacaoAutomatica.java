package lei.estg;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.*;
import lei.estg.utils.ControladorJogo;
import lei.estg.utils.ControladorJogoAutomatico;
import lei.estg.utils.JogoConfig;
import lei.estg.utils.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Random;

public class SimulacaoAutomatica {
    protected static Random random = new Random();

    public static void main(String[] args) {

        Missao missao;
        Player player;
        ControladorJogoAutomatico jogo = new ControladorJogoAutomatico();
        try {
            Path caminhoArquivo = Paths.get(SimulacaoAutomatica.class.getClassLoader().getResource("missoes/pata_coelho.json").toURI());
            missao = JsonUtils.carregarMissao(String.valueOf(caminhoArquivo));
            Path caminhoConfig = Paths.get(SimulacaoAutomatica.class.getClassLoader().getResource("config/simuladorConfig.json").toURI());
            JogoConfig config = new JogoConfig();
            player = config.carregarPlayerConfig(String.valueOf(caminhoConfig));

            System.out.println("Bem-vindo ao jogo!");

            Divisao divisaoInicial = jogo.selecionarEntrada(player, missao.getEdificio());

            Edificio<Divisao> edificio = missao.getEdificio();
            int turno = 1;

            while (jogo.isJogoAtivo()) {
                System.out.println("Turno do jogador " + turno);

                // Ações automáticas do jogador
                turnoPlayerAutomatico(player, edificio, jogo);

                // Ações automáticas dos inimigos
                System.out.println("Turno dos inimigos " + turno);
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

                turno++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (EmptyStackException e) {
            throw new RuntimeException(e);
        }
    }

    private static void turnoPlayerAutomatico(Player player, Edificio<Divisao> edificio, ControladorJogoAutomatico jogo) throws EmptyStackException {

        Divisao divisao = jogo.encontrarPlayer(player, edificio);

        System.out.println(player.getNome() + " encontra-se na divisão " + divisao.getNome());
        System.out.println("============  Status " + player.getNome() + " ============");
        System.out.println("Vida: " + player.getVida());
        System.out.println("Poder: " + player.getPoder());
        System.out.println("Colete: " + player.getVidaColete());
        System.out.println("Kits: " + player.getMochila().size());
        System.out.println("Divisao: " + divisao.getNome());
        System.out.println("============================================");

        // Ações automáticas do jogador

        // Mover-se para outra divisão
        jogo.moverPlayer(player, edificio);

        if (divisao.getInimigos() != null) {
            for (Inimigo inimigo : divisao.getInimigos()) {
                player.atacar(inimigo);
                System.out.println(player.getNome() + " atacou " + inimigo.getNome() + ". Poder restante: " + inimigo.getPoder());
            }
        }

        if (player.getVida() < 50 && player.getMochila().size() > 0) {
            player.usarKit();
            System.out.println(player.getNome() + " usou um kit para recuperar vida.");
        }

        if (divisao.getAlvo() != null) {
            player.setAlvoInteragido(true);
            System.out.println(player.getNome() + " interagiu com o alvo " + divisao.getAlvo());
        }

        if (divisao.isEntradaSaida()) {
            jogo.terminarJogo(player, edificio);
            System.out.println(player.getNome() + " saiu do edifício.");
        }
    }
}
