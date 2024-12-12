package lei.estg.models.Interfaces;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.NetworkADT;

import java.util.Iterator;

public interface EdificioADT<T> extends NetworkADT<T> {

    public Iterator<T> getVertex();
    public Iterator<T> getAdjacentes(T divisao);
    public Iterator<T> encontrarCaminho(T divisao1, T divisao2) throws EmptyStackException;
    public void updateEdge(int index1, int index2, double weight);

    int getNumVertices();

    public Iterator<T> findShortestPath(T startVertex, T endVertex);
}
