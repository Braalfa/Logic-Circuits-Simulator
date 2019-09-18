import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

public class Interfaz extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("interfaz.fxml"));
        Scene scene = new Scene(root, 711, 458);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());

    }

    public static void popUp(String title, String message)
    {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();

    }
    public static String inputDialog(String title, String message)
    {

        TextInputDialog dialog= new TextInputDialog();

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
}
