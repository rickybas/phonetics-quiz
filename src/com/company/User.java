package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * A class the stores and gets properties about a user
 */
public class User {
    private final String username;
    private ArrayList<Integer> scores;

    public User(String username) {
        this.username = username;

        scores = new ArrayList<>(); // initialize scores instance variable
        updateScores(); // update the scores variable to get current scores
    }

    /**
     * Add the score to the scores instance variable and add score to users file
     * @param score
     */
    public void addScore(int score) {
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(Utils.USERS_DIRECTORY + username + ".txt", true));
            output.append(Integer.toString(score) + "\n"); // add new score and a new line
            output.close(); // free memory
        } catch (IOException e) {
            Logging.logger.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }
        updateScores(); // get new scores with newly added one
        Logging.log(Level.INFO, username + " add score: " + score);
    }

    /**
     * Fetch the scores from the users file and update the scores instance variable
     */
    public void updateScores() {
        InputStreamReader file = null;
        try {
            file = new InputStreamReader(new FileInputStream(Utils.USERS_DIRECTORY + username + ".txt")); // open users file
        } catch (FileNotFoundException e) {
            Logging.logger.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }

        try (BufferedReader bufferedReader = new BufferedReader(file)) {
            String currentLine;

            // add new scores to scores variable for each line until the line equal null (end of file)
            while ((currentLine = bufferedReader.readLine()) != null) {
                scores.add(Integer.parseInt(currentLine));
            }

        } catch (IOException e) {
            Logging.logger.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

//    public ArrayList<Integer> getScores() {
//        return scores;
//    }

    /**
     * Finds the highest score from the scores instance variable and returns it
     * @return highest score
     */
    public int getHighScore() {
        int highest = 0;
        // loop through all scores
        for (int score : scores) {
            if (score > highest) highest = score;
        }
        return highest;
    }

    /**
     * Finds the average score from the scores instance variable and returns it
     * @return average score
     */
    public double getAverageScore() {
        if (scores.size() == 0) return 0; // just return 0 if there are no scores
        int total = 0;
        // loop through all scores
        for (int score : scores) {
            total += score;
        }
        return total / scores.size();
    }
}
