package lk.ijse.busmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        scene = new Scene(loadFXML("login"));


        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F11) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
                event.consume();
            }
        });

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
        stage.setResizable(true);
        stage.setTitle("Bus Management System");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/lk/ijse/busmanagementsystem/assets/Gunasekara.jpg")));
        System.out.println("________________Bus Management System : Gunasekara Travels________________");
        System.out.println("                    Created By AMG(Praveena Gunasekara)      ");
    }

    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F12) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
                event.consume();
            }
        });
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/lk/ijse/busmanagementsystem/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
