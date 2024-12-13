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

/**
 * The type Relatorio missao.
 */
public class RelatorioMissao {
    private String codMissao;
    private int versao;
    private EDificuldadeMissao dificuldade;
    private int pontosDeVida;
    private UnorderedArrayList<Divisao> trajetos;

    /**
     * Instantiates a new Relatorio missao.
     *
     * @param codMissao    the cod missao
     * @param versao       the versao
     * @param dificuldade  the dificuldade
     * @param pontosDeVida the pontos de vida
     * @param trajetos     the trajetos
     */
    public RelatorioMissao(String codMissao, int versao, EDificuldadeMissao dificuldade, int pontosDeVida, UnorderedArrayList<Divisao> trajetos) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.dificuldade = dificuldade;
        this.pontosDeVida = pontosDeVida;
        this.trajetos = trajetos;
    }

    /**
     * Gets cod missao.
     *
     * @return the cod missao
     */
    public String getCodMissao() {
        return codMissao;
    }

    /**
     * Sets cod missao.
     *
     * @param codMissao the cod missao
     */
    public void setCodMissao(String codMissao) {
        this.codMissao = codMissao;
    }

    /**
     * Gets versao.
     *
     * @return the versao
     */
    public int getVersao() {
        return versao;
    }

    /**
     * Sets versao.
     *
     * @param versao the versao
     */
    public void setVersao(int versao) {
        this.versao = versao;
    }

    /**
     * Gets dificuldade.
     *
     * @return the dificuldade
     */
    public EDificuldadeMissao getDificuldade() {
        return dificuldade;
    }

    /**
     * Sets dificuldade.
     *
     * @param dificuldade the dificuldade
     */
    public void setDificuldade(EDificuldadeMissao dificuldade) {
        this.dificuldade = dificuldade;
    }

    /**
     * Gets pontos de vida.
     *
     * @return the pontos de vida
     */
    public int getPontosDeVida() {
        return pontosDeVida;
    }

    /**
     * Sets pontos de vida.
     *
     * @param pontosDeVida the pontos de vida
     */
    public void setPontosDeVida(int pontosDeVida) {
        this.pontosDeVida = pontosDeVida;
    }

    /**
     * Gets trajetos.
     *
     * @return the trajetos
     */
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

    /**
     * Exports the mission report to a JSON file.
     * This method serializes the mission report to a JSON format and writes it to a file.
     * If the file already contains a report for the same mission and version, it updates the existing report.
     * Otherwise, it adds a new report to the file.
     *
     * @param missao the mission report to be exported
     */
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