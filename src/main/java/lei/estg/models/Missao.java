package lei.estg.models;

public class Missao {
    private String codMissao;
    private Alvo alvo;
    private int versao;

    public Missao(String codMissao, Alvo alvo, int versao) {
        this.codMissao = codMissao;
        this.alvo = alvo;
        this.versao = versao;
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

    public int getVersao() {
        return versao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }
}
