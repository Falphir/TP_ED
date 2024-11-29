package lei.estg.utils;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import lei.estg.dataStructures.Graph;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.models.*;
import lei.estg.models.enums.EMissaoTipo;


import java.io.FileReader;
import java.io.IOException;

public class JsonUtils {
    public static Missao carregarMissao(String caminhoArquivo) throws IOException {

        try (FileReader reader = new FileReader(caminhoArquivo)) {
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);

            Missao missao = new Missao();

            missao.setCodMissao((String) jsonObject.get("cod-missao"));
            missao.setVersao(((Number) jsonObject.get("versao")).intValue());

            Graph<Divisao> edificio = new Graph<>();

            missao.setEdificio(edificio);

            // Adiciona os vértices ao grafo
            JsonArray divisaoArray = (JsonArray) jsonObject.get("edificio");
            for (Object divisao : divisaoArray) {
                missao.getEdificio().addVertex(new Divisao((String) divisao));
            }

            JsonArray ligacoesArray = (JsonArray) jsonObject.get("ligacoes");
            for (Object conexao : ligacoesArray) {
                JsonArray ligacao = (JsonArray) conexao;
                Divisao origem = new Divisao(ligacao.get(0).toString());
                Divisao destino = new Divisao(ligacao.get(1).toString());
                ;
                missao.getEdificio().addEdge(origem, destino);
            }

            JsonArray inimigosArray = (JsonArray) jsonObject.get("inimigos");
            missao.setInimigos(new UnorderedArrayList<Inimigo>());
            for (Object inimigo : inimigosArray) {
                JsonObject inimigoObj = (JsonObject) inimigo;
                Inimigo inimigoInstancia = new Inimigo(
                        (String) inimigoObj.get("nome"),
                        (((Number) inimigoObj.get("poder")).intValue()),
                        new Divisao((String) inimigoObj.get("divisao"))
                );
                missao.getInimigos().addToRear(inimigoInstancia);
            }

            JsonArray entradasSaidasArray = (JsonArray) jsonObject.get("entradas-saidas");
            missao.setEntradasSaidas(new UnorderedArrayList<>());
            for (Object entradaSaida : entradasSaidasArray) {
                missao.getEntradasSaidas().addToRear(new Divisao((String) entradaSaida));
            }

            JsonObject alvoObj = (JsonObject) jsonObject.get("alvo");
            Alvo alvo = new Alvo(new Divisao((String) alvoObj.get("divisao")), (String) alvoObj.get("tipo"));
            missao.setAlvo(alvo);

            JsonArray itensArray = (JsonArray) jsonObject.get("itens");
            missao.setItens(new UnorderedArrayList<>());
            for (Object item : itensArray) {
                JsonObject itemObj = (JsonObject) item;
                String tipoItem = (String) itemObj.get("tipo");
                Item itemInstancia;

                if ("kit de vida".equals(tipoItem)) {
                    itemInstancia = new Kit(
                            new Divisao((String) itemObj.get("divisao")),
                            ((Long) itemObj.get("pontos-recuperados")).intValue(),
                            (String) itemObj.get("tipo")
                    );
                    missao.getItens().addToRear(itemInstancia);
                } else if ("colete".equals(tipoItem)) {
                    itemInstancia = new Colete(
                            new Divisao((String) itemObj.get("divisao")),
                            ((Long) itemObj.get("pontos-extra")).intValue(),
                            (String) itemObj.get("tipo")
                    );
                    missao.getItens().addToRear(itemInstancia);
                }
            }

            return missao;

        } catch (JsonException e) {
            System.err.println("Erro ao carregar o JSON: " + e.getMessage());
        }

        return null;
    }
}
