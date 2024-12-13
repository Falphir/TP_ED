package lei.estg.dataStructures;

import lei.estg.dataStructures.exceptions.ElementNotFoundException;
import lei.estg.dataStructures.exceptions.EmptyCollectionException;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.QueueADT;
import lei.estg.dataStructures.interfaces.BinaryTreeADT;

import java.util.Iterator;

public class LinkedBinaryTree<T> implements BinaryTreeADT<T> {

    protected int size;
    protected BinaryTreeNode<T> root;

    public LinkedBinaryTree() {
        size = 0;
        root = null;
    }

    public LinkedBinaryTree(T element) {
        size = 1;
        root = new BinaryTreeNode<T>(element);
    }

    @Override
    public T getRoot() throws EmptyCollectionException {
        if (root == null)
            throw new EmptyCollectionException("binary tree is empty");
        return root.getElement();
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
    public boolean contains(T element) throws ElementNotFoundException {
        return find(element) != null;
    }

    @Override
    public T find(T element) throws ElementNotFoundException {

        BinaryTreeNode<T> current = findAgain(element, root);

        if (current == null)
            throw new ElementNotFoundException("binary tree");
        return (current.element);
    }

    private BinaryTreeNode<T> findAgain(T element, BinaryTreeNode<T> next) {
        if (next == null)
            return null;
        if (next.element.equals(element))
            return next;

        BinaryTreeNode<T> temp = findAgain(element, next.left);

        if (temp == null)
            temp = findAgain(element, next.right);

        return temp;
    }

    @Override
    public String toString()
    {
        UnorderedArrayList<T> templist = new UnorderedArrayList<>();
        preorder (root, templist);
        return templist.toString();
    }

    @Override
    public Iterator<T> iteratorInOrder() {
        UnorderedArrayList<T> tempList = new UnorderedArrayList<T>();
        inorder(root, tempList);

        return tempList.iterator();
    }

    protected void inorder(BinaryTreeNode<T> node, UnorderedArrayList<T> list) {
        if (node != null) {
            inorder(node.left, list);
            list.addToRear(node.element);
            inorder(node.right, list);
        }
    }

    @Override
    public Iterator<T> iteratorPreOrder() {
        UnorderedArrayList<T> tempList = new UnorderedArrayList<T>();
        preorder(root, tempList);

        return tempList.iterator();
    }

    protected void preorder(BinaryTreeNode<T> node, UnorderedArrayList<T> list) {
        if (node != null) {
            list.addToRear(node.element);
            preorder(node.left, list);
            preorder(node.right, list);
        }
    }


    @Override
    public Iterator<T> iteratorPostOrder() {
        UnorderedArrayList<T> tempList = new UnorderedArrayList<T>();
        preorder(root, tempList);
        return tempList.iterator();
    }

    protected void postorder(BinaryTreeNode<T> node, UnorderedArrayList<T> list) {
        if (node != null) {
            postorder(node.left, list);
            postorder(node.right, list);
            list.addToRear(node.element);
        }
    }

    @Override
    public Iterator<T> iteratorLevelOrder() throws EmptyCollectionException, EmptyStackException {
        if (root == null) {
            throw new EmptyCollectionException("binary tree");
        }

        UnorderedArrayList<T> tempList = new UnorderedArrayList<>();
        QueueADT<BinaryTreeNode<T>> queue = new LinkedQueue<>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            BinaryTreeNode<T> current = queue.dequeue();
            tempList.addToRear(current.getElement());

            if (current.left != null) {
                queue.enqueue(current.left);
            }
            if (current.right != null) {
                queue.enqueue(current.right);
            }
        }

        return tempList.iterator();
    }



}
