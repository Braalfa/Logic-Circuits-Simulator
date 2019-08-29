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
}
