import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.util.ArrayList;

public class SuperTree {
    private List<NodoComponent> outputComponents;
    private static SuperTree instance;
    private double currentCalculation;

    public SuperTree() {
        outputComponents = new List<>();
        currentCalculation=0;
    }

    public static SuperTree getInstance() {
        if (instance == null) {
            instance = new SuperTree();
        } else {
        }
        return instance;
    }

    public List<NodoComponent> getOutputComponents() {
        return outputComponents;
    }

    public void addOutputNodes(List<NodoComponent> nodoComponentList) {
        outputComponents.addAll(nodoComponentList);
    }

    public List<Component> getOutComponents() {
        List<Component> componentList = new List<>();
        for (int i = 0; i < outputComponents.count(); i++) {
            componentList.addEnd(outputComponents.get(i).getComponent());
        }
        return componentList;
    }

    public void add(Component component) {
        outputComponents.addEnd(new NodoComponent(component));
    }

    public void setFreeOutput(NodoComponent nodo) {
        if (!this.isOnOutputList(nodo)) {
            outputComponents.addEnd(nodo);
        }
    }


    public void remove(NodoComponent nodo) {
        this.disconnectFromChildren(nodo);
        this.disconnectFromParents(nodo);
        this.outputComponents.delete(nodo);
    }

    public void remove(Component component) {
        NodoComponent nodo = this.getNode(component);
        this.disconnectFromChildren(nodo);
        this.disconnectFromParents(nodo);
        this.outputComponents.delete(nodo);
    }

    public void disconnect(Component component) {
        NodoComponent nodo = this.getNode(component);
        this.remove(nodo);
        this.setFreeOutput(nodo);
    }

    private void disconnectFromChildren(NodoComponent nodo) {
        if (nodo != null) {
            if (nodo.getPrev1() != null) {
                NodoComponent nodoChild = nodo.getPrev1();
                nodoChild.getNext().delete(nodo.getComponent());
                if (nodoChild.getNext().isEmpty()) {
                    this.setFreeOutput(nodoChild);
                }
            }
            if (nodo.getPrev2() != null) {
                NodoComponent nodoChild = nodo.getPrev2();
                nodoChild.getNext().delete(nodo.getComponent());
                if (nodoChild.getNext().isEmpty()) {
                    this.setFreeOutput(nodoChild);
                }
            }
        }

        nodo.setPrev1(null);
        nodo.setPrev2(null);
    }

    public void connect(Component parent, Component child, int inputNumber) {
        NodoComponent nodoParent = this.getNode(parent);
        NodoComponent nodoChild = this.getNode(child);
        switch (inputNumber) {
            case 1:
                nodoParent.setPrev1(nodoChild);
                break;
            case 2:
                nodoParent.setPrev2(nodoChild);
        }

        this.removeFromOutputList(nodoChild);
        nodoChild.getNext().addEnd(parent);

    }

    public void removeFromOutputList(NodoComponent nodo) {
        outputComponents.delete(nodo);
    }

    public void disconnectFromParents(NodoComponent nodo) {
        List<Component> subList = nodo.getNext();
        int index = 0;
        int limit = subList.count();
        Component component;
        NodoComponent nodoOfParent;
        while (index < limit) {
            component = subList.get(index);
            nodoOfParent = this.getNode(component);
            if (nodoOfParent.getPrev1() == nodo) {
                nodoOfParent.setPrev1(null);
            }
            if (nodoOfParent.getPrev2() == nodo) {
                nodoOfParent.setPrev2(null);
            }
            index++;
        }
        nodo.setNext(new List<>());
    }


    private boolean isOnOutputList(NodoComponent nodo) {
        int index = 0;
        int limit = outputComponents.count();
        NodoComponent nodoAux;
        while (index < limit) {
            nodoAux = outputComponents.get(index);
            if (nodoAux != null) {
                if (nodoAux == nodo) {
                    return true;
                }
            }
            index++;
        }
        return false;
    }

    public NodoComponent getNode(Component component) {
        NodoComponent nodo;
        NodoComponent result = null;
        for (int i = 0; i < outputComponents.count(); i++) {
            nodo = outputComponents.get(i);
            result = this.getNode(component, nodo);
            if (result != null) {
                break;
            }
        }
        return result;

    }

    private NodoComponent getNode(Component component, NodoComponent currentNode) {
        NodoComponent nodo = null;
        if (currentNode.getComponent() == component) {
            nodo = currentNode;
        } else {
            if (currentNode.getPrev1() != null) {
                nodo = this.getNode(component, currentNode.getPrev1());
            }
            if (nodo == null && currentNode.getPrev2() != null) {
                nodo = this.getNode(component, currentNode.getPrev2());
            }
        }
        return nodo;
    }

    public void calculateOutput() {
        NodoComponent nodo;
        currentCalculation++;
        for (int i = 0; i < outputComponents.count(); i++) {
            nodo = outputComponents.get(i);
            this.calculateOutput(nodo);
        }
    }

