/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package photos.group4.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import photos.group4.Photos;
import photos.group4.model.Album;
import photos.group4.model.Photo;
import photos.group4.model.User;
import photos.group4.model.UserList;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * JavaFX Controller Class to handle Selected Album View of Non-Admin Subsystem features
 */
public class SelectedAlbumController implements Logout, Alerts{
    /**
     * JavaFX Button to handle going back to the Album List View
     */
    @FXML
    private Button backToAlbumListButton;
    /**
     * JavaFX Button to handle Logout action
     */
    @FXML
    private Button logoutButton;
    /**
     * JavaFX Button to handle adding Photos to Album
     */
    @FXML
    private Button addPhotoButton;
    /**
     * JavaFX Button to handle naming Photos
     */
    @FXML
    private Button namePhotoButton;
    /**
     * JavaFX Button to handle captioning of Photos
     */
    @FXML
    private Button captionPhotoButton;
    /**
     * JavaFX Button to handle the deletion of Photos
     */
    @FXML
    private Button deletePhotoButton;
    /**
     * JavaFX Button to handle opening Photos in separate display
     */
    @FXML
    private Button openPhotoButton;
    /**
     * JavaFX Button to handle copying Photos between Albums
     */
    @FXML
    private Button copyPhotoToAlbumButton;
    /**
     * JavaFX Button to handle moving Photos between Albums
     */
    @FXML
    private Button movePhotoToAlbumButton;
    /**
     * JavaFX Button to handle Album Slideshow transition
     */
    @FXML
    private Button albumSlideshowButton;
    /**
     * JavaFX Button to handle Search For Photos transition
     */
    @FXML
    private Button searchForPhotosButton;
    /**
     * JavaFX TextField to enter Photo name
     */
    @FXML
    private TextField photoNameTextField;
    /**
     * JavaFX TextField to enter Photo caption
     */
    @FXML
    private TextField photoCaptionTextField;
    /**
     * JavaFX ChoiceBox to select Album to copy or move photos to
     */
    @FXML
    private ChoiceBox<Album> albumChoiceBox;
    /**
     * JavaFX ImageView to display selected Photo thumbnail
     */
    @FXML
    private ImageView photoThumbnailImageView;

    /**
     * JavaFX TilePane to display thumbnails of images in album.
     */
    @FXML
    private TilePane photoTilePane;
    /**
     * JavaFX ScrollPane to scroll TilePane in case of many images
     */
    @FXML
    private ScrollPane imageScrollPane;
    /**
     * JavaFX Label to display information of selectedPhoto
     */
    @FXML
    private Label selectedPhotoDetailsLabel;

    /**
     * Reference to list of albumPhotos in selected album
     */
    private List<Photo> photoList;
    /**
     * Reference to main UserList instance
     */
    private UserList users;
    /**
     * Reference to the User who owns the Album
     */
    private User user;
    /**
     * Reference to the Album that is open
     */
    private Album album;
    /**
     * Reference to the current selected photo in photoTilePane.
     */
    private Photo selectedPhoto = null;
    /**
     * Reference to the Album selected from the ChoiceBox
     */
    private Album albumFromChoiceBox;

