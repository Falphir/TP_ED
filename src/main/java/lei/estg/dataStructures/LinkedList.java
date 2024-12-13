package lei.estg.dataStructures;

import lei.estg.dataStructures.exceptions.CollectionNotFoundException;
import lei.estg.dataStructures.exceptions.ValueNotFoundException;

public class LinkedList<T> {

    private LinearNode<T> head;
    private LinearNode<T> tail;
    private int length;

    public LinkedList() {
        head = tail = null;
        this.length = 0;
    }

    public LinkedList(T value) {
        LinearNode<T> newLinearNode = new LinearNode<T>(value);
        this.head = newLinearNode;
        this.tail = newLinearNode;
        this.length = 1;
    }

    public void add(T value) {
        LinearNode<T> newLinearNode = new LinearNode<T>(value);

        if (head != null) {
            tail.setNext(newLinearNode);
            tail = newLinearNode;
        } else {
            head = tail = newLinearNode;
        }
        length++;
    }

    public void remove(T value) throws CollectionNotFoundException, ValueNotFoundException {

        if (head == null) {
            throw new CollectionNotFoundException("List Not Found / Not Initialized");
        }

        boolean found = false;
        LinearNode<T> prev = null;
        LinearNode<T> current = head;

        while (current != null && !found) {
            if (value.equals(current.getElement())) {
                found = true;
            } else {
                prev = current;
                current = current.getNext();
            }
        }

        if (!found) {
            throw new ValueNotFoundException("Value Not Found");
        }

        if(prev == null) {
            head = current.getNext();
        } else {
            prev.setNext(current.getNext());
        }

        length--;
    }



}
