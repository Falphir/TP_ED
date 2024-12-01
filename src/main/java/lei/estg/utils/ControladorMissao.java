package lei.estg.utils;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import lei.estg.Main;
import lei.estg.dataStructures.Graph;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.interfaces.UnorderedListADT;
import lei.estg.models.*;
import lei.estg.models.enums.EDificuldadeMissao;
import lei.estg.models.enums.EMissaoTipo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class ControladorMissao {

    public static void exportarMissaoParaJSON(String caminhoFicheiro, Missao missao) {
        // Criação do objeto JSON para a missão
        JsonObject missaoObject = new JsonObject();

        // Adicionar as informações da missão
        missaoObject.put("cod-missao", missao.getCodMissao());
        missaoObject.put("versao", missao.getVersao());
        missaoObject.put("dificuldade", missao.getDificuldade().toString().toLowerCase());

        // Adicionar inimigos da missão como um array
        JsonArray inimigosArray = new JsonArray();
        for (Inimigo inimigo : missao.getInimigos()) {
            JsonObject inimigoObject = new JsonObject();
            inimigoObject.put("nome", inimigo.getNome());
            inimigoObject.put("poder", inimigo.getPoder());
            inimigosArray.add(inimigoObject);
        }
        missaoObject.put("inimigos", inimigosArray);

        // Exportar para arquivo JSON
        try (FileWriter file = new FileWriter(caminhoFicheiro)) {
            file.write(missaoObject.toJson());
            System.out.println("Missão exportada com sucesso para: " + caminhoFicheiro);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Missao criarMissao() {
        try {
            Path caminhoArquivo = Paths.get(Main.class.getClassLoader().getResource("config/simuladorConfig.json").toURI());
            Scanner scanner = new Scanner(System.in);
            Missao missao = new Missao();
            String dificuldade = "";
            String tipo = "";

            System.out.println("Criar Missao:");
            System.out.println("Insira o código da missao:");
            missao.setCodMissao(scanner.nextLine());
            System.out.println("Insira a versao da missao:");
            missao.setVersao(scanner.nextInt());
            scanner.nextLine();
            while (true) {
                System.out.println("Insira a dificuldade da missao (facil | medio | dificil):");
                dificuldade = scanner.nextLine().trim().toLowerCase(); // Remove espaços e garante letras minúsculas

                if (dificuldade.equals("facil") || dificuldade.equals("medio") || dificuldade.equals("dificil")) {
                    missao.setDificuldade(EDificuldadeMissao.valueOf(dificuldade.toUpperCase()));
                    System.out.println("Dificuldade selecionada: " + dificuldade);
                    break; // Sai do loop se a entrada for válida
                } else {
                    System.out.println("Entrada inválida. Por favor, insira uma das opções: facil, medio, ou dificil.");
                }
            }

            while (true) {
                System.out.println("Insira o tipo de missao:");
                System.out.println("Resgate - Resgatar Reféns");
                System.out.println("Recuperar - Recuperar Bens Valiosos");
                System.out.println("Neutralizar - Neutralizar Armas de Destruição em Massa");
                tipo = scanner.nextLine().trim().toLowerCase(); // Remove espaços e garante letras minúsculas
                System.out.println("inserido: " + tipo);
                if (tipo.equals("resgate") || tipo.equals("recuperar") || tipo.equals("neutralizar")) {
                    missao.setTipo(EMissaoTipo.valueOf(tipo.toUpperCase()));
                    System.out.println("Tipo selecionado: " + missao.getTipo().getDescription());
                    break; // Sai do loop se a entrada for válida
                } else {
                    System.out.println("Entrada inválida. Por favor, insira uma das opções: Resgate, Recuperar, ou Neutralizar.");
                }
            }
            selecionarMapa(missao);

            System.out.println("Insira o tipo de Alvo:");
            System.out.println(missao.getDivisaoList());
            missao.setAlvo(new Alvo(obterDivisaoAleatoria(missao.getDivisaoList()), scanner.nextLine()));

            carregarDadosConfig(String.valueOf(caminhoArquivo), missao);

            return missao;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void selecionarMapa(Missao missao) {
        try {
            Path caminhoArquivo = Paths.get(Main.class.getClassLoader().getResource("mapas/mapa.json").toURI());
            Scanner scanner = new Scanner(System.in);

            boolean found = false;

            FileReader readerMapa = new FileReader(String.valueOf(caminhoArquivo));

            JsonObject mapaObject = (JsonObject) Jsoner.deserialize(readerMapa);

            JsonArray mapasArray = (JsonArray) mapaObject.get("mapas");

            System.out.println("Selicione o Mapa");
            for( Object mapa : mapasArray) {
                JsonObject mapaObj = (JsonObject) mapa;
                System.out.println((String) mapaObj.get("nome")+ "\n");
            }

            JsonObject mapaSelecionadoObj = null;

            while (!found) {
                System.out.print("Insira o nome do mapa: ");
                String mapaSelecionado = scanner.nextLine();

                boolean mapaEncontrado = false;

                for (Object mapa : mapasArray) {
                    JsonObject mapaObj = (JsonObject) mapa;
                    if (mapaObj.get("nome").equals(mapaSelecionado)) {
                        mapaEncontrado = true;
                        mapaSelecionadoObj = mapaObj;
                        break;
                    }
                }

                if (mapaEncontrado) {
                    System.out.println("Mapa selecionado: " + mapaSelecionado);
                    found = true;
                } else {
                    System.out.println("Mapa inválido! Tente novamente.");
                }
            }


            JsonArray divisaoArray = (JsonArray) mapaSelecionadoObj.get("edificio");
            if (divisaoArray.isEmpty()) {
                System.out.println("Nenhuma divisão encontrada no mapa.");
            } else {
                for (Object divisao : divisaoArray) {
                    Divisao instance = new Divisao((String) divisao);
                    missao.getDivisaoList().addToRear(instance);
                    missao.getEdificio().addVertex(instance);
                }
            }

            System.out.println("DivisaoList: " + missao.getDivisaoList());

            JsonArray ligacoesArray = (JsonArray) mapaSelecionadoObj.get("ligacoes");
            for (Object conexao : ligacoesArray) {
                JsonArray ligacao = (JsonArray) conexao;
                String origemNome = ligacao.get(0).toString();
                String destinoNome = ligacao.get(1).toString();

                Divisao origem = new Divisao(origemNome);
                Divisao destino = new Divisao(destinoNome);

                int origemIndex = missao.getEdificio().getIndex(missao.findorAddDivisao(origemNome));
                int destinoIndex = missao.getEdificio().getIndex(missao.findorAddDivisao(destinoNome));

                if (origemIndex != -1 && destinoIndex != -1) {
                    missao.getEdificio().addEdge(origemIndex, destinoIndex);
                } else {
                    System.out.println("Erro: uma das divisões não foi encontrada.");
                }
            }

            JsonArray entradasSaidasArray = (JsonArray) mapaSelecionadoObj.get("entradas-saidas");
            missao.setEntradasSaidas(new UnorderedArrayList<>());
            for (Object entradaSaida : entradasSaidasArray) {
                missao.getEntradasSaidas().addToRear(new Divisao((String) entradaSaida));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void carregarDadosConfig(String caminhoFicheiro, Missao missao) {
        try (FileReader reader = new FileReader(caminhoFicheiro)) {
            JsonObject configObject = (JsonObject) Jsoner.deserialize(reader);

            // Ler dados da dificuldade
            JsonObject missaoObject = (JsonObject) configObject.get("Missao");
            JsonArray dificuldadeArray = (JsonArray) missaoObject.get("dificuldade");
            String dificuldadeAtual = missao.getDificuldade().toString().toLowerCase();

            for (Object obj : dificuldadeArray) {
                JsonObject dificuldadeObject = (JsonObject) obj;

                if (dificuldadeObject.get("nome").equals(dificuldadeAtual)) {
                    int numInimigos = ((Number) dificuldadeObject.get("numInimigos")).intValue();
                    for (int i = 0; i < numInimigos; i++) {
                        missao.getInimigos().addToRear(gerarInimigo(missao));
                    }

                    int numKits = Integer.parseInt(dificuldadeObject.get("numKits").toString());
                    for (int i = 0; i < numKits; i++) {
                        missao.getItens().addToRear(gerarItem(missao, "kit"));
                    }

                    int numColetes = Integer.parseInt(dificuldadeObject.get("numColetes").toString());
                    for (int i = 0; i < numColetes; i++) {
                        missao.getItens().addToRear(gerarItem(missao, "colete"));
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de configuração: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao processar o arquivo JSON: " + e.getMessage());
        }
    }

    private static Inimigo gerarInimigo(Missao missao) {

        try {
            Random random = new Random();
            Path caminhoArquivo = Paths.get(Main.class.getClassLoader().getResource("config/inimigosConfig.json").toURI());

            FileReader readerInimigos = new FileReader(String.valueOf(caminhoArquivo));

            JsonObject inimigosObject = (JsonObject) Jsoner.deserialize(readerInimigos);
            JsonArray inimigosArray = (JsonArray) inimigosObject.get("inimigos");

            JsonObject inimigoAleatorio = (JsonObject) inimigosArray.get(random.nextInt(inimigosArray.size()));

            Inimigo inimigo = new Inimigo(
                    (String) inimigoAleatorio.get("nome"),
                    ((Number) inimigoAleatorio.get("poder")).intValue(),
                    obterDivisaoAleatoria(missao.getDivisaoList())
                );

            return inimigo;

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JsonException e) {
            throw new RuntimeException(e);
        }
    }

    private static Item gerarItem(Missao missao, String tipo) {
        try {
            EItemTipoConverter itemTipoConverter = new EItemTipoConverter();
            Random random = new Random();
            Path caminhoArquivo = Paths.get(Main.class.getClassLoader().getResource("config/itensConfig.json").toURI());

            FileReader readerItens = new FileReader(String.valueOf(caminhoArquivo));

            JsonObject itensObject = (JsonObject) Jsoner.deserialize(readerItens);

            JsonObject itens = (JsonObject) itensObject.get("itens");

            if (tipo.equals("kit")) {
                JsonArray kitsArray = (JsonArray) itens.get("kits");

                JsonObject kitAleatorio = (JsonObject) kitsArray.get(random.nextInt(kitsArray.size()));

                Item item = new Item(
                        (String) kitAleatorio.get("nome"),
                        obterDivisaoAleatoria(missao.getDivisaoList()),
                        ((Number) kitAleatorio.get("pontos-recuperados")).intValue(),
                        itemTipoConverter.convertStringToEItemTipo((String) kitAleatorio.get("tipo"))

                );
                return item;
            } else {
                JsonArray coletesArray = (JsonArray) itens.get("coletes");

                JsonObject coleteAleatorio = (JsonObject) coletesArray.get(random.nextInt(coletesArray.size()));

                Item item = new Item(
                        (String) coleteAleatorio.get("nome"),
                        obterDivisaoAleatoria(missao.getDivisaoList()),
                        ((Number) coleteAleatorio.get("pontos-recuperados")).intValue(),
                        itemTipoConverter.convertStringToEItemTipo((String) coleteAleatorio.get("tipo"))
                );
                return item;
            }


        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JsonException e) {
            throw new RuntimeException(e);
        }
    }


    private static Divisao obterDivisaoAleatoria(UnorderedListADT<Divisao> divisaoList) {
        if (divisaoList.isEmpty()) {
            throw new IllegalArgumentException("A lista de divisões está vazia ou é nula.");
        }
        Random random = new Random();
        int indexAleatorio = random.nextInt(divisaoList.size()); // Gera um índice entre 0 e tamanho-1

        Iterator<Divisao> iterator = divisaoList.iterator();
        int contador = 0;

        while (iterator.hasNext()) {
            Divisao divisao = iterator.next();
            if (contador == indexAleatorio) {
                return divisao;
            }
            contador++;
        }
        return null;
    }


}
