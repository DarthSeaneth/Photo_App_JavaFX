/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package photos.group4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import photos.group4.Photos;
import photos.group4.model.Album;
import photos.group4.model.User;
import photos.group4.model.UserList;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * JavaFX Controller Class to handle top-level Non-Admin Subsystem features of Photos Application
 */
public class NonAdminSubsystemController implements Logout, Alerts {
    /**
     * JavaFX Label to show welcome message
     */
    @FXML
    private Label welcomeLabel;
    /**
     * JavaFX Button to handle Logout action
     */
    @FXML
    private Button logoutButton;
    /**
     * JavaFX Button to handle adding Albums
     */
    @FXML
    private Button addAlbumButton;
    /**
     * JavaFX Button to handle deleting Albums
     */
    @FXML
    private Button deleteAlbumButton;
    /**
     * JavaFX Button to handle renaming Albums
     */
    @FXML
    private Button renameAlbumButton;
    /**
     * JavaFX Button to handle opening Albums
     */
    @FXML
    private Button openAlbumButton;
    /**
     * JavaFX TextField to enter Album name
     */
    @FXML
    private TextField albumNameTextField;
    /**
     * JavaFX Text to display Album information
     */
    @FXML
    private Text albumInfoText;
    /**
     * JavaFX ListView to display list of Albums
     */
    @FXML
    private ListView<Album> albumListView;
    /**
     * ObservableList to populate Album ListView
     */
    private ObservableList<Album> observableAlbumList;
    /**
     * List to populate Album Observable List
     */
    private List<Album> albumList;
    /**
     * Field to reference the main UserList instance
     */
    private UserList users;
    /**
     * Field to reference which User is accessing the Non-Admin Subsystem
     */
    private User user;

    /**
     * Sets welcome message and populates ListView with the User's Album info
     * @param stage Stage to execute method on
     */
    public void start(Stage stage){
        stage.setTitle("Album Display for " + user.getUserName());
        welcomeLabel.setText("Welcome " + user.getUserName() + "!");
        albumList = user.getAlbumList();
        observableAlbumList = FXCollections.observableArrayList(albumList);
        albumListView.setItems(observableAlbumList);
        showAlbumInfo();
    }

    /**
     * Sets the UserList instance to the main UserList instance
     * @param users UserList instance to set
     */
    public void setUserList(UserList users){
        this.users = users;
    }

    /**
     * Sets User accessing the subsystem
     * @param user the User to set
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Adds Album to the User's list of Albums and updates ListView
     * @throws IOException exception specific to JavaFX ActionEvent
     */
    @FXML
    protected void addAlbum() throws IOException{
        if(albumNameTextField.getText().isEmpty()){
            showAlertError("Please enter a valid Album name!");
            return;
        }
        for(Album album: albumList){
            if(album.getAlbumTitle().equals(albumNameTextField.getText())){
                showAlertError("Album already exists!");
                return;
            }
        }
        Optional<ButtonType> result = showAlertConfirmation("Are you sure you want to add this Album?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        Album newAlbum = new Album(albumNameTextField.getText());
        observableAlbumList.add(newAlbum);
        albumList.add(newAlbum);
        albumListView.setItems(observableAlbumList);
        albumNameTextField.setText("");
        albumListView.refresh();
        UserList.writeUserData(users);
        showAlbumInfo();
    }

    /**
     * Deletes Album from User's list of Albums and updates ListView
     * @throws IOException exception specific to JavaFX ActionEvent
     */
    @FXML
    protected void deleteAlbum() throws IOException{
        int selectedAlbum = albumListView.getSelectionModel().getSelectedIndex();
        if(selectedAlbum == -1){
            showAlertError("Please select an Album to delete!");
            return;
        }
        Optional<ButtonType> result = showAlertConfirmation("Are you sure you want to delete this Album?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        Album albumToRemove = albumList.get(selectedAlbum);
        user.removeAlbum(albumToRemove);
        albumList.remove(albumToRemove);
        observableAlbumList.remove(albumToRemove);
        albumListView.setItems(observableAlbumList);
        albumListView.refresh();
        UserList.writeUserData(users);
        showAlbumInfo();
    }

    /**
     * Renames specific Album and updates ListView
     * @throws IOException exception specific to JavaFX ActionEvent
     */
    @FXML
    protected void renameAlbum() throws IOException {
        int selectedAlbum = albumListView.getSelectionModel().getSelectedIndex();
        if(selectedAlbum == -1){
            showAlertError("Please select an Album to rename!");
            return;
        }
        if(albumNameTextField.getText().isEmpty()){
            showAlertError("Please enter a valid Album name!");
            return;
        }
        for(Album album: albumList){
            if(album.getAlbumTitle().equals(albumNameTextField.getText())){
                showAlertError("Album already exists!");
                return;
            }
        }
        Optional<ButtonType> result = showAlertConfirmation("Are you sure you want to rename this Album?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        Album albumToRename = albumList.get(selectedAlbum);
        albumToRename.setAlbumTitle(albumNameTextField.getText());
        albumListView.refresh();
        UserList.writeUserData(users);
        albumNameTextField.setText("");
        showAlbumInfo();
    }

    /**
     * Opens the selected Album (Changes JavaFX scene)
     * @param actionEvent specific ActionEvent that called function
     */
    @FXML
    protected void openAlbum(ActionEvent actionEvent){
        if(albumListView.getSelectionModel().getSelectedIndex() == -1){
            showAlertError("Please select an Album to open!");
            return;
        }
        Optional<ButtonType> result = showAlertConfirmation("Open selected Album?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Photos.class.getResource("SelectedAlbum.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            SelectedAlbumController albumController = loader.getController();
            albumController.setUserList(users);
            albumController.setUser(user);
            albumController.setAlbum(albumList.get(albumListView.getSelectionModel().getSelectedIndex()));
            albumController.start(stage);
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException ignored){}
    }

    /**
     * Performs Logout function from the Non-Admin Subsystem window
     * @param actionEvent specific ActionEvent that called function
     * @throws ClassNotFoundException exception specific to JavaFX
     */
    @FXML
    protected void logoutFromNonAdminHome(ActionEvent actionEvent) throws ClassNotFoundException{
        Optional<ButtonType> result = showAlertConfirmation("Confirm Logout");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        logout(actionEvent);
    }

    /**
     * Populates the JavaFX Text element with Album information
     */
    @FXML
    public void showAlbumInfo(){
        if(albumListView.getSelectionModel().getSelectedItem() == null){
            albumInfoText.setText("");
            return;
        }
        Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        String albumTitleLine = selectedAlbum.toString() + "\n";
        String numPhotosLine = "Number of Photos: " + selectedAlbum.getNumPhotos() + "\n";
        String rangePhotoDates = "Date Range: " + selectedAlbum.getDateRange();
        albumInfoText.setText("Album Information:\n" + albumTitleLine + numPhotosLine + rangePhotoDates);
    }
}
