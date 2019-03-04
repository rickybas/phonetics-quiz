package com.company;

import java.util.*;
import java.util.logging.Level;

/**
 * A class that holds useful constants or functions that can be used across the whole program
 */
public final class Utils {
    public static final String USERS_DIRECTORY = System.getProperty("user.home") + "/phonetics-quiz-user-data/"; // the location of th username.txt files
    final static boolean LOGGING = false; // whether whole program logging should be activated, should not be logging in production
    final static String GAME_TITLE = "Phonetics Quiz"; // the program title
    final static String VERSION = "v1.0"; // semantic versioning
    static final int WINDOW_WIDTH = 1000; // constant window width throughout running time
    static final int WINDOW_HEIGHT = 1000; // constant window height throughout running time

    static final Map<Integer, String> NUMBERS_TO_WORDS = new HashMap<Integer, String>() {{
        put(1, "One");
        put(2, "Two");
        put(3, "Three");
        put(4, "Four");
    }}; // for converting numbers to words

    static final Map<String, Integer> WORDS_TO_NUMBERS = new HashMap<String, Integer>() {{
        put("One", 1);
        put("Two", 2);
        put("Three", 3);
        put("Four", 4);
    }}; // for converting words to numbers

    /**
     * This enum is returned in the checkUsername method
     */
    public enum checkComment {
        LONG,
        EMPTY,
        GOOD
    }

    static final Map<String, String[]> WORDS = new HashMap<String, String[]>() {{
        // ay section
        put("say", new String[]{"s", "ay"});
        put("may", new String[]{"m", "ay"});
        put("way", new String[]{"w", "ay"});
        put("play", new String[]{"p", "l", "ay"});
        put("day", new String[]{"d", "ay"});
        put("spray", new String[]{"s", "p", "r", "ay"});

        // ee section
        put("see", new String[]{"s", "ee"});
        put("three", new String[]{"th", "r", "ee"});
        put("need", new String[]{"n", "ee", "d"});
        put("seed", new String[]{"s", "ee", "d"});
        put("queen", new String[]{"qu", "ee", "n"});
        put("street", new String[]{"s", "t", "r", "ee", "t"});

        // igh section
        put("high", new String[]{"h", "igh"});
        put("sight", new String[]{"s", "igh", "t"});
        put("might", new String[]{"m", "igh", "t"});
        put("night", new String[]{"n", "igh", "t"});
        put("bright", new String[]{"b", "r", "igh", "t"});
        put("tight", new String[]{"t", "igh", "t"});

        // oa section
        put("goat", new String[]{"g", "oa", "t"});
        put("boat", new String[]{"b", "oa", "t"});
        put("throat", new String[]{"th", "r", "oa", "t"});
        put("toast", new String[]{"t", "oa", "st"});
        put("coach", new String[]{"c", "oa", "ch"});
        put("goal", new String[]{"g", "oa", "l"});
    }}; // all the words with phonetics

    /**
     * Finds all the phonetics by loop through all the words
     * @return all phonetics
     */
    static ArrayList<String> getAllPhonetics() {
        ArrayList<String> allPhonetics = new ArrayList<>();
        // loop through all words
        for (Map.Entry<String, String[]> entry : WORDS.entrySet()) {
            // String word = entry.getKey();
            String[] phonetics = entry.getValue();
            // loop through phonetics in word
            for (String phonetic : phonetics) {
                if (!allPhonetics.contains(phonetic)) allPhonetics.add(phonetic); // add to all phonetics if has not been added before
            }
        }
        return allPhonetics;
    }

    /**
     * Find all the words attached to each level
     * @return all words for each level
     */
    static ArrayList<ArrayList<String>> getLevelWords() {
        ArrayList<ArrayList<String>> levels = new ArrayList<>();
        levels.add(new ArrayList<>()); // level one
        levels.add(new ArrayList<>()); // level two
        levels.add(new ArrayList<>()); // level three
        levels.add(new ArrayList<>()); // level four

        // loop through all words
        for (Map.Entry<String, String[]> entry : WORDS.entrySet()) {
            String word = entry.getKey();
            String[] phonetics = entry.getValue();
            /* if the phonetics for the word contains "ay" then it's level one,
             *  "ee" = level two, "igh" = level three, "oa" = level four */
            if (Arrays.asList(phonetics).contains("ay")) {
                levels.get(0).add(word);
            } else if (Arrays.asList(phonetics).contains("ee")) {
                levels.get(1).add(word);
            } else if (Arrays.asList(phonetics).contains("igh")) {
                levels.get(2).add(word);
            } else if (Arrays.asList(phonetics).contains("oa")) {
                levels.get(3).add(word);
            } else {
                Logging.logger.log(Level.WARNING, word + " not included in any levels");
            }
        }
        return levels;
    }

    /**
     * Format username so it's similar to rest
     * @param username
     * @return normalize username
     */
    static String normalizeUsername(String username) {
        return username.replace(" ", "").toLowerCase(); // remove all gaps and change to lowercase
    }

    /**
     * Checks username to see if it can be added, cannot equal nothing and less than 18 characters
     * @param username
     * @return why user cannot be added or it can be added as a enum
     */
    public static checkComment checkUsername(String username) {
        if (username.equals("")) {
            Logging.logger.log(Level.INFO, "username " + username + " empty");
            return checkComment.EMPTY;
        }

        if (username.length() > 18) {
            Logging.logger.log(Level.INFO, "username " + username + " long");
            return checkComment.LONG;
        }
        return checkComment.GOOD;
    }

    /**
     * Calculates the Levenshtein Distance between two words (how similar they are to each other)
     * https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
     * @param wordOne
     * @param wordTwo
     * @return the levenshtein distance score
     */
    static int levenshteinDistance(String wordOne, String wordTwo) {
        int len0 = wordOne.length() + 1;
        int len1 = wordTwo.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (wordOne.charAt(i - 1) == wordTwo.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

    /**
     * Exit the whole program closing down the main window
     */
    static void exit() {
        Logging.log(Level.INFO, "Main window closed, end of program");
        System.exit(0); // end program
    }
}
