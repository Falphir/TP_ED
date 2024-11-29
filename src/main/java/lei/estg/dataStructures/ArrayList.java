package lei.estg.dataStructures;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.exceptions.ValueNotFoundException;
import lei.estg.dataStructures.interfaces.ListADT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public abstract class ArrayList<T> implements ListADT<T>, Iterable<T> {

    private static final int DEFAULT_CAPACITY = 50;

    protected int front;
    protected int rear;
    protected T[] arrayList;
    protected int size;
    protected int modCount;

    public ArrayList() {
        arrayList = (T[])(new Comparable[DEFAULT_CAPACITY]);
        front = rear = 0;
        size = 0;
    }

    private class ArrayListIterator<T> implements Iterator<T> {

        private int cursor;
        private int expectedModCount;
        private int modCount;

        public ArrayListIterator(int modCount) {
            cursor = front;
            this.expectedModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public T next() {
            T element = (T) arrayList[cursor];

            if (!hasNext()) {
                throw new ValueNotFoundException("No more values in ArrayList");
            }
            cursor++;
            return element;
        }

        @Override
        public void remove() throws UnsupportedOperationException{
            if (!hasNext()) {
                throw new UnsupportedOperationException("Cannot remove values from ArrayList");
            }
            if (modCount!= expectedModCount) {
                throw new ConcurrentModificationException("ArrayList has been modified");
            }

            arrayList[cursor - 1] = null;
            size--;

            modCount++;

        }
    }

    @Override
    public T removeFirst() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("ArrayList is empty");
        }

        T element = arrayList[front];
        arrayList[front] = null;

        for(int i = 0; i < size; i++) {
            arrayList[i] = arrayList[i + 1];
        }

        size--;
        rear--;
        modCount++;

        return element;
    }

    @Override
    public T removeLast() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("ArrayList is empty");
        }

        T element = arrayList[rear];
        arrayList[rear] = null;

        size--;
        rear--;
        modCount++;

        return element;
    }

    @Override
    public T remove(T element) throws EmptyStackException {

        if(isEmpty()) {
            throw new EmptyStackException("ArrayList is empty");
        }

        int i = find(element);

        arrayList[i] = null;
        for (int j = i; j < rear; j++) {
            arrayList[j] = arrayList[j + 1];
        }
        size--;
        rear--;
        modCount++;
        return element;
    }

    private int find(T element) {
        boolean found = false;
        int i = front;
        while (!found && i <= size()) {
            if (arrayList[i].equals(element)) {
                found = true;
            } else {
                i++;
            }
        }
        if (!found) {
            throw new ValueNotFoundException("Value not found in ArrayList");
        }

        return i;
    }

    @Override
    public T last() {
        return arrayList[rear];
    }

    @Override
    public T first() {
        return arrayList[front];
    }

    @Override
    public boolean contains(T element) {

        for (int i = front; i <= rear; i++) {
            if (arrayList[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    protected void expandCapacity() {
       T[] newArrayList = (T[])(new Object[arrayList.length * 2]);
        for (int i = 0; i < size; i++) {
            newArrayList[i] = arrayList[i];
        }
        arrayList = newArrayList;
    }

     public String toString()
   {
      String result = "";

      for (int scan=0; scan < rear; scan++)
         result = result + arrayList[scan].toString() + " ";

      return result;
   }

    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator<T>(modCount);
    }
}
