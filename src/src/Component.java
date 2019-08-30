import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    protected Label input1Label;
    protected Label input2Label;
    protected Label outputLabel;
    private AnchorPane parent;
    private static int currentComponentInput=0;
    private static int currentComponentOutput=0;
    private static ArrayList<Integer> unusedInputs = new ArrayList<>();
    private static ArrayList<Integer> unusedOutputs = new ArrayList<>();

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

    protected void setLabels(){
        this.parent=(AnchorPane) this.getParent();
        setInput1Label();
        setOutputLabel();
        if(inputs==2){
            setInput2Label();
        }
        this.updateLabelPositions();
    }

    private void setInput1Label() {
        String text;
        if (unusedInputs.isEmpty()){
            text="i<"+currentComponentInput+">";
            currentComponentInput++;
        }else{
            text="i<"+unusedInputs.get(0)+">";
            unusedInputs.remove(0);
        }
        this.input1Label = new Label(text);
        input1Label.setFont(new Font("Facade",10));
        input1Label.setStyle("-fx-background-color: #EFEFEF");
        parent.getChildren().add(input1Label);
        parent.applyCss();
        parent.layout();
        inputCoordsl.setLocation(inputCoordsl.getX()-input1Label.getWidth(),inputCoordsl.getY()-input1Label.getHeight()/2);

    }

    private void setInput2Label() {
        String text;
        if (unusedInputs.isEmpty()){
            text="i<"+currentComponentInput+">";
            currentComponentInput++;
        }else{
            text="i<"+unusedInputs.get(0)+">";
            unusedInputs.remove(0);
        }
        this.input2Label = new Label(text);
        input2Label.setFont(new Font("Facade",10));
        input2Label.setStyle("-fx-background-color: #EFEFEF");
        parent.getChildren().add(input2Label);
        parent.applyCss();
        parent.layout();
        inputCoords2.setLocation(inputCoords2.getX()-input2Label.getWidth(),inputCoords2.getY()-input2Label.getHeight()/2);

    }

    public void setOutputLabel() {
        String text;
        if (unusedOutputs.isEmpty()){
            text="o<"+currentComponentOutput+">";
            currentComponentOutput++;
        }else{
            text="o<"+unusedOutputs.get(0)+">";
            unusedOutputs.remove(0);
        }
        outputLabel = new Label(text);
        outputLabel.setFont(new Font("Facade",10));
        outputLabel.setStyle("-fx-background-color: #EFEFEF");
        parent.getChildren().add(outputLabel);
        parent.applyCss();
        parent.layout();
        outputCoords.setLocation(outputCoords.getX(),outputCoords.getY()-outputLabel.getHeight()/2);

    }

    public void updateLabelPositions(){
        outputLabel.setLayoutX(outputCoords.getX()+this.getX());
        outputLabel.setLayoutY(outputCoords.getY()+this.getY());

        input2Label.setLayoutX(inputCoords2.getX()+this.getX());
        input2Label.setLayoutY(inputCoords2.getY()+this.getY());

        input1Label.setLayoutX(inputCoordsl.getX()+this.getX());
        input1Label.setLayoutY(inputCoordsl.getY()+this.getY());
    }
    abstract public boolean calculate();
}
