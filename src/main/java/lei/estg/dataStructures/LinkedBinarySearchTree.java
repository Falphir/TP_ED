package lei.estg.dataStructures;

import lei.estg.dataStructures.exceptions.ElementNotFoundException;
import lei.estg.dataStructures.exceptions.EmptyCollectionException;
import lei.estg.dataStructures.interfaces.BinarySearchTreeADT;

public class LinkedBinarySearchTree<T extends Comparable<T>> extends LinkedBinaryTree<T> implements BinarySearchTreeADT<T> {

    public LinkedBinarySearchTree() {
        size = 0;
        root = null;
    }

    public LinkedBinarySearchTree(T element) {
        size = 1;
        root = new BinaryTreeNode<T>(element);
    }

    @Override
    public void addElement(T element) {
        BinaryTreeNode<T> temp = new BinaryTreeNode<T>(element);
        Comparable<T> comparableElement = (Comparable<T>) element;
        if (isEmpty())
            root = temp;
        else {
           BinaryTreeNode<T> current = root;
            boolean added = false;
            while (!added) {
                if (comparableElement.compareTo(current.getElement()) < 0) {
                    if (current.getLeft() == null) {
                        current.setLeft(temp);
                        added = true;
                    } else {
                        current = current.getLeft();
                    }
                } else {
                    if (current.getRight() == null) {
                        current.setRight(temp);
                        added = true;
                    } else {
                        current = current.getRight();
                    }
                }
            }
        }
        size++;
    }

    @Override
    public void removeElement(T targetElement) throws ElementNotFoundException {
        T result = null;
        if (!isEmpty()) {
            if (((Comparable) targetElement).equals(root.getElement())) {
                result = root.getElement();
                root = replacement(root);
                size--;
            } else {
                BinaryTreeNode<T> current, parent = root;
                boolean found = false;
                if (((Comparable) targetElement).compareTo(root.getElement()) < 0)
                    current = root.getLeft();
                else {
                    current = root.getRight();
                }
                while (current != null && !found) {
                    if (targetElement.equals(current.getElement())) {
                        found = true;
                        size--;
                        result = current.getElement();

                        if (current == parent.getLeft()) {
                            parent.setLeft(replacement(current));
                        } else {
                            parent.setRight(replacement(current));
                        }
                    } else {
                        parent = current;
                        if (((Comparable) targetElement).compareTo(current.getElement()) < 0) {
                            current = current.getLeft();
                        } else {
                            current = current.getRight();
                        }
                    }
                } //while
                if (!found) {
                    throw new ElementNotFoundException("binary search tree");
                }
            }
        } // end outer if
    }

    protected BinaryTreeNode<T> replacement(BinaryTreeNode<T> node) {
        BinaryTreeNode<T> result = null;
        if ((node.getLeft() == null) && (node.getRight() == null))
            result = null;
        else if ((node.getLeft() != null) && (node.getRight() == null))
            result = node.getLeft();
        else if ((node.getLeft() == null) && (node.getRight() != null))
            result = node.getRight();
        else {
            BinaryTreeNode<T> current = node.getRight();
            BinaryTreeNode<T> parent = node;
            while (current.getLeft() != null) {
                parent = current;
                current = current.getLeft();
            }
            if (node.getRight() == current)
                current.setLeft(node.getLeft());
            else {
                parent.setLeft(current.getRight());
                current.setRight(node.getRight());
                current.setLeft(node.getLeft());
            }
            result = current;
        }
        return result;
    }

    @Override
    public void removeAllOccurrences(T element) throws EmptyCollectionException, ElementNotFoundException {

        if(isEmpty()) throw new EmptyCollectionException("binary search tree is empty.");

        try {
            while(contains(element)) {
                removeElement(element);
            }
        } catch (ElementNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T removeMin() throws EmptyCollectionException {

        if( isEmpty() ) throw new EmptyCollectionException("binary search tree is empty.");

        T result = null;
        if( root.getLeft() == null ) {
            result = root.getElement();
            root = root.getRight();
        } else {
            BinaryTreeNode<T> parent = root;
            BinaryTreeNode<T> current = root.getLeft();
            while( current.getLeft() != null ) {
                parent = current;
                current = current.getLeft();
            }
            result = current.getElement();
            parent.setLeft(current.getRight());
        }
        size--;
        return result;
    }

    @Override
    public T removeMax() throws EmptyCollectionException {
        if (isEmpty()) throw new EmptyCollectionException("binary search tree is empty.");

        T result = null;
        if (root.getRight() == null) {
            result = root.getElement();
            root = root.getLeft();
        } else {
            BinaryTreeNode<T> parent = root;
            BinaryTreeNode<T> current = root.getRight();
            while (current.getRight() != null) {
                parent = current;
                current = current.getRight();
            }
            result = current.getElement();
            parent.setRight(current.getLeft());
        }
        size--;
        return result;
    }

    @Override
    public T findMin() throws EmptyCollectionException {

        if( isEmpty() ) throw  new EmptyCollectionException("binary search tree is empty.");

        T result = null;
        if (root.getLeft() == null) {
            result = root.getElement();
        } else {
            BinaryTreeNode<T> current = root.getLeft();
            while (current.getLeft() != null) {
                current = current.getLeft();
            }
            result = current.getElement();
        }

        return result;
    }

    @Override
    public T findMax() throws EmptyCollectionException {

        if( isEmpty() ) throw new EmptyCollectionException("binary search tree is empty.");

        T result = null;

        if (root.getRight() == null) {
            result = root.getElement();
        } else {
            BinaryTreeNode<T> current = root.getRight();
            while (current.getRight() != null) {
                current = current.getRight();
            }
            result = current.getElement();
        }
        return result;
    }
}
