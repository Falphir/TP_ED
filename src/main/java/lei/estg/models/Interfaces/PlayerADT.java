package lei.estg.models.Interfaces;

import lei.estg.dataStructures.exceptions.EmptyStackException;
import lei.estg.models.Item;

public interface PlayerADT<T> extends EntidadeADT<T> {

    public void apanharItem(Item item);
    public void usarKit() throws EmptyStackException;


}
