package Tags;

import javafx.scene.layout.AnchorPane;
import Component.*;
import java.awt.Point;
import java.util.ArrayList;

/**
 * La clase InputTag representa los Tags que se encuentran en la parte anterior de los componentes
 */
public class InputTag extends Tag {
    private static int currentId=0;
    private static ArrayList<Integer> unusedIds = new ArrayList<>();
    private int inputNumber;

    /**
     * Constructor de la clase
     * @param parent AnchorPane donde se encontrara el InputTag
     * @param coords Punto relativo al componente donde se encontrara el InputTag
     * @param component Componente padre del InputTag
     * @param inputNumber Numero de InputTag en el componente padre
     */
    public InputTag(AnchorPane parent, Point coords, Component component, int inputNumber) {
        super(parent,coords, component);
        this.updateId();
        this.display(this.getId());
        this.coords.setLocation(coords.getX()-this.getWidth(),coords.getY()-this.getHeight()/2);
        this.inputNumber=inputNumber;

    }

    /**
     * Establece el id de la instancia basado en el valor estatico de la clase
     */
    public void updateId(){
        String text;
        if (unusedIds.isEmpty()){
            text="i<"+currentId+">";
            currentId++;
        }else{
            text="i<"+unusedIds.get(0)+">";
            unusedIds.remove(0);
        }
        this.setId(text);
    }

    /**
     * Clona la instancia de InputTag
     * @param parent AnchorPane en el cual se encontrara el clon
     * @param component Componente clon padre
     * @param coords Coordenadas del InputTag en relacion al padre
     * @param diplacement Desplazamientoe en el AnchorPane del clon
     * @return Clon de InputTag
     */
    public InputTag clone(AnchorPane parent, Component component, Point coords, Point diplacement){
        InputTag clone= new InputTag(parent, coords, component, inputNumber);
        clone.setLayoutX(this.getLayoutX()+diplacement.getX());
        clone.setLayoutY(this.getLayoutY()+diplacement.getY());
        clone.setLines(this.cloneLines(diplacement,parent));
        clone.setLinesColor(clone.getLinesColor());
        clone.setVisible(this.isVisible());
        return clone;
    }

    /**
     * Retorna el numero de input
     * @return Numero de input
     */
    public int getInputNumber() {
        return inputNumber;
    }

    /**
     * Agrega un Tag a la lista de nextTags
     * @param nextTag Tag a agregar
     */
    protected void addNextTag(Tag nextTag) {
        superTree.connect(this.getComponent(), nextTag.getComponent(), inputNumber);
        this.nextTag.add(nextTag);
    }


}
