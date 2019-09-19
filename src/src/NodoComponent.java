import javafx.scene.layout.AnchorPane;

import java.awt.*;

public class NodoComponent {
    private List<Component> next;
    private NodoComponent prev1;
    private NodoComponent prev2;
    private Component component;
    private int clonationTimes;
    private NodoComponent lastClone;

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

    public void setComponent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

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

    private void cloneLeft(NodoComponent clone, AnchorPane parent, Point diplacement){
        clone.setPrev1(this.prev1.clone(clonationTimes, parent, diplacement));
        clone.getPrev1().getNext().addEnd(clone.getComponent());
        Tag input= clone.getComponent().getInputTag1();
        Tag output= clone.getPrev1().getComponent().getOutputTag();
        input.getNextTag().add(output);
        output.getNextTag().add(input);
    }
    private void cloneRigth(NodoComponent clone, AnchorPane parent, Point diplacement){
        clone.setPrev2(this.prev2.clone(clonationTimes, parent, diplacement));
        clone.getPrev2().getNext().addEnd(clone.getComponent());
        Tag input= clone.getComponent().getInputTag2();
        Tag output= clone.getPrev2().getComponent().getOutputTag();
        input.getNextTag().add(output);
        output.getNextTag().add(input);
    }

    private NodoComponent clone(int currentClonationTimes, AnchorPane parent, Point diplacement){
       if(this.getClonationTimes()==currentClonationTimes){
           return this.getLastClone();
       }else{
           return this.clone(parent, diplacement);
       }

    }

    public int getClonationTimes() {
        return clonationTimes;
    }

    public NodoComponent getLastClone() {
        return lastClone;
    }
}
