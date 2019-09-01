import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;

public abstract class Tag extends Label {
    protected Point nodCoords;
    protected Point coords;
    private AnchorPane parent;
    protected ArrayList<Line> lines;
    protected Component component;

    public Tag(AnchorPane parent, Point nodCoords, Component component){
        this.coords=new Point((int)nodCoords.getX(),(int)nodCoords.getY());
        this.nodCoords=nodCoords;
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
                    Tag end=((Tag)event.getSource());
                    Tag start=((Tag)event.getGestureSource());
                    end.setVisible(false);
                    start.autoConnect(start,end);

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

    private void autoConnect(Tag start, Tag end){
        Point startPoint=start.getNodCords();
        Point endPoint = end.getNodCords();
        int startMovementX= (int)start.getComponent().getX();
        int startMovementY= (int)start.getComponent().getY();
        int endMovementX= (int)end.getComponent().getX();
        int endMovemenY= (int)end.getComponent().getY();

        Line[] lines=null;
        int linesIndex=1;
        while(lines==null){
            lines=this.createPath(linesIndex, linesIndex, (int)startPoint.getX()+startMovementX,
                    (int)startPoint.getY()+startMovementY,(int)endPoint.getX()+endMovementX,(int)endPoint.getY()+endMovemenY, 1, start,end);
            if (lines==null) {
                lines = this.createPath(linesIndex, linesIndex, (int)startPoint.getX()+startMovementX,
                        (int)startPoint.getY()+startMovementY,(int)endPoint.getX()+endMovementX,(int)endPoint.getY()+endMovemenY, -1, start,end);
            }
            System.out.print(lines);
            linesIndex++;
        }
        for(Line line:lines){
            line.setVisible(true);
        }
    }

    public Line[] createPath(int numberlines, int orgLines, int ax, int ay, int bx, int by, int direction, Tag start, Tag end ) {
        if (numberlines == 1) {
            if (ax == bx || by == ay) {
                Line[] result = new Line[orgLines];
                Line line= this.createLine(ax, ay, bx, by);
                if (!this.overlaps(line,start, end)) {
                    result[orgLines-numberlines]=line;
                    return result;
                }else{
                    return null;
                }
            } else {
                return null;
            }
        } else {
            Line[] lines = null;
            int middle;
            boolean done1 = false;
            boolean done2 = false;
            int limX = (int)parent.getWidth();
            int limY = (int)parent.getHeight();
            int iterator = 0;
            Line line = null;
            if (direction == 1) {
                middle = (by - ay) / 2;
                while (lines == null && !done1 && !done2) {
                    if (ay + middle + iterator < limY) {
                        parent.getChildren().remove(line);
                        line = this.createLine(ax, ay, ax, ay + middle + iterator);
                        if (!this.overlaps(line,start, end)) {
                            lines = createPath(numberlines - 1, orgLines, ax, ay + middle + iterator, bx, by, -1, start, end);
                        }
                    } else{
                        done1=true;
                    }
                    if (lines == null && (ay + middle - iterator > 0)) {
                        parent.getChildren().remove(line);
                        line = this.createLine(ax, ay, ax, ay + middle - iterator);
                        if (!this.overlaps(line,start, end)) {
                            lines = createPath(numberlines - 1, orgLines, ax, ay + middle - iterator, bx, by, -1,start, end);
                        }
                    } else {
                        done2 = true;
                    }
                    iterator+=1;
                }
            } else {
                middle = (bx - ax) / 2;
                while (lines == null && !done1 && !done2) {
                    if (ax + middle + iterator < limX) {
                        parent.getChildren().remove(line);
                        line = this.createLine(ax, ay, ax + middle + iterator, ay);
                        if (!this.overlaps(line,start, end)) {
                            lines = createPath(numberlines - 1, orgLines, ax + middle + iterator, ay, bx, by, 1,start, end);
                        }
                    } else{
                        done1=true;
                    }
                    if (lines == null && (ax + middle - iterator > 0)) {
                        parent.getChildren().remove(line);
                        line = this.createLine(ax, ay, ax + middle - iterator, ay);

                        if (!this.overlaps(line,start, end)) {
                            lines = createPath(numberlines - 1, orgLines, ax + middle - iterator, ay, bx, by, 1,start, end);
                        }
                    }else{
                        done2 = true;
                    }
                    iterator+=1;
                }
            }
            if (lines != null) {
                lines[orgLines - numberlines] = line;
            } else {
                parent.getChildren().remove(line);
            }
            return lines;
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
    private boolean overlaps(Line line, Tag start,Tag end){
        ObservableList<javafx.scene.Node> nodes=parent.getChildren();
        boolean result=false;
        Bounds startBn = start.getComponent().getLayoutBounds();
        Bounds endBn = start.getComponent().getLayoutBounds();

        for(Node node: nodes){
            if ((node instanceof Component) && (node!= start.getComponent()||
                    line.intersects(startBn.getMinX()+1,startBn.getMinY(),startBn.getMaxX()-startBn.getMinX()-2, startBn.getMaxY()))
                    && (node!=end.getComponent() ||
                    line.intersects(endBn.getMinX()+1,endBn.getMinY(),endBn.getMaxX()-endBn.getMinX()-2, endBn.getMaxY()))
                    ){
                if(node.getLayoutBounds().intersects(line.getLayoutBounds())){
                    result= true;
                    break;
                }
            }
        }
        return result;

    }

    private Point getNodCords(){
        return this.nodCoords;
    }

    private Component getComponent(){
        return this.component;
    }

}
