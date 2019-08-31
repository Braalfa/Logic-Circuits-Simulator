import java.awt.*;

public class Nodo {
    private Nodo next;
    private Nodo prev1;
    private Nodo prev2;
    private Component component;

    public Nodo(Component component){
        this.component=component;
        this.next=null;
        this.prev1=null;
        this.prev2=null;
    }

    public Nodo getNext() {
        return next;
    }

    public void setNext(Nodo next) {
        this.next = next;
    }

    public Nodo getPrev1() {
        return prev1;
    }

    public void setPrev1(Nodo prev1) {
        this.prev1 = prev1;
    }

    public Nodo getPrev2() {
        return prev2;
    }

    public void setPrev2(Nodo prev2) {
        this.prev2 = prev2;
    }

    public Component getComponent() {
        return component;
    }

}
