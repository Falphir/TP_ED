package lei.estg.dataStructures;

public class DoubleLinearNode<T> {
    private T value;
    private DoubleLinearNode<T> next;
    private DoubleLinearNode<T> prev;

    public DoubleLinearNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public DoubleLinearNode<T> getNext() {
        return next;
    }

    public void setNext(DoubleLinearNode<T> next) {
        this.next = next;
    }

    public DoubleLinearNode<T> getPrev() {
        return prev;
    }

    public void setPrev(DoubleLinearNode<T> prev) {
        this.prev = prev;
    }
}
