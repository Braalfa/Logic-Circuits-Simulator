import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

abstract class Component extends ImageView{
    private int inputs;
    private Point outputCoords;
    private Point inputCoordsl;
    private Point inputCoords2;
    protected boolean input1;
    protected boolean input2;
    protected boolean output;

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

    abstract public boolean calculate();
}
