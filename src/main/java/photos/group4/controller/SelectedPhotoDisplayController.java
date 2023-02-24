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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import photos.group4.Photos;
import photos.group4.model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * JavaFX Controller Class to handle Selected Photo Display View of Non-admin Subsystem features
 */
public class SelectedPhotoDisplayController implements Logout, Alerts {
    /**
     * JavaFX Button to handle going back to the Photo List View
     */
    @FXML
    private Button backToPhotoListButton;
    /**
     * JavaFX Button to handle Logout action
     */
    @FXML
    private Button logoutButton;
    /**
     * JavaFX Button to handle the addition of Photo Tags
     */
    @FXML
    private Button addPhotoTagButton;
    /**
     * JavaFX Button to handle the deletion of Photo Tags
     */
    @FXML
    private Button deletePhotoTagButton;
    /**
     * JavaFX TextField to input Photo Tag Name
     */
    @FXML
    private TextField photoTagNameTextField;
    /**
     * JavaFX TextField to input Photo Tag Value
     */
    @FXML
    private TextField photoTagValueTextField;
    /**
     * JavaFX ChoiceBox to select a Photo Tag Name from the Photo Tag Name List
     */
    @FXML
    private ChoiceBox<String> photoTagNameChoiceBox;
    /**
     * JavaFX Text to display Photo information
     */
    @FXML
    private Text photoInfoLabel;
    /**
     * JavaFX ImageView to display the Photo
     */
    @FXML
    private ImageView photoImageView;
    /**
     * JavaFX ListView to display the list of Photo Tags
     */
    @FXML
    private ListView<PhotoTag> photoTagListView;
    /**
     * ObservableList to populate ListView
     */
    private ObservableList<PhotoTag> observablePhotoTagList;
    /**
     * List to populate ObservableList
     */
    private List<PhotoTag> photoTagList;
    /**
     * Reference to main UserList instance
     */
    private UserList users;
    /**
     * Reference to the User who owns the Photo
     */
    private User user;
    /**
     * Reference to the Album that contains the Photo
     */
    private Album album;
    /**
     * Reference to the Photo that is opened
     */
    private Photo photo;
    /**
     * Reference to the Photo Tag Name selected from Choice Box
     */
    private String photoTagNameFromChoiceBox;

    /**
     * Populates PhotoTag ListView, Photo ImageView, and displays the Photo information
     * @param stage Stage to execute method on
     */
    public void start(Stage stage){
        stage.setTitle("Photo Display");
        photoTagList = photo.getTags();
        observablePhotoTagList = FXCollections.observableArrayList(photoTagList);
        photoTagListView.setItems(observablePhotoTagList);
        File file = new File(photo.getFileLocation());
        Image imageToDisplay = new Image(file.toURI().toString());
        photoImageView.setPreserveRatio(true);
        photoImageView.setImage(imageToDisplay);
        showPhotoInfo();
        photoTagNameChoiceBox.getItems().addAll(album.getPhotoTagNameList());
        photoTagNameChoiceBox.setOnAction(this::getPhotoTagName);
    }

    /**
     * Gets the selected Photo Tag Name from Choice Box content
     * @param actionEvent specific ActionEvent that called function
     */
    public void getPhotoTagName(ActionEvent actionEvent){
        photoTagNameFromChoiceBox = photoTagNameChoiceBox.getValue();
    }

    /**
     * Sets the UserList reference to the main UserList instance
     * @param users UserList instance to set
     */
    public void setUserList(UserList users){
        this.users = users;
    }

    /**
     * Sets the User reference to the User who owns the Photo
     * @param user User to set
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Sets the Album reference to the Album containing the Photo
     * @param album Album to set
     */
    public void setAlbum(Album album){
        this.album = album;
    }

    /**
     * Sets the Photo reference to the Photo being opened
     * @param photo Photo to set
     */
    public void setPhoto(Photo photo){
        this.photo = photo;
    }

