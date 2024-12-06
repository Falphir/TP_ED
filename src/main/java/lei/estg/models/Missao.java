package lei.estg.models;

import lei.estg.dataStructures.interfaces.EdificioADT;
import lei.estg.models.enums.EDificuldadeMissao;
import lei.estg.models.enums.EMissaoTipo;

public class Missao {
    private String codMissao;
    private int versao;
    private EdificioADT<Divisao> edificio;
    private EDificuldadeMissao dificuldade;
    private EMissaoTipo tipo;

    public Missao() {
        this.edificio = new Edificio<>();

    }

    public Missao(String codMissao, int versao, EDificuldadeMissao dificuldade, EMissaoTipo tipo, Edificio edificio, Alvo alvo) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.dificuldade = dificuldade;
        this.tipo = tipo;
        this.edificio = edificio;
    }

    public String getCodMissao() {
        return codMissao;
    }

    public void setCodMissao(String codMissao) {
        this.codMissao = codMissao;
    }

    public EDificuldadeMissao getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(EDificuldadeMissao dificuldade) {
        this.dificuldade = dificuldade;
    }

    public EMissaoTipo getTipo() {
        return tipo;
    }

    public void setTipo(EMissaoTipo tipo) {
        this.tipo = tipo;
    }

    public int getVersao() {
        return versao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public EdificioADT<Divisao> getEdificio() {
        return edificio;
    }

    public void setEdificio(EdificioADT<Divisao> edificio) {
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
