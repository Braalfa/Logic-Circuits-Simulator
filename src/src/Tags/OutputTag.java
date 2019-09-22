package Tags;

import javafx.scene.layout.AnchorPane;
import Component.*;
import java.awt.Point;
import java.util.ArrayList;
/**
 * La clase OutputTag representa los Tags que se encuentran en la parte anterior de los componentes
 */
public class OutputTag extends Tag {
    private static int currentId=0;
    private static ArrayList<Integer> unusedIds = new ArrayList<>();
    /**
     * Constructor de la clase
     * @param parent AnchorPane donde se encontrara el OutputTag
     * @param coords Punto relativo al componente donde se encontrara el OutputTag
     * @param component Componente padre del OutputTag
     */
    public OutputTag(AnchorPane parent, Point coords, Component component) {
        super(parent,coords, component);
        this.updateId();
        this.display(this.getId());
        this.coords.setLocation(coords.getX(),coords.getY()-this.getHeight()/2);
    }

    /**
     * Agrega un Tag a la lista de nextTags
     * @param nextTag Tag a agregar
     */
    protected void addNextTag(Tag nextTag) {
        this.nextTag.add(nextTag);
    }
    /**
     * Clona la instancia de OutputTag
     * @param parent AnchorPane en el cual se encontrara el clon
     * @param component Componente clon padre
     * @param coords Coordenadas del OutputTag en relacion al padre
     * @param diplacement Desplazamientoe en el AnchorPane del clon
     * @return Clon de OutputTag
     */
    public OutputTag clone(AnchorPane parent, Component component, Point coords, Point diplacement){
        OutputTag clone= new OutputTag(parent, coords, component);
        clone.setLayoutX(this.getLayoutX()+diplacement.getX());
        clone.setLayoutY(this.getLayoutY()+diplacement.getY());
        clone.setLines(this.cloneLines(diplacement, parent));
        clone.setLinesColor(clone.getLinesColor());
        clone.setVisible(this.isVisible());
        return clone;
    }

    /**
     * Establece el id de la instancia basado en el valor estatico de la clase
     */
    public void updateId(){
        String text;
        if (unusedIds.isEmpty()){
            text="o<"+currentId+">";
            currentId++;
        }else{
            text="o<"+unusedIds.get(0)+">";
            unusedIds.remove(0);
        }
        this.setId(text);
    }
}
