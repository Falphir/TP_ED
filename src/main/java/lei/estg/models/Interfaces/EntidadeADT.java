package lei.estg.models.Interfaces;

import lei.estg.models.Divisao;

public interface EntidadeADT<T> {

    public void receberDano(int dano);
    public void atacar(T alvo);
    public void mover(Divisao destino);


}
