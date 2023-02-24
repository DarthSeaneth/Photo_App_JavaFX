/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package photos.group4.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import photos.group4.Photos;
import photos.group4.model.UserList;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * JavaFX Controller Class to handle Login of Photos Application.
 */
public class LoginController implements Alerts {
    /**
     * JavaFX Label to display welcome message
     */
    @FXML
    private Label welcomeLabel;
    /**
     * JavaFX Button to handle Login action
     */
    @FXML
    private Button loginButton;
    /**
     * JavaFX TextField to enter username String
     */
    @FXML
    private TextField loginTextField;
    /**
     * JavaFX ImageView to display App Icon
     */
    @FXML
    private ImageView appIconImageView;
    /**
     * App Icon image file
     */
    Image appIcon = new Image(new File("data/Miscellaneous/appIcon.jpg").toURI().toString());

    /**
     * Displays the App Icon in the ImageView
     * @param stage Stage to execute method on
     */
    public void start(Stage stage){
        stage.setTitle("Photo Login");
        appIconImageView.setImage(appIcon);
    }

    /**
     * Handles Login action for Photos Application at the start
     * @param actionEvent specific ActionEvent that called Login function
     * @throws ClassNotFoundException exception specific to ActionEvent
     */
    @FXML
    protected void login(ActionEvent actionEvent) throws ClassNotFoundException{
        String username = loginTextField.getText();
        if(username.isEmpty()){
            showAlertError("Please enter valid username!");
            return;
        }
        UserList userList = null;
        try{
            UserList.readUserData();
            userList = UserList.getUserListInstance();
        }
        catch(IOException ignored){}
        if(userList == null){
            userList = UserList.getUserListInstance();
        }
        try{
            if(userList.doesUserExist(username)){
                Optional<ButtonType> result = showAlertConfirmation("Confirm Login");
                if(result.isEmpty() || result.get() == ButtonType.CANCEL){
                    return;
                }
                FXMLLoader loader = new FXMLLoader();
                if(username.equals("admin")){
                    loader.setLocation(Photos.class.getResource("AdminSubsystem.fxml"));
                    Parent root = (Parent) loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    AdminSubsystemController adminController = loader.getController();
                    adminController.start(stage);
                    stage.setScene(scene);
                    stage.show();
                }
                else{
                    loader.setLocation(Photos.class.getResource("NonAdminSubsystem.fxml"));
                    Parent root = (Parent) loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    NonAdminSubsystemController nonAdminController = loader.getController();
                    nonAdminController.setUserList(userList);
                    nonAdminController.setUser(userList.getUser(username));
                    nonAdminController.start(stage);
                    stage.setScene(scene);
                    stage.show();
                }
            }
            else{
                showAlertError("User does not exist. Please enter valid username!");
            }
        }
        catch(IOException ignored){}
    }
}