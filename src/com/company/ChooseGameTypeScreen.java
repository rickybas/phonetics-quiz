package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ChooseGameTypeScreen extends HeaderScreen {
    private final URL selectedCheckboxResource;
    private final URL unselectedCheckboxResource;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private Button playButton;
    private Button backButton;
    private ButtonGroup levelGroup;
    private JRadioButton[] levelRadioButtons;
    private JCheckBox wordSounds;
    private JCheckBox phoneticSounds;
    private UserSystem userSystem;

    ChooseGameTypeScreen() {
        super("Choose Game Type Screen"); // screen id
        setBackground(Color.BLACK);

        // paths of checkbox images
        selectedCheckboxResource = this.getClass().getResource("/selected_checkbox.png");
        unselectedCheckboxResource = this.getClass().getResource("/unselected_checkbox.png");

        userSystem = new UserSystem(); // used to manage user system interactions

        usernameLabel = new JLabel("Username: ");
        usernameLabel.setFont(new Font("Serif", Font.BOLD, 30));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // center on the x-axis

        usernameField = new JTextField();
        usernameField.setFont(new Font("Serif", Font.BOLD, 40));
        // convert all uppercase letters to lower case as the user types
        ((AbstractDocument) usernameField.getDocument()).setDocumentFilter(new LowercaseDocumentFilter());

        // create ImageIcon objects from resource paths and reduce size by 80%
        ImageIcon normal = new ImageIcon(
                new ImageIcon(unselectedCheckboxResource).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
        ImageIcon selected = new ImageIcon(
                new ImageIcon(selectedCheckboxResource).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));

        levelGroup = new ButtonGroup();

        // create empty array that will hold level buttons
        int numLevels = Utils.getLevelWords().size();
        levelRadioButtons = new JRadioButton[numLevels];

        // loop through all levels and a level radio button
        for (int i = 0; i < numLevels; i++) {
            // create JRadioButton with selected and unselected icons
            levelRadioButtons[i] = new JRadioButton("Level " + Utils.NUMBERS_TO_WORDS.get(i + 1), normal, false);
            levelRadioButtons[i].setSelectedIcon(selected);
            levelRadioButtons[i].setFocusable(false);
            levelRadioButtons[i].setFont(new Font("Serif", Font.BOLD, 30));
            levelRadioButtons[i].setSelected(true);
            levelGroup.add(levelRadioButtons[i]); // to the the group
        }

        JPanel levelGrpPanel = new JPanel(); // panel to hold the group
        levelGrpPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // center level radio boxes
        levelGrpPanel.setLayout(new BoxLayout(levelGrpPanel, BoxLayout.PAGE_AXIS));

        // add all the level radio buttons to the panel
        for (JRadioButton levelRadioButton : levelRadioButtons) {
            levelGrpPanel.add(levelRadioButton);
        }

        // check box for if the user wants the words to be spoken
        wordSounds = new JCheckBox("Word Sounds", normal, false);
        wordSounds.setSelectedIcon(selected);
        wordSounds.setFocusable(false); // non selected by default
        wordSounds.setFont(new Font("Serif", Font.BOLD, 30));

        // check box for if the user wants the phonetics to be spoken
        phoneticSounds = new JCheckBox("Phonetic Sounds", normal, false);
        phoneticSounds.setSelectedIcon(selected);
        phoneticSounds.setFocusable(false); // non selected by default
        phoneticSounds.setFont(new Font("Serif", Font.BOLD, 30));

        // panel for sound checkboxes
        JPanel soundsGrpPanel = new JPanel();
        soundsGrpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        soundsGrpPanel.setLayout(new BoxLayout(soundsGrpPanel, BoxLayout.PAGE_AXIS));

        // add the checkboxes to the sounds panel
        soundsGrpPanel.add(wordSounds);
        soundsGrpPanel.add(phoneticSounds);

        playButton = new Button("Play");
        playButton.addActionListener(e ->
                clickedPlay()); // run clickedPlay on click
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT); // center on x-axis

        backButton = new Button("Back");
        backButton.addActionListener(e -> fireSwitchScreenEvent(new SwitchScreen("Welcome Screen"))); // run fireSwitchScreenEvent on click
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT); // center on x-axis
        backButton.setBackground(ColorScheme.RED);

        body.setBorder(new EmptyBorder(20, 100, 100, 100)); // add margin around screen

        /* Add components as follows:
         * levelGrpPanel
         * gap
         * soundsGrpPanel
         * gap
         * usernameLabel
         * usernameField
         * gap
         * playButton
         * gap
         * backButton
         */

        body.add(levelGrpPanel);
        body.add(Box.createRigidArea(new Dimension(0, 30)));
        body.add(soundsGrpPanel);
        body.add(Box.createRigidArea(new Dimension(0, 30))); // gap of 30px vertically
        body.add(usernameLabel);
        body.add(usernameField);
        body.add(Box.createRigidArea(new Dimension(0, 30)));
        body.add(playButton);
        body.add(Box.createRigidArea(new Dimension(0, 30)));
        body.add(backButton);
        body.revalidate();
    }

    /**
     * Run when the play button is pressed
     */
    private void clickedPlay() {
        // get the level selected
        String levelSelected = null;
        // loop through all levels and see if selected, if selected then levelSelected equal that
        for (JRadioButton levelRadioButton : levelRadioButtons) {
            if (levelRadioButton.isSelected()) {
                levelSelected = levelRadioButton.getText().replace("Level ", "");
                break; // break from loop because only one selected at a time
            }
        }

        String normalizedUsername = Utils.normalizeUsername(usernameField.getText()); // normalize username
        Utils.checkComment checkUsername = Utils.checkUsername(normalizedUsername); // check username

        /* check usernames, if the user name is empty (only contains blank spaces or nothing),
         * is longer than 18 characters or the user already exists then do not create the user */

        if (checkUsername == Utils.checkComment.EMPTY) {
            JOptionPane.showMessageDialog(this,
                    "Enter username",
                    "Enter username",
                    JOptionPane.WARNING_MESSAGE); // show message
            return; // finish method
        }

        if (checkUsername == Utils.checkComment.LONG) {
            JOptionPane.showMessageDialog(this,
                    "Username is too long (greater than 18 characters)",
                    "Username is too long (greater than 18 characters)",
                    JOptionPane.WARNING_MESSAGE); // show message
            return; // finish method
        }

        // if the user does exist
        if (!userSystem.userExists(normalizedUsername)) {
            // find similar name
            boolean similarNameFound = false;
            ArrayList<User> users = userSystem.getAllUsers();
            /* loop though all users, if the user has a Levenshtein Distance of less than 3 than open dialog
             * to see if the user means that user */
            for (User user : users) {
                if (Utils.levenshteinDistance(normalizedUsername, user.getUsername()) < 3) {
                    Logging.logger.log(Level.INFO, "Similar username found, " + normalizedUsername + " -> " + user.getUsername());
                    int reply = JOptionPane.showConfirmDialog(null, "Similar name found: " + user.getUsername() + ", login as user?",
                            "Similar name found: " + user.getUsername() + ", login as user?", JOptionPane.YES_NO_OPTION);
                    if (reply != JOptionPane.YES_OPTION) {
                        break;
                    }
                    // if the user means that user than carry on with that username
                    normalizedUsername = user.getUsername(); // no need to normalize because usernames stored are already
                    similarNameFound = true;
                    break;
                }
            }

            // if a user does not mean that name then open the "user does not exist" dialog
            if (!similarNameFound) {
                // the user can create the user if it wishes
                int reply = JOptionPane.showConfirmDialog(null, "User does not exist. Would you like to create the user " + normalizedUsername + "?",
                        "User does not exist. Would you like to create the user " + normalizedUsername + "?", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    userSystem.addNewUser(normalizedUsername);
                } else return;
            }
        }

        // create metadata with the username, level and level options for the game
        Map<String, String> metaData = new HashMap<>();
        metaData.put("username", normalizedUsername);
        metaData.put("level", levelSelected);
        metaData.put("has_phonetic_sounds", phoneticSounds.isSelected() ? "true" : "false");
        metaData.put("has_word_sounds", wordSounds.isSelected() ? "true" : "false");

        fireSwitchScreenEvent(new SwitchScreen("Game Screen", metaData)); // switch screen and send the metadata to the game screen
    }

    @Override
    public void is_visible() {
        super.is_visible();
        // make the play button the windows default button (pressing enter)
        ((JFrame) SwingUtilities.getWindowAncestor(this)).getRootPane().setDefaultButton(playButton);

        // set up the default values for the options
        levelRadioButtons[0].setSelected(true);
        wordSounds.setSelected(false);
        phoneticSounds.setSelected(false);
        usernameField.setText(""); // username box empty
    }

    /**
     * https://kodejava.org/how-do-i-format-jtextfields-text-to-uppercase/
     * Filters out capital letters on the username input box as the user types
     */
    class LowercaseDocumentFilter extends DocumentFilter {
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String text,
                                 AttributeSet attr) throws BadLocationException {
            fb.insertString(offset, text.toLowerCase(), attr);
        }

        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
                            AttributeSet attrs) throws BadLocationException {
            fb.replace(offset, length, text.toLowerCase(), attrs);
        }
    }
}
