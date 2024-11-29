package lei.estg.dataStructures.interfaces;

import lei.estg.dataStructures.exceptions.EmptyStackException;

import java.util.Iterator;

public interface ListADT<T> extends Iterable<T> {

    public T removeFirst() throws EmptyStackException;

    public T removeLast() throws EmptyStackException;

    public T remove(T element) throws EmptyStackException;

    public T last();

    public T first();

    public boolean contains(T element);

    public boolean isEmpty();

    public int size();

    @Override
    public Iterator<T> iterator();

    @Override
    public String toString();
}