    /**
     * Adds specified PhotoTag to the list of Photo Tags for the Photo
     * @throws IOException exception specific to JavaFX
     */
    @FXML
    protected void addPhotoTag() throws IOException{
        if((photoTagNameTextField.getText().isEmpty() || photoTagValueTextField.getText().isEmpty()) && photoTagNameFromChoiceBox == null){
            showAlertError("Please select/enter a valid Tag Name and enter a valid Tag Value!");
        }
        else if(photoTagNameFromChoiceBox == null && (!photoTagNameTextField.getText().isEmpty() && !photoTagValueTextField.getText().isEmpty())) {
            String photoTagName = photoTagNameTextField.getText();
            String photoTagValue = photoTagValueTextField.getText();
            for (PhotoTag photoTag : photoTagList) {
                if (photoTag.getTagName().equals(photoTagName) && photoTag.getTagValue().equals(photoTagValue)) {
                    showAlertError("Tag already exists!");
                    return;
                }
            }
            for(String tagName: album.getPhotoTagNameList()){
                if(tagName.equals(photoTagName)){
                    showAlertError("Tag Name already exists!");
                    return;
                }
            }
            Optional<ButtonType> result = showAlertConfirmation("Add specified Photo Tag?");
            if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
                return;
            }
            album.addPhotoTagNameToList(photoTagName);
            PhotoTag photoTag = new PhotoTag(photoTagName, photoTagValue);
            photoTagList.add(photoTag);
            observablePhotoTagList.add(photoTag);
            photoTagListView.setItems(observablePhotoTagList);
            photoTagListView.refresh();
            photoTagNameTextField.setText("");
            photoTagValueTextField.setText("");
            photoTagNameChoiceBox.getItems().setAll(album.getPhotoTagNameList());
            UserList.writeUserData(users);
        }
        else if(photoTagNameFromChoiceBox != null && photoTagValueTextField.getText().isEmpty()){
            showAlertError("Please enter a valid Tag Value!");
        }
        else{
            String photoTagValue = photoTagValueTextField.getText();
            for (PhotoTag photoTag : photoTagList) {
                if (photoTag.getTagName().equals(photoTagNameFromChoiceBox) && photoTag.getTagValue().equals(photoTagValue)) {
                    showAlertError("Tag already exists!");
                    return;
                }
            }
            Optional<ButtonType> result = showAlertConfirmation("Add specified Photo Tag?");
            if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
                return;
            }
            PhotoTag photoTag = new PhotoTag(photoTagNameFromChoiceBox, photoTagValue);
            photoTagList.add(photoTag);
            observablePhotoTagList.add(photoTag);
            photoTagListView.setItems(observablePhotoTagList);
            photoTagListView.refresh();
            photoTagNameTextField.setText("");
            photoTagValueTextField.setText("");
            photoTagNameChoiceBox.setValue(null);
            UserList.writeUserData(users);
        }
    }

    /**
     * Deletes selected PhotoTag from the list of Photo Tags for the Photo
     * @throws IOException exception specific to JavaFX
     */
    @FXML
    protected void deletePhotoTag() throws IOException{
        int selectedPhotoTag = photoTagListView.getSelectionModel().getSelectedIndex();
        if(selectedPhotoTag == -1){
            showAlertError("Please select a Photo Tag to delete!");
            return;
        }
        Optional<ButtonType> result = showAlertConfirmation("Delete selected Photo Tag?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        PhotoTag photoTag = photoTagList.get(selectedPhotoTag);
        photoTagList.remove(photoTag);
        observablePhotoTagList.remove(photoTag);
        photoTagListView.setItems(observablePhotoTagList);
        photoTagListView.refresh();
        UserList.writeUserData(users);
    }

    /**
     * Goes back to the Photo List View
     * @param actionEvent specific ActionEvent that called function
     */
    @FXML
    protected void goBackToPhotoList(ActionEvent actionEvent){
        Optional<ButtonType> result = showAlertConfirmation("Go Back to Photo List?");
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
            albumController.setAlbum(album);
            albumController.start(stage);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ignored){}
    }

    /**
     * Performs Logout function from the Selected Photo Display window
     * @param actionEvent specific ActionEvent that called function
     * @throws ClassNotFoundException exception specific to JavaFX
     */
    @FXML
    protected void logoutFromPhotoDisplayHome(ActionEvent actionEvent) throws ClassNotFoundException{
        Optional<ButtonType> result = showAlertConfirmation("Confirm Logout");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        logout(actionEvent);
    }

    /**
     * Populates the JavaFX Text node with Photo information
     */
    public void showPhotoInfo(){
        photoInfoLabel.setText(photo.toString());
    }
}
