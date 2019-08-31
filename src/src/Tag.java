import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;

public abstract class Tag extends Label {
    protected Point coords;
    private AnchorPane parent;
    protected ArrayList<Line> lines;
    protected Component component;
    private Boolean onDrag;
    private double relClickX;
    private double relClickY;

    public Tag(AnchorPane parent, Point coords, Component component){
        this.coords=coords;
        this.parent=parent;
        this.component=component;
        this.lines = new ArrayList<>();
        this.onDrag=false;
        this.setMovementListener();
    }

    protected void display(String text){
        this.setText(text);
        this.setFont(new Font("Facade",10));
        this.setStyle("-fx-background-color: #EFEFEF");
        parent.getChildren().add(this);
        parent.applyCss();
        parent.layout();
    }

    private void setMovementListener(){
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(!onDrag){
                    onDrag=true;
                    relClickX=-((Tag)event.getSource()).getLayoutX()+event.getSceneX();
                    relClickY=-((Tag)event.getSource()).getLayoutY()+event.getSceneY();
                }else{

                }
                event.consume();
            }
        });
        this.setOnMouseMoved( new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(onDrag){
                    ((Tag)event.getSource()).setLayoutX(event.getSceneX()-relClickX);
                    ((Tag)event.getSource()).setLayoutY(event.getSceneY()-relClickY);
                    event.consume();
                }
            }
        });
    }

    public void updateTagPosition(){
        this.setLayoutX(coords.getX() + component.getX());
        this.setLayoutY(coords.getY() + component.getY());

    }
}
