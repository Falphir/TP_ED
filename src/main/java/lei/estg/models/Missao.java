package lei.estg.models;

import lei.estg.models.Interfaces.EdificioADT;
import lei.estg.models.enums.EDificuldadeMissao;
import lei.estg.models.enums.EMissaoTipo;

/**
 * The type Missao.
 */
public class Missao {
    private String codMissao;
    private int versao;
    private Edificio<Divisao> edificio;
    private EDificuldadeMissao dificuldade;
    private EMissaoTipo tipo;

    /**
     * Instantiates a new Missao.
     */
    public Missao() {
        this.edificio = new Edificio<>(true);
    }

    /**
     * Instantiates a new Missao.
     *
     * @param codMissao   the cod missao
     * @param versao      the versao
     * @param dificuldade the dificuldade
     * @param tipo        the tipo
     * @param edificio    the edificio
     * @param alvo        the alvo
     */
    public Missao(String codMissao, int versao, EDificuldadeMissao dificuldade, EMissaoTipo tipo, Edificio edificio, Alvo alvo) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.dificuldade = dificuldade;
        this.tipo = tipo;
        this.edificio = edificio;
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
     * Gets tipo.
     *
     * @return the tipo
     */
    public EMissaoTipo getTipo() {
        return tipo;
    }

    /**
     * Sets tipo.
     *
     * @param tipo the tipo
     */
    public void setTipo(EMissaoTipo tipo) {
        this.tipo = tipo;
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
     * Gets edificio.
     *
     * @return the edificio
     */
    public Edificio getEdificio() {
        return edificio;
    }

    /**
     * Sets edificio.
     *
     * @param edificio the edificio
     */
    public void setEdificio(Edificio<Divisao> edificio) {
        this.edificio = edificio;
    }

    @Override
    public String toString() {
        return "Missao{" +
                "codMissao='" + codMissao + '\'' +
                ", versao=" + versao +
                ", tipo=" + tipo +
                ", edificio=" + edificio +
                '}';
    }
}
