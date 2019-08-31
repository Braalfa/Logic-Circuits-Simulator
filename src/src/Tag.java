import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.*;
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

    public Tag(AnchorPane parent, Point coords, Component component){
        this.coords=coords;
        this.parent=parent;
        this.component=component;
        this.lines = new ArrayList<>();
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

        this.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Dragboard db = ((Tag)event.getSource()).startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();
                content.putString(component.getId());
                db.setContent(content);
                event.consume();
            }
        });

        this.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag and drop gesture ended */
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    ((Tag)event.getSource()).setVisible(false);
                }
                event.consume();
            }
        });

        this.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                /* accept it only if it is not dragged from the same node
                 * and if it has a string data */

                if (event.getGestureSource() != event.getSource() &&
                        event.getDragboard().hasString() &&
                        event.getGestureSource() instanceof Tag &&
                        !event.getGestureSource().getClass().equals(event.getSource().getClass()) ){
                    Dragboard db = event.getDragboard();
                    String id= (String)db.getContent(DataFormat.PLAIN_TEXT);
                    if(!id.equals(component.getId())){
                        event.acceptTransferModes(TransferMode.MOVE);
                    }

                }

                event.consume();
            }
        });

        this.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString() ) {
                    String id= (String)db.getContent(DataFormat.PLAIN_TEXT);
                    success=true;
                    ((Tag)event.getSource()).setVisible(false);


                }

                event.setDropCompleted(success);

                event.consume();
            }
        });
    }

    public void updateTagPosition(){
        this.setLayoutX(coords.getX() + component.getX());
        this.setLayoutY(coords.getY() + component.getY());

    }

    public Component getComponent(){
        return component;
    }
}
