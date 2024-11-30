package lei.estg;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.interfaces.UnorderedListADT;
import lei.estg.models.*;
import lei.estg.utils.JsonUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
        public static void main(String[] args) {
            try {
                // Caminho do arquivo JSON
                Path caminhoArquivo = Paths.get(Main.class.getClassLoader().getResource("missoes/missao.json").toURI());

                // Carregar a missão a partir do arquivo JSON
                String conteudoJson = new String(Files.readAllBytes(caminhoArquivo));
                System.out.println(conteudoJson);

                Missao missao = JsonUtils.carregarMissao(String.valueOf(caminhoArquivo));

                // Exibir dados da missão
                System.out.println("Código da missão: " + missao.getCodMissao());
                System.out.println("Versão: " + missao.getVersao());

                // Exibir os edifícios
                System.out.println("\nEdifícios: " + missao.getEdificio().toString());

                System.out.println("\nDivisoes: " + missao.getDivisaoList().toString());


                // Exibir inimigos
                System.out.println("\nInimigos:");
                for (Inimigo inimigo : missao.getInimigos()) {
                    System.out.println("Nome: " + inimigo.getNome() + ", Poder: " + inimigo.getPoder() + ", Divisão: " + inimigo.getDivisao());
                }

                // Exibir entradas e saídas
                System.out.println("\nEntradas e saídas:");
                for (Divisao entradaSaida : missao.getEntradasSaidas()) {
                    System.out.println("- " + entradaSaida);
                }

                // Exibir alvo
                System.out.println("\nAlvo:");
                System.out.println("Divisão: " + missao.getAlvo().getDivisao() + ", Tipo: " + missao.getAlvo().getTipo());

                // Exibir itens
                System.out.println("\nItens:");
                for (Item item : missao.getItens()) {
                    if (item instanceof Kit) {
                        System.out.println("Tipo: " + item.getTipo() + ", Divisão: " + item.getLocalizacao() +
                                ", Pontos Recuperados: " + item.getPontos());
                    } else if (item instanceof Colete) {
                        System.out.println("Tipo: " + item.getTipo() + ", Divisão: " + item.getLocalizacao() +
                                ", Pontos Extra: " + item.getPontos());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
/*
            Missao missao = new Missao();

            Inimigo inimigo = new Inimigo(
                    "Loki",
                    1000,
                    new Divisao("divisao1")
            );

            // Já inicializado no construtor de Missao
            missao.getInimigos().addToRear(inimigo);

            System.out.println(inimigo.toString());
            System.out.println(missao.getInimigos().toString());*/
        }
    }
