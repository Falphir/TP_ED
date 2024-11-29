package lei.estg.dataStructures;

import lei.estg.dataStructures.exceptions.ElementNotFoundException;
import lei.estg.dataStructures.exceptions.EmptyCollectionException;
import lei.estg.dataStructures.interfaces.BinarySearchTreeADT;

import java.util.Iterator;

public class ArrayBinarySearchTree<T> extends ArrayBinaryTree<T> implements BinarySearchTreeADT<T> {

    protected int height;
    protected int maxIndex;

    public ArrayBinarySearchTree() {
        super();
        height = 0;
        maxIndex = -1;
    }

    public ArrayBinarySearchTree(T element) {
        super(element);
        height = 1;
        maxIndex = 0;
    }

    @Override
    public void addElement(T element) {
        if (tree.length < maxIndex * 2 + 3)
            expandCapacity();
        Comparable<T> tempelement = (Comparable<T>) element;
        if (isEmpty()) {
            tree[0] = element;
            maxIndex = 0;
        } else {
            boolean added = false;
            int currentIndex = 0;
            while (!added) {
                if (tempelement.compareTo((tree[currentIndex])) < 0) { /** go left */
                    if (tree[currentIndex * 2 + 1] == null) {
                        tree[currentIndex * 2 + 1] = element;
                        added = true;
                        if (currentIndex * 2 + 1 > maxIndex)
                            maxIndex = currentIndex * 2 + 1;
                    } else
                        currentIndex = currentIndex * 2 + 1;
                } else { /** go right */
                    if (tree[currentIndex * 2 + 2] == null) {
                        tree[currentIndex * 2 + 2] = element;
                        added = true;
                        if (currentIndex * 2 + 2 > maxIndex)
                            maxIndex = currentIndex * 2 + 2;
                    } else
                        currentIndex = currentIndex * 2 + 2;
                }
            }
        }
        height = (int) (Math.log(maxIndex + 1) / Math.log(2)) + 1;
        size++;
    }

    @Override
    public void removeElement(T element) throws ElementNotFoundException {
        if (isEmpty()) throw new ElementNotFoundException("Array Binary Search Tree is empty");

        boolean found = false;
        int currentIndex = 0;
        Comparable<T> tempelement = (Comparable<T>) element;

        while (!found && tree[currentIndex] != null) {
            if (tempelement.compareTo(tree[currentIndex]) == 0) {
                found = true;
                tree[currentIndex] = null;
            } else if (tempelement.compareTo(tree[currentIndex]) < 0) {
                currentIndex = currentIndex * 2 + 1;
            } else {
                currentIndex = currentIndex * 2 + 2;
            }
        }

        if (!found) throw new ElementNotFoundException("Array Binary Search Tree");

        size--;
    }

    @Override
    public void removeAllOccurrences(T element) throws EmptyCollectionException, ElementNotFoundException {
        if (isEmpty()) throw new EmptyCollectionException("Array Binary Search Tree is empty");

        while (contains(element)) {
            removeElement(element);
        }
    }

    @Override
    public T removeMin() throws EmptyCollectionException {

        if (isEmpty()) throw new EmptyCollectionException("Array Binary Search Tree is empty");

        T result = null;

        int currentIndex = 1;
        int previousIndex = 0;

        while (tree[currentIndex] != null && currentIndex <= maxIndex) {
            previousIndex = currentIndex;
            currentIndex = currentIndex * 2 + 1;
        }
        result = tree[previousIndex];
        replacement (previousIndex);

        return result;
    }

    protected void replacement(int index) {
        if (tree[index * 2 + 1] == null && tree[index * 2 + 2] == null) {
            tree[index] = null;
        } else if (tree[index * 2 + 1] != null && tree[index * 2 + 2] == null) {
            tree[index] = tree[index * 2 + 1];
            tree[index * 2 + 1] = null;
        } else if (tree[index * 2 + 1] == null && tree[index * 2 + 2] != null) {
            tree[index] = tree[index * 2 + 2];
            tree[index * 2 + 2] = null;
        } else {
            int currentIndex = index * 2 + 2;
            int previousIndex = index;
            while (tree[currentIndex * 2 + 1] != null) {
                previousIndex = currentIndex;
                currentIndex = currentIndex * 2 + 1;
            }
            tree[index] = tree[currentIndex];
            replacement(currentIndex);
        }
    }

    @Override
    public T removeMax() throws EmptyCollectionException {

        if (isEmpty()) throw new EmptyCollectionException("Array Binary Search Tree is empty");

        T result = null;
        int currentIndex = 1;
        int previousIndex = 0;

        while (tree[currentIndex] != null && currentIndex <= maxIndex) {
            previousIndex = currentIndex;
            currentIndex = currentIndex * 2 + 2;
        }
        result = tree[previousIndex];
        replacement (previousIndex);

        return result;
    }

    @Override
    public T findMin() throws EmptyCollectionException {

        if (isEmpty()) throw new EmptyCollectionException("Array Binary Search Tree is empty");

        T result = null;

        int currentIndex = 0;

        while (tree[currentIndex * 2 + 1] != null) {
            currentIndex = currentIndex * 2 + 1;
        }
        result = tree[currentIndex];

        return result;
    }

    @Override
    public T findMax() throws EmptyCollectionException {

        if( isEmpty() ) throw new EmptyCollectionException("Array Binary Search Tree is empty.");

        T result = null;

        int currentIndex = 0;

        while (tree[currentIndex * 2 + 2] != null) {
            currentIndex = currentIndex * 2 + 2;
        }
        result = tree[currentIndex];

        return result;
    }
}
