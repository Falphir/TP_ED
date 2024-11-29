package lei.estg.dataStructures;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.ListADT;

import java.util.Iterator;

public abstract class DoubleLinkedList<T> implements ListADT<T>, Iterable<T> {

    protected DoubleLinearNode<T> head;
    protected DoubleLinearNode<T> tail;
    protected int size;

    private class DoubleLinkedListIterator<T> implements Iterator<T> {

        private DoubleLinearNode<T> cursor;
        private int modCount;
        private int expectedModCount;

        public DoubleLinkedListIterator() {
            cursor = (DoubleLinearNode<T>) head;
        }

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public T next() {
            T element = cursor.getValue();
            cursor = cursor.getNext();
            return element;
        }

        @Override
        public void remove() throws UnsupportedOperationException {
            if(!hasNext()) {
                throw new UnsupportedOperationException("Cannot remove values from ArrayList");
            }

            cursor.getNext().setPrev(cursor.getPrev());
            cursor.getPrev().setNext(cursor.getNext());

            size--;
        }
    }

    public DoubleLinkedList() {
        head = tail = null;
        size = 0;
    }

    @Override
    public T removeFirst() throws EmptyStackException {
        if(isEmpty()) throw new EmptyStackException("List is empty");

        T element = head.getValue();
        head.getNext().setPrev(null);
        head = head.getNext();
        size--;

        return element;
    }

    @Override
    public T removeLast() throws EmptyStackException {
        if(isEmpty()) throw new EmptyStackException("List is empty");

        T element = tail.getValue();
        tail.getPrev().setNext(null);
        tail = tail.getPrev();
        size--;

        return element;
    }

    @Override
    public T remove(T element) throws EmptyStackException {
        if (isEmpty()) throw new EmptyStackException("List is empty");
        //DoubleLinearNode<T> prv = null;
        DoubleLinearNode<T> current = head;
        while (current != null) {
            if (current.getValue().equals(element)) {
                current.getPrev().setNext(current.getNext());
                current.getNext().setPrev(current.getPrev());
                size--;
                return element;
            } else {
                current = current.getNext();
            }
        }

        return null;
    }


    @Override
    public T last() {
        return tail.getValue();
    }

    @Override
    public T first() {
        return head.getValue();
    }

    @Override
    public boolean contains(T element) {
        DoubleLinearNode<T> current = head;
        while (current != null) {
            if (current.getValue().equals(element)) {
                return true;
            } else {
                current = current.getNext();
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new DoubleLinkedListIterator<T>();
    }
}
