/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package photos.group4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import photos.group4.model.User;
import photos.group4.model.UserList;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JavaFX Controller Class to handle Admin Subsystem features of Photos Application
 */
public class AdminSubsystemController implements Logout, Alerts {
    /**
     * JavaFX Button to handle Logout action
     */
    @FXML
    private Button logoutButton;
    /**
     * JavaFX Button to handle addition of Users
     */
    @FXML
    private Button addUserButton;
    /**
     * JavaFX Button to handle deletion of Users
     */
    @FXML
    private Button deleteUserButton;
    /**
     * JavaFX TextField to enter username String
     */
    @FXML
    private TextField addUserTextField;
    /**
     * JavaFX ListView to show list of Users in GUI
     */
    @FXML
    private ListView<User> userListView;
    /**
     * ObservableList to populate ListView in GUI
     */
    private ObservableList<User> observableUserList;
    /**
     * List to populate ObservableList
     */
    private List<User> userList = new ArrayList<User>();
    /**
     * UserList field to reference the main UserList of the application
     */
    private UserList users;

    /**
     * Populates the ListView with the User list data
     * @param stage Stage execute start method on
     * @throws IOException exception specific to JavaFX
     */
    public void start(Stage stage) throws IOException {
        stage.setTitle("Admin Control");
        try {
            UserList.readUserData();
            users = UserList.getUserListInstance();
        }
        catch(ClassNotFoundException | EOFException e){
            users = UserList.getUserListInstance();
        }
        userList = users.getUserList();
        observableUserList = FXCollections.observableArrayList(userList);
        userListView.setItems(observableUserList);
    }

    /**
     * Adds User to the UserList and updates ListView
     * @param actionEvent specific ActionEvent that called function
     * @throws IOException exception specific to JavaFX ActionEvent
     */
    @FXML
    protected void addUser(ActionEvent actionEvent) throws IOException{
        if(addUserTextField.getText().isEmpty()){
            showAlertError("Please enter a valid username!");
            return;
        }
        if(users.doesUserExist(addUserTextField.getText())){
            showAlertError("User already exists!");
            return;
        }
        Optional<ButtonType> result = showAlertConfirmation("Are you sure you want to add this User?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        User newUser = new User(addUserTextField.getText());
        userList.add(newUser);
        observableUserList.add(newUser);
        userListView.setItems(observableUserList);
        addUserTextField.setText("");
        userListView.refresh();
        UserList.writeUserData(users);
    }

    /**
     * Deletes User from the UserList and updates the ListView
     * @param actionEvent specific ActionEvent that called function
     * @throws IOException exception specific to JavaFX ActionEvent
     */
    @FXML
    protected void deleteUser(ActionEvent actionEvent) throws IOException{
        int selectedUser = userListView.getSelectionModel().getSelectedIndex();
        if(selectedUser == -1){
            showAlertError("Please select a User to delete!");
            return;
        }
        if(selectedUser == 0){
            showAlertError("Admin User cannot be deleted!");
            return;
        }
        Optional<ButtonType> result = showAlertConfirmation("Are you sure you want to delete this User?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        User userToRemove = userList.get(selectedUser);
        users.removeUser(userToRemove);
        userList.remove(userToRemove);
        observableUserList.remove(userToRemove);
        userListView.setItems(observableUserList);
        userListView.refresh();
        UserList.writeUserData(users);
    }

    /**
     * Performs Logout function from the Admin Subsystem screen
     * @param actionEvent specific ActionEvent that called function
     * @throws ClassNotFoundException exception specific to JavaFX ActionEvent
     */
    @FXML
    protected void logoutFromAdminHome(ActionEvent actionEvent) throws ClassNotFoundException{
        Optional<ButtonType> result = showAlertConfirmation("Confirm Logout");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        logout(actionEvent);
    }
}
