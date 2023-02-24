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
import javafx.stage.Stage;
import photos.group4.Photos;
import photos.group4.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * JavaFX Controller Class to handle Album Photo Search View of Non-admin Subsystem features
 */
public class AlbumPhotoSearchController implements Logout, Alerts {
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
     * JavaFX Button to handle searching for Photos by date range
     */
    @FXML
    private Button searchByDateRangeButton;
    /**
     * JavaFX Button to handle searching for Photos by single Tag Name/Value pair
     */
    @FXML
    private Button singleTagNameValuePairSearchButton;
    /**
     * JavaFX Button to handle conjunctive searching for Photos by Tag Name/Value pairs
     */
    @FXML
    private Button conjunctiveSearchButton;
    /**
     * JavaFX Button to handle disjunctive searching for Photos by Tag Name/Value pairs
     */
    @FXML
    private Button disjunctiveSearchButton;
    /**
     * JavaFX Button to handle creating Album from search results
     */
    @FXML
    private Button createAlbumFromResultsButton;
    /**
     * JavaFX TextField to input date range search starting year
     */
    @FXML
    private TextField fromYearTextField;
    /**
     * JavaFX TextField to input date range search ending year
     */
    @FXML
    private TextField toYearTextField;
    /**
     * JavaFX TextField to input Tag Value (Single Tag Name/Value pair)
     */
    @FXML
    private TextField singleTagValueTextField;
    /**
     * JavaFX TextField to input first Tag's Value (Conjunctive searching)
     */
    @FXML
    private TextField conjunctiveTagValueTextField1;
    /**
     * JavaFX TextField to input second Tag's Value (Conjunctive searching)
     */
    @FXML
    private TextField conjunctiveTagValueTextField2;
    /**
     * JavaFX TextField to input first Tag's Value (Disjunctive searching)
     */
    @FXML
    private TextField disjunctiveTagValueTextField1;
    /**
     * JavaFX TextField to input second Tag's Value (Disjunctive searching)
     */
    @FXML
    private TextField disjunctiveTagValueTextField2;
    /**
     * JavaFX TextField to input Album title
     */
    @FXML
    private TextField albumTitleTextField;
    /**
     * JavaFX ChoiceBox to select from-month for date range search
     */
    @FXML
    private ChoiceBox<Integer> fromMonthChoiceBox;
    /**
     * JavaFX ChoiceBox to select to-month for date range search
     */
    @FXML
    private ChoiceBox<Integer> toMonthChoiceBox;
    /**
     * JavaFX ChoiceBox to select from-day for date range search
     */
    @FXML
    private ChoiceBox<Integer> fromDayChoiceBox;
    /**
     * JavaFX ChoiceBox to select to-day for date range search
     */
    @FXML
    private ChoiceBox<Integer> toDayChoiceBox;
    /**
     * JavaFX ChoiceBox to select Photo Tag Name for single photo tag search
     */
    @FXML
    private ChoiceBox<String> singleTagNameChoiceBox;
    /**
     * JavaFX ChoiceBox to select first Photo Tag Name for conjunctive photo tag search
     */
    @FXML
    private ChoiceBox<String> conjunctiveTagNameChoiceBox1;
    /**
     * JavaFX ChoiceBox to select second Photo Tag Name for conjunctive photo tag search
     */
    @FXML
    private ChoiceBox<String> conjunctiveTagNameChoiceBox2;
    /**
     * JavaFX ChoiceBox to select first Photo Tag Name for disjunctive photo tag search
     */
    @FXML
    private ChoiceBox<String> disjunctiveTagNameChoiceBox1;
    /**
     * JavaFX ChoiceBox to select second Photo Tag Name for disjunctive photo tag search
     */
    @FXML
    private ChoiceBox<String> disjunctiveTagNameChoiceBox2;
    /**
     * JavaFX ImageView to display selected Photo's thumbnail
     */
    @FXML
    private ImageView thumbnailImageView;
    /**
     * JavaFX ListView to display Photo information
     */
    @FXML
    private ListView<Photo> photoListView;
    /**
     * Observable List to populate List View
     */
    private ObservableList<Photo> observablePhotoList;
    /**
     * List to populate Observable List
     */
    private List<Photo> photoList;
    /**
     * Reference to the main UserList instance
     */
    private UserList users;
    /**
     * Reference to the User who owns the opened Album
     */
    private User user;
    /**
     * Reference to the Album that is being accessed
     */
    private Album album;
    /**
     * Integer array of month values to populate Choice Boxes
     */
    private final Integer[] months = {1,2,3,4,5,6,7,8,9,10,11,12};
    /**
     * Integer array of day values to populate Choice Boxes
     */
    private final Integer[] days = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    /**
     * Reference to from-month chosen from Choice Box
     */
    private int fromMonthFromChoiceBox = -1;
    /**
     * Reference to to-month chosen from Choice Box
     */
    private int toMonthFromChoiceBox = -1;
    /**
     * Reference to from-day chosen from Choice Box
     */
    private int fromDayFromChoiceBox = -1;
    /**
     * Reference to to-day chosen from Choice Box
     */
    private int toDayFromChoiceBox = -1;
    /**
     * Reference to tag name chosen from Choice Box
     */
    private String singleTagNameFromChoiceBox;
    /**
     * Reference to tag name chosen from Choice Box
     */
    private String conjunctiveTagNameFromChoiceBox1;
    /**
     * Reference to tag name chosen from Choice Box
     */
    private String conjunctiveTagNameFromChoiceBox2;
    /**
     * Reference to tag name chosen from Choice Box
     */
    private String disjunctiveTagNameFromChoiceBox1;
    /**
     * Reference to tag name chosen from Choice Box
     */
    private String disjunctiveTagNameFromChoiceBox2;

