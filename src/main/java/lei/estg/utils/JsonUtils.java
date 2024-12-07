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

public class JsonUtils {
    public static Missao carregarMissao(String caminhoArquivo) throws IOException {
        try (FileReader reader = new FileReader(caminhoArquivo)) {
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);

            EItemTipoConverter itemTipoConverter = new EItemTipoConverter();

            Missao missao = new Missao();
            missao.setCodMissao((String) jsonObject.get("cod-missao"));
            missao.setVersao(((Number) jsonObject.get("versao")).intValue());
            missao.setDificuldade(EDificuldadeMissao.valueOf(((String) jsonObject.get("dificuldade")).toUpperCase()));
            missao.setTipo(EMissaoTipo.valueOf(((String) jsonObject.get("tipo")).toUpperCase()));

            EdificioADT<Divisao> edificio = new Edificio<>();
            missao.setEdificio(edificio);

            JsonArray divisaoArray = (JsonArray) jsonObject.get("edificio");
            for (Object divisao : divisaoArray) {
                Divisao instance = new Divisao((String) divisao);
                edificio.addVertex(instance);
            }

            JsonArray ligacoesArray = (JsonArray) jsonObject.get("ligacoes");
            for (Object conexao : ligacoesArray) {
                JsonArray ligacao = (JsonArray) conexao;
                String origemNome = ligacao.get(0).toString();
                String destinoNome = ligacao.get(1).toString();
                int peso = 0;

                Divisao origem = new Divisao(origemNome);
                Divisao destino = new Divisao(destinoNome);

                edificio.addEdge(origem, destino, peso);
            }

            JsonArray inimigosArray = (JsonArray) jsonObject.get("inimigos");
            for (Object inimigoObj : inimigosArray) {
                JsonObject inimigoJson = (JsonObject) inimigoObj;
                Inimigo inimigo = new Inimigo(
                        (String) inimigoJson.get("nome"),
                        ((Number) inimigoJson.get("poder")).intValue()
                );
                String divisaoNome = (String) inimigoJson.get("divisao");
               // System.out.println("Here: " + edificio);
                Divisao divisao = getDivisao(divisaoNome, (Edificio) edificio);
                if (divisao != null) {
                    divisao.getInimigos().addToRear(inimigo);
                }
            }

            JsonArray itensArray = (JsonArray) jsonObject.get("itens");
            for (Object itemObj : itensArray) {
                JsonObject itemJson = (JsonObject) itemObj;
                String tipo = (String) itemJson.get("tipo");

                String divisaoNome = (String) itemJson.get("divisao");
                Divisao divisao = getDivisao(divisaoNome, (Edificio) edificio);

                System.out.println("tipo: " + tipo);

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

            return missao;
        } catch (JsonException e) {
            System.err.println("Erro ao carregar o JSON: " + e.getMessage());
        }

        return null;
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
}
