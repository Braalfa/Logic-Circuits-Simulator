package Component;

import SimpleList.List;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import SuperList.*;
import Tags.*;
import Component.*;
import javax.imageio.ImageIO;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

public class SuperComponent extends Label {
    private SuperTree clone;
    private ObservableList<Node> elements;
    private Image image;
    private Rectangle2D bounds;

    private ObservableList<Node> elementsClone;
    private SuperTree cloneClone;

    public SuperComponent(String name, Parent parent) {
        AnchorPane copyPane = new AnchorPane();
        copyPane.setPrefHeight(800);
        copyPane.setPrefWidth(1200);
        copyPane.setStyle("-fx-background-color: #F8FAFF;");
        this.clone= SuperTree.getInstance().clone(copyPane,new Point(0,0));
        this.elements=copyPane.getChildren();
        this.setText(name);
        this.setBounds((int)parent.getLayoutBounds().getWidth(),(int)parent.getLayoutBounds().getHeight(), ((AnchorPane)parent).getChildren());
        this.setImage(name,parent);
        this.updatePositions();
    }

    public void generate(AnchorPane parent, Point diplacement){
        this.cloneClone= clone.clone(parent, diplacement);
        this.elementsClone=parent.getChildren();
        SuperTree.getInstance().addOutputNodes(this.getOutputs());

    }



    public void updatePositions(){
        int newCeroX= (int)this.bounds.getMinX();
        int newCeroY=(int)this.bounds.getMinY();
        for(Node node: elements){
            if(node instanceof Component){
                Component comp= (Component)node;
                comp.setX(comp.getX()-newCeroX);
                comp.setY(comp.getY()-newCeroY);
            }else if( node instanceof Line ){
                Line line= (Line)node;
                line.setStartX(line.getStartX()-newCeroX);
                line.setStartY(line.getStartY()-newCeroY);
                line.setEndX(line.getEndX()-newCeroX);
                line.setEndY(line.getEndY()-newCeroY);
            }else if(node instanceof Tag) {
               Tag tag = (Tag) node;
               tag.setLayoutX(tag.getLayoutX()-newCeroX);
               tag.setLayoutY(tag.getLayoutY()-newCeroY);

            }
        }
    }

    public File saveImageAsPng(String name, Parent parent) {
        SnapshotParameters snapshotParameters= new SnapshotParameters();
        snapshotParameters.setViewport(this.getBounds());
        snapshotParameters.setFill(Color.TRANSPARENT);
        System.out.print(this.getBounds());
        WritableImage image = parent.snapshot(snapshotParameters, null);

        File file = new File("src/src/imgs/"+name+".png");
        try {
            file.createNewFile();
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    public void setImage(String name, Parent parent) {
        File file=this.saveImageAsPng(name, parent);
        this.image= new Image(file.toURI().toString());
    }

    public Image getImage() {
        return image;
    }

    public void setBounds(int maxXParent, int maxYParent, ObservableList<Node> elements){
        int minX=maxXParent;
        int minY=maxYParent;
        int maxX=0;
        int maxY=0;
        int currentX;
        int currentY;
        for(Node node: elements){
            if(node instanceof Component || node instanceof Line ||node instanceof Tag) {
                currentX = (int) node.getBoundsInParent().getMinX();
                currentY = (int) node.getBoundsInParent().getMinY();
                if (currentX < minX) {
                    minX = currentX;
                }
                if (currentY < minY) {
                    minY = currentY;
                }
                if(node instanceof Tag) {
                    currentX += (int) ((Tag)node).getWidth();
                    currentY += (int) ((Tag)node).getHeight();
                }else{
                    currentX += (int) node.getLayoutBounds().getWidth();
                    currentY += (int) node.getLayoutBounds().getHeight();

                }
                if (currentX > maxX) {
                    maxX = currentX;
                }
                if (currentY > maxY) {
                    maxY = currentY;
                }
            }
        }
        this.bounds= new Rectangle2D(minX,minY,maxX-minX,maxY-minY);
    }

    public List<NodoComponent> getOutputs() {
        return cloneClone.getOutputComponents();
    }
    public SuperTree getClone() {
        return clone;
    }

    public ObservableList<Node> getElements() {
        return elementsClone;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

}
