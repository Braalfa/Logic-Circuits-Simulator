import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
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

    private void selfConnect(Point endPoint, Point startPoint){
        Line[] lines=null;
        int linesIndex=1;
        while(lines==null){
            lines=this.createPath(linesIndex, linesIndex, startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY(), 1);
            if (lines==null) {
                lines = this.createPath(linesIndex, linesIndex, startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(), 1);
            }
        }
    }

    public Line[] createPath(int numberlines,int orgLines, double ax, double ay, double bx, double by, int direction) {
        if (numberlines == 1) {
            if (ax == bx || by == ay) {
                Line[] result = new Line[orgLines];
                result[orgLines - 1] = this.createLine(ax,ay,bx,by);
                return result;
            }else{
                return null;
            }
        } else {
            Line[] lines = null;
            double middle;
            boolean done = false;
            double limX= parent.getLayoutX();
            double limY= parent.getLayoutY();
            int iterator = 0;
            Line line =this.createLine(ax,ay,bx,by);
            if (this.overlaps(line)) {
                if (direction == 1) {
                    middle = (by - ay) / 2;
                    while (lines == null && !done) {
                        if (ay + middle + iterator < limY) {
                            lines = createPath(numberlines - 1, orgLines, ax, ay + middle + iterator, bx, by, -1);
                        }else if (lines == null && (ay + middle - iterator>0)) {
                            lines = createPath(numberlines - 1, orgLines, ax, ay + middle - iterator, bx, by, -1);
                        }else{
                            done=true;
                        }
                        iterator++;
                    }
                } else {
                    middle = (bx - ax) / 2;
                    while (lines == null && !done) {
                        if (ax + middle + iterator < limX) {
                            lines = createPath(numberlines - 1, orgLines, ax + middle + iterator, ay, bx, by, -1);
                        }else if (lines == null && (ax + middle - iterator>0) ) {
                            lines = createPath(numberlines - 1, orgLines, ax + middle - iterator, ay, bx, by, -1);
                        }
                        iterator++;
                    }
                }
                if(lines!=null){
                    lines[orgLines-numberlines]=line;
                }
                return lines;
            }else{
                return null;
            }

        }
    }
    private Line createLine(double ax, double ay, double bx, double by){
        Line line = new Line();
        line.setStartX(ax);
        line.setEndX(bx);
        line.setStartY(ay);
        line.setEndY(by);
        line.setVisible(false);
        parent.getChildren().add(line);
        return line;
    }
    private boolean overlaps(Line line){
        ObservableList<javafx.scene.Node> nodes=parent.getChildren();
        boolean result=false;
        for(Node node: nodes){
            if (node instanceof Component){
                if(component.getLayoutBounds().intersects(line.getLayoutBounds())){
                    result= true;
                    break;
                }
            }
        }
        return result;

    }

}
