package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WelcomeScreen extends HeaderScreen {
    private Button playButton;
    private Button statisticsButton;
    private Button settingsButton;
    private Button quitButton;

    WelcomeScreen() {
        super("Welcome Screen", new GridLayout(0, 1, 80, 80));  // screen id

        playButton = new Button("Play");
        statisticsButton = new Button("Statistics");
        settingsButton = new Button("Settings");
        quitButton = new Button("Quit");
        quitButton.setBackground(ColorScheme.RED);

        playButton.addActionListener(e -> // on click switch screen to Choose Game Type Screen
                fireSwitchScreenEvent(new SwitchScreen("Choose Game Type Screen")));
        statisticsButton.addActionListener(e -> // on click switch screen to Statistics Screen
                fireSwitchScreenEvent(new SwitchScreen("Statistics Screen")));
        settingsButton.addActionListener(e -> // on click switch screen to Settings Screen
                fireSwitchScreenEvent(new SwitchScreen("Settings Screen")));
        quitButton.addActionListener(e -> Utils.exit()); // on click switch screen to quit program

        body.setBorder(new EmptyBorder(20, 200, 20, 200)); // create a margin around the screen

        /* Add components as follows:
         * playButton
         * statisticsButton
         * settingsButton
         * backButton
         */

        body.add(playButton);
        body.add(statisticsButton);
        body.add(settingsButton);
        body.add(quitButton);

        body.revalidate();
    }

    @Override
    public void is_visible() {
        super.is_visible();
        // set the playButton as the default button for the window (clicking on enter)
        ((JFrame) SwingUtilities.getWindowAncestor(this)).getRootPane().setDefaultButton(playButton);
    }

}
