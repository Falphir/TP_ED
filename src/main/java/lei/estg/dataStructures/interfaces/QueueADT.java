package lei.estg.dataStructures.interfaces;

import lei.estg.dataStructures.exceptions.EmptyStackException;

public interface QueueADT<T> {

    public void enqueue(T element);

    public T dequeue() throws EmptyStackException;

    public T first() throws EmptyStackException;

    public boolean isEmpty();

    public int size();

    @Override
    public String toString();
}
