package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MainWindow extends JFrame {
    private JPanel cards;
    private CardLayout cardLayout;

    private WelcomeScreen welcomeScreen;
    private ChooseGameTypeScreen chooseGameTypeScreen;
    private GameScreen gameScreen;
    private GameScoreScreen gameScoreScreen;
    private StatisticsScreen statisticsScreen;
    private SettingsScreen settingsScreen;

    private Screen[] screens;
    private Map<String, Screen> screenMap;

    /**
     * The main window of the program where each screen is shown
     */
    MainWindow() {
        super(Utils.GAME_TITLE + " - " + Utils.VERSION); // title text

        Logging.log(Level.INFO, "Main window open");

        welcomeScreen = new WelcomeScreen(); // a type of jpanel that stores the welcome screen's contents
        chooseGameTypeScreen = new ChooseGameTypeScreen(); // a type of jpanel that stores the choose game type screen's contents
        gameScreen = new GameScreen(); // a type of jpanel that stores the game screen's contents
        gameScoreScreen = new GameScoreScreen(); // a type of jpanel that stores the game score screen's contents
        statisticsScreen = new StatisticsScreen(); // a type of jpanel that stores the statistics screen's contents
        settingsScreen = new SettingsScreen(); // a type of jpanel that stores the settings screen's contents

        screens = new Screen[]{
                welcomeScreen,
                chooseGameTypeScreen,
                gameScreen,
                gameScoreScreen,
                statisticsScreen,
                settingsScreen
        }; // store all the screens in an array

        // loop through all screen and link the screen titles with the screen objects using the screen map
        screenMap = new HashMap<>();
        for (Screen screen : screens) {
            screenMap.put(screen.title, screen); // map the screens title to the screen's object
            screen.addSwitchScreenListener(evt -> selectScreen(evt.screen, evt.metaData)); // if the screen requests a change in screen then run selectScreen
        }

        // the card layout hold a list of screens where only one screen can be shown at a time to the window
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // add screens to cards jpanel, with the title as the unique identifier
        for (Screen screen : screens) {
            cards.add(screen, screen.title);
        }

        selectScreen(welcomeScreen.title, null); // welcome screen is the first screen to be shown

        getContentPane().add(cards); // add the cards jpanel to the window
        pack();
    }

    /**
     * show screen with title and pass metadata to the next screen
     * @param title
     * @param metaData
     */
    private void selectScreen(String title, Map<String, String> metaData) {
        // if metadata exists then pass metadata to the screen with id title
        if (metaData != null) {
            screenMap.get(title).metaData = metaData;
            Logging.log(Level.INFO, "Screen " + title + " sent metadata: " + metaData.toString());
        }

        cardLayout.show(cards, title); // show screen with id title
    }

}