    private void calculateOutput(NodoComponent currentNode) {
        NodoComponent prev1= currentNode.getPrev1();
        NodoComponent prev2= currentNode.getPrev2();
        Component component;
        if (prev1!= null) {
            component=prev1.getComponent();
            if (component.getLastCalculation()!=currentCalculation) {
                calculateOutput(currentNode.getPrev1());
                component.setLastCalculation(currentCalculation);
                currentNode.getComponent().setInput1(currentNode.getPrev1().getComponent().getOutput());
            }
        }
        if (prev2 != null) {
            component=prev2.getComponent();
            if (component.getLastCalculation()!=currentCalculation) {
                component.setLastCalculation(currentCalculation);
                calculateOutput(currentNode.getPrev2());
                currentNode.getComponent().setInput2(currentNode.getPrev2().getComponent().getOutput());
            }
        }
        currentNode.getComponent().calculate();
    }

    public ArrayList<InputTag> getFreeInputTags() {
        NodoComponent nodo;
        ArrayList<InputTag> tags = new ArrayList<>();
        for (int i = 0; i < outputComponents.count(); i++) {
            nodo = outputComponents.get(i);
            this.getFreeInputTags(nodo, tags);
        }
        return tags;
    }

    private void getFreeInputTags(NodoComponent currentNode, ArrayList<InputTag> tags) {
        if (currentNode.getPrev1() != null) {
            getFreeInputTags(currentNode.getPrev1(), tags);
        } else {
            InputTag inputTag1 = currentNode.getComponent().getInputTag1();
            if (!tags.contains(inputTag1)) {
                tags.add(inputTag1);
            }
        }
        if (currentNode.getPrev2() != null) {
            getFreeInputTags(currentNode.getPrev2(), tags);
        } else {
            InputTag inputTag2 = currentNode.getComponent().getInputTag2();
            if (!tags.contains(inputTag2) && inputTag2 != null) {
                tags.add(inputTag2);
            }
        }
    }

    private void setOutputComponents(List<NodoComponent> outputComponents) {
        this.outputComponents = outputComponents;
    }

    public boolean willFeedback(Component nextComp, Component prevComp){
        NodoComponent first= this.getNode(prevComp);
        NodoComponent match = this.getNode(nextComp);
        return this.willFeedback(match,first);
    }

    private boolean willFeedback(NodoComponent match,NodoComponent currentNode){
        boolean result=false;
        if (currentNode.getPrev1() == match|| currentNode.getPrev2()==match) {
            result= true;
        } else {
            if(currentNode.getPrev1() != null) {
                result= this.willFeedback(match, currentNode.getPrev1());
            }
            if(!result && currentNode.getPrev2() != null) {
                result = this.willFeedback(match, currentNode.getPrev2());
            }
        }
        return result;


    }

    public SuperTree clone(AnchorPane parent, Point diplacement){
        NodoComponent nodo;
        List<NodoComponent>  clone=new List<>();
        for(int i=0;i<outputComponents.count();i++){
            nodo=outputComponents.get(i);
            clone.addEnd(nodo.clone(parent, diplacement));
        }
        SuperTree superTreeClone= new SuperTree();
        superTreeClone.setOutputComponents(clone);
        return superTreeClone;
    }

    public String[][] getTrueTable(){
        ArrayList<InputTag> inputs= this.getFreeInputTags();
        int inputsSize=inputs.size();
        int outputsSize=this.getOutputComponents().count();
        boolean[][] table= new boolean[inputsSize+outputsSize][(int)Math.pow(2,inputsSize)];

        for(int i=0;i<inputs.size();i++){
            int secuence=(int)Math.pow(2,inputsSize-i-1);
            for(int j=0;j<table[0].length;j++){
                if((j/secuence)%2==0){
                    table[i][j]=false;
                }else{
                    table[i][j]=true;
                }
            }
        }
        this.fillTrueTable(table, inputs);

        String[][] finalTable = new String[table[0].length+1][table.length];
        for(int i=0;i<inputsSize;i++){
            finalTable[0][i]=inputs.get(i).getId();
        }
        for(int i=inputsSize;i<finalTable[0].length;i++){
            finalTable[0][i]=outputComponents.get(i-inputsSize).getComponent().getOutputTag().getId();
        }
        for(int i=0;i<finalTable[0].length;i++){
            for(int j=1;j<finalTable.length;j++){
                if(table[i][j-1]){
                    finalTable[j][i]="1";
                }else{
                    finalTable[j][i]="0";
                }
            }
        }

        return finalTable;
    }

    public void fillTrueTable(boolean[][] table, ArrayList<InputTag> inputs){
        int inputsSize=inputs.size();
        int numOutputs= this.outputComponents.count();
        int limit=numOutputs+inputsSize;
        for(int j = 0;j<table[0].length;j++){
            for(int i=0; i<inputsSize;i++){
                InputTag current=inputs.get(i);
                Component component=current.getComponent();
                if(current.getInputNumber()==1){
                    component.setInput1(table[i][j]);
                }else{
                    component.setInput2(table[i][j]);
                }
            }
            this.calculateOutput();
            for(int h=inputsSize;h<limit;h++){
                boolean value= this.outputComponents.get(h-inputsSize).getComponent().getOutput();
                table[h][j]=value;
            }
        }
    }

}
