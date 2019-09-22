package Tags;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import SuperList.*;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Random;
import java.awt.Color;
import Component.Component;

/**
 * La clase Tag representa Labels en los componentes, disenados para conectarse entre si y generar lineas, ademas de indicarle al resto
 * del programa que se conectaron.
 * Esta clase es abstracta
 */
public abstract class Tag extends Label {
    /**
     *Coordenadas originales del Tag en el componente
     */
    private Point nodCoords;
    /**
     *Coordenadas actuales del Tag en el componente
     */
    protected Point coords;
    /**
     *AnchorPane en el cual se encuentra el Tag
     */
    private AnchorPane parent;
    /**
     *ArrayList de lineas del Tag
     */
    private ArrayList<Line> lines;
    /**
     *Componente padre del Tag
     */
    protected Component component;
    /**
     *Posicion relativa en x del click antes de arrastrar
     */
    private double relClickX;
    /**
     *Posicion relativa en y del click antes de arrastrar
     */
    private double relClickY;
    /**
     * ArrayList de tags con los cuales se conecta
     */
    protected ArrayList<Tag> nextTag;
    /**
     * SuperTree
     */
    protected SuperTree superTree;
    /**
     * Color de las lineas
     */
    private Color linesColor;

    /**
     * Agrega un Tag a la lista de Tags siguientes
     * @param nextTag Tag al que se conecta
     */
    abstract protected void addNextTag(Tag nextTag);

    /**
     * Consrtructor
     * @param parent AnchorPane que contiene al tag
     * @param nodCoords Coordenadas del tag relativas a su componente padre
     * @param component Componente padre
     */
    public Tag(AnchorPane parent, Point nodCoords, Component component){
        this.coords=new Point((int)nodCoords.getX(),(int)nodCoords.getY());
        this.nodCoords=nodCoords;
        this.parent=parent;
        this.component=component;
        this.lines = new ArrayList<>();
        this.setMovementListener();
        this.nextTag=new ArrayList<>();
        this.superTree= SuperTree.getInstance();

        Random random=new Random();
        this.linesColor=new Color(random.nextFloat(),random.nextFloat(),random.nextFloat());
    }

    /**
     * Le da formato al tag
     * @param text Texto que tendra el tag
     */
    protected void display(String text){
        this.setText(text);
        this.setFont(new Font("Facade",10));
        this.setStyle("-fx-background-color: #EFEFEF");
        parent.getChildren().add(this);
        parent.applyCss();
        parent.layout();
    }

