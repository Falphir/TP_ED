package lei.estg.models;

import lei.estg.dataStructures.Graph;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.interfaces.UnorderedListADT;
import lei.estg.models.enums.EMissaoTipo;

import java.util.Iterator;

public class Missao {
    private String codMissao;
    private int versao;
    private EMissaoTipo tipo;
    private Graph<Divisao> edificio;
    private UnorderedListADT<Divisao> divisaoList = new UnorderedArrayList<>();
    private UnorderedListADT<Inimigo> inimigos;
    private UnorderedListADT<Divisao> entradasSaidas;
    private Alvo alvo;
    private UnorderedListADT<Item> itens;

    public Missao() {
        this.inimigos = new UnorderedArrayList<>();
        this.entradasSaidas = new UnorderedArrayList<>();
        this.itens = new UnorderedArrayList<>();
    }

    public Missao(String codMissao, int versao, EMissaoTipo tipo, Graph edificio, Alvo alvo) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.tipo = tipo;
        this.edificio = edificio;
        this.inimigos = new UnorderedArrayList<>();
        this.entradasSaidas = new UnorderedArrayList<>();
        this.alvo = alvo;
        this.itens = new UnorderedArrayList<>();
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

    public UnorderedListADT<Inimigo> getInimigos() {
        return inimigos;
    }

    public void setInimigos(UnorderedListADT<Inimigo> inimigos) {
        this.inimigos = inimigos;
    }

    public UnorderedListADT<Divisao> getEntradasSaidas() {
        return entradasSaidas;
    }

    public void setEntradasSaidas(UnorderedListADT<Divisao> entradasSaidas) {
        this.entradasSaidas = entradasSaidas;
    }

    public UnorderedListADT<Item> getItens() {
        return itens;
    }

    public void setItens(UnorderedListADT<Item> itens) {
        this.itens = itens;
    }

    public UnorderedListADT<Divisao> getDivisaoList() {
        return divisaoList;
    }

    public void setDivisaoList(UnorderedListADT<Divisao> divisaoList) {
        this.divisaoList = divisaoList;
    }

    public Divisao findorAddDivisao(String nome) {
        Iterator<Divisao> iter = divisaoList.iterator();
        while (iter.hasNext()) {
            Divisao divisao = iter.next();
            if (divisao.getNome().equals(nome)) {
                return divisao;
            }
        }

        Divisao novaDivisao = new Divisao(nome);
        divisaoList.addToRear(novaDivisao);
        return novaDivisao;
    }

    @Override
    public String toString() {
        return "Missao{" +
                "codMissao='" + codMissao + '\'' +
                ", versao=" + versao +
                ", tipo=" + tipo +
                ", edificio=" + edificio +
                ", inimigos=" + inimigos +
                ", entradasSaidas=" + entradasSaidas +
                ", alvo=" + alvo +
                ", itens=" + itens +
                '}';
    }
}
