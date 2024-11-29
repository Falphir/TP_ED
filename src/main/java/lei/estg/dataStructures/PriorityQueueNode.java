package lei.estg.dataStructures;

public class PriorityQueueNode<T> implements Comparable<PriorityQueueNode> {

    private static int nextorder = 0;
    private int priority;
    private int order;
    private T element;

    public PriorityQueueNode (T obj, int prio) {
        element = obj;
        priority = prio;
        order = nextorder;
        nextorder++;
    }

    public T getElement() {
        return element;
    }

    public int getPriority() {
        return priority;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        String temp = (element.toString() + priority + order);
        return temp;
    }

    @Override
    public int compareTo(PriorityQueueNode obj) {
        int result;
        PriorityQueueNode<T> temp = obj;
        if (priority > temp.getPriority())
            result = 1;
        else if (priority < temp.getPriority())
            result = -1;
        else if (order > temp.getOrder())
            result = 1;
        else
            result = -1;
        return result;

    }
}
