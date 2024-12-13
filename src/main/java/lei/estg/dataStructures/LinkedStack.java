package lei.estg.dataStructures;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.StackADT;

public class LinkedStack<T> implements StackADT<T> {

    private LinearNode<T> top;
    private int size;

    public LinkedStack() {
        top = null;
        size = 0;
    }

    @Override
    public void push(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);
        newNode.setNext(top);
        top = newNode;
        size++;
    }

    @Override
    public T pop() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("Stack is empty");
        }

        LinearNode<T> temp = top;
        top = top.getNext();
        size--;

        return temp.getElement();
    }

    @Override
    public T peek() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("Stack is empty");
        }

        return top.getElement();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        String result = "LinkedStack: [ " + top.getElement();
        LinearNode<T> current = top.getNext();
        while (current!= null) {
            result += " => " + current.getElement();
            current = current.getNext();
        }
        result += " => ]";
        return result;
    }


}
