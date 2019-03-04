package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;

public class GameScoreScreen extends HeaderScreen {
    private TextArea textArea;
    private Button welcomeScreenButton;

    private UserSystem userSystem;
    private SoundPlayer soundPlayer;

    GameScoreScreen() {
        super("Game Score Screen"); // screen id
        setBackground(Color.BLACK);

        userSystem = new UserSystem(); // used to manage user system interactions
        soundPlayer = new SoundPlayer(); // used to add sounds on this screen

        textArea = new TextArea("", 10, 100, TextArea.SCROLLBARS_NONE); // non scrollable text box
        textArea.setEnabled(false); // no input
        textArea.setFont(new Font("Serif", Font.BOLD, 40));

        welcomeScreenButton = new Button("Go to Welcome Screen");
        welcomeScreenButton.setAlignmentX(Component.CENTER_ALIGNMENT); // center on x-axis
        // on click switch to welcome screen
        welcomeScreenButton.addActionListener(e -> fireSwitchScreenEvent(new SwitchScreen("Welcome Screen")));

        body.setBorder(new EmptyBorder(0, 200, 200, 200)); // add a margin around the screen

        // body.setPreferredSize(new Dimension(Utils.WINDOW_WIDTH, 600));

        /* Add components as follows:
         * textArea
         * gap
         * welcomeScreenButton
         */
        body.add(textArea);
        body.add(Box.createRigidArea(new Dimension(0, 30)));
        body.add(welcomeScreenButton);
        body.revalidate();
    }

    @Override
    public void is_visible() {
        super.is_visible();

        // get data from metadata
        String username = metaData.get("username");
        String level = metaData.get("level");
        int score = Integer.parseInt(metaData.get("score"));
        int outOf = Integer.parseInt(metaData.get("outOf"));

        int percentage = score * 100 / outOf; // percentage score
        String comment = "You got " + score + " out of " + outOf + "." + "\n"; // first line comment

        // Add next line comment according to the percentage
        if (percentage == 100) { // 100%
            comment += "All correct! Well done!";
            soundPlayer.applause(); // applause sound
        } else if (percentage > 80) {
            comment += "Well done!";
            soundPlayer.applause(); // applause sound
        } else if (percentage >= 50) {
            comment += "Nice work. Try playing again to improve your score.";
            soundPlayer.applause();
        } else if (percentage == 0) { // 0%
            comment += "Try again to improve your score.";
        } else {
            comment += "Good try. Try to improve your score.";
        }

        // add the more in depth information
        String info = comment + "\n\n"
                + "Username: " + username + "\n"
                + "Level: " + level + "\n"
                + "Score: " + score + "\n"
                + "Out of: " + outOf + "\n"
                + "Percentage: " + percentage + "%";

        textArea.setText(info); // add all of that text to the text box

        Logging.log(Level.INFO, "Completed game with, info: " + info);

        User user = userSystem.getUser(username);
        user.addScore(percentage);
    }

}
