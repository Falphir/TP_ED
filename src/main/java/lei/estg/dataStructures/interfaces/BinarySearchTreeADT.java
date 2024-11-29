package lei.estg.dataStructures.interfaces;


import lei.estg.dataStructures.exceptions.ElementNotFoundException;
import lei.estg.dataStructures.exceptions.EmptyCollectionException;

public interface BinarySearchTreeADT<T> extends BinaryTreeADT<T> {

    public void addElement(T element);

    public void removeElement(T element) throws ElementNotFoundException;

    public void removeAllOccurrences(T element) throws EmptyCollectionException, ElementNotFoundException;

    public T removeMin() throws EmptyCollectionException;

    public T removeMax() throws EmptyCollectionException;

    public T findMin() throws EmptyCollectionException;

    public T findMax() throws EmptyCollectionException;
}
