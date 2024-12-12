package lei.estg.utils;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import lei.estg.models.Edificio;
import lei.estg.models.Interfaces.EdificioADT;
import lei.estg.models.*;
import lei.estg.models.enums.EDificuldadeMissao;
import lei.estg.models.enums.EMissaoTipo;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

public class JsonUtils {
    public static Missao carregarMissao(String caminhoArquivo) throws IOException {
        try (FileReader reader = new FileReader(caminhoArquivo)) {
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);

            JsonArray missoesArray = (JsonArray) jsonObject.get("missoes");
            Missao missao = null;

            Random random = new Random();
            int indexAleatorio = random.nextInt(missoesArray.size());
            JsonObject missaoJson = (JsonObject) missoesArray.get(indexAleatorio);

            missao = criarMissaoFromJson(missaoJson);

            return missao;
        } catch (JsonException e) {
            System.err.println("Erro ao carregar o JSON: " + e.getMessage());
        }

        return null;
    }

    private static Missao criarMissaoFromJson(JsonObject missaoJson) {
        EItemTipoConverter itemTipoConverter = new EItemTipoConverter();
        Missao missao = new Missao();
        missao.setCodMissao((String) missaoJson.get("cod-missao"));
        missao.setVersao(((Number) missaoJson.get("versao")).intValue());
        missao.setDificuldade(EDificuldadeMissao.valueOf(((String) missaoJson.get("dificuldade")).toUpperCase()));
        missao.setTipo(EMissaoTipo.valueOf(((String) missaoJson.get("tipo")).toUpperCase()));

        // Carregando divisões
        JsonArray divisaoArray = (JsonArray) missaoJson.get("edificio");
        Edificio<Divisao> edificio = new Edificio<>(true, divisaoArray.size());
        missao.setEdificio(edificio);

        int index = 0;
        Divisao[] divisaoArrayList = new Divisao[divisaoArray.size()];
        for (Object divisao : divisaoArray) {
            Divisao instance = new Divisao((String) divisao);
            edificio.addVertex(instance);
            divisaoArrayList[index++] = instance;
        }

        // Carregando ligações
        JsonArray ligacoesArray = (JsonArray) missaoJson.get("ligacoes");
        for (Object conexao : ligacoesArray) {
            JsonArray ligacao = (JsonArray) conexao;
            String origemNome = ligacao.get(0).toString();
            String destinoNome = ligacao.get(1).toString();

            Divisao origem = null;
            Divisao destino = null;

            for (Divisao divisao : divisaoArrayList) {
                if (divisao.getNome().equals(origemNome)) {
                    origem = divisao;
                }
                if (divisao.getNome().equals(destinoNome)) {
                    destino = divisao;
                }
            }

            int peso = 1;
            if (origem != null && destino != null) {
                edificio.addEdge(origem, destino, peso);
            }
        }

        // Carregando inimigos
        JsonArray inimigosArray = (JsonArray) missaoJson.get("inimigos");
        for (Object inimigoObj : inimigosArray) {
            JsonObject inimigoJson = (JsonObject) inimigoObj;
            Inimigo inimigo = new Inimigo(
                    (String) inimigoJson.get("nome"),
                    ((Number) inimigoJson.get("poder")).intValue()
            );
            String divisaoNome = (String) inimigoJson.get("divisao");

            Divisao divisao = getDivisao(divisaoNome, edificio);
            if (divisao != null) {
                divisao.getInimigos().addToRear(inimigo);
                atualizarEdges(divisao, edificio, inimigo);
            }
        }

        // Carregando itens
        JsonArray itensArray = (JsonArray) missaoJson.get("itens");
        for (Object itemObj : itensArray) {
            JsonObject itemJson = (JsonObject) itemObj;
            String tipo = (String) itemJson.get("tipo");

            String divisaoNome = (String) itemJson.get("divisao");
            Divisao divisao = getDivisao(divisaoNome, edificio);

            if (tipo.equals("kit de vida")) {
                Item item = new Item(
                        (String) itemJson.get("nome"),
                        ((Number) itemJson.get("pontos-recuperados")).intValue(),
                        itemTipoConverter.convertStringToEItemTipo(tipo)
                );
                if (divisao != null) {
                    divisao.getItems().addToRear(item);
                }
            } else {
                Item item = new Item(
                        (String) itemJson.get("nome"),
                        ((Number) itemJson.get("pontos-extra")).intValue(),
                        itemTipoConverter.convertStringToEItemTipo(tipo)
                );
                if (divisao != null) {
                    divisao.getItems().addToRear(item);
                }
            }
        }

        // Carregando entradas e saídas
        JsonArray entradaSaidaArray = (JsonArray) missaoJson.get("entradas-saidas");
        for (Object entradaSaidaObj : entradaSaidaArray) {
            String divisaoNome = (String) entradaSaidaObj;
            Divisao divisao = getDivisao(divisaoNome, edificio);
            if (divisao != null) {
                divisao.setEntradaSaida(true);
            }
        }

        // Carregando alvo
        JsonObject alvoObj = (JsonObject) missaoJson.get("alvo");
        Alvo alvo = new Alvo(
                (String) alvoObj.get("tipo")
        );
        String divisaoNome = (String) alvoObj.get("divisao");
        Divisao divisao = getDivisao(divisaoNome, edificio);
        if (divisao != null) {
            divisao.setAlvo(alvo);
        }

        return missao;
    }

    public static Divisao getDivisao(String nomeDivisao, Edificio edificio) {
        Iterator divisoes = edificio.getVertex();
        while (divisoes.hasNext()) {
            Divisao divisao = (Divisao) divisoes.next();
            if (divisao.getNome().equals(nomeDivisao)) {
                return divisao;
            }
        }
        return null;
    }

    private static void atualizarEdges(Divisao divisao, Edificio<Divisao> edificio, Inimigo inimigo) {
        Iterator<Divisao> adjacentes = edificio.getAdjacentes(divisao);
        while (adjacentes.hasNext()) {
            Divisao adjacente = adjacentes.next();
            double pesoAtual = edificio.getWeight(divisao, adjacente);
            double novoPeso = pesoAtual + inimigo.getPoder();

            edificio.updateEdge(edificio.getIndex(divisao), edificio.getIndex(adjacente), novoPeso);
        }
    }
}