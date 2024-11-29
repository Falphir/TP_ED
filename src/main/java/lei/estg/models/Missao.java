package lei.estg.models;

import lei.estg.dataStructures.Graph;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.models.enums.EMissaoTipo;

public class Missao {
    private String codMissao;
    private int versao;
    private EMissaoTipo tipo;
    private Graph<Divisao> edificio;
    private UnorderedArrayList<Inimigo> inimigos;
    private UnorderedArrayList<Divisao> entradasSaidas;
    private Alvo alvo;
    private UnorderedArrayList<Item> itens;

    public Missao() {
    }

    public Missao(String codMissao, int versao, EMissaoTipo tipo, Graph edificio, UnorderedArrayList<Inimigo> inimigos, UnorderedArrayList<Divisao> entradasSaidas, Alvo alvo, UnorderedArrayList<Item> itens) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.tipo = tipo;
        this.edificio = edificio;
        this.inimigos = inimigos;
        this.entradasSaidas = entradasSaidas;
        this.alvo = alvo;
        this.itens = itens;
    }

    public String getCodMissao() {
        return codMissao;
    }

    public void setCodMissao(String codMissao) {
        this.codMissao = codMissao;
    }

    public Alvo getAlvo() {
        return alvo;
    }

    public void setAlvo(Alvo alvo) {
        this.alvo = alvo;
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

    public Graph<Divisao> getEdificio() {
        return edificio;
    }

    public void setEdificio(Graph<Divisao> edificio) {
        this.edificio = edificio;
    }

    public UnorderedArrayList<Inimigo> getInimigos() {
        return inimigos;
    }

    public void setInimigos(UnorderedArrayList<Inimigo> inimigos) {
        this.inimigos = inimigos;
    }

    public UnorderedArrayList<Divisao> getEntradasSaidas() {
        return entradasSaidas;
    }

    public void setEntradasSaidas(UnorderedArrayList<Divisao> entradasSaidas) {
        this.entradasSaidas = entradasSaidas;
    }

    public UnorderedArrayList<Item> getItens() {
        return itens;
    }

    public void setItens(UnorderedArrayList<Item> itens) {
        this.itens = itens;
    }
}
