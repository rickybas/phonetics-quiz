package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;

/**
 * A class that managers user interaction
 */
public class UserSystem {
    /**
     * Finds all users by looping through all user filenames and adding User objects from that
     * @return all registered users
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        File dir = new File(Utils.USERS_DIRECTORY); // stores users directory
        File[] directoryListing = dir.listFiles(); // all files in directory
        for (File child : directoryListing) {
            users.add(new User(child.getName().replace(".txt", ""))); // make new User object from username
        }
        return users;
    }

    /**
     * Returns all users arranged alphabetically from the getAllUsers method
     * @return all registered users arranged alphabetically
     */
    public ArrayList<User> getAllUsersAlphabetically() {
        ArrayList<User> users = getAllUsers();
        Collections.sort(users, Comparator.comparing(User::getUsername)); // sort users alphabetically
        return users;
    }

    /**
     * Returns all users arranged by high score (high to low) from the getAllUsers method
     * @return all registered users arranged by high score
     */
    public ArrayList<User> getAllUsersByHighScore() {
        ArrayList<User> users = getAllUsers();

        // insertion sort arrange users by high score
        User temp; // temporary swap variable
        for (int i = 1; i < users.size(); i++) {
            // loop through the array backwards
            for (int j = i; j > 0; j--) {
                // if user high score larger than current looped through user move user to the list at the start
                if (users.get(j).getHighScore() > users.get(j - 1).getHighScore()) {
                    temp = users.get(j);
                    users.set(j, users.get(j - 1));
                    users.set(j - 1, temp); // swap users
                }
            }
        }

        return users;
    }

    /**
     * Returns all users arranged by average score (high to low) from the getAllUsers method
     * @return all registered users arranged by average score
     */
    public ArrayList<User> getAllUsersByAverageScore() {
        ArrayList<User> users = getAllUsers();

        User temp; // temporary swap variable
        for (int i = 1; i < users.size(); i++) {
            // loop through the array backwards
            for (int j = i; j > 0; j--) {
                // if user average score larger than current looped through user move user to the list at the start
                if (users.get(j).getAverageScore() > users.get(j - 1).getAverageScore()) {
                    temp = users.get(j);
                    users.set(j, users.get(j - 1));
                    users.set(j - 1, temp); // swap users
                }
            }
        }
        return users;
    }

    /**
     * Creates a new user by creating its file with file name username.txt
     * @param username
     * @return new user as User object
     */
    public User addNewUser(String username) {
        username = username.toLowerCase(); // convert username to lowercase, as all usernames are stored as lowercase
        if (userExists(username)) {
            Logging.logger.log(Level.WARNING, "Tried to add user that already exists");
            return null; // finish method now if user exists already
        }
        File file = new File(Utils.USERS_DIRECTORY + username + ".txt"); // open file
        try {
            file.createNewFile(); // create the file
        } catch (IOException e) {
            Logging.logger.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }

        User user = new User(username); // create new User object to return

        Logging.log(Level.INFO, "Created new user " + username);

        return user;
    }

    /**
     * Get the user in a User object by username
     * @param username
     * @return the User from username, null if user does not exist
     */
    public User getUser(String username) {
        username = username.toLowerCase();
        if (!userExists(username)) return null; // finish method and return null if the user exists
        return new User(username);
    }

    /**
     * Checks to see if the username from username exists in the file data set
     * @param username
     * @return if the user exists
     */
    public boolean userExists(String username) {
        username = username.toLowerCase(); // convert to lower case, as all usernames are stored as lowercase
        InputStream filePath = null;
        try {
            filePath = new FileInputStream(Utils.USERS_DIRECTORY + username + ".txt"); // open user file
        } catch (FileNotFoundException e) {
            return false;
        }
        try {
            filePath.read(); // check to see if file readable (not corrupted)
        } catch (IOException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }

        return true;
    }

    /**
     * Remove the user from the username from the file data base
     * @param username
     */
    public void removeUser(String username) {
        username = username.toLowerCase(); // convert to lower case, as all usernames are stored as lowercase
        if (!userExists(username)) {
            Logging.logger.log(Level.WARNING, "Tried to remove user that does not exist");
            return; // if the user does not exist then finish method
        }
        File file = new File(Utils.USERS_DIRECTORY + username + ".txt");
        file.delete(); // delete user file
        Logging.log(Level.INFO, "Removed user " + username);
    }

}
