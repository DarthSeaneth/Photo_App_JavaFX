/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package photos.group4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photos.group4.controller.LoginController;

import java.io.File;
import java.io.IOException;

/**
 * Main Class to run Photos Application.
 */
public class Photos extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Photos.class.getResource("Login.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            LoginController loginController = loader.getController();
            Scene scene = new Scene(root);
            Image appIcon = new Image(new File("data/Miscellaneous/appIcon.jpg").toURI().toString());
            stage.getIcons().add(appIcon);
            stage.setTitle("Photos Application");
            stage.setScene(scene);
            stage.setResizable(false);
            loginController.start(stage);
            stage.show();
        }
        catch(Exception ignored){}

    }

    /**
     * The entry point of Photos application.
     * @param args input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}