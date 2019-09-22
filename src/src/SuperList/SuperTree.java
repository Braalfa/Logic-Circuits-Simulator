package SuperList;

import SimpleList.List;
import javafx.scene.layout.AnchorPane;
import Component.Component;
import java.awt.*;
import java.util.ArrayList;
import Tags.*;

/**
 * Clase que implementa una variacion de la lista enlazada para representar las conexiones de un circuito logico.
 * Se hace uso del patron de diseno Singleton.
 */
public class SuperTree {
    /**
     * Lista de NodoComponentes sin output conectado
     */
    private List<NodoComponent> outputComponents;
    /**
     * Instancia del SuperTree
     */
    private static SuperTree instance;
    /**
     * Numero de ultimo calculo de salida
     */
    private double currentCalculation;

    /**
     * Constructot de la clase
     */
    private SuperTree() {
        outputComponents = new List<>();
        currentCalculation=0;
    }

    /**
     * Retorna la instancia de la clase
     * @return Instancia de la clase
     */
    public static SuperTree getInstance() {
        if (instance == null) {
            instance = new SuperTree();
        } else {
        }
        return instance;
    }

    /**
     * Retorna la lista de NodoComponent que no tienen salida
     * @return outputComponents
     */
    public List<NodoComponent> getOutputComponents() {
        return outputComponents;
    }

    /**
     * Agrega una lista de NodoComponent a la lista outputComponent
     * @param nodoComponentList Lista de NodoComponent a agregar
     */
    public void addOutputNodes(List<NodoComponent> nodoComponentList) {
        outputComponents.addAll(nodoComponentList);
    }

    /**
     * Retorna una lista de los componentes que no tienen salida
     * @return Lista de componentes sin salida
     */
    public List<Component> getOutComponents() {
        List<Component> componentList = new List<>();
        for (int i = 0; i < outputComponents.count(); i++) {
            componentList.addEnd(outputComponents.get(i).getComponent());
        }
        return componentList;
    }

    /**
     * Agrega el componente al final de la lista de NodoComponentes sin salida
     * @param component Componente a agregar
     */
    public void add(Component component) {
        outputComponents.addEnd(new NodoComponent(component));
    }

    /**
     * Agrega un NodoComponent al final de la lista de outputComponents
     * @param nodo NodoComponent a agregar
     */
    private void setFreeOutput(NodoComponent nodo) {
        if (!this.isOnOutputList(nodo)) {
            outputComponents.addEnd(nodo);
        }
    }

    /**
     * Remueve un NodoComponent del SuperTree
     * @param nodo  NodoComponent a remover
     */
    private void remove(NodoComponent nodo) {
        this.disconnectFromChildren(nodo);
        this.disconnectFromParents(nodo);
        this.outputComponents.delete(nodo);
    }

    /**
     * Remueve un componente del SuperTree
     * @param component Componente a eliminar
     */
    public void remove(Component component) {
        NodoComponent nodo = this.getNode(component);
        this.disconnectFromChildren(nodo);
        this.disconnectFromParents(nodo);
        this.outputComponents.delete(nodo);
    }

    /**
     * Desconecta un componente del SuperTree y lo agrega como componente libre
     * @param component Componente a desconectar
     */
    public void disconnect(Component component) {
        NodoComponent nodo = this.getNode(component);
        this.remove(nodo);
        this.setFreeOutput(nodo);
    }

    /**
     * Desconecta un NodoComponent de sus hijos
     * @param nodo NodoComponent a desconectar
     */
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

    /**
     * Conecta dos componentes
     * @param parent Componente padre
     * @param child Componente hijo
     * @param inputNumber Numero de entrada que se usara
     */
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

    /**
     * Remueveun NodoComponent de la outputList
     * @param nodo NodoComponent a eliminar
     */
    public void removeFromOutputList(NodoComponent nodo) {
        outputComponents.delete(nodo);
    }