    /**
     * Populates ListView with Photo information, displays thumbnails when necessary, and sets up Choice Boxes
     * @param stage Stage to execute method on
     */
    public void start(Stage stage){
        stage.setTitle("Photo Search");
        photoList = album.getAlbumPhotos();
        observablePhotoList = FXCollections.observableArrayList(photoList);
        photoListView.setItems(observablePhotoList);
        showThumbnailImage();
        fromMonthChoiceBox.getItems().addAll(months);
        fromMonthChoiceBox.setOnAction(this::getFromMonth);
        toMonthChoiceBox.getItems().addAll(months);
        toMonthChoiceBox.setOnAction(this::getToMonth);
        fromDayChoiceBox.getItems().addAll(days);
        fromDayChoiceBox.setOnAction(this::getFromDay);
        toDayChoiceBox.getItems().addAll(days);
        toDayChoiceBox.setOnAction(this::getToDay);
        singleTagNameChoiceBox.getItems().addAll(album.getPhotoTagNameList());
        singleTagNameChoiceBox.setOnAction(this::getSingleTagName);
        conjunctiveTagNameChoiceBox1.getItems().addAll(album.getPhotoTagNameList());
        conjunctiveTagNameChoiceBox1.setOnAction(this::getConjunctiveTagName1);
        conjunctiveTagNameChoiceBox2.getItems().addAll(album.getPhotoTagNameList());
        conjunctiveTagNameChoiceBox2.setOnAction(this::getConjunctiveTagName2);
        disjunctiveTagNameChoiceBox1.getItems().addAll(album.getPhotoTagNameList());
        disjunctiveTagNameChoiceBox1.setOnAction(this::getDisjunctiveTagName1);
        disjunctiveTagNameChoiceBox2.getItems().addAll(album.getPhotoTagNameList());
        disjunctiveTagNameChoiceBox2.setOnAction(this::getDisjunctiveTagName2);
    }

    /**
     * Gets from-month from Choice Box content
     * @param actionEvent specific ActionEvent that called function
     */
    public void getFromMonth(ActionEvent actionEvent){
        fromMonthFromChoiceBox = fromMonthChoiceBox.getValue() - 1;
    }

    /**
     * Gets to-month from Choice Box content
     * @param actionEvent specific ActionEvent that called function
     */
    public void getToMonth(ActionEvent actionEvent){
        toMonthFromChoiceBox = toMonthChoiceBox.getValue() - 1;
    }

    /**
     * Gets from-day from Choice Box content
     * @param actionEvent specific ActionEvent that called function
     */
    public void getFromDay(ActionEvent actionEvent){
        fromDayFromChoiceBox = fromDayChoiceBox.getValue();
    }

    /**
     * Gets to-day from Choice Box content
     * @param actionEvent specific ActionEvent that called function
     */
    public void getToDay(ActionEvent actionEvent){
        toDayFromChoiceBox = toDayChoiceBox.getValue();
    }

