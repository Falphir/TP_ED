package lei.estg;

import lei.estg.dataStructures.Network;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.*;
import lei.estg.models.enums.EItemTipo;
import lei.estg.utils.ControladorJogo;
import lei.estg.utils.JsonUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class movimentarInimigoEPlayer {
    public static void main(String[] args) throws EmptyStackException, URISyntaxException, IOException {

        Path caminhoArquivo = Paths.get(Main.class.getClassLoader().getResource("missoes/missao.json").toURI());
        Missao missao = JsonUtils.carregarMissao(String.valueOf(caminhoArquivo));

        ControladorJogo controlador = new ControladorJogo();

        Player player = new Player("Jogador 1", 20, 100, 5);

        controlador.selecionarEntrada(player, missao.getEdificio());

        System.out.println("Movimentação do Player:");
        controlador.moverPlayer(player, missao.getEdificio());

        System.out.println("\nMovimentação do Inimigo:");

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
            controlador.moverInimigo(inimigo, missao.getEdificio());
        }

    }
}
