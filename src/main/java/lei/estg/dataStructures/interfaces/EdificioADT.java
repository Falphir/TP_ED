package lei.estg.dataStructures.interfaces;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.Divisao;
import lei.estg.models.Inimigo;

import java.util.Iterator;

public interface EdificioADT<T> extends NetworkADT<T> {

    public void addDivisao(T divisao);
    public void removeDivisao(T divisao);
    public boolean containsDivisao(T divisao);
    public Iterator<T> getDivisoes();

    public void addLigacao(T divisao1, T divisao2, int peso);
    public void removeLigacao(T divisao1, T divisao2);
    public boolean existeLigacao(T divisao1, T divisao2);
    public int getPesoLigacao(T divisao1, T divisao2);
    public Iterator<T> getAdjacentes(T divisao);

    /*
    public void addInimigo(T divisao, Inimigo inimigo);
    public void removeInimigo(T divisao, Inimigo inimigo);
    public Iterator<Inimigo> getInimigos(T divisao);
*/

    public boolean existeCaminho(T divisao1, T divisao2);
    public Iterator<T> encontrarCaminho(T divisao1, T divisao2) throws EmptyStackException;

    public void updateEdge(int index1, int index2, int weight);
}
