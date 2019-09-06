public class NodoComponent {
    private List<Component> next;
    private NodoComponent prev1;
    private NodoComponent prev2;
    private Component component;

    public NodoComponent(Component component){
        this.component=component;
        this.next= new List<Component>();
        this.prev1=null;
        this.prev2=null;
    }

    public List<Component> getNext() {
        return next;
    }

    public void setNext(List<Component> next) {
        this.next = next;
    }

    public NodoComponent getPrev1() {
        return prev1;
    }

    public void setPrev1(NodoComponent prev1) {
        this.prev1 = prev1;
    }

    public NodoComponent getPrev2(){
        return prev2;
    }

    public void setPrev2(NodoComponent prev2) {
        this.prev2 = prev2;
    }

    public Component getComponent() {
        return component;
    }
}
