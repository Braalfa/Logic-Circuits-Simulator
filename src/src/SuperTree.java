import java.util.ArrayList;

public class SuperTree {
    private List<NodoComponent> outputComponents;
    private static SuperTree instance;
    public SuperTree(){
        outputComponents=new List<>();
    }

    public static SuperTree getInstance(){
        if(instance==null){
            instance= new SuperTree();
        }else{
        }
        return instance;
    }

    public List<Component> getOutComponents(){
        List<Component> componentList= new List<>();
        for(int i = 0; i<outputComponents.count();i++){
            componentList.addEnd(outputComponents.get(i).getComponent());
        }
        return componentList;
    }

    public void add(Component component) {
        outputComponents.addEnd(new NodoComponent(component));
    }

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

    public void remove(Component component){
        NodoComponent nodo= this.getNode(component);
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

    public NodoComponent getNode(Component component){
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

    public void calculateOutput(){
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

    public ArrayList<InputTag> getFreeInputTags(){
        NodoComponent nodo;
        ArrayList<InputTag> tags= new ArrayList<>();
        for(int i=0;i<outputComponents.count();i++){
            nodo=outputComponents.get(i);
            this.getFreeInputTags(nodo, tags);
        }
        return tags;
    }

    private  void getFreeInputTags(NodoComponent currentNode, ArrayList<InputTag> tags){
        if(currentNode.getPrev1()!=null){
            getFreeInputTags(currentNode.getPrev1(), tags);
        }else{
            InputTag inputTag1= currentNode.getComponent().getInputTag1();
            if(!tags.contains(inputTag1)){
                tags.add(inputTag1);
            }
        }
        if(currentNode.getPrev2()!=null){
            getFreeInputTags(currentNode.getPrev2(), tags);
        }else{
            InputTag inputTag2= currentNode.getComponent().getInputTag2();
            if(!tags.contains(inputTag2) && inputTag2!=null){
                tags.add(inputTag2);
            }
        }
    }
}
