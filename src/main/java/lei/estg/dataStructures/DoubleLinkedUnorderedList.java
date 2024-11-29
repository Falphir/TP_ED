package lei.estg.dataStructures;


import lei.estg.dataStructures.interfaces.UnorderedListADT;

public class DoubleLinkedUnorderedList<T extends Comparable<T>> extends DoubleLinkedList<T> implements UnorderedListADT<T> {

    @Override
    public void addToFront(T element) {
        
        DoubleLinearNode<T> newNode = new DoubleLinearNode<T>(element);


        if(isEmpty()){
            head = newNode;
            tail = newNode;
            head.setNext(null);
            head.setPrev(null);
           
        }else{
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
           
        }

      size++;
       
    }

    @Override
    public void addToRear(T element) {
        DoubleLinearNode<T> newNode = new DoubleLinearNode<T>(element);

        if(isEmpty()){
            head = newNode;
            tail = newNode;
            tail.setNext(null);
            tail.setPrev(null);
            size++;
        }else{
            tail.setNext(newNode);
        newNode.setPrev(tail);
        newNode.setNext(null);
        tail = newNode;
        }

      size++;

    }

    @Override
    public void addAfter(T element, T target) {

        
    
        DoubleLinearNode<T> newnode = new DoubleLinearNode<T>(element);
        DoubleLinearNode<T> current = head;


            while (current != null && current.getValue().compareTo(target) != 0) {
                current = current.getNext();
            }

            if(current == null){
                throw new RuntimeException("O target nao foi encontrado");
            }

                DoubleLinearNode<T> nextNode = current.getNext();

                current.setNext(newnode);       
                newnode.setPrev(current);
                newnode.setNext(nextNode);

                if(nextNode != null){
                    nextNode.setPrev(newnode);
                  
                }else{
                    tail = newnode;
                  
                   
                }
                size++;
            }


            public T[] toArray(){
                @SuppressWarnings("unchecked")
                T[] array = (T[]) new Object[size];
                DoubleLinearNode<T> current = head;
                int index = 0;

                while (current != null) {
                    array[index++] = current.getValue();
                    current = current.getNext();
                }
        
                return array;
            }
        

        }  

