package lei.estg.dataStructures;

import lei.estg.dataStructures.exceptions.ElementNotFoundException;
import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.dataStructures.exceptions.ModificationException;
import lei.estg.dataStructures.exceptions.ValueNotFoundException;
import lei.estg.dataStructures.interfaces.ListADT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class ArrayList<T> implements ListADT<T>, Iterable<T> {

    private static final int DEFAULT_CAPACITY = 50;

    protected int front;
    protected int rear;
    protected T[] arrayList;
    protected int size;
    protected int modCount;

    public ArrayList() {
        arrayList = (T[])(new Object[DEFAULT_CAPACITY]);
        front = rear = 0;
        size = 0;
    }

    private class ArrayListIterator implements Iterator<T> {
        private int current;
        private int expectedModCount;
        private boolean okToRemove;

        public ArrayListIterator() {
            current = 0;
            expectedModCount = modCount;
            okToRemove = false;
        }

        @Override
        public boolean hasNext() {
            return current < rear;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (expectedModCount != modCount) {
                try {
                    throw new ModificationException("A lista foi modificada.");
                } catch (ModificationException e) {
                    throw new RuntimeException(e);
                }
            }

            okToRemove = true;
            return arrayList[current++];
        }

        @Override
        public void remove() {
            if (expectedModCount != modCount) {
                try {
                    throw new ModificationException("A lista foi modificada.");
                } catch (ModificationException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!okToRemove) {
                throw new IllegalStateException("Chamada inválida a remove().");
            }

            try {
                ArrayList.this.remove(arrayList[current - 1]);

                rear--;

                expectedModCount = modCount;

                okToRemove = false;

            } catch (EmptyStackException e) {
                System.out.println("Elemento não encontrado: " + e.getMessage());
            }
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
        return new ArrayListIterator();
    }
}
