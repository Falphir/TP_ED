package lei.estg.dataStructures;


import java.util.NoSuchElementException;

import lei.estg.dataStructures.ArrayList;

import lei.estg.dataStructures.interfaces.UnorderedListADT;

public class UnorderedArrayList<T> extends ArrayList<T> implements UnorderedListADT<T> {

    @Override
    public void addToFront(T element) {

        if (rear == arrayList.length) {
            expandCapacity();

        }
        //Se a arrayLista tiver mais que elemento
        if (rear > 0) {
            for (int i = rear; i > 0; i--) {
                arrayList[i] = arrayList[i - 1];
            }
        }

        arrayList[0] = element;
        size++;
        rear++;
    }


    @Override
    public void addToRear(T element) {

        if (rear == arrayList.length) {
            expandCapacity();
        }
        arrayList[rear] = element;
        size++;
        rear++;
    }

    @Override
    public void addAfter(T element, T target) {

        if (rear == arrayList.length) {
            expandCapacity();
        }


        int index = -1;
        //Loop para encontrar o index da array
        for (int i = 0; i < rear; i++) {
            if (((Comparable) arrayList[i]).compareTo(target) == 0) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new NoSuchElementException("Target element not found.");
        }
        //Mover a arrayLista para a direita
        for (int i = rear; i > index; i--) {
            arrayList[i] = arrayList[i - 1];
        }

        arrayList[index + 1] = element;
        size++;
        rear++;

    }

    @Override
    public String toString() {
        if (rear == 0) {
            return "Empty List";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rear; i++) {
            sb.append(arrayList[i]);
            if (i < rear - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}

 


    