    /**
     * Gets tag name from Choice Box content
     * @param actionEvent specific ActionEvent that called function
     */
    public void getSingleTagName(ActionEvent actionEvent){
        singleTagNameFromChoiceBox = singleTagNameChoiceBox.getValue();
    }
    /**
     * Gets tag name from Choice Box content
     * @param actionEvent specific ActionEvent that called function
     */
    public void getConjunctiveTagName1(ActionEvent actionEvent){
        conjunctiveTagNameFromChoiceBox1 = conjunctiveTagNameChoiceBox1.getValue();
    }
    /**
     * Gets tag name from Choice Box content
     * @param actionEvent specific ActionEvent that called function
     */
    public void getConjunctiveTagName2(ActionEvent actionEvent){
        conjunctiveTagNameFromChoiceBox2 = conjunctiveTagNameChoiceBox2.getValue();
    }
    /**
     * Gets tag name from Choice Box content
     * @param actionEvent specific ActionEvent that called function
     */
    public void getDisjunctiveTagName1(ActionEvent actionEvent){
        disjunctiveTagNameFromChoiceBox1 = disjunctiveTagNameChoiceBox1.getValue();
    }
    /**
     * Gets tag name from Choice Box content
     * @param actionEvent specific ActionEvent that called function
     */
    public void getDisjunctiveTagName2(ActionEvent actionEvent){
        disjunctiveTagNameFromChoiceBox2 = disjunctiveTagNameChoiceBox2.getValue();
    }

    /**
     * Sets UserList reference to the main UserList instance
     * @param users UserList instance to set
     */
    public void setUserList(UserList users){
        this.users = users;
    }

    /**
     * Sets User reference to the User who owns the Album
     * @param user User to set
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Sets Album reference to the Album being accessed
     * @param album Album to set
     */
    public void setAlbum(Album album){
        this.album = album;
    }

    /**
     * Searches for Photos in the Album by specific date range
     */
    @FXML
    protected void searchByDateRange(){
        if(fromYearTextField.getText().isEmpty() || toYearTextField.getText().isEmpty()){
            showAlertError("Please enter valid years!");
            return;
        }
        if(fromMonthFromChoiceBox == -1 && toMonthFromChoiceBox== -1 && fromDayFromChoiceBox == -1 && toDayFromChoiceBox == -1){
            try {
                int fromYear = Integer.parseInt(fromYearTextField.getText());
                int toYear = Integer.parseInt(toYearTextField.getText());
                Predicate<Photo> fromYearPredicate = photo -> photo.getDate().get(Calendar.YEAR) >= fromYear;
                Predicate<Photo> toYearPredicate = photo -> photo.getDate().get(Calendar.YEAR) <= toYear;
                List<Photo> newPhotoList = filter(photoList, fromYearPredicate.and(toYearPredicate));
                observablePhotoList = FXCollections.observableArrayList(newPhotoList);
                photoListView.setItems(observablePhotoList);
                fromYearTextField.setText("");
                toYearTextField.setText("");
                photoListView.refresh();
                showThumbnailImage();
            }
            catch(NumberFormatException e){
                showAlertError("Please enter valid years!");
            }
        }
        else if((fromMonthFromChoiceBox == -1 && toMonthFromChoiceBox != -1) || (fromMonthFromChoiceBox != -1 && toMonthFromChoiceBox == -1)){
            showAlertError("Please choose months!");
        }
        else if(fromDayFromChoiceBox == -1 && toDayFromChoiceBox == -1){
            try {
                int fromYear = Integer.parseInt(fromYearTextField.getText());
                int toYear = Integer.parseInt(toYearTextField.getText());
                Predicate<Photo> fromYearPredicate = photo -> photo.getDate().get(Calendar.YEAR) >= fromYear;
                Predicate<Photo> toYearPredicate = photo -> photo.getDate().get(Calendar.YEAR) <= toYear;
                Predicate<Photo> fromMonthPredicate = photo -> photo.getDate().get(Calendar.MONTH) >= fromMonthFromChoiceBox;
                Predicate<Photo> toMonthPredicate = photo -> photo.getDate().get(Calendar.MONTH) <= toMonthFromChoiceBox;
                List<Photo> newPhotoList = filter(photoList, fromYearPredicate.and(toYearPredicate).and(fromMonthPredicate).and(toMonthPredicate));
                observablePhotoList = FXCollections.observableArrayList(newPhotoList);
                photoListView.setItems(observablePhotoList);
                fromYearTextField.setText("");
                toYearTextField.setText("");
                fromMonthChoiceBox.setValue(null);
                toMonthChoiceBox.setValue(null);
                photoListView.refresh();
                showThumbnailImage();
            }
            catch(NumberFormatException e){
                showAlertError("Please enter valid years!");
            }
        }
        else if((fromDayFromChoiceBox == -1 && toDayFromChoiceBox != -1) || (fromDayFromChoiceBox != -1 && toDayFromChoiceBox == -1)){
            showAlertError("Please choose days!");
        }
        else{
            try {
                int fromYear = Integer.parseInt(fromYearTextField.getText());
                int toYear = Integer.parseInt(toYearTextField.getText());
                Predicate<Photo> fromYearPredicate = photo -> photo.getDate().get(Calendar.YEAR) >= fromYear;
                Predicate<Photo> toYearPredicate = photo -> photo.getDate().get(Calendar.YEAR) <= toYear;
                Predicate<Photo> fromMonthPredicate = photo -> photo.getDate().get(Calendar.MONTH) >= fromMonthFromChoiceBox;
                Predicate<Photo> toMonthPredicate = photo -> photo.getDate().get(Calendar.MONTH) <= toMonthFromChoiceBox;
                Predicate<Photo> fromDayPredicate = photo -> photo.getDate().get(Calendar.DAY_OF_MONTH) >= fromDayFromChoiceBox;
                Predicate<Photo> toDayPredicate = photo -> photo.getDate().get(Calendar.DAY_OF_MONTH) <= toDayFromChoiceBox;
                List<Photo> newPhotoList = filter(photoList, fromYearPredicate.and(toYearPredicate).and(fromMonthPredicate).and(toMonthPredicate).and(fromDayPredicate).and(toDayPredicate));
                observablePhotoList = FXCollections.observableArrayList(newPhotoList);
                photoListView.setItems(observablePhotoList);
                fromYearTextField.setText("");
                toYearTextField.setText("");
                fromMonthChoiceBox.setValue(null);
                toMonthChoiceBox.setValue(null);
                fromDayChoiceBox.setValue(null);
                toDayChoiceBox.setValue(null);
                photoListView.refresh();
                showThumbnailImage();
            }
            catch(NumberFormatException e){
                showAlertError("Please enter valid years!");
            }
        }
    }

