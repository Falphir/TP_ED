package lei.estg.dataStructures.interfaces;


import lei.estg.dataStructures.exceptions.EmptyCollectionException;

public interface HeapADT<T> extends BinaryTreeADT<T> {

    public void addElement(T element);

    public T removeMin() throws EmptyCollectionException;

    public T findMin() throws EmptyCollectionException;
}
