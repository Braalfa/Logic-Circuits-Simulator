package SimpleList;

/**
 * Clase que genera una lista simplemente enlazada
 * @param <T> El tipo de dato almacenado en la lista
 */
public class List<T> {
    /**
     * Nodo inicial de la lista
     */
    private Nodo first;

    /**
     * Constructor de la clase List
     */
    public List()
    {}

    /**
     * Verifica si la lista esta vacia
     * @return Booleano que indica si la lista esta vacia
     */
    public boolean isEmpty()
    {
        boolean empty;
        if(this.first == null){
            empty = true;
        }else{
            empty = false;
        }
        return empty;
    }

    /**
     * Metodo que introduce un nuevo elemento al final de la lista
     * @param element Elemento a introducir
     */
    public void addEnd(T element)
    {
        boolean added= false;
        Nodo nodo = this.first;
        if(this.first != null){
            while(!added)
            {
                if(nodo.getNext()==null){
                    nodo.setNext(new Nodo(element));
                    added=true;
                }else{
                    nodo = nodo.getNext();
                }
            }
        }else{
            first = new Nodo(element);
        }
    }

    /**
     * Metodo que introduce un elemento al inicio de la lista
     * @param element Elemento a introducir
     */
    public void addStart(T element)
    {
        Nodo nodo = new Nodo(element);
        nodo.setNext(nodo);
        this.first = nodo;
    }

    /**
     * Metodo que introduce una lista al final de la lista
     * @param list Lista a introducir
     */
    public void addAll(List<T> list){
        boolean added= false;
        Nodo nodo = this.first;
        if(this.first != null){
            while(!added)
            {
                if(nodo.getNext()==null){
                    nodo.setNext(new Nodo(list.get(0)));
                    added=true;
                    nodo = nodo.getNext();
                }else{
                    nodo = nodo.getNext();
                }
            }
        }else{
            first = new Nodo(list.get(0));
        }
        int limit=list.count();
        for(int i=1;i<limit;i++){
            nodo.setNext(new Nodo(list.get(i)));
            nodo=nodo.getNext();
        }
    }

    /**
     * Metodo que retorna el i-esimo elemento de la lista
     * @param index Posicion en la lista
     * @return Elemento de la lista
     */
    public T get(int index) {
        int counter = 0;
        Nodo<T> nodo = this.first;
        while (nodo != null && counter < index) {
            nodo = nodo.getNext();
            counter++;
        }
        if (nodo != null) {
            return nodo.getElement();
        }else{
            return null;
        }
    }

    /**
     * Metodo que convierte la lista en string
     * @return String
     */
    public String toString()
    {
        String string = "";
        Nodo nodo= this.first ;
        while(nodo!=null){
            string = string + nodo.toString() +"\n";
            nodo= nodo.getNext();
        }
        return string;
    }

    /**
     * Metodo que retorna el tamano de la lista
     * @return Tamano de la lista
     */
    public int count()
    {
        int counter = 0;
        Nodo nodo= this.first ;
        while(nodo!=null){
            counter++;
            nodo = nodo.getNext();
        }
        return counter;
    }

    /**
     * Devuelve la posicion de la primera aparicion del elemento en la lista
     * @param element Elemento a buscar
     * @return Posicion del elemento
     */
    public int getFirst(T element)
    {
        int position = 0;
        Nodo nodo= this.first;
        boolean found =false;
        while(nodo!=null && !found){
            if(nodo.getElement().equals(element) ){
                found = true;
            }else{
                position++;
                nodo = nodo.getNext();
            }
        }

        if(!found){
            position = -1;
        }else{}

        return position;

    }

    /**
     *  Metodo que elimina el elemento de la lista
     * @param element Elemento a eliminar
     * @return Booleano que indica si se elimino el elemento
     */
    public boolean delete(T element)
    {
        Nodo nodoAnterior= this.first;
        Nodo nodoSiguiente;
        int posicion = this.getFirst(element);
        if(posicion>0){
            for(int indice = 0;indice < posicion-1;indice++){
                nodoAnterior= nodoAnterior.getNext();
            }
            nodoSiguiente = nodoAnterior.getNext().getNext();
            nodoAnterior.setNext(nodoSiguiente);
            return true;
        }else if(posicion == 0){
            nodoSiguiente = nodoAnterior.getNext();
            this.first = nodoSiguiente;
            return true;
        }else{
            return false;
        }
    }

}
