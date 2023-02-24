/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package photos.group4.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Class to store list of Users maintained by Photo Application.
 * Only the special Admin User can modify
 * Main source of Serialization of Photo Application User data
 * Utilizes Singleton Design Pattern
 */
public class UserList implements Serializable {
    /**
     * serialVersionUID specific to User List object
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * List of Users
     */
    private List<User> userList;
    /**
     * User List instance to ensure only single User List object exists (Singleton Design Pattern)
     */
    private static UserList userListInstance = null;

    /**
     * No-arg constructor
     * Instantiates a new User list.
     * Sets up the admin User and the stock User
     */
    private UserList(){
        userList = new ArrayList<User>();
        userList.add(new User("admin"));
        userList.add(new User("stock"));
        User stockUser = getUser("stock");
        Album newAlbum = new Album("stock");
        Calendar date = Calendar.getInstance();
        date.set(Calendar.MILLISECOND, 0);
        Photo photo1 = new Photo("Shrek", date, "data/StockPhotos/stockphoto1.jpg");
        photo1.setCaption("Shrek");
        Photo photo2 = new Photo("Shrek2", date, "data/StockPhotos/stockphoto2.jpg");
        photo2.setCaption("Shrek");
        Photo photo3 = new Photo("Shrek3", date, "data/StockPhotos/stockphoto3.jpg");
        photo3.setCaption("Shrek");
        Photo photo4 = new Photo("Shrek4", date, "data/StockPhotos/stockphoto4.png");
        photo4.setCaption("Shrek");
        Photo photo5 = new Photo("Shrek5", date, "data/StockPhotos/stockphoto5.jpg");
        photo5.setCaption("Shrek & Donkey");
        newAlbum.addPhoto(photo1);
        newAlbum.addPhoto(photo2);
        newAlbum.addPhoto(photo3);
        newAlbum.addPhoto(photo4);
        newAlbum.addPhoto(photo5);
        stockUser.addAlbum(newAlbum);
    }

    /**
     * Gets User List instance, ensuring only a single User List object exists (Singleton Design Pattern)
     * @return Single User List instance to be managed by Photo Application
     */
    public static UserList getUserListInstance(){
        if(userListInstance == null){
            userListInstance = new UserList();
        }
        return userListInstance;
    }

    /**
     * Gets list of Users.
     * @return the list of Users
     */
    public List<User> getUserList(){
        return userList;
    }

    /**
     * Removes User from User list.
     * @param user the User to remove
     */
    public void removeUser(User user){
        userList.remove(user);
    }

    /**
     * Gets User from list of Users by username String
     * @param username String username attached to User
     * @return corresponding User or null if not found
     */
    public User getUser(String username){
        for(User user: userList){
            if(user.getUserName().equals(username)){
                return user;
            }
        }
        return null;
    }

    /**
     * Determines if User exists in the list of Users
     * @param username String username to check against Users in the list of Users
     * @return boolean value indicating if User exists
     */
    public boolean doesUserExist(String username){
        for(User user: userList){
            if(user.getUserName().equals(username)){
                return true;
            }
        }
        return false;
    }

    /**
     * Writes User data into data/user.dat.
     * @param userList the User list
     * @throws IOException exception specific to Serialization
     */
    public static void writeUserData(UserList userList) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("data" + File.separator + "users.dat"));
        stream.writeObject(userList);
        stream.close();
    }

    /**
     * Reads User data from data/users.dat and return corresponding User List object.
     * @throws IOException exception specific to Serialization
     * @throws ClassNotFoundException exception specific to Serialization
     */
    public static void readUserData() throws IOException, ClassNotFoundException{
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream("data" + File.separator + "users.dat"));
        UserList userList = (UserList) stream.readObject();
        stream.close();
        userListInstance = userList;
    }
}
