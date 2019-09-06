import javafx.scene.Node;

public class ListComponent {
    private List<NodoComponent> outputComponents;
    public ListComponent(){}



    public void setFreeOutput(NodoComponent nodo){
        if(!this.isOnOutputList(nodo)){
            outputComponents.addEnd(nodo);
        }
    }


    public void remove(NodoComponent nodo){
        this.disconnectFromChildren(nodo);
        this.disconnectFromParents(nodo);
        this.outputComponents.delete(nodo);
    }

    public void disconnect(Component component){
        NodoComponent nodo= this.getNode(component);
        this.remove(nodo);
        this.setFreeOutput(nodo);
    }

    public void disconnectFromChildren(NodoComponent nodo){
        if(nodo!=null) {
            if(nodo.getPrev1()!=null){
                NodoComponent nodoChild=nodo.getPrev1();
                nodoChild.getNext().delete(nodo.getComponent());
                if(nodoChild.getNext().isEmpty()){
                    this.setFreeOutput(nodoChild);
                }
            }
            if(nodo.getPrev2()!=null){
                NodoComponent nodoChild=nodo.getPrev2();
                nodoChild.getNext().delete(nodo.getComponent());
                if(nodoChild.getNext().isEmpty()){
                    this.setFreeOutput(nodoChild);
                }
            }
        }

        nodo.setPrev1(null);
        nodo.setPrev2(null);
    }
    public void connect(Component parent, Component child, int inputNumber){
        NodoComponent nodoParent= this.getNode(parent);
        NodoComponent nodoChild= this.getNode(child);
        switch (inputNumber){
            case 1:
                nodoParent.setPrev1(nodoChild);
                break;
            case 2:
                nodoParent.setPrev2(nodoChild);
        }

        this.removeFromOutputList(nodoChild);
        nodoChild.getNext().addEnd(parent);

    }

    public void removeFromOutputList(NodoComponent nodo){
        outputComponents.delete(nodo);
    }

    public void disconnectFromParents(NodoComponent nodo){
        List<Component> subList= nodo.getNext();
        int index=0;
        int limit=subList.count();
        Component component;
        NodoComponent nodoOfParent;
        while(index<limit){
            component=subList.get(index);
            nodoOfParent=this.getNode(component);
            if(nodoOfParent.getPrev1()==nodo){
                nodoOfParent.setPrev1(null);
            }
            if(nodoOfParent.getPrev2()==nodo){
                nodoOfParent.setPrev2(null);
            }
            index++;
        }
        nodo.setNext(new List<>());
    }


    private boolean isOnOutputList(NodoComponent nodo){
        int index=0;
        int limit=outputComponents.count();
        NodoComponent nodoAux;
        while(index<limit){
            nodoAux=outputComponents.get(index);
            if(nodoAux!=null){
                if(nodoAux==nodo){
                    return true;
                }
            }
            index++;
        }
        return false;
    }

    private NodoComponent getNode(Component component){
        NodoComponent nodo;
        NodoComponent result=null;
        for(int i=0;i<outputComponents.count();i++){
            nodo=outputComponents.get(i);
            result= this.getNode(component, nodo);
            if(result!=null){
                break;
            }
        }
        return result;

    }

    private NodoComponent getNode(Component component, NodoComponent currentNode){
        NodoComponent nodo=null;
        if(currentNode.getComponent()==component){
            nodo= currentNode;
        }else{
            if(currentNode.getPrev1()!=null) {
                nodo = this.getNode(component, currentNode.getPrev1());
            }
            if( nodo==null && currentNode.getPrev2()!=null){
                nodo = this.getNode(component, currentNode.getPrev2());
            }
        }
        return nodo;
    }

    private void calculateOutput(){
        NodoComponent nodo;
        for(int i=0;i<outputComponents.count();i++){
            nodo=outputComponents.get(i);
            this.calculateOutput(nodo);
        }

    }

    private void calculateOutput(NodoComponent currentNode){
        if(currentNode.getPrev1()!=null){
            calculateOutput(currentNode.getPrev1());
            currentNode.getComponent().setInput1(currentNode.getPrev1().getComponent().getOutput());
        }
        if(currentNode.getPrev2()!=null){
            calculateOutput(currentNode.getPrev2());
            currentNode.getComponent().setInput2(currentNode.getPrev2().getComponent().getOutput());
        }
        currentNode.getComponent().calculate();
    }
}