    /**
     * Populates Photo ListView with Photo information, displays thumbnail image when necessary, and populates ChoiceBox data
     * @param stage Stage to execute method on
     */
    public void start(Stage stage){
        stage.setTitle("Current album: " + album.getAlbumTitle());
        photoList = album.getAlbumPhotos();
        showThumbnailImage();
        albumChoiceBox.getItems().addAll(user.getAlbumList());
        albumChoiceBox.setOnAction(this::getAlbum);
        selectedPhotoDetailsLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
        refreshTilePane();
    }
    private ImageView createImageView(Photo photo) {
        File file = new File(photo.getFileLocation());
        ImageView newIV = null;
        try {
            newIV = new ImageView(new Image(file.toURI().toString(), 100, 0, true, true));
            newIV.setFitWidth(100);
            newIV.setPreserveRatio(true);
            newIV.setPickOnBounds(true);
            newIV.setOnMouseClicked(mouseEvent -> {
                selectedPhoto = photo;
                photoThumbnailImageView.setImage(new Image(file.toURI().toString()));
                photoThumbnailImageView.setPreserveRatio(true);
                selectedPhotoDetailsLabel.setText(photo.toString());

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newIV;
    }
    private void refreshTilePane() {
        photoTilePane.getChildren().clear();
        int numRows = album.getNumPhotos() / 3;
        photoTilePane.setPrefColumns(5);
        photoTilePane.setPrefRows(numRows);
        if (photoList.equals(null)) { //make this check after completing.
            return;
        }
        for (Photo photo : album.getAlbumPhotos()) {
            ImageView imageView = createImageView(photo);
            photoTilePane.getChildren().addAll(imageView); //might need to be add instead of addAll
        }
    }

    /**
     * Gets the selected Album from the ChoiceBox content
     * @param actionEvent specific ActionEvent that called function
     */
    public void getAlbum(ActionEvent actionEvent){
        albumFromChoiceBox = albumChoiceBox.getValue();
    }

    /**
     * Sets the UserList reference to the main UserList instance
     * @param users UserList instance to set
     */
    public void setUserList(UserList users){
        this.users = users;
    }

    /**
     * Sets the User reference to the User accessing the Album
     * @param user the User to set
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Sets the Album reference to the Album that is being accessed
     * @param album the Album to set
     */
    public void setAlbum(Album album){
        this.album = album;
    }

    /**
     * Prompts User to upload Photo, then adds Photo to the User's Album Photo List
     * @param actionEvent specific ActionEvent that called function
     * @throws IOException exception specific to JavaFX
     */
    @FXML
    protected void addPhoto(ActionEvent actionEvent) throws IOException{
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Upload Photo");
        String[] possibleTypes = {"*.JPEG", "*.JPG", "*.PNG", "*.GIF", "*.BMP"};

        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG, JPG, PNG, GIF, or BMP files", possibleTypes),
                new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.JPEG"),
                new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG"),
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG"),
                new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.GIF"),
                new FileChooser.ExtensionFilter("BMP files (*.bmp)", "*.BMP"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        List<File> fileList = chooser.showOpenMultipleDialog(stage);
        if (fileList == null) {return;}
        for (File file : fileList) {
            if(file == null){
                selectedPhoto = null;
                refreshTilePane();
                showThumbnailImage();
                return;
            }
            Calendar date = Calendar.getInstance();
            date.set(Calendar.MILLISECOND, 0);
            Photo photo = new Photo(file.getName(), date, file.toString());
            for(Photo p: photoList){
                if(p.duplicateFile(photo)){
                    showAlertError("Duplicate photo in batch, operation cancelled");
                    return;
                }
            }
            for(Album album: user.getAlbumList()){
                for(Photo p: album.getAlbumPhotos()){
                    if(p.duplicateFile(photo)){
                        photo = p;
                        break;
                    }
                }
            }
            album.addPhoto(photo);
            selectedPhoto = photo;
        }
        UserList.writeUserData(users);
        photoCaptionTextField.setText(selectedPhoto.getCaption());
        photoNameTextField.setText(selectedPhoto.getName());
        showThumbnailImage();
        refreshTilePane();
    }

    /**
     * Sets the name of the selected Photo
     * @throws IOException exception specific to JavaFX
     */
    @FXML
    protected void namePhoto() throws IOException{
        if (selectedPhoto == null) {
            showAlertError("Please select a Photo to name!");
            return;
        }

        if(photoNameTextField.getText().isEmpty()){
            showAlertError("Please enter a valid name!");
            return;
        }
        Optional<ButtonType> result = showAlertConfirmation("Add specified name to Photo?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        selectedPhoto.setName(photoNameTextField.getText());
        photoNameTextField.clear();
        UserList.writeUserData(users);
        showThumbnailImage();
        refreshTilePane();
    }

    /**
     * Sets the caption of the selected Photo
     * @throws IOException exception specific to JavaFX
     */
    @FXML
    protected void captionPhoto() throws IOException{
        if (selectedPhoto == null) {
            showAlertError("Please select a Photo to caption!");
            return;
        }
        if(photoCaptionTextField.getText().isEmpty()){
            showAlertError("Please enter a valid caption!");
            return;
        }

        Optional<ButtonType> result = showAlertConfirmation("Add specified caption to Photo?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        selectedPhoto.setCaption(photoCaptionTextField.getText());
        photoCaptionTextField.clear();
        selectedPhotoDetailsLabel.setText(selectedPhoto.toString());
        UserList.writeUserData(users);
        showThumbnailImage();
        refreshTilePane();
    }

    /**
     * Removes the selected Photo from the User's Album Photo List
     * @throws IOException exception specific to JavaFX
     */
    @FXML
    protected void deletePhoto() throws IOException{
        if (selectedPhoto == null) {
            showAlertError("Please select a Photo to delete!");
            return;
        }
        Optional<ButtonType> result = showAlertConfirmation("Are you sure you want to delete the selected Photo?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        album.removePhoto(selectedPhoto);
        UserList.writeUserData(users);
        selectedPhoto = null;
        showThumbnailImage();
        refreshTilePane();
    }

    /**
     * Opens the selected Photo in a separate window
     * @param actionEvent specific ActionEvent that called function
     */
    @FXML
    protected void openPhoto(ActionEvent actionEvent){
        if (selectedPhoto == null) {
            showAlertError("Please select a Photo to open!");
            return;
        }
        Optional<ButtonType> result = showAlertConfirmation("Open selected Photo?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Photos.class.getResource("SelectedPhotoDisplay.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            SelectedPhotoDisplayController photoDisplayController = loader.getController();
            photoDisplayController.setUserList(users);
            photoDisplayController.setUser(user);
            photoDisplayController.setAlbum(album);
            photoDisplayController.setPhoto(selectedPhoto);
            photoDisplayController.start(stage);
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException ignored){}
    }

    /**
     * Copies selected Photo to specified Album
     * @throws IOException exception specific to JavaFX
     */
    @FXML
    protected void copyPhotoToAlbum() throws IOException{
        if (selectedPhoto == null) {
            showAlertError("Please select a Photo to copy!");
            return;
        }
        if(albumFromChoiceBox == null){
            showAlertError("Please choose an Album to copy the Photo to!");
            return;
        }
        for (int i = 0; i < albumFromChoiceBox.getAlbumPhotos().size(); i++) {
            if (albumFromChoiceBox.getAlbumPhotos().get(i) == selectedPhoto) {
                showAlertError("Target album already has this photo!");
                return;
            }
        }

        Optional<ButtonType> result = showAlertConfirmation("Copy selected Photo to specified Album?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }

        albumFromChoiceBox.addPhoto(selectedPhoto);
        albumChoiceBox.setValue(null);
        UserList.writeUserData(users);
        refreshTilePane();
    }

    /**
     * Moves the selected Photo to specified Album
     * @throws IOException exception specific to JavaFX
     */
    @FXML
    protected void movePhotoToAlbum() throws IOException{
        int selectedIndex = album.getAlbumPhotos().indexOf(selectedPhoto); //can remove
        if (selectedPhoto == null) {
            showAlertError("Please select a Photo to move!");
            return;
        }
        if(albumFromChoiceBox == null){
            showAlertError("Please choose an Album to move the Photo to!");
            return;
        }
        for (int i = 0; i < albumFromChoiceBox.getAlbumPhotos().size(); i++) {
            if (albumFromChoiceBox.getAlbumPhotos().get(i) == selectedPhoto) {
                showAlertError("Target album already has this photo!");
                return;
            }
        }
        Optional<ButtonType> result = showAlertConfirmation("Move selected Photo to specified Album? (This will delete the Photo from this Album)");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        albumFromChoiceBox.addPhoto(selectedPhoto);
        album.removePhoto(selectedPhoto);
        albumChoiceBox.setValue(null);
        UserList.writeUserData(users);
        selectedPhoto = null;
        refreshTilePane();
        showThumbnailImage();
    }

    /**
     * Opens Album Slideshow View for selected Album
     * @param actionEvent specific ActionEvent that called function
     */
    @FXML
    protected void albumSlideshow(ActionEvent actionEvent){
        if(album.getNumPhotos() == 0){
            showAlertError("No Photos to display!");
            return;
        }
        Optional<ButtonType> result = showAlertConfirmation("Open Slideshow View of Album?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Photos.class.getResource("SelectedAlbumSlideshow.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            SelectedAlbumSlideshowController slideshowController = loader.getController();
            slideshowController.setUserList(users);
            slideshowController.setUser(user);
            slideshowController.setAlbum(album);
            slideshowController.setPhotoList(album.getAlbumPhotos());
            slideshowController.start(stage);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ignored){}
    }

    /**
     * Opens Photo Search View for selected Album
     * @param actionEvent specific ActionEvent that called function
     */
    @FXML
    protected void searchForPhotos(ActionEvent actionEvent){
        if(album.getNumPhotos() == 0){
            showAlertError("No Photos to search for!");
            return;
        }
        Optional<ButtonType> result = showAlertConfirmation("Search for Photos in Album?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Photos.class.getResource("AlbumPhotoSearch.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            AlbumPhotoSearchController photoSearchController = loader.getController();
            photoSearchController.setUserList(users);
            photoSearchController.setUser(user);
            photoSearchController.setAlbum(album);
            photoSearchController.start(stage);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ignored){}
    }

    /**
     * Goes back to the Album List View
     * @param actionEvent specific ActionEvent that called function
     */
    @FXML
    protected void goBackToAlbumList(ActionEvent actionEvent){
        Optional<ButtonType> result = showAlertConfirmation("Go Back to Album List?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Photos.class.getResource("NonAdminSubsystem.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            NonAdminSubsystemController nonAdminController = loader.getController();
            nonAdminController.setUserList(users);
            nonAdminController.setUser(user);
            nonAdminController.start(stage);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ignored){}
    }

    /**
     * Performs Logout function from the Selected Album Photo List window
     * @param actionEvent specific ActionEvent that called function
     * @throws ClassNotFoundException exception specific to JavaFX
     */
    @FXML
    protected void logoutFromPhotoListHome(ActionEvent actionEvent) throws ClassNotFoundException{
        Optional<ButtonType> result = showAlertConfirmation("Confirm Logout");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        logout(actionEvent);
    }

    /**
     * Displays thumbnail image of selected Photo
     */
    @FXML
    public void showThumbnailImage(){
        if(selectedPhoto == null){
            photoThumbnailImageView.setImage(null);
            return;
        }

        photoThumbnailImageView.setImage(new Image(new File(selectedPhoto.getFileLocation()).toURI().toString()));

    }
}
