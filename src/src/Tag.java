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
    private static AnchorPane parent;
    protected ArrayList<Line> lines;
    protected Component component;
    private boolean onDrag;
    private double relClickX;
    private double relClickY;
    private Tag nextTag;
    private ArrayList<Bounds> bounds;

    public Tag(AnchorPane parent, Point nodCoords, Component component){
        this.coords=new Point((int)nodCoords.getX(),(int)nodCoords.getY());
        this.nodCoords=nodCoords;
        this.parent=parent;
        this.component=component;
        this.lines = new ArrayList<>();
        this.setMovementListener();
        this.onDrag=false;
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
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Tag source= (Tag)event.getSource();
                relClickX=-source.getLayoutX()+event.getSceneX();
                relClickY=-source.getLayoutY()+event.getSceneY();
                event.consume();
                Line line= new Line();
                if(lines.isEmpty()){
                    double xpos= source.getNodCords().getX()+source.getComponent().getX();
                    double ypos= source.getNodCords().getY()+source.getComponent().getY();
                    line.setStartX(xpos);
                    line.setStartY(ypos);
                    line.setEndX(xpos);
                    line.setEndY(ypos);
                }else{
                    Line lastLine= lines.get(lines.size()-1);
                    line.setStartX(lastLine.getEndX());
                    line.setEndX(lastLine.getEndX());
                    line.setStartY(lastLine.getEndY());
                    line.setEndY(lastLine.getEndY());
                }
                parent.getChildren().add(line);
                lines.add(line);
            }
        });
        this.setOnMouseDragged( new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Tag source= (Tag)event.getSource();
                source.setLayoutX(event.getSceneX()-relClickX);
                source.setLayoutY(event.getSceneY()-relClickY);
                Line thisLine= lines.get(lines.size()-1);
                if(Math.abs(source.getLayoutX()-thisLine.getStartX())>Math.abs(source.getLayoutY()-thisLine.getStartY())){
                    thisLine.setEndX(source.getLayoutX());
                    thisLine.setEndY(thisLine.getStartY());
                }else{
                    thisLine.setEndY(source.getLayoutY());
                    thisLine.setEndX(thisLine.getStartX());
                }

                event.consume();

            }
        });
        this.setOnMouseDragged( new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Tag source= (Tag)event.getSource();
                source.setLayoutX(event.getSceneX()-relClickX);
                source.setLayoutY(event.getSceneY()-relClickY);
                Line thisLine= lines.get(lines.size()-1);
                if(Math.abs(source.getLayoutX()-thisLine.getStartX())>Math.abs(source.getLayoutY()-thisLine.getStartY())){
                    thisLine.setEndX(source.getLayoutX());
                    thisLine.setEndY(thisLine.getStartY());
                }else{
                    thisLine.setEndY(source.getLayoutY());
                    thisLine.setEndX(thisLine.getStartX());
                }

                event.consume();

            }
        });
        this.setOnMouseReleased( new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Tag source= (Tag)event.getSource();
                Line thisLine= lines.get(lines.size()-1);
                Tag overlap = source.getOverlap();
                if(overlap==null) {
                    if (thisLine.getStartX() > thisLine.getEndX()) {
                        source.setLayoutX(thisLine.getEndX() - source.getWidth());
                        source.setLayoutY(thisLine.getEndY() - source.getHeight() / 2);
                    } else if (thisLine.getStartX() < thisLine.getEndX()) {
                        source.setLayoutX(thisLine.getEndX());
                        source.setLayoutY(thisLine.getEndY() - source.getHeight() / 2);
                    } else if (thisLine.getStartY() > thisLine.getEndY()) {
                        source.setLayoutX(thisLine.getEndX() - source.getWidth() / 2);
                        source.setLayoutY(thisLine.getEndY() - source.getHeight());
                    } else if (thisLine.getStartY() < thisLine.getEndY()) {
                        source.setLayoutX(thisLine.getEndX() - source.getWidth() / 2);
                        source.setLayoutY(thisLine.getEndY());
                    }
                }else{
                    source.setVisible(false);
                    overlap.setVisible(false);
                    autoConnect(source,overlap);
                    nextTag=overlap;
                }

            }
        });
    }

    public void updateTagPosition(){
        this.setLayoutX(coords.getX() + component.getX());
        this.setLayoutY(coords.getY() + component.getY());

    }


    private boolean overlaps(Line line, Tag start,Tag end){
        boolean result=false;
        Bounds startBn = start.getComponent().getLayoutBounds();
        Bounds endBn = end.getComponent().getLayoutBounds();

        if( line.intersects(startBn.getMinX()+1,startBn.getMinY(),startBn.getMaxX()-startBn.getMinX()-2, startBn.getMaxY()-startBn.getMinY())  ||
            line.intersects(endBn.getMinX()+1,endBn.getMinY(),endBn.getMaxX()-endBn.getMinX()-2, endBn.getMaxY()-endBn.getMinY())){
                result=true;

        }
        return result;

    }

    private Tag getOverlap(){
        ObservableList<Node> nodos=parent.getChildren();
        Tag overlap=null;
        for(Node nodo:nodos){
            if(nodo instanceof Tag && !nodo.getClass().equals(this.getClass())){
                if(nodo.getBoundsInParent().intersects(this.getBoundsInParent())){
                    overlap=(Tag)nodo;
                    break;
                }
            }
        }
        return overlap;
    }

    private void autoConnect(Tag start, Tag end) {
        Point startPoint = start.getLastLinePoint();
        Point endPoint = end.getLastLinePoint();
        this.removeLastLine();
        Line[] lines = null;
        int linesIndex = 1;
        this.updateBounds(start,end);
        while (lines == null) {
            lines = this.createPath(linesIndex, linesIndex, (int) startPoint.getX() ,
                    (int) startPoint.getY() , (int) endPoint.getX() , (int) endPoint.getY() , 1, start, end);
            if (lines == null) {
                lines = this.createPath(linesIndex, linesIndex, (int) startPoint.getX() ,
                        (int) startPoint.getY() , (int) endPoint.getX() , (int) endPoint.getY() , -1, start, end);
            }
            linesIndex++;
            System.out.print(linesIndex+"\n");
        }
        for (Line line : lines) {
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
                    parent.getChildren().remove(line);
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
            int lim1 = this.getDownLimit(ax);
            int lim2 = this.getUpLimit(ax);
            int lim3 = this.getRigthLimit(ay);
            int lim4 = this.getLeftLimit(ay);
            int iterator = 0;
            Line line = null;
            if (direction == 1) {
                middle = (by - ay) / 2;
                while (lines == null && !done1 && !done2) {
                    if (ay + middle + iterator < lim1) {
                        parent.getChildren().remove(line);
                        line = this.createLine(ax, ay, ax, ay + middle + iterator);
                        if (!this.overlaps(line,start, end)) {
                            lines = createPath(numberlines - 1, orgLines, ax, ay + middle + iterator, bx, by, -1, start, end);
                        }
                    } else{
                        done1=true;
                    }
                    if (lines == null && (ay + middle - iterator > lim2)) {
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
                    if (ax + middle + iterator < lim3) {
                        parent.getChildren().remove(line);
                        line = this.createLine(ax, ay, ax + middle + iterator, ay);
                        if (!this.overlaps(line, start, end)) {
                            lines = createPath(numberlines - 1, orgLines, ax + middle + iterator, ay, bx, by, 1, start, end);
                        }
                    } else {
                        done1 = true;
                    }
                    if (lines == null && (ax + middle - iterator > lim4)) {
                        parent.getChildren().remove(line);
                        line = this.createLine(ax, ay, ax + middle - iterator, ay);

                        if (!this.overlaps(line, start, end)) {
                            lines = createPath(numberlines - 1, orgLines, ax + middle - iterator, ay, bx, by, 1, start, end);
                        }
                    } else {
                        done2 = true;
                    }
                    iterator += 1;
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
        line.setStartY(ay);
        line.setEndX(bx);
        line.setEndY(by);
        parent.getChildren().add(line);
        return line;
    }
    private void updateBounds(Tag start,Tag end){
        ObservableList<javafx.scene.Node> nodes=parent.getChildren();
        ArrayList<Bounds> bounds= new ArrayList<>();
        for(Node node: nodes){
            if ((node instanceof Component) && (node!= start.getComponent() && node!=end.getComponent())){
                bounds.add(node.getBoundsInParent());
            }
        }
        this.bounds=bounds;

    }

    private int getRigthLimit(int posY){
        int limX=(int) parent.getWidth();
        for(Bounds bound: bounds){
            if(bound.getMinY()<=posY && bound.getMaxY()>=posY){
                if(limX>bound.getMinX()){
                    limX= (int) bound.getMinX();
                }
            }
        }
        return limX;
    }
    private int getLeftLimit( int posY){
        int limX=0;
        for(Bounds bound: bounds){
            if(bound.getMinY()<=posY && bound.getMaxY()>=posY){
                if(limX<bound.getMaxX()){
                    limX= (int) bound.getMinX();
                }
            }
        }
        return limX;
    }
    private int getDownLimit( int posX){
        int limY=(int) parent.getHeight();
        for(Bounds bound: bounds){
            if(bound.getMinX()<=posX && bound.getMaxX()>=posX){
                if(limY>bound.getMinY()){
                    limY= (int) bound.getMinY();
                }
            }
        }
        return limY;
    }
    private int getUpLimit(int posX){
        int limY=0;
        for(Bounds bound: bounds){
            if(bound.getMinX()<=posX && bound.getMaxX()>=posX){
                if(limY<bound.getMaxY()){
                    limY= (int) bound.getMaxY();
                }
            }
        }
        return limY;
    }

    private Point getNodCords(){
        return this.nodCoords;
    }

    private Component getComponent(){
        return this.component;
    }

    private void removeLastLine(){
        Line line= lines.get(lines.size()-1);
        parent.getChildren().remove(line);
        lines.remove(line);
    }

    private Point getLastLinePoint(){
        if(this.lines.isEmpty() || lines.size()==1 ){
            return new Point((int)(nodCoords.getX()+component.getX()),(int)(nodCoords.getY()+component.getY()));
        }else{
            Line lastLine= lines.get(lines.size()-2);
            return new Point((int)lastLine.getEndX(),(int)lastLine.getEndY());
        }
    }

}
