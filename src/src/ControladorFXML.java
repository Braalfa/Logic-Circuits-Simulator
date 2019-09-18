import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ControladorFXML {

    @FXML
    private VBox componentsVbox;
    @FXML
    private AnchorPane circuitPane;

    @FXML
    private Button simulateBtn;
    @FXML
    private Button saveBtn;

    private ImageView componentSelected;
    private ArrayList<SuperComponent> superComponents;

    private ArrayList<String> ilegalNames;

    private double xOffSetDrag;
    private double yOffSetDrag;

    private double componentOrgX;
    private double componentOrgY;

    public ControladorFXML(){

    }

    @FXML
    public void initialize(){
        ObservableList<Node> components=componentsVbox.getChildren();
        ilegalNames=new ArrayList<>();
        superComponents=new ArrayList<>();
        for(Node component: components){
            setUpDrag(component);
            ilegalNames.add(component.getId());
        }
        simulateBtn.setOnAction(this::handleButtonAction);
        saveBtn.setOnAction(this::handleSaveButtonAction);
        setUpDrop();
    }

    private void handleSaveButtonAction(ActionEvent event) {
        String name= Interfaz.inputDialog("Nombre", "Ingrese el nombre del componente");
        if(name!=null && !ilegalNames.contains(name)){
            SuperComponent superComponent=this.createSuperComponent(name);
            superComponents.add(superComponent);
            componentsVbox.getChildren().add(superComponent);
            ilegalNames.add(name);
        }else{
            Interfaz.popUp("Error","Debe digitarse un nombre adecuado" );
        }
    }

    private void handleButtonAction(ActionEvent event)
    {
        SuperTree superTree = SuperTree.getInstance();
        ArrayList<InputTag> tags=superTree.getFreeInputTags();
        if(tags.size()>0) {
            boolean[] values = Interfaz.getInputs(tags);
            System.out.print(tags);
            if (values == null) {
                Interfaz.popUp("Error en la entrada", "Los valores ingresados no son correctos\n Ingrese solo 1 o 0");
            } else {
                for (int i = 0; i < tags.size(); i++) {
                    InputTag tag = tags.get(i);
                    if (tag.getInputNumber() == 1) {
                        tag.getComponent().setInput1(values[i]);
                    } else {
                        tag.getComponent().setInput2(values[i]);
                    }
                }
            }
            if(values!=null) {
                superTree.calculateOutput();
                List<Component> componentList = superTree.getOutComponents();
                String results = "";
                for (int i = 0; i < componentList.count(); i++) {
                    Component component = componentList.get(i);
                    int result=0;
                    if(component.getOutput()){
                        result=1;
                    }
                    results += String.format("%1$-30s%2$-30s\n", component.getOutputTag().getId() + ":", result + "");
                }
                String inputs="";
                for (int i = 0; i < tags.size(); i++) {
                    InputTag tag=tags.get(i);
                    int result=0;
                    if(values[i]){
                        result=1;
                    }
                    inputs += String.format("%1$-30s%2$-30s\n", tag.getId() + ":", result + "");
                }
                Interfaz.popUp("Resultados", "Los resultados son:\n" + results+"\nLas entradas son:\n"+inputs);
            }
        }
    }
    public void handleClickComponents(ImageView source) {
        source.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(source.getOpacity()==1.0){
                    source.setOpacity(0.6) ;
                    componentSelected= source;
                }else{
                    source.setOpacity(1) ;
                    componentSelected= null;
                }
                event.consume();
            }
        });
    }
    public void setUpDrag(Node source){
        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start a drag-and-drop gesture*/
                /* allow any transfer mode */
                Dragboard db = source.startDragAndDrop(TransferMode.COPY);

                /* Put a string on a dragboard */
                ClipboardContent content = new ClipboardContent();
                if(source instanceof ImageView) {
                    content.putString("<C>"+source.getId());
                    db.setDragView(((ImageView) source).getImage());
                    db.setDragViewOffsetX(event.getX());
                    db.setDragViewOffsetY(event.getY());

                }else{
                    content.putString("<S>"+((SuperComponent)source).getText());
                    db.setDragView(((SuperComponent) source).getImage());
                    db.setDragViewOffsetX(((SuperComponent) source).getBounds().getWidth()/2);
                    db.setDragViewOffsetY(((SuperComponent) source).getBounds().getHeight()/2);

                }
                db.setContent(content);

                xOffSetDrag= event.getX();
                yOffSetDrag= event.getY();
                event.consume();
            }
        });

        source.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag and drop gesture ended */
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                }
                event.consume();
            }
        });
    }

    public void setUpDrop() {
        circuitPane.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                /* accept it only if it is not dragged from the same node
                 * and if it has a string data */
                if (event.getGestureSource() != circuitPane &&
                        event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY);
                }

                event.consume();
            }
        });

        circuitPane.setOnDragEntered(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != circuitPane &&
                        event.getDragboard().hasString()) {
                }

                event.consume();
            }
        });

        circuitPane.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */

                event.consume();
            }
        });

        circuitPane.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    String transferText= (String)db.getContent(DataFormat.PLAIN_TEXT);
                    if(transferText.substring(0,3).equals("<C>")) {
                        String id=transferText.substring(3);
                        double xPos = event.getX();
                        double yPos = event.getY();
                        id = id.toUpperCase();

                        Component component = Component_Factory.getComponent(ComponentType.valueOf(id));
                        circuitPane.getChildren().add(component);
                        component.setX(xPos - xOffSetDrag);
                        component.setY(yPos - yOffSetDrag);

                        SuperTree.getInstance().add(component);
                        component.setUpLabels();
                        success = true;
                    }else{
                        addSuperComponent(getSuperComponent(transferText.substring(3)), new Point((int)event.getX(),(int)event.getY()));
                        success=true;
                    }
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });

    }

    public SuperComponent getSuperComponent(String name){
        for(SuperComponent superComponent:superComponents){
            if(superComponent.getText().equals(name)){
                return superComponent;
            }
        }
        return null;
    }


    public SuperComponent createSuperComponent(String name){
        SuperComponent superComponent= new SuperComponent(name, circuitPane);
        this.setUpDrag(superComponent);
        return superComponent;
    }

    public void addSuperComponent(SuperComponent superComponent, Point cliqCoords){
        int centerX = (int)superComponent.getBounds().getWidth()/2;
        int centerY = (int)superComponent.getBounds().getHeight()/2;
        superComponent.generate(circuitPane, new Point((int)cliqCoords.getX()-centerX,(int)cliqCoords.getY()-centerY));

    }




}
