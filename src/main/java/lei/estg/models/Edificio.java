package lei.estg.models;

import lei.estg.dataStructures.Network;
import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.Interfaces.EdificioADT;
import lei.estg.dataStructures.interfaces.UnorderedListADT;

import java.util.Iterator;

public class Edificio<T> extends Network<T> implements EdificioADT<T> {

    public Edificio() {
        super();
    }

    @Override
    public Iterator<T> getVertex() {
        UnorderedListADT<T> vertex = new UnorderedArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            vertex.addToRear(vertices[i]);
        }
        return vertex.iterator();
    }

    @Override
    public Iterator<T> getAdjacentes(T divisao) {
        UnorderedListADT<T> adjacentes = new UnorderedArrayList<>();
        int index = getIndex(divisao);
        if (indexIsValid(index)) {
            for (int i = 0; i < numVertices; i++) {
                if (weightMatrix[index][i] != Integer.MAX_VALUE) {
                    adjacentes.addToRear(vertices[i]);
                }
            }
        }
        return adjacentes.iterator();
    }

    @Override
    public Iterator<T> encontrarCaminho(T divisao1, T divisao2) throws EmptyStackException {
        UnorderedListADT<T> caminho = new UnorderedArrayList<>();
        boolean[] visitado = new boolean[numVertices];
        if(dfsCaminho(getIndex(divisao1), getIndex(divisao2), visitado, caminho)) {
            return caminho.iterator();
        }
        return null;
    }

    private boolean dfsCaminho(int index1, int index2, boolean[] visitado, UnorderedListADT<T> caminho) throws EmptyStackException {
        visitado[index1] = true;
        caminho.addToRear(vertices[index1]);
        if (index1 == index2) {
            return true;
        }
        for (int i = 0; i < numVertices; i++) {
            if (weightMatrix[index1][i] != Integer.MAX_VALUE && !visitado[i]) {
                if (dfsCaminho(i, index2, visitado, caminho)) {
                    return true;
                }
            }
        }
        caminho.remove(vertices[index1]);
        return false;
    }

    @Override
    public void updateEdge(int index1, int index2, int weight) {

        if (indexIsValid(index1) && indexIsValid(index2)) {
            weightMatrix[index1][index2] = weight;

            if (isBidirectional) {
                weightMatrix[index2][index1] = weight;
            }
        } else {
            throw new IllegalArgumentException("Um ou ambos os vértices não foram encontrados na rede.");
        }
    }

}
