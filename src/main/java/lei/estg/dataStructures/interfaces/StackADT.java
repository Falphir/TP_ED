package lei.estg.dataStructures.interfaces;


import lei.estg.dataStructures.exceptions.EmptyStackException;

public interface StackADT<T> {

    public void push(T element);

    public T pop() throws EmptyStackException;

    public T peek() throws EmptyStackException;

    public boolean isEmpty();

    @Override
    public String toString();
}