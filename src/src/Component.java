import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    private static int currentComponentInput=0;
    private static int currentComponentOutput=0;
    private static ArrayList<Integer> unusedInputs= new ArrayList<>();
    private static ArrayList<Integer> unusedOutputs= new ArrayList<>();

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

        setInput1Label();
        setOutputLabel();
        if(inputs==2){
            setInput2Label();
        }
    }

    public void setInput1Label() {
        String text;
        ((AnchorPane)(this.getParent())).getChildren().add(input1Label);
        if (unusedInputs.isEmpty()){
            text="i<"+currentComponentInput+">";
            currentComponentInput++;
        }else{
            text="i<"+unusedInputs.get(0)+">";
            unusedInputs.remove(0);
        }
        this.input1Label = new Label(text);
        this.input1Label.setLayoutX(inputCoordsl.getX());
        this.input1Label.setLayoutY(inputCoordsl.getY());

    }

    public void setInput2Label() {
        String text;
        ((AnchorPane)(this.getParent())).getChildren().add(input2Label);
        if (unusedInputs.isEmpty()){
            text="i<"+currentComponentInput+">";
            currentComponentInput++;
        }else{
            text="i<"+unusedInputs.get(0)+">";
            unusedInputs.remove(0);
        }
        this.input2Label = new Label(text);
        this.input2Label.setLayoutX(inputCoords2.getX());
        this.input2Label.setLayoutY(inputCoords2.getY());
    }

    public void setOutputLabel() {
        String text;
        if (unusedOutputs.isEmpty()){
            text="i<"+currentComponentOutput+">";
            currentComponentOutput++;
        }else{
            text="i<"+unusedOutputs.get(0)+">";
            unusedOutputs.remove(0);
        }
        ((AnchorPane) (this.getParent())).getChildren().add(outputLabel);
        this.outputLabel = new Label(text);
        this.outputLabel.setLayoutX(outputCoords.getX());
        this.outputLabel.setLayoutY(outputCoords.getY());
    }
    abstract public boolean calculate();
}
