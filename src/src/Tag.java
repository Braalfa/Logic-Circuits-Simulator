import javafx.collections.ObservableList;
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

    public void setNextTag(Tag nextTag) {
        this.nextTag = nextTag;
    }

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
                if (!source.overlapsComponents(thisLine)) {
                    if (Math.abs(source.getLayoutX() - thisLine.getStartX()) > Math.abs(source.getLayoutY() - thisLine.getStartY())) {
                        thisLine.setEndX(source.getLayoutX());
                        thisLine.setEndY(thisLine.getStartY());
                    } else {
                        thisLine.setEndY(source.getLayoutY());
                        thisLine.setEndX(thisLine.getStartX());
                    }
                    if (!source.overlapsComponents(thisLine)) {
                        thisLine.setVisible(true);
                    }else{
                        thisLine.setEndY(thisLine.getStartY());
                        thisLine.setEndX(thisLine.getStartX());
                    }
                }else{
                    thisLine.setEndY(thisLine.getStartY());
                    thisLine.setEndX(thisLine.getStartX());
                    thisLine.setVisible(false);

                }

                event.consume();

            }
        });
        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Tag source = (Tag) event.getSource();
                Tag overlap;
                Line thisLine = lines.get(lines.size() - 1);
                overlap = source.getOverlap();
                if (overlap == null) {
                    if (thisLine.getStartX() == thisLine.getEndX() && thisLine.getStartY() == thisLine.getEndY()) {
                        if (lines.size() > 1) {
                            lines.remove(thisLine);
                            parent.getChildren().remove(thisLine);
                            thisLine = lines.get(lines.size() - 1);
                            source.uptadeDisplacedTag(thisLine);
                        } else {
                            source.setLayoutX(coords.getX() + component.getX());
                            source.setLayoutY(coords.getY() + component.getY());
                        }
                    } else {
                        source.uptadeDisplacedTag(thisLine);
                    }
                } else {
                    if (((overlap instanceof InputTag && !overlap.hasNextTag()) || overlap instanceof OutputTag) && overlap.getComponent() != source.getComponent()) {
                        source.setVisible(false);
                        overlap.setVisible(false);
                        ConnectorSingleton connector = ConnectorSingleton.getInstance(source, overlap);
                        boolean result = connector.autoConnect(source, overlap);
                        nextTag = overlap;
                        overlap.setNextTag(source);
                        if (!result) {
                            source.uptadeDisplacedTag(thisLine);
                        }
                    } else {
                        if (lines.size() > 1) {
                            lines.remove(thisLine);
                            parent.getChildren().remove(thisLine);
                            thisLine = lines.get(lines.size() - 1);
                            source.uptadeDisplacedTag(thisLine);
                        } else {
                            source.setLayoutX(coords.getX() + component.getX());
                            source.setLayoutY(coords.getY() + component.getY());
                        }
                    }
                }


            }
        });
    }

    public Tag getNextTag() {
        return nextTag;
    }

    public void uptadeDisplacedTag(Line thisLine){
        if (thisLine.getStartX() > thisLine.getEndX()) {
            this.setLayoutX(thisLine.getEndX() - this.getWidth());
            this.setLayoutY(thisLine.getEndY() - this.getHeight() / 2);
        } else if (thisLine.getStartX() < thisLine.getEndX()) {
            this.setLayoutX(thisLine.getEndX());
            this.setLayoutY(thisLine.getEndY() - this.getHeight() / 2);
        } else if (thisLine.getStartY() > thisLine.getEndY()) {
            this.setLayoutX(thisLine.getEndX() - this.getWidth() / 2);
            this.setLayoutY(thisLine.getEndY() - this.getHeight());
        } else {
            this.setLayoutX(thisLine.getEndX() - this.getWidth() / 2);
            this.setLayoutY(thisLine.getEndY());
        }

    }

    public void updateTagPosition(){
        this.clearLines();
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

    public Tag getOverlap(){
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

    private Point getNodCords(){
        return this.nodCoords;
    }

    public Component getComponent(){
        return this.component;
    }

    public void removeLastLine(){
        Line line= lines.get(lines.size()-1);
        parent.getChildren().remove(line);
        lines.remove(line);
    }

    public Point getLastLineStartPoint(){
        if(this.lines.isEmpty() ){
            return new Point((int)(nodCoords.getX()+component.getX()),(int)(nodCoords.getY()+component.getY()));
        }else if ( lines.size()==1){
            return new Point((int)(nodCoords.getX()+component.getX()),(int)(nodCoords.getY()+component.getY()));
        }else{
            Line lastLine= lines.get(lines.size()-2);
            return new Point((int)lastLine.getEndX(),(int)lastLine.getEndY());
        }
    }
    public Point getLastLineEndPoint(){
        if(this.lines.isEmpty() ){
            return new Point((int)(nodCoords.getX()+component.getX()),(int)(nodCoords.getY()+component.getY()));
        }else{
            Line lastLine= lines.get(lines.size()-1);
            return new Point((int)lastLine.getEndX(),(int)lastLine.getEndY());
        }
    }
    public boolean hasNextTag(){
        if (this.nextTag!=null){
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void clearLines(){
        for(Line line:this.lines){
            parent.getChildren().remove(line);
        }
        lines= new ArrayList<>();
        if(this.hasNextTag()){
            this.nextTag.setNextTag(null);
            this.nextTag.updateTagPosition();
            nextTag.setVisible(true);
            this.setVisible(true);
            this.setNextTag(null);
        }
        this.setVisible(true);
    }

    private boolean overlapsComponents(Line line){
        ObservableList<Node> nodes=parent.getChildren();
        Bounds bounds;
        boolean result= false;
        for(Node node: nodes){
            bounds=component.getBoundsInParent();
            if ((node instanceof Component) && ((node!= this.getComponent()
                    ||line.intersects(bounds.getMinX()+1,bounds.getMinY(),bounds.getMaxX()-bounds.getMinX()-2, bounds.getMaxY()-bounds.getMinY())))){
                bounds=node.getBoundsInParent();
                if(line.intersects(bounds)){
                    System.out.print(node);
                    result=true;
                    break;
                 }
            }
        }
        return result;
    }

}
