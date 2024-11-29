package lei.estg.dataStructures;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.interfaces.QueueADT;

public class CircularArrayQueue<T> implements QueueADT<T> {

    private static int DEFAULT_CAPACITY = 5;

    private int front;
    private int rear;
    private T[] queue;
    private int size;

    public CircularArrayQueue() {
        front = rear = 0;
        queue = (T[])(new Object[DEFAULT_CAPACITY]);
    }

    @Override
    public void enqueue(T element) {
        if ((rear + 1) % queue.length == front) {
            expandCapacity();
        }

        queue[rear] = element;
        rear = (rear + 1) % queue.length;
        size++;
    }

    @Override
    public T dequeue() throws EmptyStackException {
        if(isEmpty()) {
            throw new EmptyStackException("Queue is empty");
        }

        T element = queue[front];

        queue[front] = null;
        front = (front + 1) % queue.length;

        size--;
        return element;
    }

    @Override
    public T first() throws EmptyStackException{
        if(isEmpty()) {
            throw new EmptyStackException("Queue is empty");
        }

        return queue[front];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    private void expandCapacity() {

        T[] newQueue = (T[])(new Object[queue.length * 2]);
        int j = 0;

        for (int i = front; i != rear; i = (i + 1) % queue.length) {
            newQueue[j] = queue[i];
            j++;
        }

        front = 0;
        rear = j;
        queue = newQueue;
    }

    @Override
    public String toString() {

        String result = "CircularArrayQueue: [";

        for (int i = front; i < rear; i = (i + 1) % queue.length) {
            result += queue[i] + ", ";
        }
        result += "]";

        return result;
    }
}