    /**
     * Searches for Photos in the Album by single pair of Tag Name and Value
     */
    @FXML
    protected void singleTagNameValuePairSearch(){
        if(singleTagNameFromChoiceBox == null || singleTagValueTextField.getText().isEmpty()){
            showAlertError("Please choose a valid Tag name and enter Tag value!");
            return;
        }
        String tagValue = singleTagValueTextField.getText();
        PhotoTag photoTagToSearchFor = new PhotoTag(singleTagNameFromChoiceBox, tagValue);
        Predicate<Photo> tagPredicate = photo -> photo.getTag(singleTagNameFromChoiceBox, tagValue).equals(photoTagToSearchFor);
        List<Photo> newPhotoList = filter(photoList, tagPredicate);
        observablePhotoList = FXCollections.observableArrayList(newPhotoList);
        photoListView.setItems(observablePhotoList);
        singleTagValueTextField.setText("");
        singleTagNameChoiceBox.setValue(null);
        photoListView.refresh();
        showThumbnailImage();
    }

    /**
     * Searches for Photos in the Album with both specified Photo Tags
     */
    @FXML
    protected void conjunctiveSearch(){
        if(conjunctiveTagNameFromChoiceBox1 == null || conjunctiveTagNameFromChoiceBox2 == null || conjunctiveTagValueTextField1.getText().isEmpty() || conjunctiveTagValueTextField2.getText().isEmpty()){
            showAlertError("Please choose valid Tag names and enter Tag values!");
            return;
        }
        String firstTagValue = conjunctiveTagValueTextField1.getText();
        String secondTagValue = conjunctiveTagValueTextField2.getText();
        PhotoTag firstTag = new PhotoTag(conjunctiveTagNameFromChoiceBox1, firstTagValue);
        PhotoTag secondTag = new PhotoTag(conjunctiveTagNameFromChoiceBox2, secondTagValue);
        Predicate<Photo> firstTagPredicate = photo -> photo.getTag(conjunctiveTagNameFromChoiceBox1, firstTagValue).equals(firstTag);
        Predicate<Photo> secondTagPredicate = photo -> photo.getTag(conjunctiveTagNameFromChoiceBox2, secondTagValue).equals(secondTag);
        List<Photo> newPhotoList = filter(photoList, firstTagPredicate.and(secondTagPredicate));
        observablePhotoList = FXCollections.observableArrayList(newPhotoList);
        photoListView.setItems(observablePhotoList);
        conjunctiveTagValueTextField1.setText("");
        conjunctiveTagValueTextField2.setText("");
        conjunctiveTagNameChoiceBox1.setValue(null);
        conjunctiveTagNameChoiceBox2.setValue(null);
        photoListView.refresh();
        showThumbnailImage();
    }

