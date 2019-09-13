import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;

public class ControladorFXML {

    @FXML
    private VBox componentsVbox;
    @FXML
    private AnchorPane circuitPane;

    @FXML
    private Button simulateBtn;
    private ImageView componentSelected;

    private double xOffSetDrag;
    private double yOffSetDrag;

    private double componentOrgX;
    private double componentOrgY;

    public ControladorFXML(){

    }

    @FXML
    public void initialize(){
        ObservableList<Node> components=componentsVbox.getChildren();

        for(Node component: components){
            setUpDrag((ImageView) component);

        }
        simulateBtn.setOnAction(this::handleButtonAction);
        setUpDrop();
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
    public void setUpDrag(ImageView source){
        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start a drag-and-drop gesture*/
                /* allow any transfer mode */
                Dragboard db = source.startDragAndDrop(TransferMode.COPY);

                /* Put a string on a dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putString(source.getId());
                db.setContent(content);
                db.setDragView(source.getImage());
                db.setDragViewOffsetX(event.getX());
                db.setDragViewOffsetY(event.getY());

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
                    String id= (String)db.getContent(DataFormat.PLAIN_TEXT);
                    double xPos=event.getX();
                    double yPos=event.getY();
                    id = id.toUpperCase();

                    Component component = Component_Factory.getComponent(ComponentType.valueOf(id));
                    circuitPane.getChildren().add(component);
                    component.setX(xPos-xOffSetDrag);
                    component.setY(yPos-yOffSetDrag);

                    SuperTree.getInstance().add(component);
                    setMovementHandlers(component);
                    component.setUpLabels();
                    success=true;
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });

    }

    public void setMovementHandlers(Component component) {

        component.setOnMousePressed( new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    componentOrgX = -component.getX() + event.getX();
                    componentOrgY = -component.getY() +event.getY();
                }else if(event.getButton()==MouseButton.SECONDARY){
                    component.destroy();
                }
            }
        });
        component.setOnMouseDragged( new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    component.setX(event.getX()-componentOrgX);
                    component.setY(event.getY()-componentOrgY);
                    component.updateTagsPositions();
                }
            }
        });

    }
}
