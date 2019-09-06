import java.awt.*;

public class Nodo<T> {
    private Nodo next;
    private T element;

    public Nodo(T element){
        this.element=element;
        this.next=null; }

    public Nodo getNext() {
        return next;
    }

    public void setNext(Nodo next) {
        this.next = next;
    }

    public T getElement() {
        return element;
    }

}
