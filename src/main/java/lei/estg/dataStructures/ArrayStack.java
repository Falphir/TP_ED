package lei.estg.dataStructures;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.StackADT;

public class ArrayStack<T> implements StackADT<T> {

    private final int DEFAULT_CAPACITY = 5;
    private int top;
    private T[] stack;

    public ArrayStack() {
        top = 0;
        stack = (T[])(new Object[DEFAULT_CAPACITY]);
    }

    public ArrayStack (int initialCapacity) {
        top = 0;
        stack = (T[])(new Object[initialCapacity]);
    }

    @Override
    public void push(T element) {
        if (size() == stack.length) {
            expandCapacity();
        }

        stack[top++] = element;
    }

    @Override
    public T pop() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("Stack is empty");
        }

        return stack[--top];
    }

    @Override
    public T peek() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("Stack is empty");
        }

        return stack[top - 1];
    }

    @Override
    public boolean isEmpty() {
        return top == 0;
    }

    public int size() {
        return top;
    }

    private void expandCapacity() {
        //System.out.println("Expanding capacity...");
        T[] newStack = (T[])(new Object[stack.length * 2]);
        for (int i = 0; i < top; i++) {
            newStack[i] = stack[i];
        }
        stack = newStack;
    }
}