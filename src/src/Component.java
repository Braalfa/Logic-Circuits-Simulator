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
    protected boolean input1;
    protected boolean input2;
    protected boolean output;
    protected OutputTag outputTag;
    protected InputTag inputTag1;
    protected InputTag inputTag2;



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
        inputTag1.updateTagPosition();
        outputTag.updateTagPosition();
        if(inputs==2){
            inputTag2.updateTagPosition();
        }

    }

    abstract public boolean calculate();
}
