package lei.estg.dataStructures;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.QueueADT;

public class LinkedQueue<T> implements QueueADT<T> {

    LinearNode<T> front;
    LinearNode<T> rear;
    int size;

    public LinkedQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    @Override
    public void enqueue(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);

        if (isEmpty()) {
            front = newNode;
        } else {
            rear.setNext(newNode);
        }
        rear = newNode;
        size++;
    }

    @Override
    public T dequeue() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("Queue is empty");
        }

        LinearNode<T> temp = front;
        front = front.getNext();
        size--;

        return temp.getElement();
    }

    @Override
    public T first() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("Queue is empty");
        }

        return front.getElement();
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        String result = "LinkedQueue: [";
        LinearNode<T> current = front;
        while (current!= null) {
            result += current.getElement() + " => ";
            current = current.getNext();
        }
        result += "]";
        return result;
    }
}
