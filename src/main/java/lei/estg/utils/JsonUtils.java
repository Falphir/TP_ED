package lei.estg.utils;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import lei.estg.dataStructures.Graph;
import lei.estg.dataStructures.Network;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.interfaces.NetworkADT;
import lei.estg.models.*;
import lei.estg.models.enums.EItemTipo;
import lei.estg.models.enums.EMissaoTipo;


import java.io.FileReader;
import java.io.IOException;

public class JsonUtils {
    public static Missao carregarMissao(String caminhoArquivo) throws IOException {

        try (FileReader reader = new FileReader(caminhoArquivo)) {
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);

            EItemTipoConverter itemTipoConverter = new EItemTipoConverter();
            
            Missao missao = new Missao();

            missao.setCodMissao((String) jsonObject.get("cod-missao"));
            missao.setVersao(((Number) jsonObject.get("versao")).intValue());

            Network<Divisao> edificio = new Network<>();

            missao.setEdificio(edificio);

            JsonArray divisaoArray = (JsonArray) jsonObject.get("edificio");
            for (Object divisao : divisaoArray) {
                Divisao instance = new Divisao((String) divisao);

                missao.getDivisaoList().addToRear(instance);
                missao.getEdificio().addVertex(instance);
            }

            JsonArray ligacoesArray = (JsonArray) jsonObject.get("ligacoes");
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

            JsonArray inimigosArray = (JsonArray) jsonObject.get("inimigos");
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
                    itemInstancia = new Item(
                            (String) itemObj.get("nome"),
                            new Divisao((String) itemObj.get("divisao")),
                            (((Number) itemObj.get("pontos-recuperados")).intValue()),
                            itemTipoConverter.convertStringToEItemTipo(tipoItem)
                    );
                    missao.getItens().addToRear(itemInstancia);
                } else if ("colete".equals(tipoItem)) {
                    itemInstancia = new Item(
                            (String) itemObj.get("nome"),
                            new Divisao((String) itemObj.get("divisao")),
                            (((Number) itemObj.get("pontos-extra")).intValue()),
                            itemTipoConverter.convertStringToEItemTipo(tipoItem)
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
