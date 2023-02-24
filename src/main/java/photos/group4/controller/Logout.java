/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package photos.group4.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import photos.group4.Photos;

/**
 * Interface to allow re-use of Logout action
 * Most Controller Classes will implement
 */
public interface Logout {
    /**
     * Executes Logout function for Photos Application
     * @param actionEvent Specific ActionEvent that called Logout function
     * @throws ClassNotFoundException exception specific to ActionEvent
     */
    default void logout(ActionEvent actionEvent) throws ClassNotFoundException{
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Photos.class.getResource("Login.fxml"));
            Parent parent = loader.load();
            LoginController loginController = loader.getController();
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            loginController.start(stage);
            stage.show();
        }
        catch(Exception ignored){}
    }
}
