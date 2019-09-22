import SimpleList.List;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import Component.*;
import SuperList.*;
import Interfaz.*;
import Tags.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Esta clase represanta el controlador del archivo de interfaz.fxml
 * Se encarga de controlar las acciones del usuario al nivel mas superior
 */
public class ControladorFXML {

    /**
     *Panel de componentes
     */
    @FXML
    private VBox componentsVbox;
    /**
     *Panel del circuito
     */
    @FXML
    private AnchorPane circuitPane;

    /**
     *Boton de simular el circuito
     */
    @FXML
    private Button simulateBtn;
    /**
     *Boton de guardar el circuito
     */
    @FXML
    private Button saveBtn;
    /**
     *Boton de generar tabla de verdad
     */
    @FXML
    private Button tableBtn;
    /**
     *Lista de super componentes disponibles
     */
    private ArrayList<SuperComponent> superComponents;
    /**
     *Lista de nombres ilegales para los super componentes
     */
    private ArrayList<String> ilegalNames;

    /**
     *Coordenada relativa en x del Drag
     */
    private double xOffSetDrag;
    /**
     *Coordenada relativa en y del Drag
     */
    private double yOffSetDrag;

    /**
     * Constructor de la clase
     */
    public ControladorFXML(){

    }

    /**
     * Metodo para inicializar los botones y el panel de componentes
     */
    @FXML
    public void initialize(){
        ObservableList<Node> components=componentsVbox.getChildren();
        ilegalNames=new ArrayList<>();
        superComponents=new ArrayList<>();
        for(Node component: components){
            setUpDrag(component);
            ilegalNames.add(component.getId());
        }
        simulateBtn.setOnAction(this::handleSimulateButtonAction);
        saveBtn.setOnAction(this::handleSaveButtonAction);
        tableBtn.setOnAction(this::handleTableButtonAction);
        setUpDrop();
    }

    /**
     * Metodo para manejar las acciones del boton de guardar
     * Se guarda el component en pantalla como un SuperComponente
     * @param event
     */
    private void handleSaveButtonAction(ActionEvent event) {
        SimpleList.List<Component> components=SuperTree.getInstance().getOutComponents();
        if(components.count()>0) {
            String name = Interfaz.inputDialog("Nombre", "Ingrese el nombre del componente");
            if (name!=null) {
                if(!name.equals("") && !ilegalNames.contains(name) ){
                    SuperComponent superComponent = this.createSuperComponent(name);
                    superComponents.add(superComponent);
                    componentsVbox.getChildren().add(superComponent);
                    VBox.setMargin(superComponent, new Insets(5, 0, 5, 0));
                    ilegalNames.add(name);
                }else{
                    Interfaz.popUp("Error", "Debe digitarse un nombre adecuado");
                }
            }
        }else{
            Interfaz.popUp("Error","Deben de haber componentes para guardar");
        }
    }

    /**
     * Metodo para manejar las acciones del boton de generar tabla
     * Se calcula y muestra la tabla de verdad en pantalla
     * @param event
     */
    private void handleTableButtonAction(ActionEvent event){
        ArrayList<InputTag> tags=SuperTree.getInstance().getFreeInputTags();

        if(tags.size()>0 && tags.size()<20){
            String[][] table=SuperTree.getInstance().getTrueTable();
            Interfaz.showTable(table,"Tabla de Verdad");
        }else if(tags.size()>=20){
            Interfaz.popUp("Error","No se pueden procesar tantos valores de entrada");
        }else{
            Interfaz.popUp("Error","Deben de haber componentes para simular");
        }
    }
    /**
     * Metodo para manejar las acciones del boton de simular
     * Se calculan los outputs y se muestran en pantalla
     * @param event
     */
    private void handleSimulateButtonAction(ActionEvent event)
    {
        SuperTree superTree = SuperTree.getInstance();
        ArrayList<InputTag> tags=superTree.getFreeInputTags();
        if(tags.size()>0) {
            try {


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
                if (values != null) {
                    superTree.calculateOutput();
                    List<Component> componentList = superTree.getOutComponents();
                    int outputsNumber = componentList.count();

                    String[][] results = new String[2][tags.size() + outputsNumber];

                    for (int i = 0; i < tags.size(); i++) {
                        InputTag tag = tags.get(i);
                        String result = "0";
                        if (values[i]) {
                            result = "1";
                        }
                        results[0][i] = tag.getId();
                        results[1][i] = result;
                    }

                    for (int i = tags.size(); i < tags.size() + outputsNumber; i++) {
                        Component component = componentList.get(i - tags.size());
                        String result = "0";
                        if (component.getOutput()) {
                            result = "1";
                        }
                        results[0][i] = component.getOutputTag().getId();
                        results[1][i] = result;

                    }
                    Interfaz.showTable(results, "Resultado");
                }
            }catch (NoSuchElementException e){}
        }else{
            Interfaz.popUp("Error","Deben de haber componentes para simular");
        }
    }

    /**
     * Metodo para establecer el drag en cada uno de los componentes
     * @param source Nodo al cual se le establece el drag
     */
    private void setUpDrag(Node source){
        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Dragboard db = source.startDragAndDrop(TransferMode.COPY);

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
                if (event.getTransferMode() == TransferMode.MOVE) {
                }
                event.consume();
            }
        });
    }

    /**
     * Metodo para establecer el drop en el circuitPane
     */
    private void setUpDrop() {
        circuitPane.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getGestureSource() != circuitPane &&
                        event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.COPY);
                }

                event.consume();
            }
        });

        circuitPane.setOnDragEntered(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getGestureSource() != circuitPane &&
                        event.getDragboard().hasString()) {
                }

                event.consume();
            }
        });

        circuitPane.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                event.consume();
            }
        });

        circuitPane.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
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
                event.setDropCompleted(success);

                event.consume();
            }
        });

    }

   /**
     * Busca el super componente en la lista de super componentes
     * @param name Nombre del super componente
     * @return SuperComponent encontrado
     */
    private SuperComponent getSuperComponent(String name){
        for(SuperComponent superComponent:superComponents){
            if(superComponent.getText().equals(name)){
                return superComponent;
            }
        }
        return null;
    }

    /**
     * Crea y devuelve un super componente basado en el circuito en el circuitPane
     * @param name Nombre del super componente
     * @return SuperComponent generado
     */
    private SuperComponent createSuperComponent(String name){
        SuperComponent superComponent= new SuperComponent(name, circuitPane);
        this.setUpDrag(superComponent);
        return superComponent;
    }

    /**
     * Agrega un super componente al circuitPane
     * @param superComponent Super componente que se desea agregar
     * @param cliqCoords Coordenadas donde se hizo el drop en el circuitPane
     */
    private void addSuperComponent(SuperComponent superComponent, Point cliqCoords){
        int centerX = (int)superComponent.getBounds().getWidth()/2;
        int centerY = (int)superComponent.getBounds().getHeight()/2;

        superComponent.generate(circuitPane, new Point((int)cliqCoords.getX()-centerX,(int)cliqCoords.getY()-centerY));

    }




}
