package lei.estg.utils;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.models.Divisao;
import lei.estg.models.enums.EDificuldadeMissao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RelatorioMissao {
    private String codMissao;
    private int versao;
    private EDificuldadeMissao dificuldade;
    private int pontosDeVida;
    private UnorderedArrayList<Divisao> trajetos;

    public RelatorioMissao(String codMissao, int versao, EDificuldadeMissao dificuldade, int pontosDeVida, UnorderedArrayList<Divisao> trajetos) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.dificuldade = dificuldade;
        this.pontosDeVida = pontosDeVida;
        this.trajetos = trajetos;
    }

    public String getCodMissao() {
        return codMissao;
    }

    public void setCodMissao(String codMissao) {
        this.codMissao = codMissao;
    }

    public int getVersao() {
        return versao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public EDificuldadeMissao getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(EDificuldadeMissao dificuldade) {
        this.dificuldade = dificuldade;
    }

    public int getPontosDeVida() {
        return pontosDeVida;
    }

    public void setPontosDeVida(int pontosDeVida) {
        this.pontosDeVida = pontosDeVida;
    }

    public UnorderedArrayList<Divisao> getTrajetos() {
        return trajetos;
    }

    @Override
    public String toString() {
        return "RelatorioMissao{" +
                "codMissao='" + codMissao + '\'' +
                ", versao=" + versao +
                ", dificuldade=" + dificuldade +
                ", pontosDeVida=" + pontosDeVida +
                ", trajetos=" + trajetos +
                '}';
    }

    public void exportarParaJSON(RelatorioMissao missao) {
        JsonObject root;
        String filePath = "relatorioMissoes.json";

        try (FileReader reader = new FileReader(filePath)) {
            root = (JsonObject) Jsoner.deserialize(reader);
        } catch (Exception e) {
            root = new JsonObject();
            root.put("missoes", new JsonArray());
        }
    
        JsonArray missoes = (JsonArray) root.get("missoes");
    
        // Procurar missão existente
        JsonObject missaoExistente = null;
        for (Object obj : missoes) {
            JsonObject m = (JsonObject) obj;
            if (m.get("codMissao").equals(missao.getCodMissao()) && m.get("versao").equals(missao.getVersao())) {
                missaoExistente = m;
                break;
            }
        }
    
        if (missaoExistente == null) {
            missaoExistente = new JsonObject();
            missaoExistente.put("codMissao", missao.getCodMissao());
            missaoExistente.put("dificuldade", missao.getDificuldade().toString());
            missaoExistente.put("versao", missao.getVersao());
            missaoExistente.put("tentativas", new JsonArray());
            missoes.add(missaoExistente);
        }

        JsonArray tentativas = (JsonArray) missaoExistente.get("tentativas");
        JsonObject novaTentativa = new JsonObject();
        novaTentativa.put("codTentativa", String.valueOf(tentativas.size() + 1));
        novaTentativa.put("vida", missao.getPontosDeVida());
    
        JsonArray jsonTrajetos = new JsonArray();
        for (Divisao divisao : missao.getTrajetos()) {
            jsonTrajetos.add("Divisao: " + divisao.getNome());
        }
        novaTentativa.put("trajetos", jsonTrajetos);
    
        tentativas.add(novaTentativa);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(root.toJson());
            writer.flush();
            System.out.println("Relatório exportado para JSON com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao exportar o relatório para JSON: " + e.getMessage());
        }
    }
    
}