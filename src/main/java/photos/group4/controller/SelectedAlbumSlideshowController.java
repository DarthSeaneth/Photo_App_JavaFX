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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import photos.group4.Photos;
import photos.group4.model.Album;
import photos.group4.model.Photo;
import photos.group4.model.User;
import photos.group4.model.UserList;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * JavaFX Controller Class to handle Album Slideshow View of Non-admin Subsystem features
 */
public class SelectedAlbumSlideshowController implements Logout, Alerts {
    /**
     * JavaFX Button to handle going back to Photo List View
     */
    @FXML
    private Button backToPhotoListButton;
    /**
     * JavaFX Button to handle Logout action
     */
    @FXML
    private Button logoutButton;
    /**
     * JavaFX Button to handle displaying the previous Photo
     */
    @FXML
    private Button previousPhotoButton;
    /**
     * JavaFX Button to handle displaying the next Photo
     */
    @FXML
    private Button nextPhotoButton;
    /**
     * JavaFX ImageView to display the desired Photo
     */
    @FXML
    private ImageView photoImageView;
    /**
     * Reference to the main UserList instance
     */
    private UserList users;
    /**
     * Reference to the User who owns the Album
     */
    private User user;
    /**
     * Reference to the Album that is opened
     */
    private Album album;
    /**
     * Reference to the List of Photos in the Album
     */
    private List<Photo> photoList;
    /**
     * Reference to the current index of the Photo List
     */
    private int index;

    /**
     * Displays the first Photo in the list of Photos and disables Buttons when necessary
     * @param stage Stage to execute method on
     */
    public void start(Stage stage){
        stage.setTitle("Slideshow for " + album.getAlbumTitle());
        index = 0;
        Photo photoToDisplay = photoList.get(index);
        File file = new File(photoToDisplay.getFileLocation());
        Image imageToDisplay = new Image(file.toURI().toString());
        photoImageView.setImage(imageToDisplay);
        previousPhotoButton.setDisable(index == 0);
        nextPhotoButton.setDisable(index == photoList.size() - 1);
    }

    /**
     * Sets the UserList reference to the main UserList instance
     * @param users UserList to set
     */
    public void setUserList(UserList users){
        this.users = users;
    }

    /**
     * Sets the User reference to the User who owns the Album
     * @param user User to set
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Sets the Album reference to the Album being opened
     * @param album Album to set
     */
    public void setAlbum(Album album){
        this.album = album;
    }

    /**
     * Sets the Photo List reference to the list of Photos in the Album
     * @param photoList Photo List to set
     */
    public void setPhotoList(List<Photo> photoList){
        this.photoList = photoList;
    }

    /**
     * Displays the next Photo (if possible) and disables Buttons when necessary
     */
    @FXML
    protected void nextPhoto(){
        index ++;
        Photo photoToDisplay = photoList.get(index);
        File file = new File(photoToDisplay.getFileLocation());
        Image imageToDisplay = new Image(file.toURI().toString());
        photoImageView.setImage(imageToDisplay);
        previousPhotoButton.setDisable(index == 0);
        nextPhotoButton.setDisable(index == photoList.size() - 1);
    }

    /**
     * Displays the previous Photo (if possible) and disables Buttons when necessary
     */
    @FXML
    protected void previousPhoto(){
        index --;
        Photo photoToDisplay = photoList.get(index);
        File file = new File(photoToDisplay.getFileLocation());
        Image imageToDisplay = new Image(file.toURI().toString());
        photoImageView.setImage(imageToDisplay);
        previousPhotoButton.setDisable(index == 0);
        nextPhotoButton.setDisable(index == photoList.size() - 1);
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
     * Performs Logout function from the Album Slideshow window
     * @param actionEvent specific ActionEvent that called function
     * @throws ClassNotFoundException exception specific to JavaFX
     */
    @FXML
    protected void logoutFromAlbumSlideshowHome(ActionEvent actionEvent) throws ClassNotFoundException{
        Optional<ButtonType> result = showAlertConfirmation("Confirm Logout");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        logout(actionEvent);
    }

}
