import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;

abstract class Component extends ImageView{
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

    public Component(int inputs, Point outputCoords, Point inputCoordsl, Point inputCoords2, Image image){
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
        nextid++;
    }

    protected void setUpLabels(){
        AnchorPane parent=(AnchorPane) this.getParent();
        this.inputTag1=new InputTag(parent, inputCoordsl,this);
        this.outputTag=new OutputTag(parent, outputCoords,this);
        if(inputs==2){
            this.inputTag2=new InputTag(parent, inputCoords2,this);
        }
        this.updateTagsPositions();
    }

    public void updateTagsPositions(){
        inputTag1.disconect();
        outputTag.disconect();
        if(inputs==2){
            inputTag2.disconect();
        }

    }

    public void destroy(){
        inputTag1.destroy();
        outputTag.destroy();
        if(inputs==2){
            inputTag2.destroy();
        }
        ((AnchorPane)this.getParent()).getChildren().remove(this);
    }

    abstract public boolean calculate();
}
