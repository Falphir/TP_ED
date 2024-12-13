package lei.estg.dataStructures.interfaces;



import lei.estg.dataStructures.exceptions.ElementNotFoundException;
import lei.estg.dataStructures.exceptions.EmptyCollectionException;
import lei.estg.dataStructures.exceptions.EmptyStackException;

import java.util.Iterator;

public interface BinaryTreeADT<T> {

    public T getRoot() throws EmptyCollectionException;

    public boolean isEmpty();

    public int size();

    public boolean contains(T element) throws ElementNotFoundException;

    public T find(T element) throws ElementNotFoundException;

    public String toString();

    public Iterator<T> iteratorInOrder();

    public Iterator<T> iteratorPreOrder();

    public Iterator<T> iteratorPostOrder();

    public Iterator<T> iteratorLevelOrder() throws EmptyCollectionException, EmptyStackException;
}
