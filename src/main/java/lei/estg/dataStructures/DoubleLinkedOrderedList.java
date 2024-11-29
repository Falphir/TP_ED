package lei.estg.dataStructures;


import lei.estg.dataStructures.interfaces.OrderedListADT;

public class DoubleLinkedOrderedList<T extends Comparable<T>> extends DoubleLinkedList<T> implements OrderedListADT<T> {
    @Override
    public void add(T element) {

        DoubleLinearNode<T> current = head;

        if(current == null){
            DoubleLinearNode<T> newNode = new DoubleLinearNode<>(element);
            head = tail = newNode;
            size++;
            return;
        }

        while (current != null) {
            if (element.compareTo(current.getValue()) > 0) {
                DoubleLinearNode<T> newNode = new DoubleLinearNode<>(element);
                if (current == head) {
                    newNode.setNext(current);
                    current.setPrev(newNode);
                    head = newNode;
                    size++;
                    return;
                }
                if (current == tail) {
                    newNode.setPrev(current);
                    current.setNext(newNode);
                    tail = newNode;
                    size++;
                    return;
                }

                newNode.setNext(current);
                newNode.setPrev(current.getPrev());
                current.getPrev().setNext(newNode);
                current.setPrev(newNode);
                size++;
                return;
            }
            current = current.getNext();
        }
    }

    @Override
    public String toString() {
        String result = "DoubleLinkedOrderedList: [";
        DoubleLinearNode<T> current = head;
        while (current!= null) {
            result += current.getValue() + " <=> ";
            current = current.getNext();
        }
        result += "]";
        return result;
    }
}
