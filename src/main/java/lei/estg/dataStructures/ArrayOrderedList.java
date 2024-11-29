package lei.estg.dataStructures;

import lei.estg.dataStructures.interfaces.OrderedListADT;

public class ArrayOrderedList<T extends Comparable<T>> extends ArrayList<T> implements OrderedListADT<T> {
    @Override
    public void add(T element) {
        if (size() == arrayList.length) {
            expandCapacity();
        }

        int index = 0;
        while (index < size && element.compareTo(arrayList[index]) > 0) {
            index++;
        }
        for (int i = size; i > index; i--) {
            arrayList[i] = arrayList[i - 1];
        }
        arrayList[index] = element;
        rear++;
        size++;
    }

    @Override
    public String toString() {
        String str = "[";
        for (int i = 0; i < size; i++) {
            str += arrayList[i] + ", ";
        }
        str += "]";
        return str;
    }
}
