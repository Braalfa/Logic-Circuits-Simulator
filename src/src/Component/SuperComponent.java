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
import javax.imageio.ImageIO;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

/**
 * Esta clase se encarga de contener los componentes personalizados, guardados por el usuario
 */
public class SuperComponent extends Label {
    private SuperTree clone;
    private ObservableList<Node> elements;
    private Image image;
    private Rectangle2D bounds;

    private ObservableList<Node> elementsClone;
    private SuperTree cloneClone;

    /**
     * Constructor de SuperComponent
     * @param name Nombre del super componente
     * @param parent Parent en donde se encuentra el super componente
     */
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

    /**
     * Este método genera una copia de si mismo, para entregarla a quien necesite
     * @param parent AnchorPane donde se encontrara el super componente
     * @param diplacement Desplazamiento del super componente en su parent
     */
    public void generate(AnchorPane parent, Point diplacement){
        this.cloneClone= clone.clone(parent, diplacement);
        this.elementsClone=parent.getChildren();
        SuperTree.getInstance().addOutputNodes(this.getOutputs());

    }


    /**
     * Este método establece el inicio de coordenadas del super componente en su parte superior izquierda
     */
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

    /**
     * Este método genera y guarda la imagen del supercomponente en memoria
     * @param name Nombre del super componente
     * @param parent Padre del super componente
     * @return Archivo de imagen
     */
    private  File saveImageAsPng(String name, Parent parent) {
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

    /**
     * Este método establece la imagen del super componente
     * @param name Nombre del super componente
     * @param parent Padre del super componente
     */
    private void setImage(String name, Parent parent) {
        File file=this.saveImageAsPng(name, parent);
        this.image= new Image(file.toURI().toString());
    }

    /**
     * Retorna la imagen del super componente
     * @return Imagen del super componente
     */
    public Image getImage() {
        return image;
    }

    /**
     * Establece las coordenadas del rectángulo que comparte coordenadas con el super componente
     * @param maxXParent Altura del padre
     * @param maxYParent Ancho del padre
     * @param elements Nodos del super componente
     */
    private void setBounds(int maxXParent, int maxYParent, ObservableList<Node> elements){
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

    /**
     * Se retorna la lista de NodoComponentes sin output de la última copia del super componente guardado
     * @return Lista de NodoComponentes sin salida del super componente
     */
    public List<NodoComponent> getOutputs() {
        return cloneClone.getOutputComponents();
    }

    /**
     * Se retornan los limites del rectangulo en donde se encontraba el super componente
     * @return Rectangulo con los limites del super componente
     */
    public Rectangle2D getBounds() {
        return bounds;
    }

}
