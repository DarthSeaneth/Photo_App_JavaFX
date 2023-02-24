/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package photos.group4.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Class to store Photo data.
 */
public class Photo implements Serializable {
    /**
     * serialVersionUID specific to Photo object
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Specific name of Photo
     */
    private String name;
    /**
     * Specific date of Photo
     */
    private Calendar date;
    /**
     * Specific file location of Photo
     */
    private String fileLocation;
    /**
     * Specific caption String of Photo
     */
    private String caption;
    /**
     * List of Photo Tags attached to Photo
     */
    private List<PhotoTag> photoTags;

    /**
     * No-arg constructor
     * Instantiates a new Photo.
     */
    public Photo(){
        name = "New Photo";
        date = Calendar.getInstance();
        date.set(Calendar.MILLISECOND, 0);
        fileLocation = "";
        caption = "";
        photoTags = new ArrayList<PhotoTag>();
    }

    /**
     * 3-arg constructor
     * Instantiates a new Photo
     * @param name String name of Photo
     * @param date Calendar date of Photo
     * @param fileLocation file location of Photo
     */
    public Photo(String name, Calendar date, String fileLocation){
        this.name = name;
        this.date = date;
        this.fileLocation = fileLocation;
        caption = "";
        photoTags = new ArrayList<PhotoTag>();
    }

    /**
     * Gets the name String of Photo
     * @return String name
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the nam String of Photo
     * @param name String name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets date of Photo
     * @return the date of Photo
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Gets the file location of the Photo
     * @return String file location
     */
    public String getFileLocation() {
        return fileLocation;
    }

    /**
     * Sets file location of Photo
     * @param fileLocation String file location
     */
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * Gets List of Photo Tags of Photo
     * @return Photo Tag list of Photo
     */
    public List<PhotoTag> getTags() {
        return photoTags;
    }

    /**
     * Gets the Photo Tag by the Tag Name and Value
     * @param tagName String Tag Name to check for
     * @param tagValue String Tag Value to check for
     * @return the corresponding Photo Tag (Empty Photo Tag if not found)
     */
    public PhotoTag getTag(String tagName, String tagValue){
        for(PhotoTag photoTag: photoTags){
            if(photoTag.getTagName().equals(tagName) && photoTag.getTagValue().equals(tagValue)){
                return photoTag;
            }
        }
        return new PhotoTag("", "");
    }

    /**
     * Gets the caption String of Photo
     * @return String caption
     */
    public String getCaption(){
        return caption;
    }

    /**
     * Sets caption of Photo.
     * @param caption the caption string of Photo
     */
    public void setCaption(String caption){
        this.caption = caption;
    }

    /**
     * Adds Photo Tag to Photo
     * @param tagName String tag-name of Photo Tag
     * @param tagValue String tag-value of Photo Tag
     */
    public void addTag(String tagName, String tagValue){
        photoTags.add(new PhotoTag(tagName, tagValue));
    }

    /**
     * Determines if the Photo has the same photo file as another Photo
     * @param photo Photo to compare to
     * @return boolean value representing the equality of the file locations
     */
    public boolean duplicateFile(Photo photo){
        return this.getFileLocation().compareTo(photo.getFileLocation()) == 0;
    }

    /**
     * Overridden equals method for Photo Class
     * @param o Object to check equality against
     * @return boolean value representing the equality of the two objects
     */
    public boolean equals(Object o){
        if(!(o instanceof Photo photo)){
            return false;
        }
        return this.getName().compareTo(photo.getFileLocation()) == 0
                && this.duplicateFile(photo)
                && this.getDate().toString().compareTo(photo.getDate().toString()) == 0
                && this.getCaption().compareTo(photo.getCaption()) == 0;
    }

    /**
     * Overridden toString method for Photo Class
     * @return String representation of a Photo
     */
    public String toString(){
        return "Name: " + name + ":\nCaption: " + caption + "\nDate: " + date.getTime();
    }
}
