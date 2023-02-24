/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package photos.group4.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Interface to allow re-use of different types of Alert messages
 * All Controller Classes will implement
 */
public interface Alerts {
    /**
     * Shows Error Alert message
     * @param errorMessage String message to display
     */
    default void showAlertError(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    /**
     * Shows Confirmation Alert message
     * @param confirmationMessage String message to display
     * @return Optional of type ButtonType> Object
     */
    default Optional<ButtonType> showAlertConfirmation(String confirmationMessage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText(confirmationMessage);
        return alert.showAndWait();
    }
}
