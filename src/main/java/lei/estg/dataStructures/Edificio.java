package lei.estg.dataStructures;

import lei.estg.dataStructures.interfaces.EdificioADT;
import lei.estg.dataStructures.interfaces.UnorderedListADT;
import lei.estg.models.Divisao;

public class Edificio<T> extends Network<T> implements EdificioADT<T> {

    public Edificio() {
        super();
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

    @Override
    public int getWeight(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            return (int) weightMatrix[index1][index2];
        } else {
            throw new IllegalArgumentException("Um ou ambos os vértices não foram encontrados na rede.");
        }
    }

    @Override
    public UnorderedListADT<T> getAdjVertex(int index) {
        UnorderedListADT<T> adjVertices = new UnorderedArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[index][i]) {
                adjVertices.addToRear(vertices[i]);
            }
        }
        return adjVertices;
    }



}
