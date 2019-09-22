package SimpleList;

public class List<T> {
    private Nodo first;
    public List()
    {}

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

    public boolean isContained(T element){
        Nodo nodo= this.first;
        boolean found =false;

        while(nodo!=null && !found){
            if(nodo.getElement().equals(element) ){
                found = true;
            }else{
                nodo = nodo.getNext();
            }
        }
        return found;

    }
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

    public void addStart(T element)
    {
        Nodo nodo = new Nodo(element);
        nodo.setNext(nodo);
        this.first = nodo;
    }

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
