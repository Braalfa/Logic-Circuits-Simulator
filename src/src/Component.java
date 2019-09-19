import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;

abstract class Component extends ImageView{
    private ComponentType type;
    private int inputs;
    private Point outputCoords;
    private Point inputCoordsl;
    private Point inputCoords2;
    private static int nextid=0;
    protected boolean input1;
    protected boolean input2;
    protected boolean output;
    protected OutputTag outputTag;
    protected InputTag inputTag1;
    protected InputTag inputTag2;
    private double componentOrgX;
    private double componentOrgY;

    public boolean getInput1() {
        return input1;
    }

    public void setInput1(boolean input1) {
        this.input1 = input1;
    }

    public boolean getInput2() {
        return input2;
    }

    public void setInput2(boolean input2) {
        this.input2 = input2;
    }

    public boolean getOutput() {
        return output;
    }

    public void setOutput(boolean output) {
        this.output = output;
    }

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

    protected void setUpLabels(){
        AnchorPane parent=(AnchorPane) this.getParent();
        this.inputTag1=new InputTag(parent, inputCoordsl,this,1);
        this.outputTag=new OutputTag(parent, outputCoords,this);
        if(inputs==2){
            this.inputTag2=new InputTag(parent, inputCoords2,this,2);
        }
        this.updateTagsPositions();
    }

    public InputTag getInputTag1() {
        return inputTag1;
    }

    public InputTag getInputTag2() {
        return inputTag2;
    }

    public OutputTag getOutputTag() {
        return outputTag;
    }

    public void updateTagsPositions(){
        SuperTree.getInstance().disconnect(this);
        inputTag1.disconect();
        outputTag.disconect();
        if(inputs==2){
            inputTag2.disconect();
        }

    }

    public void destroy(){
        SuperTree.getInstance().remove(this);
        inputTag1.destroy();
        outputTag.destroy();
        if(inputs==2){
            inputTag2.destroy();
        }
        ((AnchorPane)this.getParent()).getChildren().remove(this);
    }

    abstract public boolean calculate();

    public void setOutputTag(OutputTag outputTag) {
        this.outputTag = outputTag;
    }

    public void setInputTag1(InputTag inputTag1) {
        this.inputTag1 = inputTag1;
    }

    public void setInputTag2(InputTag inputTag2) {
        this.inputTag2 = inputTag2;
    }

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

    public void setMovementHandlers() {
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