    /**
     * Searches for Photos in the Album with either specified Photo Tags
     */
    @FXML
    protected void disjunctiveSearch(){
        if(disjunctiveTagNameFromChoiceBox1 == null || disjunctiveTagNameFromChoiceBox2 == null || disjunctiveTagValueTextField1.getText().isEmpty() || disjunctiveTagValueTextField2.getText().isEmpty()){
            showAlertError("Please choose valid Tag names and enter Tag values");
            return;
        }
        String firstTagValue = disjunctiveTagValueTextField1.getText();
        String secondTagValue = disjunctiveTagValueTextField2.getText();
        PhotoTag firstTag = new PhotoTag(disjunctiveTagNameFromChoiceBox1, firstTagValue);
        PhotoTag secondTag = new PhotoTag(disjunctiveTagNameFromChoiceBox2, secondTagValue);
        Predicate<Photo> firstTagPredicate = photo -> photo.getTag(disjunctiveTagNameFromChoiceBox1, firstTagValue).equals(firstTag);
        Predicate<Photo> secondTagPredicate = photo -> photo.getTag(disjunctiveTagNameFromChoiceBox2, secondTagValue).equals(secondTag);
        List<Photo> newPhotoList = filter(photoList, firstTagPredicate.or(secondTagPredicate));
        observablePhotoList = FXCollections.observableArrayList(newPhotoList);
        photoListView.setItems(observablePhotoList);
        disjunctiveTagValueTextField1.setText("");
        disjunctiveTagValueTextField2.setText("");
        disjunctiveTagNameChoiceBox1.setValue(null);
        disjunctiveTagNameChoiceBox2.setValue(null);
        photoListView.refresh();
        showThumbnailImage();
    }

    /**
     * Creates a new Album with the Photos in the search results and adds to the User's list of Albums
     * @throws IOException exception specific to JavaFX
     */
    @FXML
    protected void createAlbumFromResults() throws IOException{
        if(albumTitleTextField.getText().isEmpty()){
            showAlertError("Please enter a valid Album title to add the results to!");
            return;
        }
        Optional<ButtonType> result = showAlertConfirmation("Create new Album from search results?");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        String albumTitle = albumTitleTextField.getText();
        boolean albumFound = false;
        for(Album album: user.getAlbumList()){
            if(album.getAlbumTitle().equals(albumTitle)){
                albumFound = true;
                break;
            }
        }
        if(albumFound){
            showAlertError("Album already exists!");
        }
        else{
            Album newAlbum = new Album(albumTitle);
            for(Photo photo: observablePhotoList){
                newAlbum.addPhoto(photo);
            }
            user.addAlbum(newAlbum);
            UserList.writeUserData(users);
        }
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
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Performs Logout function from the Photo Search window
     * @param actionEvent specific ActionEvent that called function
     * @throws ClassNotFoundException exception specific to JavaFX
     */
    @FXML
    protected void logoutFromPhotoSearchHome(ActionEvent actionEvent) throws ClassNotFoundException{
        Optional<ButtonType> result = showAlertConfirmation("Confirm Logout");
        if(result.isEmpty() || result.get() == ButtonType.CANCEL){
            return;
        }
        logout(actionEvent);
    }

    /**
     * Displays the thumbnail image of the selected Photo
     */
    @FXML
    public void showThumbnailImage(){
        thumbnailImageView.setPreserveRatio(true);
        if(photoListView.getSelectionModel().getSelectedItem() == null){
            thumbnailImageView.setImage(null);
            return;
        }
        Photo photoToDisplay = photoListView.getSelectionModel().getSelectedItem();
        File file = new File(photoToDisplay.getFileLocation());
        Image imageToDisplay = new Image(file.toURI().toString());
        thumbnailImageView.setImage(imageToDisplay);
    }

    /**
     * Filters the Photo List based on specific criteria and returns a Photo List with the results
     * @param photoList Photo List to filter from
     * @param p specific Predicate with testing criteria
     * @return Photo List with filtered results
     */
    private static<Photo> List<Photo> filter(List<Photo> photoList, Predicate<Photo> p){
        List<Photo> result = new ArrayList<Photo>();
        for(Photo photo: photoList){
            if(p.test(photo)){
                result.add(photo);
            }
        }
        return result;
    }
}
