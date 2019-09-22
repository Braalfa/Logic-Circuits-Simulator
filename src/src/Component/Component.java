package Component;

import Tags.InputTag;
import Tags.OutputTag;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import SuperList.*;
import  java.awt.Point;

/**
 * La presente clase representa una compuerta logica, la cual es representada por su imagen, y tiene una funcion
 * de calculcular su valor de output basado en sus inputs.
 * Esta clase es abstracta, debe se der implementada para definir el tipo de compuerta logica
 */
public abstract class Component extends ImageView{
    /**
     * Tipo de componente
     */
    private ComponentType type;
    /**
     * Cantidad de inputs
     */
    private int inputs;
    /**
     * Coordenadas del OutputTag en si mismo
     */
    private Point outputCoords;
    /**
     * Coordenadas del InputTag1 en si mismo
     */
    private Point inputCoordsl;
    /**
     * Coordenadas del InputTag2 en si mismo
     */
    private Point inputCoords2;
    /**
     * Valor estatico del siguiente id
     */
    private static int nextid=0;
    /**
     * Valor de input 1
     */
    protected boolean input1;
    /**
     * Valor de input 2
     */
    protected boolean input2;
    /**
     * Valor de output
     */
    protected boolean output;
    /**
     * Tag de output
     */
    protected OutputTag outputTag;
    /**
     * Tag de input 1
     */
    protected InputTag inputTag1;
    /**
     * Tag de input 2
     */
    protected InputTag inputTag2;
    /**
     * Posicion original del componente en x antes de ser arrastrado
     */
    private double componentOrgX;
    /**
     * Posicion original del componente en y antes de ser arrastrado
     */
    private double componentOrgY;



    /**
     * Se establece el valor de la entrada 1 del componente
     * @param input1 Valor booleano que tomará la entrada 1
     */
    public void setInput1(boolean input1) {
        this.input1 = input1;
    }

    /**
     * Se establece el valor de la entrada 2 del componente
     * @param input2 Valor booleano que tomará la entrada 2
     */
    public void setInput2(boolean input2) {
        this.input2 = input2;
    }

    /**
     * Se retorna el valor del output del componente
     * @return Valor booleano de la salida
     */
    public boolean getOutput() {
        return output;
    }


    /**
     * Constructor del componente
     * @param inputs Cantidad de entradas
     * @param outputCoords Coordenadas del tag de salida
     * @param inputCoordsl Coordenadas del tag de entrada 1
     * @param inputCoords2 Coordenadas del tag de entrada 2
     * @param image Imagen del componente
     * @param type Tipo de componente
     */
 public Component(int inputs, Point outputCoords, Point inputCoordsl, Point inputCoords2, Image image, ComponentType type){
        this.inputs=inputs;
        this.outputCoords=outputCoords;
        this.inputCoordsl=inputCoordsl;
        this.inputCoords2=inputCoords2;
        this.setImage(image);
        this.setFitHeight(32);
        this.setPreserveRatio(true);
        this.setCursor(Cursor.HAND);
        this.setPickOnBounds(true);
        this.setId("comp"+nextid);
        this.type=type;
        nextid++;
        this.setMovementHandlers();
    }

    /**
     * Se crean los tags asociados al componente
     */
 public void setUpLabels(){
        AnchorPane parent=(AnchorPane) this.getParent();
        this.inputTag1=new InputTag(parent, inputCoordsl,this,1);
        this.outputTag=new OutputTag(parent, outputCoords,this);
        if(inputs==2){
            this.inputTag2=new InputTag(parent, inputCoords2,this,2);
        }
        this.updateTagsPositions();
    }

    /**
     * Se retorna el inputTag numero 1
     * @return El InputTag1
     */
    public InputTag getInputTag1() {
        return inputTag1;
    }
    /**
     * Se retorna el inputTag numero 2
     * @return El InputTag2
     */
    public InputTag getInputTag2() {
        return inputTag2;
    }

    /**
     * Se retorna el OutputTag
     * @return El OutputTag
     */
    public OutputTag getOutputTag() {
        return outputTag;
    }

    /**
     * Se devuelven los tags a sus posiciones base en el componente
     */
    public void updateTagsPositions(){
        SuperTree.getInstance().disconnect(this);
        inputTag1.disconect();
        outputTag.disconect();
        if(inputs==2){
            inputTag2.disconect();
        }

    }

    /**
     * Se destruye el componente y sus Tags
     */
    public void destroy(){
        SuperTree.getInstance().remove(this);
        inputTag1.destroy();
        outputTag.destroy();
        if(inputs==2){
            inputTag2.destroy();
        }
        ((AnchorPane)this.getParent()).getChildren().remove(this);
    }

    /**
     * Se calcula el valor booleano de output del componente
     * @return El valor booleano de output
     */
    abstract public boolean calculate();

    /**
     * Se establece el OutputTag
     * @param outputTag El OutputTag
     */
    private void setOutputTag(OutputTag outputTag) {
        this.outputTag = outputTag;
    }

    /**
     * Se establece el InputTag1
     * @param inputTag1 El InputTag1
     */
    private void setInputTag1(InputTag inputTag1) {
        this.inputTag1 = inputTag1;
    }

    /**
     * Se establece el InputTag2
     * @param inputTag2 El InputTag1
     */
    private void setInputTag2(InputTag inputTag2) {
        this.inputTag2 = inputTag2;
    }

    /**
     * Se retorna un clon del Componente
     * @param parent El anchorPane que contendra al clon
     * @param diplacement El desplazamiento del clon con respecto al cero
     * @return Un clon del componente
     */
    public Component clone(AnchorPane parent, Point diplacement){
        Component clone = Component_Factory.getComponent(this.type);
        parent.getChildren().add(clone);
        clone.setX(this.getX()+diplacement.getX());
        clone.setY(this.getY()+diplacement.getY());
        if(inputs==2){
            clone.setInputTag2(inputTag2.clone(parent,clone,inputCoords2, diplacement));
        }
        clone.setInputTag1(inputTag1.clone(parent, clone, inputCoordsl,diplacement));
        clone.setOutputTag(outputTag.clone(parent,clone, outputCoords,diplacement));
        return clone;
    }

    /**
     * Se establece el manejo de acciones sobre el componente
     */
    private void setMovementHandlers() {
        Component component=this;
        this.setOnMousePressed( new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    componentOrgX = -component.getX() + event.getX();
                    componentOrgY = -component.getY() +event.getY();
                }else if(event.getButton()==MouseButton.SECONDARY){
                    component.destroy();
                }
            }
        });
        this.setOnMouseDragged( new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    component.setX(event.getX()-componentOrgX);
                    component.setY(event.getY()-componentOrgY);
                    component.updateTagsPositions();
                }
            }
        });

    }


}
