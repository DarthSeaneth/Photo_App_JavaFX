/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package photos.group4.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class to store Photo Tag data.
 */
public class PhotoTag implements Serializable {
    /**
     * serialVersionUID specific to Photo Tag object
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * String to store tag-name
     */
    private String tagName;
    /**
     * String to store tag-value
     */
    private String tagValue;

    /**
     * 2-arg constructor
     * Instantiates a new Photo tag.
     * @param tagName the tag-name String
     * @param tagValue the tag-value String
     */
    public PhotoTag(String tagName, String tagValue){
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    /**
     * Gets tag-name String.
     * @return the tag-name String
     */
    public String getTagName(){
        return tagName;
    }

    /**
     * Gets tag-value String.
     * @return the tag-value String
     */
    public String getTagValue(){
        return tagValue;
    }

    /**
     * Overridden toString method for Photo Tag Class
     * @return String representation of a Photo Tag
     */
    public String toString(){
        return tagName + " : " + tagValue;
    }

    /**
     * Overridden equals method for Photo Tag Class
     * @param o Object to check equality against
     * @return boolean value representing the equality of the two objects
     */
    public boolean equals(Object o){
        if(!(o instanceof PhotoTag photoTag)){
            return false;
        }
        return this.tagName.equals(photoTag.tagName) && this.tagValue.equals(photoTag.tagValue);
    }
}
