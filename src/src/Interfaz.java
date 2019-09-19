import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.w3c.dom.Text;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Interfaz extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("interfaz.fxml"));
        Scene scene = new Scene(root, 711, 458);
        primaryStage.getIcons().add(new Image("imgs/icon1.png") );
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());

    }

    public static void popUp(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("imgs/icon3.png") );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(message);

        alert.showAndWait();

    }
    public static String inputDialog(String title, String message)
    {

        TextInputDialog dialog= new TextInputDialog();
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("imgs/icon1.png") );

        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        Optional<String> result= dialog.showAndWait();
        if(result.isPresent()){
            return result.get();
        }else{
            return null;
        }
    }
    public static boolean[] getInputs(ArrayList<InputTag> tags){
        Dialog<ArrayList<String>> dialog = new Dialog<>();
        dialog.setTitle("Valores iniciales");
        dialog.setHeaderText("Ingrese los valores iniciales");
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("imgs/icon1.png") );

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK");
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 10));

        ArrayList<TextField> textFields=new ArrayList<>();
        ArrayList<Label> labels=new ArrayList<>();
        Label label;
        TextField textField;
        for(int i=0;i<tags.size();i++){
            label=new Label("     "+tags.get(i).getId());
            labels.add(label);
            gridPane.add(label,0,i);
            textField=new TextField();
            gridPane.add(textField,1,i);
            textFields.add(textField);
        }

        dialog.getDialogPane().setContent(gridPane);
        dialog.showAndWait();
        boolean[] results=new boolean[textFields.size()];
        for(int i=0;i<textFields.size();i++){
            String text=textFields.get(i).getText();
            if(text.equals("1")){
                results[i]=true;
            }else if(text.equals("0")) {
                results[i]=false;
            }else{
                return null;
            }
        }
        return results;
    }
    public static void showTable(String[][] table, String title){
        Dialog<TableView<String>> dialog = new Dialog<>();
        dialog.setTitle(title);
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("imgs/icon2.png") );

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK");
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);
        ScrollPane scrollPane = new ScrollPane();

        TableView<String[]> tableView= new TableView<>();
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(table));
        data.remove(0);
        for (int i = 0; i < table[0].length; i++) {
            TableColumn column = new TableColumn(table[0][i]);
            final int columnNumber = i;
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[columnNumber]));
                }
            });
            tableView.getColumns().add(column);
        }
        int rowHeight=30;
        int headerHeight= 10;
        int margin=10;
        tableView.setItems(data);
        tableView.prefHeightProperty().bind(Bindings.max(2, Bindings.size(tableView.getItems()))
                .multiply(rowHeight)
                .add(headerHeight)
                .add(margin));
        tableView.minHeightProperty().bind(tableView.prefHeightProperty());
        tableView.maxHeightProperty().bind(tableView.prefHeightProperty());

        tableView.prefWidthProperty().bind(Bindings.max(2, Bindings.size(tableView.getColumns()))
                .multiply(50)
                .add(headerHeight)
                .add(margin));
        tableView.maxWidthProperty().bind(tableView.prefWidthProperty());
        tableView.minWidthProperty().bind(tableView.prefWidthProperty());


        scrollPane.setContent(tableView);
        dialog.setHeight(500);
        dialog.setWidth(300);
        dialog.getDialogPane().setContent(scrollPane);
        dialog.showAndWait();
    }

}