    /**
     * Se establecen los listeners de movimiento para el tag
     */
    private void setMovementListener(){
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /*
                    Si se presiona con el mouse izquierdo se prepara para arrastrar
                    Si se presiona con el mouse derecho el tag vuelve a su posicion inicial
                 */
                Tag source = (Tag) event.getSource();
                if(event.getButton()==MouseButton.PRIMARY) {
                    ScrollPane scrollpane = (ScrollPane) component.getScene().lookup("#scrollPane");
                    relClickX = -source.getLayoutX() + event.getSceneX()+scrollpane.getHvalue()*(parent.getWidth()-scrollpane.getViewportBounds().getWidth());
                    relClickY = -source.getLayoutY() + event.getSceneY()+scrollpane.getVvalue()*(parent.getHeight()-scrollpane.getViewportBounds().getHeight());
                    event.consume();
                    Line line = new Line();
                    if (lines.isEmpty()) {
                        double xpos = source.getNodCords().getX() + source.getComponent().getX();
                        double ypos = source.getNodCords().getY() + source.getComponent().getY();
                        line.setStartX(xpos);
                        line.setStartY(ypos);
                        line.setEndX(xpos);
                        line.setEndY(ypos);
                    } else {
                        Line lastLine = lines.get(lines.size() - 1);
                        line.setStartX(lastLine.getEndX());
                        line.setEndX(lastLine.getEndX());
                        line.setStartY(lastLine.getEndY());
                        line.setEndY(lastLine.getEndY());
                    }
                    source.addLine(line);
                    parent.getChildren().add(line);

                }else if (event.getButton()==MouseButton.SECONDARY){
                    source.goBackHome();
                    source.clearLines();
                }
            }
        });
        this.setOnMouseDragged( new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /*
                Se actualiza la posicion del tag al arrastrarlo
                 */
                Tag source= (Tag)event.getSource();
                ScrollPane scrollpane = (ScrollPane) component.getScene().lookup("#scrollPane");
                source.setLayoutX(event.getSceneX()+scrollpane.getHvalue()*(parent.getWidth()-scrollpane.getViewportBounds().getWidth())-relClickX);
                source.setLayoutY(event.getSceneY()+scrollpane.getVvalue()*(parent.getHeight()-scrollpane.getViewportBounds().getHeight())-relClickY);
                if(lines.size()>=1) {
                    Line thisLine = lines.get(lines.size() - 1);
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
                        } else {
                            thisLine.setEndY(thisLine.getStartY());
                            thisLine.setEndX(thisLine.getStartX());
                        }
                    } else {
                        thisLine.setEndY(thisLine.getStartY());
                        thisLine.setEndX(thisLine.getStartX());
                        thisLine.setVisible(false);

                    }
                }
                event.consume();

            }
        });
        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    /*
                    Si el click es con el boton izquierdo se mueve el tag
                     */
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
                        if ((((overlap instanceof InputTag && !overlap.hasNextTag() && !superTree.willFeedback(overlap.getComponent(),source.getComponent()))
                                || (overlap instanceof OutputTag && !superTree.willFeedback(source.getComponent(),overlap.getComponent())))
                                && overlap.getComponent() != source.getComponent()) ){
                            ConnectorSingleton connector = ConnectorSingleton.getInstance(source, overlap);
                            boolean result = connector.autoConnect();
                            if (!result) {
                                source.uptadeDisplacedTag(thisLine);
                            } else {
                                source.addNextTag(overlap);
                                overlap.addNextTag(source);
                                source.setVisible(false);
                                overlap.setVisible(false);
                                if(source instanceof InputTag){
                                    overlap.setLinesColor(source.getLinesColor());
                                    source.getLines().addAll(overlap.getLines());
                                    overlap.resetLines();
                                }else{
                                    source.setLinesColor(overlap.getLinesColor());
                                    overlap.getLines().addAll(source.getLines());
                                    source.resetLines();
                                }
                                overlap.goBackHome();
                                source.goBackHome();
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
            }
        });
    }

    /**
     * Actualiza la posicion del tag luego de que se arrastrara
     * @param thisLine Linea creada en el arrastre
     */
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

    /**
     * Devuelve el tag a su posicion original
     */
    public void goBackHome() {
        this.setLayoutX(coords.getX() + component.getX());
        this.setLayoutY(coords.getY() + component.getY());
    }

    /**
     * Desconecta el tag por completo y lo devuelve a su posicion original
     */
    public void disconect(){
        this.clearAllNeighboors();
        this.clearLines();
        this.setLinesColor(this.getLinesColor());
        this.setLayoutX(coords.getX() + component.getX());
        this.setLayoutY(coords.getY() + component.getY());
    }

    /**
     * Averigua si hay un tag en overlap con si mismo y lo retorna
     * @return Tag en overlap con si mismo
     */
    public Tag getOverlap(){
        ObservableList<Node> nodos=parent.getChildren();
        Tag overlap=null;
        for(Node nodo:nodos){
            if(nodo instanceof Tag && !nodo.getClass().equals(this.getClass()) && (nodo.isVisible() || nodo instanceof OutputTag)){
                if(nodo.getBoundsInParent().intersects(this.getBoundsInParent())){
                    overlap=(Tag)nodo;
                    break;
                }
            }
        }
        return overlap;
    }

    /**
     * Retorna las coordenadas originales del tag en el padre
     * @return Las coordenadas originales del tag en el padre
     */
    private Point getNodCords(){
        return this.nodCoords;
    }

    /**
     * Retorna el componente padre del tag
     * @return Componente padre del tag
     */
    public Component getComponent(){
        return this.component;
    }

    /**
     * Elimina la ultima linea creada
     */
    public void removeLastLine(){
        Line line= lines.get(lines.size()-1);
        parent.getChildren().remove(line);
        lines.remove(line);
    }

    /**
     * Retorna el punto de inicio de la ultima linea creada
     * @return Punto de inicio de ultima linea creada
     */
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

    /**
     * Retorna el punto final de la ultima linea creada
     * @return Punto final de ultima linea creada
     */
    public Point getLastLineEndPoint(){
        if(this.lines.isEmpty() || this.hasNextTag()){
            return new Point((int)(nodCoords.getX()+component.getX()),(int)(nodCoords.getY()+component.getY()));
        }else{
            Line lastLine= lines.get(lines.size()-1);
            return new Point((int)lastLine.getEndX(),(int)lastLine.getEndY());
        }
    }

    /**
     * Retorna un booleano que indica si el tag esta conectado
     * @return Booleano que indica si esta conectado
     */
    public boolean hasNextTag(){
        if (!this.nextTag.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Retorna las lineas
     * @return Las lineas del tag
     */
    public ArrayList<Line> getLines() {
        return lines;
    }

    /**
     * Retorna la lista de tags conectados
     * @return Lista de tags conectados
     */
    public ArrayList<Tag> getNextTag() {
        return nextTag;
    }

    /**
     * Hace que todos los vecinos vallan a su casa (todos los tags conectados a simismo vuelvan a su punto de origen)
     */
    public void clearAllNeighboors(){
        if(this.hasNextTag()){
            for (Tag tag : nextTag) {
                tag.goBackHome();
                if(tag.getNextTag().size()==1){
                    tag.clearLines();
                    tag.setVisible(true);

                }
                tag.getNextTag().remove(this);

            }
            this.nextTag=new ArrayList<>();
        }
        this.setVisible(true);
    }

    /**
     * Elimina todas las lineas
     */
    public void clearLines(){
        for(Line line:this.lines){
            parent.getChildren().remove(line);
        }
        lines=new ArrayList<>();
    }

    /**
     * Averigua si una linea choca con algun elemento en el AnchorPane
     * @param line linea para averiguar si hay choque
     * @return Booleano que indica si hay choque
     */
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

    /**
     * Metodo para destruir el tag
     */
    public void destroy(){
        this.disconect();
        parent.getChildren().remove(this);
    }

    /**
     * Actualiza el color en todas las lineas
     * @param color Color para actualizar las lineas
     */
    public void setLinesColor(Color color){
        int r=color.getRed();
        int g=color.getGreen();
        int b=color.getBlue();
        for(Line line: this.lines){
            this.setLineColor(line,r,g,b);
        }
    }

    /**
     * Metodo para cambiar el color de una linea
     * @param line Linea a la cual se le desea cambiar el color
     * @param r Nivel de rojo
     * @param g Nivel de verde
     * @param b Nivel del azul
     */
    private void setLineColor(Line line, int r, int g, int b){
        line.setStyle("-fx-stroke:  rgb(" + r + "," + g + ", " + b + ");");
    }

    /**
     * Metodo para agregar una linea al tag
     * @param line Linea que se desea agregar
     */
    public void addLine(Line line){
        line.setVisible(true);
        this.getLines().add(line);
        this.setLineColor(line);
    }

    /**
     * Metodo para cambiar el color de una linea, de acuerdo al color intrinsico del tag
     * @param line Linea a la cual se le desea cambiar el color
     */
    private void setLineColor(Line line){
        int r=this.getLinesColor().getRed();
        int g=this.getLinesColor().getGreen();
        int b=this.getLinesColor().getBlue();
        line.setStyle("-fx-stroke:  rgb(" + r + "," + g + ", " + b + ");");
    }

    /**
     * Retorna el color intrinsico de las lineas
     * @return Color intrinsico de las lineas
     */
    public Color getLinesColor(){
        return this.linesColor;
    }

    /**
     * Reinicia el array de lineas
     */
    private void resetLines() {
        this.lines = new ArrayList<>();
    }

    /**
     * Clona todas las lineas
     * @param diplacement Desplazamiento en el AnchorPane de las lineas cloes
     * @param parent AnchorPane en el cual estaran las lineas clones
     * @return ArrayList de las lineas clonadas
     */
    public ArrayList<Line> cloneLines(Point diplacement, AnchorPane parent){
        Line copyLine;
        ArrayList<Line> linesCopy=new ArrayList<>();
        for(Line line:lines){
            copyLine=new Line(line.getStartX()+diplacement.getX(),line.getStartY()+diplacement.getY(),line.getEndX()+diplacement.getX(),line.getEndY()+diplacement.getY());
            linesCopy.add(copyLine);
            parent.getChildren().add(copyLine);
        }
        return linesCopy;
    }
    /**
     * Establce el ArrayList de lineas
     * @param lines Nuevo ArrayList de lineas
     */
    public void setLines(ArrayList<Line> lines){
        this.lines=lines;
    }

}