    /**
     * Desconecta un NodoComponent de sus padres
     * @param nodo NodoComponent a desconectar
     */
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

    /**
     * Retorna un booleano que indica si el NodoComponent esta en la outputList
     * @para  nodo NodoComponent a buscar en la lista
     * @return Booleano que indica si se encuentra en la lista
     */
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

    /**
     * Retorna el NodoComponent en donde se encuentra el componente
     * @param component Componente a buscar
     * @return NodoComponent buscado
     */
    private NodoComponent getNode(Component component) {
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

    /**
     * Metodo auxiliar de getNode(Component component)
     * @param component Componente a buscar
     * @param currentNode Nodo en el cual se esta buscando
     * @return Nodo en el cual se encuentra el componente
     */
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

    /**
     * Metodo que calcula el output en cada uno de los componentes de la SuperList
     */
    public void calculateOutput() {
        NodoComponent nodo;
        currentCalculation++;
        for (int i = 0; i < outputComponents.count(); i++) {
            nodo = outputComponents.get(i);
            this.calculateOutput(nodo);
        }
    }

    /**
     * Metodo auxiliar de calculateOutput()
     * @param currentNode Nodo en el cual se encuentra calculando outputs
     */
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

    /**
     * Retorna los InputTags libres
     * @return ArrayList de InputTags libres
     */
    public ArrayList<InputTag> getFreeInputTags() {
        NodoComponent nodo;
        ArrayList<InputTag> tags = new ArrayList<>();
        for (int i = 0; i < outputComponents.count(); i++) {
            nodo = outputComponents.get(i);
            this.getFreeInputTags(nodo, tags);
        }
        return tags;
    }

    /**
     * Metodo auciliar de getFreeInputTags()
     * @param currentNode Nodo en el cual se encuentra buscando actualmente
     * @param tags Arraylist de tags encontrados
     */
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

    /**
     * Metodo para establecer la lista outputComponents
     * @param outputComponents Nueva lista de outputComponents
     */
    private void setOutputComponents(List<NodoComponent> outputComponents) {
        this.outputComponents = outputComponents;
    }

    /**
     * Metodo que indica si abra retroalimaentacion al conectar prevComp con nextComp
     * @param nextComp Componente a conectar adelante
     * @param prevComp Componente a conectar atras
     * @return Booleano que indica posible retroalimentacion
     */
    public boolean willFeedback(Component nextComp, Component prevComp){
        NodoComponent first= this.getNode(prevComp);
        NodoComponent match = this.getNode(nextComp);
        return this.willFeedback(match,first);
    }

    /**
     * Metodo auxiliar de willFeedback
     * @param match Nodo a analizar retroalimentacion
     * @param currentNode Nodo con el que se esta analizando
     * @return Booleano que indica posible retroalimentacion
     */
    private boolean willFeedback(NodoComponent match, NodoComponent currentNode){
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

    /**
     * Metodo para generar un clon de toda la lista
     * @param parent AnchorPane donde se quiere tener el clone
     * @param diplacement Desplazamiento con respecto al cero para los elementos clones
     * @return Clone de SuperTree
     */
    public SuperTree clone(AnchorPane parent, Point diplacement){
        NodoComponent nodo;
        List<NodoComponent> clone=new List<>();
        for(int i=0;i<outputComponents.count();i++){
            nodo=outputComponents.get(i);
            clone.addEnd(nodo.clone(parent, diplacement));
        }
        SuperTree superTreeClone= new SuperTree();
        superTreeClone.setOutputComponents(clone);
        return superTreeClone;
    }

    /**
     * Calcula y devuelve la tabla de verdad del circuito
     * @return Array bidimensional de la tabla de verdad del circuito
     */
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

    /**
     * Calcula los outputs en cada uno de los casos de combinaciones de inputs presentes en la tabla
     * @param table Array bidimensional de los booleanos de entrada
     * @param inputs ArrayList de los InputTags
     */
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
