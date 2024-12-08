package lei.estg.utils;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import lei.estg.models.Missao;
import lei.estg.models.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JogoConfig {

    public JsonObject carregarConfig(String caminhoFicheiro) {
        try (FileReader reader = new FileReader(caminhoFicheiro)) {
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);
            return jsonObject;
        } catch (JsonException | IOException e) {

        }
        return null;
    }

    public Player carregarPlayerConfig(String caminhoFicheiro) {
        if (caminhoFicheiro == null) return null;

        JsonObject playerObject = (JsonObject) carregarConfig(caminhoFicheiro).get("player");

        Player player = new Player(
                (String) playerObject.get("nome"),
                ((Number) playerObject.get("poder")).intValue(),
                ((Number) playerObject.get("vidaMaxima")).intValue(),
                ((Number) playerObject.get("mochila-limite")).intValue()
        );
        return player;

    }

}
