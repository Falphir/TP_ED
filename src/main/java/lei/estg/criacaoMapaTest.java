package lei.estg;

import lei.estg.models.Inimigo;
import lei.estg.models.Item;
import lei.estg.models.Missao;
import lei.estg.utils.ControladorMissao;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class criacaoMapaTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("VM does not support mandatory encoding UTF-8");
        }

        // Teste de criação de missão
        System.out.println("Iniciando o processo de criação de missão...");
        Missao missao = ControladorMissao.criarMissao();

        // Exibindo informações sobre a missão criada
        System.out.println("Missão criada com sucesso!");
        System.out.println("Código da Missão: " + missao.getCodMissao());
        System.out.println("Versão da Missão: " + missao.getVersao());
        System.out.println("Dificuldade da Missão: " + missao.getDificuldade());
        System.out.println("Tipo da Missão: " + missao.getTipo());
        System.out.println("Alvo da Missão: " + missao.getAlvo());

        // Teste de exportação para JSON


        System.out.println("\nInsira o nome do ficheiro a exportar: ");
        String nomeFicheiro = scanner.nextLine();
        String caminhoFicheiro = "src/main/resources/" + nomeFicheiro + ".json";
        ControladorMissao.exportarMissaoParaJSON(caminhoFicheiro, missao);

        // Teste de geração de inimigos e itens
        System.out.println("\nGerando inimigos e itens para a missão...");
        for (Inimigo inimigo : missao.getInimigos()) {
            System.out.println("Inimigo gerado: " + inimigo.getNome() + " com poder " + inimigo.getPoder());
        }

        for (Item item : missao.getItens()) {
            System.out.println("Item gerado: " + item.getNome() + " com pontos " + item.getPontos());
        }

        // Teste de seleção aleatória de divisões
        System.out.println("\nSeleção de divisão aleatória...");
        missao.getDivisaoList().forEach(divisao -> {
            System.out.println("Divisão: " + divisao.getNome());
        });

        // Teste de mapa
        System.out.println("\nFinalizando o teste de mapa...");
        // O mapa será selecionado automaticamente no processo de criação da missão
        // Nenhuma ação extra necessária, já que isso ocorre na função 'selecionarMapa'
    }
}
