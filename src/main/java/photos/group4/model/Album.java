/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package photos.group4.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to store Album data.
 */
public class Album implements Serializable {
    /**
     * serialVersionUID specific to Album object
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * String title of Album
     */
    private String albumTitle;
    /**
     * Array list of Photos owned by Album
     */
    private List<Photo> albumPhotos;
    /**
     * Number of photos in Album
     */
    private int numPhotos;
    /**
     * List to store pre-set and user-defined Photo Tag Names
     */
    private List<String> photoTagNameList;

    /**
     * No-arg constructor
     * Instantiates a new Album.
     */
    public Album(){
        albumTitle = "";
        albumPhotos = new ArrayList<Photo>();
        numPhotos = 0;
        photoTagNameList = new ArrayList<String>();
        photoTagNameList.add("Name");
        photoTagNameList.add("Location");
    }

    /**
     * 1-arg constructor
     * Instantiates a new Album
     * @param albumTitle String to assign to albumTitle field
     */
    public Album(String albumTitle){
        this.albumTitle = albumTitle;
        albumPhotos = new ArrayList<Photo>();
        numPhotos = 0;
        photoTagNameList = new ArrayList<String>();
        photoTagNameList.add("Name");
        photoTagNameList.add("Location");
    }

    /**
     * Gets Album title string.
     * @return the string title of Album
     */
    public String getAlbumTitle(){
        return albumTitle;
    }

    /**
     * Gets Album photo array list.
     * @return the array list of photos in Album
     */
    public List<Photo> getAlbumPhotos(){
        return albumPhotos;
    }

    /**
     * Gets number of photos in Album.
     * @return the number of photos
     */
    public int getNumPhotos(){
        return numPhotos;
    }

    /**
     * Gets the List of pre-set and user-defined Photo Tag Names
     * @return list of Photo Tag Names
     */
    public List<String> getPhotoTagNameList(){
        return photoTagNameList;
    }

    /**
     * Adds a user-defined Photo Tag Name to the list
     * @param photoTagName String Photo Tag Name to add
     */
    public void addPhotoTagNameToList(String photoTagName){
        photoTagNameList.add(photoTagName);
    }

    /**
     * Sets Album title.
     * @param albumTitle the Album title
     */
    public void setAlbumTitle(String albumTitle){
        this.albumTitle = albumTitle;
    }

    /**
     * Adds photo to Album photos array list.
     * @param photo the photo to add
     */
    public void addPhoto(Photo photo){
        albumPhotos.add(photo);
        numPhotos ++;
    }

    /**
     * Removes photo from Album photos array list.
     * @param photo the photo to remove
     */
    public void removePhoto(Photo photo){
//        albumPhotos.remove(photo);
        int targetIndex = -1;
        for(int i = 0; i < albumPhotos.size(); i++) {
            if (albumPhotos.get(i) == photo) {
                targetIndex = i;
            }
        }
        albumPhotos.remove(targetIndex);
        numPhotos --;
    }

    /**
     * Overridden toString method for Album Class
     * @return String representation of Album
     */
    public String toString(){
        return "Album Title: " + getAlbumTitle();
    }

    /**
     * Gets the oldest Photo from the Photo List according to the Calendar instance
     * @return oldest Photo (null if no Photos exist)
     */
    public Photo getOldestPhoto(){
        if(numPhotos == 0){
            return null;
        }
        Photo oldestPhoto = albumPhotos.get(0);
        for(Photo photo: albumPhotos){
            if(photo.getDate().compareTo(oldestPhoto.getDate()) < 0){
                oldestPhoto = photo;
            }
        }
        return oldestPhoto;
    }

    /**
     * Gets newest Photo from the Photo list according to the Calendar instance
     * @return newest Photo (null if no Photos exist)
     */
    public Photo getNewestPhoto(){
        if(numPhotos == 0){
            return null;
        }
        Photo newestPhoto = albumPhotos.get(0);
        for(Photo photo: albumPhotos){
            if(photo.getDate().compareTo(newestPhoto.getDate()) > 0){
                newestPhoto = photo;
            }
        }
        return newestPhoto;
    }

    /**
     * Gets String representation of oldest Photo date
     * @return String representation of oldest Photo date (Empty if it doesn't exist)
     */
    public String oldestPhotoDate(){
        Photo oldestPhoto = getOldestPhoto();
        if(oldestPhoto == null){
            return "";
        }
        return oldestPhoto.getDate().getTime().toString();
    }

    /**
     * Gets String representation of newest Photo date
     * @return String representation of newest Photo date (Empty if it doesn't exist)
     */
    public String newestPhotoDate(){
        Photo newestPhoto = getNewestPhoto();
        if(newestPhoto == null){
            return "";
        }
        return newestPhoto.getDate().getTime().toString();
    }

    /**
     * Gets String representation of the date range of the Photos in the list
     * @return String representation of date range (Empty if no Photos exist)
     */
    public String getDateRange(){
        String oldestPhotoDate = oldestPhotoDate();
        String newestPhotoDate = newestPhotoDate();
        if(oldestPhotoDate.equals("") || newestPhotoDate.equals("")){
            return "";
        }
        return oldestPhotoDate + " - " + newestPhotoDate;
    }
}
