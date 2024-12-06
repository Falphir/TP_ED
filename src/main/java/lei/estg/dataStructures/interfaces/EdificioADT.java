package lei.estg.dataStructures.interfaces;

import lei.estg.dataStructures.UnorderedArrayList;
import lei.estg.models.Divisao;

public interface EdificioADT<T> extends NetworkADT<T> {
    public void updateEdge(int index1, int index2, int weight);
    public int getWeight(int index1, int index2);
    public UnorderedListADT<T> getAdjVertex(int index);
}
