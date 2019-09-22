package SimpleList;

/**
 * Clase que crea los nodos para las listas de tipo List
 * @param <T> Elemento contenido en la lista
 */
public class Nodo<T> {
    private Nodo next;
    private T element;

    /**
     * Constructor del nodo
     * @param element Elemento contenido en el nodo
     */
    public Nodo(T element){
        this.element=element;
        this.next=null; }

    /**
     * Retorna el siguiente nodo
     * @return Siguiente nodo
     */
    public Nodo getNext() {
        return next;
    }

    /**
     * Establece el siguiente nodo
     * @param next Siguiente nodo
     */
    public void setNext(Nodo next) {
        this.next = next;
    }

    /**
     * Retorna elemento en el nodo
     * @return Elemento en el nodo
     */
    public T getElement() {
        return element;
    }

}
