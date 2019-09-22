package SuperList;

import SimpleList.List;
import javafx.scene.layout.AnchorPane;
import Component.Component;
import Tags.*;
import java.awt.*;

/**
 * Nodo para la lista SuperTree
 */
public class NodoComponent {
    private List<Component> next;
    private NodoComponent prev1;
    private NodoComponent prev2;
    private Component component;
    private int clonationTimes;
    private NodoComponent lastClone;

    /**
     * Constructor de la clase NodoComponent
     * @param component
     */
    public NodoComponent(Component component){
        this.component=component;
        this.next= new List<Component>();
        this.prev1=null;
        this.prev2=null;
    }

    /**
     * Retorna la lista de Componentes siguientes
     * @return List de componentes siguientes
     */
    public List<Component> getNext() {
        return next;
    }

    /**
     * Establece la lista de componentes siguientes
     * @param next Lista de componentes siguientes
     */
    public void setNext(List<Component> next) {
        this.next = next;
    }

    /**
     * Retorna el nodo prev1
     * @return NodoComponent prev1
     */
    public NodoComponent getPrev1() {
        return prev1;
    }

    /**
     * Establece el NodoComponent prev1
     * @param prev1 NodoComponent prev1
     */
    public void setPrev1(NodoComponent prev1) {
        this.prev1 = prev1;
    }
    /**
     * Retorna el nodo prev2
     * @return NodoComponent prev2
     */

    public NodoComponent getPrev2(){
        return prev2;
    }

    /**
     * Establece el NodoComponent prev2
     * @param prev2 NodoComponent prev2
     */
    public void setPrev2(NodoComponent prev2) {
        this.prev2 = prev2;
    }

    /**
     * Establece el componente que se almacena
     * @param component Componente almacenado
     */
    public void setComponent(Component component) {
        this.component = component;
    }

    /**
     * Retorna el componente
     * @return Componente
     */
    public Component getComponent() {
        return component;
    }

    /**
     * Clona el NodoComponent
     * @param parent AnchorPane que sera el padre del clon del componente
     * @param diplacement Desplazamiento de los elementos en el AnchorPane
     * @return Clon de el NodoComponent
     */
    public NodoComponent clone(AnchorPane parent, Point diplacement) {
        clonationTimes++;
        NodoComponent clone = new NodoComponent(component.clone(parent, diplacement));
        if(this.prev1!=null){
            this.cloneLeft(clone,  parent, diplacement);
        }
        if(this.prev2!=null){
            this.cloneRigth(clone,  parent, diplacement);
        }
        lastClone=clone;
        return clone;
    }

    /**
     * Clona el lado izquierdo del NodoComponent
     * @param clone El clon de NodoComponent
     * @param parent El AnchorPane que sera el padre de los componentes
     * @param diplacement El desplazamiento de los elementos en el padre
     */
    private void cloneLeft(NodoComponent clone, AnchorPane parent, Point diplacement){
        clone.setPrev1(this.prev1.clone(clonationTimes, parent, diplacement));
        clone.getPrev1().getNext().addEnd(clone.getComponent());
        Tag input= clone.getComponent().getInputTag1();
        Tag output= clone.getPrev1().getComponent().getOutputTag();
        input.getNextTag().add(output);
        output.getNextTag().add(input);
    }

    /**
     * Clona el lado derecho del NodoComponent
     * @param clone El clon de NodoComponent
     * @param parent El AnchorPane que sera el padre de los componentes
     * @param diplacement El desplazamiento de los elementos en el padre
     */

    private void cloneRigth(NodoComponent clone, AnchorPane parent, Point diplacement){
        clone.setPrev2(this.prev2.clone(clonationTimes, parent, diplacement));
        clone.getPrev2().getNext().addEnd(clone.getComponent());
        Tag input= clone.getComponent().getInputTag2();
        Tag output= clone.getPrev2().getComponent().getOutputTag();
        input.getNextTag().add(output);
        output.getNextTag().add(input);
    }

    /**
     * Metodo auxiliar para la clonacion
     * @param currentClonationTimes Cantidad de clonaciones llevadas a cabo del SuperTree
     * @param parent AnchorPane que sera el padre de los elementos clonados
     * @param diplacement Desplazamiento de los elementos en el padre
     * @return Clon del nodo
     */
    private NodoComponent clone(int currentClonationTimes, AnchorPane parent, Point diplacement){
       if(this.getClonationTimes()==currentClonationTimes){
           return this.getLastClone();
       }else{
           return this.clone(parent, diplacement);
       }

    }

    /**
     * Retorna la cantidad de veces que se ha clonado el SuperTree
     * @return
     */
    public int getClonationTimes() {
        return clonationTimes;
    }

    /**
     * Retorna el ultimo clon generado
     * @return Ultimo clon generado
     */
    public NodoComponent getLastClone() {
        return lastClone;
    }
}
