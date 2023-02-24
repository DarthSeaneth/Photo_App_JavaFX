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
 * Class to store User data.
 */
public class User implements Serializable {
    /**
     * serialVersionUID specific to User object
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * String username of User
     */
    private String userName;
    /**
     * Array list of Albums owned by User
     */
    private List<Album> albumList;

    /**
     * No-arg constructor
     * Instantiates a new User.
     */
    public User(){
        userName = "";
        albumList = new ArrayList<Album>();
    }

    /**
     * 1-arg constructor
     * Instantiates a new User.
     * @param userName the username String
     */
    public User(String userName){
        this.userName = userName;
        albumList = new ArrayList<Album>();
    }

    /**
     * Gets username String.
     * @return the username String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets Album array list.
     * @return the Album array list
     */
    public List<Album> getAlbumList() {
        return albumList;
    }

    /**
     * Adds Album to User Album array list.
     * @param album the album to add
     */
    public void addAlbum(Album album){
        albumList.add(album);
    }

    /**
     * Removes Album from User Album array list.
     * @param album the Album to remove
     */
    public void removeAlbum(Album album){
        albumList.remove(album);
    }

    /**
     * Overridden toString method for User Class
     * @return String representation of User
     */
    public String toString(){
        return "Username: " + userName;
    }
}
